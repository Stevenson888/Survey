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
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author 
 * @since 2023-07-30
 */
@Data
public class StoreVo implements Serializable {

    private Integer id;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long storeId;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    private String name;
    private String licenseNo;
    private String logo;
    private String telephone;
    private String businessHours;
    private Integer level;
    private Integer tradeLevel;
    private Integer starLevel;
    private String provinceCode;
    private String provinceName;
    private String cityCode;
    private String cityName;
    private String districtCode;
    private String districtName;
    private String detailAddress;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private String geoPoint;
    private String location;
    private Integer status;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long createUid;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Integer isEnable;
    private Boolean isDelete;

    private String realName;
    private String roleFlag;
    private String createUserName;
    private AppUser appUser;
    private Integer customerAmountOfStore;      //零售店对应的消费者数量
    private Integer answerAmountOfStore;        //零售店答卷数量
    private Integer answerAmountOfUser;         //消费者答卷数量
    private Integer answerTotalAmount;          //总答卷数量
    private Integer finishedStoreAnswer;        //已完成门店问卷
    private Integer finishedCustomerAnswer;     //已完成消费者问卷
    private Integer availableStoreAnswer;       //可参与门店问卷

}
