package com.xwl.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xwl.entity.PaperQuestion;
import com.xwl.entity.Question;
import com.xwl.mapper.PaperQuestionMapper;
import com.xwl.service.IPaperQuestionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
public class PaperQuestionServiceImpl extends ServiceImpl<PaperQuestionMapper, PaperQuestion> implements IPaperQuestionService {
    @Resource
    private PaperQuestionMapper paperQuestionMapper;

    @Override
    public List<Long> getQuestionIdListByPaperId(Long paperId) {
        return paperQuestionMapper.getQuestionIdListByPaperId(paperId);
    }

    @Override
    public List<PaperQuestion> getQuestionListByPaperId(Long paperId) {
        return paperQuestionMapper.getQuestionListByPaperId(paperId);
    }

}
