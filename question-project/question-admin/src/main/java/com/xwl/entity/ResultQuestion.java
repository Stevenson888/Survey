package com.xwl.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ResultQuestion implements Serializable {

    //试题id
    @JsonSerialize(using = ToStringSerializer.class)
    private Long questionId;
    //问卷id
    @JsonSerialize(using = ToStringSerializer.class)
    private Long paperId;

    //试题类型
    private int type;
    //试题顺序
    private int sort;
    //试题标题
    private String title;

    //单选和多选对应的选项
    private List<ResultQuestionOption> questionOptionResultList;
}