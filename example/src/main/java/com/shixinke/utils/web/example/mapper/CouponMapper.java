package com.shixinke.utils.web.example.mapper;

import com.shixinke.utils.web.annotation.cat.CatLog;
import com.shixinke.utils.web.annotation.cat.CatLogType;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * coupon mapper
 * @author shixinke
 */
@Repository
public interface CouponMapper {

    /**
     * query coupon list
     * @param type
     * @param useType
     * @return
     */
    @CatLog(methodType = CatLogType.SQL)
    @Select("select * FROM sms_coupon WHERE type = #{type} and use_type=#{useType} LIMIT 5")
    public List<Map<String, Object>> queryList(int type, int useType);

}
