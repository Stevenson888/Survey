package com.xwl.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author 
 * @since 2023-05-23
 */
@Getter
@Setter
@ApiModel(value = "Question对象", description = "")
public class Question implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("试题表")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @JsonSerialize(using = ToStringSerializer.class)  //可以防止雪花算法生成的ID前后端显示不一致
    @ApiModelProperty("试题id")
    private Long questionId;

    @ApiModelProperty("问题")
    private String title;

    @ApiModelProperty("1单选、2复选、3文本、4产品、5图片")
    private Integer type;

    @ApiModelProperty("是否必选 1是；0否")
    private Integer isRequire;

    @ApiModelProperty("随机抽取概率（1-100）")
    private Integer probability;

    @ApiModelProperty("试题层级")
    private Integer level;

    @ApiModelProperty("试题标签（1基础信息问题，2行为态度问题）")
    private Integer label;

    @ApiModelProperty("距离上次出现该问题的等待天数，0为无需等待")
    private Integer waitDays;

    @ApiModelProperty("创建人")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long createUid;

    @ApiModelProperty("创建时间")
    private Date createTime;

    //问卷中,每道题目的所有选项,在所有答卷中,被选中的总和
    @TableField(exist = false)
    private int questionTotal;

    //答卷中,无选项填空题(没有questionOption)的作答
    @TableField(exist = false)
    private String answerDetailContent;

    //每道题目的选项列表
    @TableField(exist = false)
    private List<QuestionOption> questionOptionList;

    //每道题目的选项列表
//    @TableField(exist = false)
//    private List<ResultQuestionOption> resultQuestionOptionList;


//    @TableField(exist = false)
//    private Integer counter1;
//    @TableField(exist = false)
//    private Integer counter2;
//    @TableField(exist = false)
//    private Integer counter3;
//    @TableField(exist = false)
//    private Integer counter4;
//    @TableField(exist = false)
//    private Integer counter5;
//    @TableField(exist = false)
//    private Integer counterSum;

    @TableField(exist = false)
    private double c1prop;
    @TableField(exist = false)
    private double c2prop;
    @TableField(exist = false)
    private double c3prop;
    @TableField(exist = false)
    private double c4prop;
    @TableField(exist = false)
    private double c5prop;

    @TableField(exist = false)
    private double minValue;
    @TableField(exist = false)
    private double num1;
    @TableField(exist = false)
    private double num2;
    @TableField(exist = false)
    private double num3;
    @TableField(exist = false)
    private double num4;
    @TableField(exist = false)
    private double maxValue;
    @TableField(exist = false)
    private double interval;


}
