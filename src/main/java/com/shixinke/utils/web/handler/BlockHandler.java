package com.shixinke.utils.web.handler;

import com.alibaba.csp.sentinel.slots.block.BlockException;

/**
 * @author shixinke
 * crated 19-4-9 下午3:46
 * @version 1.0
 */
public interface BlockHandler<T> {
    /**
     * handle the flow exception
     * @param ex the object of exception
     * @return T
     * @throws BlockException block exception
     */
    public default T handle(BlockException ex) throws BlockException {
        throw ex;
    }
}