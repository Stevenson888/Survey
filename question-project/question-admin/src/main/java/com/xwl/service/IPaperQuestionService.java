package com.xwl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xwl.entity.PaperQuestion;
import com.xwl.entity.Question;
import com.xwl.entity.ResultQuestionOption;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 
 * @since 2023-05-24
 */
public interface IPaperQuestionService extends IService<PaperQuestion> {

    List<Long> getQuestionIdListByPaperId(Long paperId);

    List<PaperQuestion> getQuestionListByPaperId(Long paperId);


}
