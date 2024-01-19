package com.xwl.mapper;

import com.xwl.entity.ProjectActivity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 
 * @since 2023-08-12
 */
public interface ProjectActivityMapper extends BaseMapper<ProjectActivity> {

    @Select("select activity_id from project_activity where project_id = #{projectId}")
    List<Long> getActivityIdsByProjectId(@Param("projectId")Long projectId);

    @Delete("delete from project_activity where project_id = #{projectId}")
    int deleteProjectActivityByProjectId(@Param("projectId") Long projectId);

    @Delete("delete from project_activity where activity_id = #{activityId}")
    int deleteProjectActivityByActivityId(@Param("activityId") Long activityId);


}
