package com.xwl.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 访问员
 * </p>
 *
 * @author 
 * @since 2023-07-31
 */
@Getter
@Setter
@ApiModel(value = "Partner对象", description = "访问员")
public class Partner implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String mobile;

    private String name;

    @ApiModelProperty("4：访问员；8：客户经理")
    private Integer type;


}
