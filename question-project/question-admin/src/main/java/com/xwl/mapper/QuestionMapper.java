package com.xwl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xwl.entity.Question;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 
 * @since 2023-05-23
 */
public interface QuestionMapper extends BaseMapper<Question> {

    //根据问卷id查询试题列表
//    List<Question> questionList(@Param("paperId") Long paperId);

}
