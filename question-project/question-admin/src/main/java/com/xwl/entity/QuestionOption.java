package com.xwl.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
 * @since 2023-05-24
 */
@Getter
@Setter
@TableName("question_option")
@ApiModel(value = "QuestionOption对象", description = "")
public class QuestionOption implements Serializable {

    private static final long serialVersionUID = 1L;

//1.选项
    @ApiModelProperty("选项表")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    //问卷试题的选项id
    @JsonSerialize(using = ToStringSerializer.class)  //可以防止雪花算法生成的ID前后端显示不一致
    @ApiModelProperty("选项id")
    private Long optionId;

    @ApiModelProperty("答卷中-选项顺序（ABCD）")
    private Integer sort;

    @ApiModelProperty("选项分值（为结果页面提供总分）")
    private Integer score;

    @ApiModelProperty("是否排他")
    private Integer exclusive;

    @ApiModelProperty("创建时间")
    private Date createTime;

    //问卷中,选项内容 questionOptionContent
    @ApiModelProperty("选项内容")
    private String content;

    //问卷中：选项是否被选中
    @TableField(exist = false)
    private Boolean isSelected;

//    @TableField(exist = false)
//    private Integer total;


//2.活动-问卷
    //活动id
    @TableField(exist = false)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long activityId;
    //问卷id
    @TableField(exist = false)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long paperId;


//3.题目
    // 试题id
    @JsonSerialize(using = ToStringSerializer.class)  //可以防止雪花算法生成的ID前后端显示不一致
    @ApiModelProperty("试题id")
    private Long questionId;
    //问卷试题Title
    @TableField(exist = false)
    private String questionTitle;
    //问卷试题类型
    @TableField(exist = false)
    private String questionType;


//4.答卷
    //答卷id
    @TableField(exist = false)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long answerId;
    //答卷中,选中选项id
    @TableField(exist = false)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long answerDetailId;
    //答卷中,选中选项的内容
    @TableField(exist = false)
    private String answerDetailContent;

//    //答卷中,选中选项的内容
//    @TableField(exist = false)
//    private String answerDetailContent;

    //答卷中,选项总条数
    @TableField(exist = false)
    private int optionTotal;
    //答卷中,此选项被选中的比例
    @TableField(exist = false)
    private double optionProportion;

//5.管理
    //门店id
    @TableField(exist = false)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long storeId;
    //用户id
    @TableField(exist = false)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;
    //客户经理id
    @TableField(exist = false)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long partnerUid;


}
