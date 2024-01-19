package com.xwl.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jdk.nashorn.internal.objects.annotations.Setter;
import lombok.Data;
import lombok.ToString;

//import org.locationtech.jts.geom.Point;
//import org.locationtech.jts.geom.Geometry;


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
 * @since 2023-07-30
 */
@Data
@ApiModel(value = "Store对象", description = "")
@ToString
public class Store implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("门店表")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @JsonSerialize(using = ToStringSerializer.class)  //可以防止雪花算法生成的ID前后端显示不一致
    @ApiModelProperty("门店id")
    private Long storeId;

    @JsonSerialize(using = ToStringSerializer.class)  //可以防止雪花算法生成的ID前后端显示不一致
    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("门店名称")
    private String name;

    @ApiModelProperty("专卖证号")
    private String licenseNo;

    @ApiModelProperty("门店logo")
    private String logo;

    @ApiModelProperty("电话号码")
    private String telephone;

    @ApiModelProperty("营业时间")
    private String businessHours;

    @ApiModelProperty("门店等级")
    private Integer level;

    @ApiModelProperty("行业等级")
    private Integer tradeLevel;

    @ApiModelProperty("门店星级0-10，共5个满星")
    private Integer starLevel;

    @ApiModelProperty("省份编号")
    private String provinceCode;

    @ApiModelProperty("省份名称")
    private String provinceName;

    @ApiModelProperty("城市编号")
    private String cityCode;

    @ApiModelProperty("城市名称")
    private String cityName;

    @ApiModelProperty("区域编号")
    private String districtCode;

    @ApiModelProperty("区域名称")
    private String districtName;

    @ApiModelProperty("详细地址")
    private String detailAddress;

    @ApiModelProperty("经度")
    private BigDecimal longitude;

    @ApiModelProperty("纬度")
    private BigDecimal latitude;

    private String geoPoint;

    @ApiModelProperty("定位位置")
    private String location;

    @ApiModelProperty("门店状态 1正常")
    private Integer status;

    @JsonSerialize(using = ToStringSerializer.class)  //可以防止雪花算法生成的ID前后端显示不一致
    @ApiModelProperty("创建人id")
    private Long createUid;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("最后修改时间")
    private Date updateTime;

    @ApiModelProperty("是否启用,启用后可被消费者看到")
    private Boolean isEnable;

    @ApiModelProperty("是否删除")
    private Boolean isDelete;

    @ApiModelProperty("bind_time")
    private Date bindTime;

    @TableField(exist = false)
    private String realName;
    @TableField(exist = false)
    private String roleFlag;
    @TableField(exist = false)
    private AppUser storeOwner;
    @TableField(exist = false)
    private String createUserName;
    @TableField(exist = false)
    private AppUser createPartner;
    @TableField(exist = false)
    private Integer customerAmountOfStore;      //零售店对应的消费者数量
    @TableField(exist = false)
    private Integer answerAmountOfStore;        //零售店答卷数量
    @TableField(exist = false)
    private Integer answerAmountOfUser;         //消费者答卷数量
    @TableField(exist = false)
    private Integer answerTotalAmount;          //总答卷数量
    @TableField(exist = false)
    private Integer finishedStoreAnswer;        //已完成门店问卷
    @TableField(exist = false)
    private Integer finishedCustomerAnswer;     //已完成消费者问卷
    @TableField(exist = false)
    private Integer availableStoreAnswer;       //可参与门店问卷

}
