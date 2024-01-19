package com.xwl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xwl.entity.*;
import com.xwl.mapper.QuestionMapper;
import com.xwl.mapper.QuestionOptionMapper;
import com.xwl.service.IPaperQuestionService;
import com.xwl.service.IPaperService;
import com.xwl.service.IQuestionOptionService;
import com.xwl.service.IQuestionService;
import com.xwl.utils.SnowflakeIdWorker;
import com.xwl.utils.TokenUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.IdGenerator;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 
 * @since 2023-05-23
 */
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements IQuestionService {

    @Resource
    private QuestionMapper questionMapper;
    @Resource
    private QuestionOptionMapper questionOptionMapper;

    @Autowired
    private IPaperService paperService;
    @Autowired
    private IQuestionService questionService;

    @Autowired
    private IPaperQuestionService paperQuestionService;
    @Autowired
    private IQuestionOptionService questionOptionService;


/*    @Override
    @Transactional      //删除和保存要同时成败
    public void saveQuestion(List<Question> questionList) {
        //先删除，再保存
        for (int i=0;i<questionList.size();i++){
            //判断是否是单选、多选；需要删除对应的选项数据
            if(questionList.get(i).getType()==1 || questionList.get(i).getType()==2){
                //删除选项，构造删除的条件
                QueryWrapper<QuestionOption> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().eq(QuestionOption::getQuestionId,questionList.get(i).getQuestionId());
                //删除选项
                questionOptionService.remove(queryWrapper);
            }
            //删除试题
            //方法一:
//            int id = getIdByQuestionId(questionList.get(i).getQuestionId());
            this.baseMapper.deleteById(questionList.get(i).getId());
            //方法二:
//            questionMapper.deleteById(questionList.get(i).getQuestionId());
            //方法三:
            //QueryWrapper<Question> query = new QueryWrapper<>();
            //query.lambda().eq(Question::getPaperId,questionList.get(i).getPaperId());
            //this.baseMapper.delete(query);

        }
        //保存
        for (int k =0;k<questionList.size();k++){
            //先保存试题
            Question question = new Question();
            BeanUtils.copyProperties(questionList.get(k),question); //把questionList.get(k)复制到question中去
            //试题序号
//？？？         question.setQuestionOrder(k+1L);
            //保存试题
            question.setQuestionId(SnowflakeIdWorker.getNextId());  //雪花算法生成17位id
            this.baseMapper.insert(question);
            //判断是否是单选、多选;需要保存选项
            if(questionList.get(k).getType() == 1 || questionList.get(k).getType() == 2){
                //批量保存，保存之前，需要设置试题的id
                questionList.get(k).getQuestionOptionList().forEach(option ->{
                    //设置试题的id
                    option.setQuestionId(question.getQuestionId());
                    option.setOptionId(SnowflakeIdWorker.getNextId());
                });
                //保存选项
                questionOptionService.saveBatch(questionList.get(k).getQuestionOptionList());
            }
        }
    }*/


    @Override
    @Transactional      //删除和保存要同时成败
    public void saveQuestion(Long paperId, List<Question> questionList) {
        //保存
        for (int k =0;k<questionList.size();k++){

            if(questionList.get(k).getQuestionId() != null){
                continue;
            }

            //1.先保存试题
            Question question = new Question();
            BeanUtils.copyProperties(questionList.get(k),question); //把questionList.get(k)复制到question中去

            //试题序号
//？？？         question.setQuestionOrder(k+1L);

            //保存试题
            Long questionId = SnowflakeIdWorker.getNextId();
            question.setQuestionId(questionId);  //雪花算法生成17位id

            //TODO:
            question.setIsRequire(0);



            question.setProbability(0);
            question.setLevel(0);
            question.setLabel(0);
            question.setWaitDays(0);
            question.setCreateTime(new Date());
            question.setCreateUid((long)TokenUtils.getCurrentUser().getId());

            questionMapper.insert(question);
//            this.baseMapper.insert(question);

            //2.单选、多选：需要保存选项
            if(questionList.get(k).getType() == 1 || questionList.get(k).getType() == 2){
                //批量保存，保存之前，需要设置试题的id
//                questionList.get(k).getQuestionOptionList().forEach(option ->{
//                    //设置试题的id
//                    option.setQuestionId(question.getQuestionId());
//                    option.setOptionId(SnowflakeIdWorker.getNextId());
//
//                    option.setSort();
//                    option.setContent();
//                    //选项分值（为结果页面提供总分）
//                    option.setScore();
//                    option.setExclusive();
//
//                    option.setCreateTime(new Date());
//                });


                for(QuestionOption questionOption: questionList.get(k).getQuestionOptionList()){
                    //设置试题的id
                    questionOption.setQuestionId(question.getQuestionId());
                    questionOption.setOptionId(SnowflakeIdWorker.getNextId());
                    questionOption.setScore(0);

                    //TODO:
                    questionOption.setExclusive(0);

                    questionOption.setCreateTime(new Date());
                };

                //保存选项
                questionOptionService.saveBatch(questionList.get(k).getQuestionOptionList());
            }

            //3.把试题保存到问卷中
            PaperQuestion paperQuestion = new PaperQuestion();
            paperQuestion.setPaperId(paperId);
            paperQuestion.setQuestionId(question.getQuestionId());
            paperQuestionService.save(paperQuestion);

        }
    }

    @Override
    public List<Question> getQuestionList(Long paperId) {

        //试题列表
//        QueryWrapper<Paper> query = new QueryWrapper<>();
//        query.lambda().eq(Paper::getPaperId,paperId);
//        Paper paper = paperService.getOne(query);
//        paper.getId()

        List<Long> questionIdList = paperQuestionService.getQuestionIdListByPaperId(paperId);
//        List<Question> questionList = questionService.listByIds(questionIdList);

        List<Question> questionList = new ArrayList<>();
        for (Long questionId : questionIdList) {
            Question question = getQuestionByQuestionId(questionId);
            questionList.add(question);
        }
//        //构造查询条件
//        QueryWrapper<Question> query = new QueryWrapper<>();
//        query.lambda().eq(Question::getPaperId,paperId);
//        //试题列表
//        List<Question> questionList = this.baseMapper.selectList(query);

        //如果是单选、多选，需要查询对于的选项
        if(questionList.size()>0){
            for (int i =0;i<questionList.size();i++){
                if(questionList.get(i).getType() == 1 || questionList.get(i).getType() == 2 ){
                    //构造查询选项的条件
                    QueryWrapper<QuestionOption> queryWrapper = new QueryWrapper<>();
                    queryWrapper.lambda().eq(QuestionOption::getQuestionId,questionList.get(i).getQuestionId());
                    //查询选项
                    List<QuestionOption> questionOptionlist = questionOptionService.list(queryWrapper);
                    //把查询到的选项设置到试题
                    questionList.get(i).setQuestionOptionList(questionOptionlist);
                }
            }
        }
        return questionList;
    }

    @Override
    public List<QuestionParm> getQuestionParmList(Long paperId) {
        //根据问卷id查询所有的试题列表
//        List<Question> questionList = questionMapper.questionList(paperId);
        List<Long> questionIdList = paperQuestionService.getQuestionIdListByPaperId(paperId);
        List<Question> questionList = new ArrayList<>();
        for (Long questionId : questionIdList){
            Question question = questionService.getQuestionByQuestionId(questionId);
            questionList.add(question);
        }

        List<QuestionParm> questionParmList = new ArrayList<>();
        if(questionList != null && questionList.size() > 0){
            //循环试题列表
            for(int i = 0;i<questionList.size();i++){
                QuestionParm questionParm = new QuestionParm();
                BeanUtils.copyProperties(questionList.get(i),questionParm);
                //判断是否是单选或者多选，查询对于的选项
                List<QuestionOption> optionList = questionOptionMapper.getOptionList(questionList.get(i).getQuestionId());
                //设置试题对应的选项
                questionParm.setQuestionOptionList(optionList);
                questionParmList.add(questionParm);
            }
        }
        return questionParmList;
    }

    @Override
    public Question getQuestionByQuestionId(Long questionId){
        QueryWrapper<Question> query = new QueryWrapper<>();
        query.lambda().eq(Question::getQuestionId, questionId);
        Question question = questionService.getOne(query);
        return question;
    }

    @Override
    public int getIdByQuestionId(Long questionId){
        QueryWrapper<Question> query = new QueryWrapper<>();
        query.lambda().eq(Question::getQuestionId, questionId);
        Question question = questionService.getOne(query);
        int id = question.getId();
        return id;
    }
}
