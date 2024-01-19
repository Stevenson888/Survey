package com.xwl.service.impl;

import com.xwl.entity.ProjectActivity;
import com.xwl.mapper.PaperQuestionMapper;
import com.xwl.mapper.ProjectActivityMapper;
import com.xwl.mapper.ProjectMapper;
import com.xwl.service.IProjectActivityService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 
 * @since 2023-08-12
 */
@Service
public class ProjectActivityServiceImpl extends ServiceImpl<ProjectActivityMapper, ProjectActivity> implements IProjectActivityService {

    @Resource
    private ProjectActivityMapper projectActivityMapper;

    @Override
    public List<Long> getActivityIdsByProjectId(Long projectId) {
        return projectActivityMapper.getActivityIdsByProjectId(projectId);
    }

    @Override
    public int deleteProjectActivityByProjectId(Long projectId) {
        return projectActivityMapper.deleteProjectActivityByProjectId(projectId);
    }

    @Override
    public int deleteProjectActivityByActivityId(Long activityId) {
        return projectActivityMapper.deleteProjectActivityByActivityId(activityId);
    }


}
