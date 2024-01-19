package com.xwl.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xwl.entity.Answer;
import com.xwl.entity.AppUser;
import com.xwl.entity.Store;
import com.xwl.mapper.AppUserMapper;
import com.xwl.mapper.CustomerStoreMapper;
import com.xwl.service.IAnswerService;
import com.xwl.service.IAppUserService;
import com.xwl.service.IStoreService;
import com.xwl.utils.ResultUtils;
import com.xwl.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 
 * @since 2023-08-05
 */
@Service
public class AppUserServiceImpl extends ServiceImpl<AppUserMapper, AppUser> implements IAppUserService {

    @Autowired
    private IAppUserService appUserService;
    @Resource
    private CustomerStoreMapper customerStoreMapper;
    @Resource
    private AppUserMapper appUserMapper;
    @Resource
    private IStoreService storeService;
    @Resource
    private IAnswerService answerService;


}
