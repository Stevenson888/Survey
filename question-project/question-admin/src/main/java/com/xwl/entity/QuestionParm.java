package com.xwl.entity;

//import com.xwl.entity.QuestionChoice;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class QuestionParm implements Serializable {

    //试题id
    @JsonSerialize(using = ToStringSerializer.class)
    private Long questionId;
    //问卷id
    @JsonSerialize(using = ToStringSerializer.class)
    private Long paperId;

    //试题序号
    private int sort;
    //试题类型
    private int type;
    //标题
    private String title;

    private List<QuestionOption> questionOptionList;
}