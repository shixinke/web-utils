package com.shixinke.utils.web.interceptor;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Message;
import com.dianping.cat.message.Transaction;
import com.shixinke.utils.web.annotation.cat.CatLog;
import com.shixinke.utils.web.annotation.cat.Nullable;
import com.shixinke.utils.web.handler.ApiExceptionHandler;
import com.shixinke.utils.web.handler.CacheExceptionHandler;
import com.shixinke.utils.web.handler.SqlExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

/**
 * @author shixinke
 * crated 19-4-9 下午3:30
 * @version 1.0
 */
@Component
@Aspect
@Slf4j
public class CatAspect {

    @Value("${cat.enabled}")
    private boolean catEnabled;

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

        if (catEnabled) {
            Cat.enable();
        } else {
            Cat.disable();
        }
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
                log.error("实例化{}类失败:{}", cls.getSimpleName(), ex);
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
                    log.error("执行方法{}失败:{}", method.getName(), e);
                }
            } else {
                String fullMethodName = cls.getName() + "." + catLog.exceptionHandler();
                log.error("设置的异常处理方法{}不存在", fullMethodName);
            }
        } else {
            log.debug("未设置异常处理方法,使用默认的处理方法");
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

}
