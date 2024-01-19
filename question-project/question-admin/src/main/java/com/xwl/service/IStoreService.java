package com.xwl.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xwl.entity.Answer;
import com.xwl.entity.ResultQuestionOption;
import com.xwl.entity.Store;
import com.xwl.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 
 * @since 2023-07-30
 */
public interface IStoreService extends IService<Store> {

//     List<Long> getStoreIdListByAllAdmin();
     List<Long> getCustomerIdListByAllAdmin();
     List<Long> getCustomerIdListByAdminUserId(Long userId);

//    List<Store> getStoresByUserId(Integer id, String name);

    List<Store> getStoresByAdminUserId(Long userId);
    List<Long> getStoreIdListByAdminUserId(Long userId);

//    List<Long> getStoreListByUserId(Long userId);
    List<Store> getStoreListByDevWithParm( Store store );
    List<Store> getStoreListByAdminWithParm( Store store );

//    IPage<Store> getStoresByAdminUserId(Page<Store> page, Long userId);

    //已完成门店问卷
    Integer getFinishedStoreAnswerByStoreId(Long storeId);
    //完成消费者问卷
    Integer getFinishedCustomerAnswerByStoreId(Long storeId);

    //可参与门店问卷
//    List<Answer> getAvailableStoreAnswerByCityCode(String cityCode);
    List<Answer> getAvailableStoreAnswerByDistrictCode(String districtCode);

    Integer deleteAuthForStore(Long storeId);

    Integer deleteAuthForUser(Long userId);

    Integer insertStore(Store store);
    Integer updateStore(Store store);

    Integer selectStoreIdByLicenseNo(String licenseNo);

}
