package com.xwl.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author 
 * @since 2023-08-12
 */
@Deprecated
@Data
public class ProjectVo implements Serializable {
    private Integer id;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long projectId;

    private String code;
    private String name;
    private String info;
    private String content;
    private String productCategory;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long createMuid;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long updateMuid;

    private Date startTime;
    private Date endTime;
    private Date createTime;
    private Date updateTime;

    private List<String>  activityAreaLimitCityCodeList;

}
