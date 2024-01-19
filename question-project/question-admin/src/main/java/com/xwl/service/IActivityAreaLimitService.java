package com.xwl.service;

import com.xwl.entity.ActivityAreaLimit;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 
 * @since 2023-08-13
 */
public interface IActivityAreaLimitService extends IService<ActivityAreaLimit> {

    int deleteAllActivityAreaLimitByActivityId(Long activityId);

}
