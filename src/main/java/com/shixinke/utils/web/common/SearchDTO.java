package com.shixinke.utils.web.common;

/**
 * @author shixinke
 * @version 1.0
 * @Description 搜索传输基类
 * @Date 19-2-22 下午4:38
 */
public class SearchDTO implements RequestDTO {
    private Integer page;
    private Integer pageSize;
    private Integer offset;

    public Integer getPage() {
        return checkNum(page);
    }

    public void setPage(Integer page) {
        this.page = checkNum(page);
    }

    public Integer getPageSize() {
        return checkNum(pageSize);
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = checkNum(pageSize);
    }

    public Integer getOffset() {
        if (offset == null || offset < 0) {
            return (page - 1) * pageSize;
        }
        return offset;
    }

    public void setOffset(Integer offset) {
        if (offset == null || offset < 0) {
            offset = 0;
        }
        this.offset = offset;
    }

    private Integer checkNum(Integer num) {
        if (num == null || num < 1) {
            return 1;
        }
        return num;
    }

    @Override
    public String toString() {
        return "SearchDTO{" +
                "page=" + page +
                ", pageSize=" + pageSize +
                ", offset=" + offset +
                '}';
    }
}
