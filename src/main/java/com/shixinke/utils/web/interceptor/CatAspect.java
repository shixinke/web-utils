package com.shixinke.utils.web.interceptor;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Message;
import com.dianping.cat.message.Transaction;
import com.shixinke.utils.web.annotation.cat.CatLog;
import com.shixinke.utils.web.annotation.cat.CatLogType;
import com.shixinke.utils.web.annotation.cat.Nullable;
import com.shixinke.utils.web.config.CatConfig;
import com.shixinke.utils.web.handler.ApiExceptionHandler;
import com.shixinke.utils.web.handler.CacheExceptionHandler;
import com.shixinke.utils.web.handler.SqlExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;

/**
 * @author shixinke
 * crated 19-4-9 下午3:30
 * @version 1.0
 */
@Component
@Aspect
@Slf4j
public class CatAspect {

    @Autowired
    private CatConfig catConfig;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Pointcut("@annotation(com.shixinke.utils.web.annotation.cat.CatLog)")
    private void pointCutMethodService(){
    }

    @Around("pointCutMethodService()")
    public Object doAroundService(ProceedingJoinPoint joinPoint) throws Throwable{
        long startTime = System.currentTimeMillis();
        Object obj = null;
        Object[] args = joinPoint.getArgs();
        CatLog catLog = ((MethodSignature)joinPoint.getSignature()).getMethod().getAnnotation(CatLog.class);
        String fullClassName = joinPoint.getThis().getClass().getSimpleName();
        String className = fullClassName.substring(0, fullClassName.indexOf("$"));
        String methodName = joinPoint.getSignature().getName();
        if (!StringUtils.isEmpty(catLog.type())) {
            className = catLog.type();
        }

        if (!StringUtils.isEmpty(catLog.page())) {
            methodName = catLog.page();
        }

        if (catConfig.getEnable()) {
            Cat.enable();
        } else {
            Cat.disable();
        }
        if (StringUtils.isEmpty(className)) {
            className = joinPoint.getSignature().getDeclaringType().getSimpleName();
        }
        sqlLog(joinPoint, catLog, className, methodName, args);
        Message message = getMessageObj(catLog, className, methodName, startTime);
        try {
            obj = joinPoint.proceed(args);
            message.setStatus(Transaction.SUCCESS);
        } catch (Exception ex) {
            message.setStatus(ex);
            Cat.logError(ex);
            Class cls = joinPoint.getThis().getClass();
            if (catLog.exceptionClass() != Nullable.class) {
                cls = catLog.exceptionClass();
            }
            obj = getResult(catLog, cls, ex);
        } finally {
            message.complete();
        }
        return obj;
    }

    /**
     * get the instance of message
     * @param catLog
     * @param className
     * @param methodName
     * @param startTime
     * @return
     */
    private Message getMessageObj(CatLog catLog, String className, String methodName, Long startTime) {
        Message message = Cat.newTransaction(catLog.methodType().getValue(), className + "."+methodName);
        switch (catLog.messageType()) {
            case TRANSACTION_WITH_DURATION:
                message = Cat.newTransactionWithDuration(catLog.methodType().getValue(), className + "."+methodName, startTime);
                break;
            case EVENT:
                message = Cat.newEvent(catLog.methodType().getValue(), className + "."+methodName);
                break;
            case TRACE:
                message = Cat.newTrace(catLog.methodType().getValue(), className + "."+methodName);
                break;
            default:
                message = Cat.newTransaction(catLog.methodType().getValue(), className + "."+methodName);
        }
        return message;
    }

