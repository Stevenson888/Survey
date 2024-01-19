package com.xwl.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author 
 * @since 2023-07-31
 */
@Getter
@Setter
@TableName("answer_detail")
@ApiModel(value = "AnswerDetail对象", description = "")
public class AnswerDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("答题详情表")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("详情id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long answerDetailId;

    @ApiModelProperty("答卷id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long answerId;

    @ApiModelProperty("试题id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long questionId;

    @ApiModelProperty("试题内容")
    private String questionTitle;

    @ApiModelProperty("试题类型，同试题表")
    private Integer questionType;

    @ApiModelProperty("选项id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long optionId;

    @ApiModelProperty("选项内容")
    private String content;


}
