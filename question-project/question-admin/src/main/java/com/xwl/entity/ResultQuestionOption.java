package com.xwl.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ResultQuestionOption implements Serializable {

    //活动id
    @JsonSerialize(using = ToStringSerializer.class)
    private Long activityId;
    //问卷id
    @JsonSerialize(using = ToStringSerializer.class)
    private Long paperId;

    //问卷试题id
    @JsonSerialize(using = ToStringSerializer.class)
    private Long questionId;
    //问卷试题Title
    private String questionTitle;
    //问卷试题类型
    private String questionType;
    //问卷试题的选项id
    @JsonSerialize(using = ToStringSerializer.class)
    private Long optionId;

    //答卷id
    @JsonSerialize(using = ToStringSerializer.class)
    private Long answerId;
    //答卷中,选中选项id
    @JsonSerialize(using = ToStringSerializer.class)
    private Long answerDetailId;
    ///答卷中,选项内容
    private String content;
    //答卷中,选项顺序(ABCD)
    private int sort;

    //答卷中,选项总条数
    private int total;

    //门店id
    @JsonSerialize(using = ToStringSerializer.class)
    private Long storeId;
    //用户id
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;
    //客户经理id
    @JsonSerialize(using = ToStringSerializer.class)
    private Long partnerUid;

    private List<ResultQuestionOption> resultQuestionOptionList;


}