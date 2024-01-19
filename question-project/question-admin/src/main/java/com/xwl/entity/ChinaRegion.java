package com.xwl.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 地区码表
 * </p>
 *
 * @author 
 * @since 2023-08-13
 */
@Getter
@Setter
@TableName("china_region")
@ApiModel(value = "ChinaRegion对象", description = "地区码表")
public class ChinaRegion implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("地区id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("地区编码")
    private String areaCode;

    @ApiModelProperty("地区名")
    private String areaName;

    @ApiModelProperty("地区级别")
    private String level;

    @ApiModelProperty("城市编码")
    private String cityCode;

    @ApiModelProperty("城市中心点（即：经纬度坐标）")
    private String center;

    @ApiModelProperty("地区父节点")
    private Integer parentId;

    @ApiModelProperty("是否启用 1启用 0禁用")
    private Integer isEnable;

    @TableField(exist = false)
    private List<ChinaRegion> child;  //子级元素集合

}
