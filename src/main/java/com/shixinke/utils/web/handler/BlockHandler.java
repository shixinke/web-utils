package com.shixinke.utils.web.handler;

import com.alibaba.csp.sentinel.slots.block.BlockException;

/**
 * @author shixinke
 * crated 19-4-9 下午3:46
 * @version 1.0
 */
public interface BlockHandler<T> {
    /**
     * 流量异常处理方法
     * @param ex
     * @return
     * @throws BlockException
     */
    public default T handle(BlockException ex) throws BlockException {
        throw ex;
    }
}