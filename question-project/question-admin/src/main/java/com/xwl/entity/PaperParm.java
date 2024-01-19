package com.xwl.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class PaperParm implements Serializable {
    //当前页
    private Long currentPage;
    //页容量 每页的条数
    private Long pageSize;
    //问卷标题
    private String title;
}
