package com.fzujxl.aicomputerplatform.dto;

import com.github.pagehelper.PageInfo;
import java.util.List;

/**
 * 通用分页响应结果
 * @param <T> 分页数据类型
 */
public record PageResultResponse<T>(
    int pageNum,
    int pageSize,
    Long total,
    int totalPages,
    List<T> list
) {
    public static <T> PageResultResponse<T> of(PageInfo<T> pageInfo) {
        return new PageResultResponse<>(
                pageInfo.getPageNum(),
                pageInfo.getPageSize(),
                pageInfo.getTotal(),
                pageInfo.getPages(),
                pageInfo.getList()
        );
    }
}