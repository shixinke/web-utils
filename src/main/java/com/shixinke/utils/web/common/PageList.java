package com.shixinke.utils.web.common;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Pagination
 * @author shixinke
 * @version 1.0
 * created 19-2-22 下午4:39
 */
@Data
public class PageList<T> {
    /**
     * total records
     */
    private Long total;
    /**
     * page number
     */
    private Integer page;
    /**
     * page size
     */
    private Integer pageSize;
    /**
     * total pages
     */
    private Integer pages;
    /**
     * data list
     */
    private List<T> list;


    public PageList(long total, int page, int pageSize, List<T> list) {
        this.total = total;
        this.page = page;
        this.pageSize = pageSize;
        this.pages = calcPages(total, pageSize);
        if (list == null) {
            list = new ArrayList(0);
        }
        this.list = list;
    }

    public PageList() {
        this(0L, 1, 10, null);
    }

    public PageList(long total, int pageSize) {
        this(total, 1, pageSize, null);
    }

    public PageList(long total, int page, int pageSize) {
        this(total, page, pageSize, null);
    }

    public static int calcPages(long total, int pageSize) {
        if (total <= pageSize) {
            return 1;
        }
        int remain = (int) (total % pageSize);
        int pages = (int) (total / pageSize);
        if (remain != 0) {
            pages += 1;
        }
        return pages;
    }


}
