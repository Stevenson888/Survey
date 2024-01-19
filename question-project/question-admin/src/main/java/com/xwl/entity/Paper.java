package com.xwl.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.xwl.entity.Question;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data  //自动生成get和set方法
@TableName("paper") //指明该实体对应的数据库表名是 paper
public class Paper implements Serializable {

//    //主键 自动递增
//    @TableId(type = IdType.AUTO)
//@JsonSerialize(using = ToStringSerializer.class)
//    private Long questionId;
//
//    //问卷标题
//    private String questionTitle;
//    //问卷描述
//    private String questionDesc;
//    //图片路径
//    private String questionImg;
//    //问卷状态 0：关闭 1：调查中
//    private String questionStatus;
//    //序号
//@JsonSerialize(using = ToStringSerializer.class)
//    private Long questionOrder;
//
//    //问卷对应的试题列表,不属于问卷表，需要排除
//    @TableField(exist = false)
//    private List<SysPaper> listPaper;


    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @JsonSerialize(using = ToStringSerializer.class)  //可以防止雪花算法生成的ID前后端显示不一致
    @ApiModelProperty("问卷编号")
    private Long paperId;

    @ApiModelProperty("问卷标题")
    private String title;

    @ApiModelProperty("问卷副标题")
    private String subTitle;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("问卷类型 1:固定问卷；2:随机问卷")
    private Integer category;

    @ApiModelProperty("图片url")
    private String pictureUrl;

    @ApiModelProperty("问卷完成后是否可以查看各题选项占比")
    private Integer isFinishView;

    @ApiModelProperty("创建人id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long createUid;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty("最后修改人id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long updateUid;

    @ApiModelProperty("最后修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    //问卷对应的试题列表,不属于问卷表，需要排除
    @TableField(exist = false)
    private List<Question> questionList;

    @TableField(exist = false)
    private List<Question> selectQuestionList;

    @TableField(exist = false)
    private List<Question> blankQuestionList;

    @TableField(exist = false)
    private List<Activity> activityList;

    @TableField(exist = false)
    private Project project;

}
