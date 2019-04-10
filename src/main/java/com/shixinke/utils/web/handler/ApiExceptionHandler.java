package com.shixinke.utils.web.handler;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.shixinke.utils.web.annotation.cat.ExceptionHandler;
import com.shixinke.utils.web.annotation.cat.Nullable;
import com.shixinke.utils.web.common.Errors;
import com.shixinke.utils.web.common.ResponseDTO;
import com.shixinke.utils.web.exception.ValidateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * @author shixinke
 * crated 19-4-9 下午3:39
 * @version 1.0
 */
public class ApiExceptionHandler implements ExceptionHandler<ResponseDTO> {
    static final Logger logger = LoggerFactory.getLogger(ApiExceptionHandler.class);

    public ResponseDTO handle(Exception ex, String message, Class clz) {
        ResponseDTO responseDTO = ResponseDTO.error();
        String msg = null;
        int code = -1;
        if (ex instanceof ValidateException) {
            msg = ex.getMessage();
            code = ((ValidateException) ex).getCode();
            logger.error("参数验证失败:{}", ex);
        } else if (ex instanceof BlockException) {
            msg = Errors.FLOW_ERROR.getMessage();
            code = Errors.FLOW_ERROR.getCode();
            logger.error("流量超过限制:{}", ex);
        } else {
            msg = StringUtils.isEmpty(message) ? "未获取到数据" : message + "失败";
            code = Errors.SERVER_ERROR.getCode();
            logger.error("获取数据或操作失败:{}", ex);
        }
        if (clz == null || clz == Nullable.class) {
            return responseDTO.setError(code, msg);
        } else {
            return responseDTO.setEmpty(msg, clz);
        }
    }
}

