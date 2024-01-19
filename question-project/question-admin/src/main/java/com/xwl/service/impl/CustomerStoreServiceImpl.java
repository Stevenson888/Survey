package com.xwl.service.impl;

import com.xwl.entity.CustomerStore;
import com.xwl.mapper.CustomerStoreMapper;
import com.xwl.service.ICustomerStoreService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 消费者门店关联表 服务实现类
 * </p>
 *
 * @author 
 * @since 2023-08-08
 */
@Service
public class CustomerStoreServiceImpl extends ServiceImpl<CustomerStoreMapper, CustomerStore> implements ICustomerStoreService {

}
