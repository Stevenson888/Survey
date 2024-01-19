package com.xwl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xwl.entity.Store;
import com.xwl.entity.User;
import com.xwl.entity.UserDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 用户的数据访问层
 *  继承 mybatis plus 的 BaseMapper，
 *  可以使用他提供的通用的增删改查的方法
 *
 */
public interface UserMapper extends BaseMapper<User> {

    @Update("update m_user set password = #{user.newPassword} where username = #{user.username} and password = #{user.password}")
    int updatePassword( @Param("user") User user);

    @Update("update m_user set password = #{user.newPassword} where username = #{user.username} ")
    int updateOthersPassword( @Param("user") User user);

    @Update("update m_user set status = #{user.status} where username = #{user.username} ")
    int updateUserStatus( @Param("user") User user);


    // 一对多查询
//    List<User> getStoresByUserId(@Param("userId") Long userId, @Param("name") String name );

//    @Select("select * from store where create_uid in (select app_user_id from m_user where (user_id=#{userId}} or create_uid=#{userId}) and app_user_id != 0)")
//    List<Store> getStoresByAdminUserId(@Param("userId") Long userId);

}
