package com.shixinke.utils.web.example.request;

import com.shixinke.utils.web.common.SearchDTO;
import lombok.Data;

/**
 * coupon search data transfer object
 * @author shixinke
 */
@Data
public class CouponSearchDTO extends SearchDTO {

    private Integer type;
}
