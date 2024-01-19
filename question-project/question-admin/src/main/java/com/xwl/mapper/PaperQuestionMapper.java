package com.xwl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xwl.entity.PaperQuestion;
import com.xwl.entity.Question;
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
 * @since 2023-05-24
 */
public interface PaperQuestionMapper extends BaseMapper<PaperQuestion> {

//    @Delete("delete from sys_role_menu where role_id = #{roleId}")
//    int deleteByRoleId(@Param("roleId") Integer roleId);

    @Select("select question_id from paper_question where paper_Id = #{paperId}")
    List<Long> getQuestionIdListByPaperId(@Param("paperId")Long paperId);

    @Select("select * from paper_question where paper_Id = #{paperId}")
    List<PaperQuestion> getQuestionListByPaperId(@Param("paperId")Long paperId);

}
