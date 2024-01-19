package com.xwl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xwl.entity.QuestionOption;
import com.xwl.entity.ResultQuestionOption;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 
 * @since 2023-05-24
 */
public interface QuestionOptionMapper extends BaseMapper<QuestionOption> {

    //根据试卷id(paperId),查询试卷中所有试题所有选项的列表
    List<QuestionOption> getQuestionOptionList(@Param("paperId") Long paperId);

    //根据试题id(questionId),查询所有选项的列表
    List<QuestionOption> getOptionList(@Param("questionId") Long questionId);

    //查询选项的票数统计
    List<ResultQuestionOption> getQuestionOptionResultList(@Param("paperId") Long paperId);

    // 查询试题中所有选项的总票数 计算：选择题每个试题的 所有选项 一共选择多少次
//    List<ResultQuestionOption> getQuestionResults(@Param("activityId") Long activityId);

    // 查询选项的票数统计 计算： 选择题计算 每个选项 选择了多少次
    List<QuestionOption> getSelectQuestionOptionResultsByActivityId(@Param("activityId") Long activityId);
    // 查询：填空题 题干+选项
    List<QuestionOption> getBlankQuestionOptionResultsByActivityId(@Param("activityId") Long activityId);

//    List<QuestionOption> getSelectQuestionOptionResultsByAnswerId(@Param("answerId") Long answerId);
//    List<QuestionOption> getBlankQuestionOptionResultsByAnswerId(@Param("answerId") Long answerId);




}
