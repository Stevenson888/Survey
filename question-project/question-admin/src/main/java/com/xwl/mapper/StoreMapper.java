package com.xwl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xwl.entity.Answer;
import com.xwl.entity.Store;
import com.xwl.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 
 * @since 2023-07-30
 */
public interface StoreMapper extends BaseMapper<Store> {

//    List<Long> getStoreIdListByAllAdmin();
    List<Long> getCustomerIdListByAllAdmin();
    List<Long> getCustomerIdListByAdminUserId(@Param("userId") Long userId);


    @Select("select store_id from store where is_delete=0 and create_uid in (select app_user_id from m_user where create_uid=#{userId} and app_user_id!=0 ) ")
    List<Long> getStoreIdListByAdminUserId(@Param("userId") Long userId);
    @Select("select * from store where is_delete=0 and create_uid in (select app_user_id from m_user where create_uid=#{userId} and app_user_id!=0 ) ")
    List<Store> getStoresByAdminUserId(@Param("userId") Long userId);


//    // getStoreList - Dev
//    @Select("select store_id from store where create_uid=#{userId} and is_delete=0")
//    List<Long> getStoreListByUserId(@Param("userId") Long userId);

    // getStoreList - Dev
    List<Store> getStoreListByDevWithParm(@Param("store") Store store );
    // getStoreList - Admin
    List<Store> getStoreListByAdminWithParm(@Param("store") Store store);

    //已完成门店问卷
    @Select("select count(*) from answer where store_id=#{storeId} and activity_id in (select activity_id from activity where for_user_type = 2)")
    Integer getFinishedStoreAnswerByStoreId(@Param("storeId") Long storeId);
    //完成消费者问卷
    @Select("select count(*) from answer where store_id=#{storeId} and activity_id in (select activity_id from activity where for_user_type = 1)")
    Integer getFinishedCustomerAnswerByStoreId(@Param("storeId") Long storeId);

    //可参与门店问卷
//    @Select("select count(*) from activity a left join activity_area_limit l on a.activity_id = l.activity_id where l.city_code is null and end_time > NOW() and for_user_type in (1, 2) or l.city_code=#{cityCode}")
//    List<Answer> getAvailableStoreAnswerByCityCode(@Param("cityCode") String cityCode);
    @Select("select count(*) from activity a left join activity_area_limit l on a.activity_id = l.activity_id where l.city_code is null and end_time > NOW() and for_user_type in (1, 2) or l.city_code=#{districtCode}")
    List<Answer> getAvailableStoreAnswerByDistrictCode(@Param("districtCode") String districtCode);

    @Update("update store set user_id=0, status=0 where store_id=#{storeId}")
    Integer deleteAuthForStore(@Param("storeId") Long storeId);
    @Update("update user set user_type=1 where user_id=#{userId}")
    Integer deleteAuthForUser(@Param("userId") Long userId);

    Integer insertStore( @Param("store") Store store);
    Integer updateStore( @Param("store") Store store);

    Integer selectStoreIdByLicenseNo(@Param("licenseNo") String licenseNo);

}
