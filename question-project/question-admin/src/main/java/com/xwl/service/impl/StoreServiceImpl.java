package com.xwl.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xwl.entity.Answer;
import com.xwl.entity.ResultQuestionOption;
import com.xwl.entity.Store;
import com.xwl.entity.User;
import com.xwl.mapper.StoreMapper;
import com.xwl.service.IStoreService;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 
 * @since 2023-07-30
 */
@Service
public class StoreServiceImpl extends ServiceImpl<StoreMapper, Store> implements IStoreService {

//    @Override
//    public List<Store> getStoresByUserId(Integer id , String name) {
//        return this.baseMapper.getStoresByUserId(id, name);
//    }

//    @Override
//    public List<Long> getStoreIdListByAllAdmin() {
//        return this.baseMapper.getStoreIdListByAllAdmin();
//    }
    @Override
    public List<Long> getCustomerIdListByAllAdmin() {
        return this.baseMapper.getCustomerIdListByAllAdmin();
    }


    @Override
    public List<Store> getStoresByAdminUserId(Long userId) {
        return this.baseMapper.getStoresByAdminUserId(userId);
    }
    @Override
    public List<Long> getStoreIdListByAdminUserId(Long userId) {
        return this.baseMapper.getStoreIdListByAdminUserId(userId);
    }
    @Override
    public List<Long> getCustomerIdListByAdminUserId(Long userId) {
        return this.baseMapper.getCustomerIdListByAdminUserId(userId);
    }

//    @Override
//    public List<Long> getStoreListByUserId(Long userId) {
//        return this.baseMapper.getStoreListByUserId(userId);
//    }
    @Override
    public List<Store> getStoreListByDevWithParm(Store store) {
        return this.baseMapper.getStoreListByDevWithParm(store);
    }
    @Override
    public List<Store> getStoreListByAdminWithParm(Store store) {
        return this.baseMapper.getStoreListByAdminWithParm(store);
    }
//    @Override
//    public IPage<Store> getStoresByAdminUserId(Page<Store> page, Long userId) {
//        return this.baseMapper.getStoresByAdminUserId(page, userId);
//    }

    @Override
    public Integer getFinishedStoreAnswerByStoreId(Long storeId) {
        return this.baseMapper.getFinishedStoreAnswerByStoreId(storeId);
    }
    @Override
    public Integer getFinishedCustomerAnswerByStoreId(Long storeId) {
        return this.baseMapper.getFinishedCustomerAnswerByStoreId(storeId);
    }

//    @Override
//    public List<Answer> getAvailableStoreAnswerByCityCode(String cityCode) {
//        return this.baseMapper.getAvailableStoreAnswerByCityCode(cityCode);
//    }
    @Override
    public List<Answer> getAvailableStoreAnswerByDistrictCode(String districtCode) {
        return this.baseMapper.getAvailableStoreAnswerByDistrictCode(districtCode);
    }

    @Override
    public Integer deleteAuthForStore(Long storeId) {
        return this.baseMapper.deleteAuthForStore(storeId);
    }

    @Override
    public Integer deleteAuthForUser(Long userId) {
        return this.baseMapper.deleteAuthForUser(userId);
    }

    @Override
    public Integer insertStore(Store store) {
        return this.baseMapper.insertStore(store);
    }

    @Override
    public Integer updateStore(Store store) {
        return this.baseMapper.updateStore(store);
    }

    @Override
    public Integer selectStoreIdByLicenseNo(String licenseNo) {
        return this.baseMapper.selectStoreIdByLicenseNo(licenseNo);
    }

}
