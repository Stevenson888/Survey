package com.xwl.service;

import com.xwl.entity.ProjectActivity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 
 * @since 2023-08-12
 */
public interface IProjectActivityService extends IService<ProjectActivity> {

    List<Long> getActivityIdsByProjectId(Long projectId);

    int deleteProjectActivityByProjectId(Long projectId);

    int deleteProjectActivityByActivityId(Long activityId);

}
