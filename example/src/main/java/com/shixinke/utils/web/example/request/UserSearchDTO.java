package com.shixinke.utils.web.example.request;

import com.shixinke.utils.web.common.SearchDTO;
import lombok.Data;

import java.util.*;

/**
 * @author shixinke
 * crated 19-3-29 下午3:55
 * @version 1.0
 */

@Data
public class UserSearchDTO extends SearchDTO {
    private Long userId;
    private String nickname;
    private SortedSet<Long> itemIds;
    private Map<String, String> configMap;
}
