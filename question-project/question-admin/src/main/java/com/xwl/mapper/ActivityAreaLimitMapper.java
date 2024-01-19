package com.xwl.mapper;

import com.xwl.entity.ActivityAreaLimit;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 
 * @since 2023-08-13
 */
public interface ActivityAreaLimitMapper extends BaseMapper<ActivityAreaLimit> {

    @Delete("delete from activity_area_limit where activity_id = #{activityId}")
    int deleteAllActivityAreaLimitByActivityId(@Param("activityId") Long activityId);

}
