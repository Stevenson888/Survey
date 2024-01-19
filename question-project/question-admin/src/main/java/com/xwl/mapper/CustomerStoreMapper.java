package com.xwl.mapper;

import com.xwl.entity.CustomerStore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xwl.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 消费者门店关联表 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2023-08-08
 */
public interface CustomerStoreMapper extends BaseMapper<CustomerStore> {

//    @Select("select menu_id from m_role_menu where role_id = #{roleId}")
//    List<Integer> selectByRoleId(@Param("roleId")Integer roleId);

    @Select("select user_id from customer_store where store_id = #{storeId}")
    List<Long> getCustomerIdListByStoreId(@Param("storeId")Long storeId);

    @Select("select store_id from customer_store where user_id = #{userId}")
    List<Long> getStoreIdByCustomerId(@Param("userId")Long userId);

}
