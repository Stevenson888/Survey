package com.xwl.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xwl.entity.QuestionOption;
import com.xwl.entity.ResultQuestionOption;
import com.xwl.mapper.QuestionOptionMapper;
import com.xwl.service.IQuestionOptionService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 
 * @since 2023-05-24
 */
@Service
public class QuestionOptionServiceImpl extends ServiceImpl<QuestionOptionMapper, QuestionOption> implements IQuestionOptionService {

    @Override
    public List<QuestionOption> getQuestionOptionList(Long paperId) {
        return this.baseMapper.getQuestionOptionList(paperId);
    }

    @Override
    public List<ResultQuestionOption> getQuestionOptionResultList(Long paperId) {
        return this.baseMapper.getQuestionOptionResultList(paperId);
    }


    @Override
    public List<QuestionOption> getSelectQuestionOptionResultsByActivityId(Long activityId) {
        return this.baseMapper.getSelectQuestionOptionResultsByActivityId(activityId);
    }
    @Override
    public List<QuestionOption> getBlankQuestionOptionResultsByActivityId(Long activityId) {
        return this.baseMapper.getBlankQuestionOptionResultsByActivityId(activityId);
    }

//    @Override
//    public List<QuestionOption> getSelectQuestionOptionResultsByAnswerId(Long answerId) {
//        return this.baseMapper.getSelectQuestionOptionResultsByAnswerId(answerId);
//    }
//    @Override
//    public List<QuestionOption> getBlankQuestionOptionResultsByAnswerId(Long answerId) {
//        return this.baseMapper.getBlankQuestionOptionResultsByAnswerId(answerId);
//    }

}