    private Object getResult(CatLog catLog, Class cls, Exception ex) {
        Object obj = null;
        if (catLog.exceptionClass() != Nullable.class) {
            cls = catLog.exceptionClass();
        }

        if (!StringUtils.isEmpty(catLog.exceptionHandler())) {
            Object instance = null;
            Method method = null;
            try {
                instance = cls.newInstance();
            } catch (Exception e) {
                log.error("initialize {} failed:{}", cls.getSimpleName(), ex);
            }
            Method[] methods = cls.getDeclaredMethods();
            for (Method m : methods) {
                if (m.getName().equalsIgnoreCase(catLog.exceptionHandler())) {
                    method = m;
                }
            }
            if (method != null) {
                try {
                    obj = method.invoke(instance, ex, catLog.remark(), catLog.empty());
                } catch (Exception e) {
                    log.error("execute the method {} failed:{}", method.getName(), e);
                }
            } else {
                String fullMethodName = cls.getName() + "." + catLog.exceptionHandler();
                log.error("the exception handler {} does not exists", fullMethodName);
            }
        } else {
            log.debug("does not set the exception handler,use the default handler");
            switch (catLog.methodType()) {
                case API:
                case SEARCH:
                case SERVICE:
                    obj = new ApiExceptionHandler().handle(ex, catLog.remark(), catLog.empty());
                    break;
                case SQL:
                    obj = new SqlExceptionHandler().handle(ex, catLog.remark(), catLog.empty());
                    break;
                case CACHE:
                    obj = new CacheExceptionHandler().handle(ex, catLog.remark(), catLog.empty());
                    break;
                default:
                    obj = new ApiExceptionHandler().handle(ex, catLog.remark(), catLog.empty());

            }
        }
        return obj;
    }

    /**
     * add sql log
     * @param joinPoint
     * @param catLog
     * @param className
     * @param methodName
     * @param args
     */
    private void sqlLog(ProceedingJoinPoint joinPoint, CatLog catLog, String className, String methodName, Object[] args) {
        if (catLog.methodType() == CatLogType.SQL) {
            String methodPath = joinPoint.getSignature().getDeclaringType().getName() + "."+methodName;
            log.info(methodPath);
            MappedStatement mappedStatement = sqlSessionFactory.getConfiguration()
                    .getMappedStatement(methodPath);
            Object parameterObject = args;
            if (args.length == 1) {
                parameterObject = args[0];
            }
            BoundSql boundSql = mappedStatement.getBoundSql(parameterObject);
            String sql = getSql(sqlSessionFactory.getConfiguration(), boundSql);
            Cat.logEvent(CatLogType.SQL.getValue(), className + "." + methodName, Message.SUCCESS, sql);
        }

    }

    /**
     * parse sql
     * @param configuration
     * @param boundSql
     * @return
     */
    private String getSql(Configuration configuration, BoundSql boundSql) {
        Object parameterObject = boundSql.getParameterObject();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        String sql = boundSql.getSql().replaceAll("[\\s]+", " ");
        if (parameterMappings.size() > 0 && parameterObject != null) {
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(getParameterValue(parameterObject)));
            } else {
                MetaObject metaObject = configuration.newMetaObject(parameterObject);
                boolean isArray = isArray(parameterObject);
                if (isArray) {
                    Object[] parameters = (Object[]) parameterObject;
                    for (int i = 0; i< parameterMappings.size(); i++) {
                        if (i > parameters.length) {
                            break;
                        }
                        Object obj = parameters[i];
                        sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(getParameterValue(obj)));
                    }
                } else {
                    for (ParameterMapping parameterMapping : parameterMappings) {
                        String propertyName = parameterMapping.getProperty();
                        if (metaObject.hasGetter(propertyName)) {
                            Object obj = metaObject.getValue(propertyName);
                            sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(getParameterValue(obj)));
                        } else if (boundSql.hasAdditionalParameter(propertyName)) {
                            Object obj = boundSql.getAdditionalParameter(propertyName);
                            sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(getParameterValue(obj)));
                        }
                    }
                }

            }
        }
        return sql;
    }

    /**
     * parameter value parser
     * @param obj
     * @return
     */
    private String getParameterValue(Object obj) {
        String value = null;
        if (obj instanceof String) {
            value = "'" + obj.toString() + "'";
        } else if (obj instanceof Date) {
            DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
            value = "'" + formatter.format((Date)obj) + "'";
        } else {
            if (obj != null) {
                value = obj.toString();
            } else {
                value = "";
            }

        }
        return value;
    }

    /**
     * check the parameter
     * @param obj
     * @return
     */
    private static boolean isArray(Object obj) {
        if( obj == null) {
            return false;
        }
        return obj.getClass().isArray();
    }



}
