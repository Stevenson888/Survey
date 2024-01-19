package com.xwl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xwl.entity.QuestionOption;
import com.xwl.entity.ResultQuestionOption;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 
 * @since 2023-05-24
 */
public interface IQuestionOptionService extends IService<QuestionOption> {

    List<QuestionOption> getQuestionOptionList(Long paperId);

    //查询选项的票数统计
    List<ResultQuestionOption> getQuestionOptionResultList(Long paperId);


    List<QuestionOption> getSelectQuestionOptionResultsByActivityId (Long activityId);
    List<QuestionOption> getBlankQuestionOptionResultsByActivityId (Long activityId);

//    List<QuestionOption> getSelectQuestionOptionResultsByAnswerId (Long answerId);
//    List<QuestionOption> getBlankQuestionOptionResultsByAnswerId (Long answerId);

}
