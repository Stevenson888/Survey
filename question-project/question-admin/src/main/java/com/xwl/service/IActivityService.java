package com.xwl.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xwl.entity.*;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 
 * @since 2023-08-03
 */
public interface IActivityService extends IService<Activity> {

    List<ActivityVo> getAllUnselectedActivityVoList();

    Boolean isSelectedActivity (Long activityId);

    Long getProjectIdFromSelectedActivity (Long activityId);

    List<Question> getQuestionListByActivityId(Long activityId);

//    List<String> getActivityAreaNameListByActivityId(Long activityId);

 }
