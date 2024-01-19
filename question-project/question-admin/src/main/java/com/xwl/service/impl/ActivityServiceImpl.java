package com.xwl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xwl.entity.*;
import com.xwl.mapper.ActivityMapper;
import com.xwl.mapper.QuestionMapper;
import com.xwl.mapper.QuestionOptionMapper;
import com.xwl.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xwl.utils.TokenUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 
 * @since 2023-08-03
 */
@Service
public class ActivityServiceImpl extends ServiceImpl<ActivityMapper, Activity> implements IActivityService {

    @Autowired
    private IProjectActivityService projectActivityService;
    @Autowired
    private IActivityService activityService;
    @Autowired
    private IPaperQuestionService paperQuestionService;
    @Autowired
    private IActivityPaperService activityPaperService;
    @Resource
    private QuestionOptionMapper questionOptionMapper;
    @Resource
    private IQuestionService questionService;
    @Autowired
    private IActivityAreaLimitService activityAreaLimitService;
    @Autowired
    private IChinaRegionService chinaRegionService;

    //获取没有被project关联上的所有activity(获取闲置的活动)
    public List<ActivityVo> getAllUnselectedActivityVoList() {
        List<Activity> activityList = list();
        List<ActivityVo> unselectedActivityVoList = new ArrayList<>();
        for( Activity activity : activityList ){
            Boolean isMyActivity = TokenUtils.getCurrentUser().getUserId().equals(activity.getCreateUid());
            if(isMyActivity==true){
                if(!isSelectedActivity(activity.getActivityId())) {  //activity未被project关联上
                    ActivityVo activityVo = new ActivityVo();
                    BeanUtils.copyProperties(activity,activityVo);
                    unselectedActivityVoList.add(activityVo);
                }
            }
        }
        return unselectedActivityVoList;
    }

    //  项目:活动 = 1:n; 活动:问卷 = n:1
    //activity是否被project关联上了
    public Boolean isSelectedActivity (Long activityId) {
        QueryWrapper<ProjectActivity> projectActivityQueryWrapper = new QueryWrapper<>();
        projectActivityQueryWrapper.lambda().eq(ProjectActivity::getActivityId, activityId);
        ProjectActivity projectActivity = projectActivityService.getOne(projectActivityQueryWrapper);
        if(projectActivity != null){   //activity已被project关联上了
            return true;
        }
//        List<ProjectActivity> projectActivityList = projectActivityService.list(projectActivityQueryWrapper);
//        for(ProjectActivity projectActivity : projectActivityList){
//            if(projectActivity != null){   //activity已被project关联上了
//                return true;
//            }
//        }
        return false;
    }

    //  项目:活动 = 1:n; 活动:问卷 = n:1
    //activity被project关联上了,就返回 projectId
    public Long getProjectIdFromSelectedActivity (Long activityId) {
        QueryWrapper<ProjectActivity> projectActivityQueryWrapper = new QueryWrapper<>();
        projectActivityQueryWrapper.lambda().eq(ProjectActivity::getActivityId, activityId);
        ProjectActivity projectActivity = projectActivityService.getOne(projectActivityQueryWrapper);
        if(projectActivity != null){   //activity已被project关联上了
            return projectActivity.getProjectId();
        }
//        return null;
        return new Long(0);
    }

//    //获取没有被project关联上的所有activity(获取闲置的活动)
//    public List<Question> getQuestionListByActivityId(Long activityId) {
//        QueryWrapper<ActivityPaper> activityPaperQueryWrapper = new QueryWrapper<>();
//        activityPaperQueryWrapper.lambda().eq(ActivityPaper::getActivityId, activityId);
//        ActivityPaper activityPaper = activityPaperService.getOne(activityPaperQueryWrapper);
//
//        Long paperId = activityPaper.getPaperId();
//        QueryWrapper<PaperQuestion> paperQuestionQueryWrapper = new QueryWrapper<>();
//        paperQuestionQueryWrapper.lambda().eq(PaperQuestion::getPaperId, paperId);
//        List<PaperQuestion> paperQuestionList = paperQuestionService.list(paperQuestionQueryWrapper);
//
//        List<Question> questionList = new ArrayList<>();
//        for( PaperQuestion paperQuestion : paperQuestionList){
//            Long questionId = paperQuestion.getQuestionId();
//            QueryWrapper<Question> questionQueryWrapper = new QueryWrapper<>();
//            questionQueryWrapper.lambda().eq(Question::getQuestionId, questionId);
//            Question question = questionService.getOne(questionQueryWrapper);
//            questionList.add(question);
//        }
//        return questionList;
//    }

