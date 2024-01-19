package com.xwl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xwl.entity.Role;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author
 * @since 2022-02-10
 */
public interface RoleMapper extends BaseMapper<Role> {

    @Select("select id from m_role where role_flag = #{roleFlag}")
    Integer selectByFlag(@Param("roleFlag") String roleFlag);
}
