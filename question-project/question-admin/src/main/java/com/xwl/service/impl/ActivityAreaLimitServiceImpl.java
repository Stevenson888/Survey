package com.xwl.service.impl;

import com.xwl.entity.ActivityAreaLimit;
import com.xwl.mapper.ActivityAreaLimitMapper;
import com.xwl.mapper.ProjectActivityMapper;
import com.xwl.service.IActivityAreaLimitService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 
 * @since 2023-08-13
 */
@Service
public class ActivityAreaLimitServiceImpl extends ServiceImpl<ActivityAreaLimitMapper, ActivityAreaLimit> implements IActivityAreaLimitService {

    @Resource
    private ActivityAreaLimitMapper activityAreaLimitMapper;


    @Override
    public int deleteAllActivityAreaLimitByActivityId(Long activityId) {
        return activityAreaLimitMapper.deleteAllActivityAreaLimitByActivityId(activityId);
    }

}
