package com.xwl.service;

import com.xwl.entity.Question;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xwl.entity.QuestionParm;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 
 * @since 2023-05-23
 */
public interface IQuestionService extends IService<Question> {

    //保存试题
    void saveQuestion(Long paperId, List<Question> questionList);

    //试题回显
    List<Question> getQuestionList(Long paperId);

    //根据问卷id查询试题列表回显
    List<QuestionParm> getQuestionParmList(Long paperId);

     Question getQuestionByQuestionId(Long questionId);
     int getIdByQuestionId(Long questionId);


}
