package com.xwl.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 零售户门店
 * </p>
 *
 * @author 
 * @since 2024-01-13
 */
@Getter
@Setter
@TableName("retailer_store")
@ApiModel(value = "RetailerStore对象", description = "零售户门店")
public class RetailerStore implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("门店id")
    private Long storeId;

    @ApiModelProperty("是否当前选择的门店")
    private Integer isCurrent;

    private Date createTime;

    private Integer isDelete;

    @ApiModelProperty("删除绑定关系时间")
    private Date deleteTime;


}
