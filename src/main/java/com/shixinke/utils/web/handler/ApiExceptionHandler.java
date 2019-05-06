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

    @Override
    public ResponseDTO handle(Exception ex, String message, Class clz) {
        ResponseDTO responseDTO = ResponseDTO.error();
        String msg = null;
        int code = -1;
        if (ex instanceof ValidateException) {
            msg = ex.getMessage();
            code = ((ValidateException) ex).getCode();
            logger.error("parameter validate failed:{}", ex);
        } else if (ex instanceof BlockException) {
            msg = Errors.FLOW_ERROR.getMessage();
            code = Errors.FLOW_ERROR.getCode();
            logger.error("flow control over the :{}", ex);
        } else {
            msg = StringUtils.isEmpty(message) ? "empty" : message + "failed";
            code = Errors.SERVER_ERROR.getCode();
            logger.error("get data failed:{}", ex);
        }
        if (clz == null || clz == Nullable.class) {
            return responseDTO.setError(code, msg);
        } else {
            return responseDTO.setEmpty(msg, clz);
        }
    }
}