    //获取没有被project关联上的所有activity(获取闲置的活动)
    public List<Question> getQuestionListByActivityId(Long activityId) {
        QueryWrapper<ActivityPaper> activityPaperQueryWrapper = new QueryWrapper<>();
        activityPaperQueryWrapper.lambda().eq(ActivityPaper::getActivityId, activityId);
        ActivityPaper activityPaper = activityPaperService.getOne(activityPaperQueryWrapper);

        Long paperId = activityPaper.getPaperId();
        QueryWrapper<PaperQuestion> paperQuestionQueryWrapper = new QueryWrapper<>();
        paperQuestionQueryWrapper.lambda().eq(PaperQuestion::getPaperId, paperId);
        List<PaperQuestion> paperQuestionList = paperQuestionService.list(paperQuestionQueryWrapper);
        List<Long> questionIdList = paperQuestionList.stream().map(PaperQuestion::getQuestionId).collect(Collectors.toList());

        QueryWrapper<Question> questionQueryWrapper = new QueryWrapper<>();
        questionQueryWrapper.lambda().in(Question::getQuestionId, questionIdList);
        List<Question> questionList = questionService.list(questionQueryWrapper);
        Map<Long, Question> questionMap = questionList.stream().collect(Collectors.toMap(Question::getQuestionId, Function.identity()));        //List to Map

        List<Question> returnQuestionList = new ArrayList<>();
        for( PaperQuestion paperQuestion : paperQuestionList){
            Long questionId = paperQuestion.getQuestionId();
            Question question = questionMap.get(questionId);
            returnQuestionList.add(question);
        }
        return questionList;
    }


//    //获取Activity涉及的区域
//    public List<String> getActivityAreaNameListByActivityId(Long activityId) {
//        List<String> activityAreaLimitCityCodeList = new ArrayList<>();
//        QueryWrapper<ActivityAreaLimit> activityAreaLimitQueryWrapper = new QueryWrapper<>();
//        activityAreaLimitQueryWrapper.lambda().eq(ActivityAreaLimit::getActivityId, activityId);
//        List<ActivityAreaLimit> activityAreaLimits = activityAreaLimitService.list(activityAreaLimitQueryWrapper);
//        for(ActivityAreaLimit activityAreaLimit : activityAreaLimits){
//            String cityCode = activityAreaLimit.getCityCode();
//            activityAreaLimitCityCodeList.add(cityCode);
//        }
//        //Activity-ActivityAreaLimit-ChinaRegion 项目涉及区域的名字
//        List<String> distinctCityCodeList = activityAreaLimitCityCodeList.stream().distinct().collect(Collectors.toList());
//        List<String> distinctAreaNameList = new ArrayList<>();
//        for( String cityCode : distinctCityCodeList){
//            QueryWrapper<ChinaRegion> chinaRegionQueryWrapper = new QueryWrapper<>();
//            chinaRegionQueryWrapper.lambda().eq(ChinaRegion::getAreaCode, cityCode);
//            ChinaRegion chinaRegion = chinaRegionService.getOne(chinaRegionQueryWrapper);
//            if(chinaRegion!=null){
//                distinctAreaNameList.add(chinaRegion.getAreaName());
//            }
//        }
//        return distinctAreaNameList;
//    }


}
