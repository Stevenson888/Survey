package com.xwl.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xwl.service.*;
import com.xwl.utils.Constants;
import com.xwl.utils.ResultUtils;
import com.xwl.utils.ResultVo;
import com.xwl.entity.*;
//import com.xwl.service.QuestionService;
//import com.xwl.entity.Paper;
//import com.xwl.service.PaperService;
//import com.xwl.entity.PaperChoice;
//import com.xwl.service.PaperChoiceService;
import com.xwl.utils.TokenUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 问卷控制器
 */
@RestController
@RequestMapping("/api/paper")
public class PaperController {
    @Autowired
    private IPaperService paperService;
    @Autowired
    private IQuestionService questionService;
    @Autowired
    private IQuestionOptionService questionOptionService;
    @Autowired
    private IPaperQuestionService paperQuestionService;
    @Autowired
    private IActivityPaperService activityPaperService;
    @Autowired
    private IActivityService activityService;
    @Autowired
    private IAnswerService answerService;
    @Autowired
    private IProjectActivityService projectActivityService;
    @Autowired
    private IProjectService projectService;
    @Autowired
    private IActivityAreaLimitService activityAreaLimitService;
    @Autowired
    private IChinaRegionService chinaRegionService;



    /**
     * 新增问卷
     * @param paper
     * @return
     */
    @PostMapping
    public ResultVo add(@RequestBody Paper paper){
        User currentUser = TokenUtils.getCurrentUser();

        paper.setCreateTime(new Date());
        paper.setUpdateTime(new Date());
        paper.setCreateUid((long)currentUser.getId());
        paper.setUpdateUid((long)currentUser.getId());

        boolean save = paperService.save(paper);
        if(save){
            return ResultUtils.success("新增问卷成功!");
        }
        return ResultUtils.error("新增问卷失败!");
    }

    /**
     * 编辑问卷
     */
    @PutMapping
    public ResultVo edit(@RequestBody Paper paper){
        User currentUser = TokenUtils.getCurrentUser();

        paper.setUpdateTime(new Date());
        paper.setUpdateUid((long)currentUser.getId());

        boolean b = paperService.updateById(paper);
        if(b){
            return ResultUtils.success("编辑问卷成功!");
        }
        return ResultUtils.error("编辑问卷失败!");
    }

    /**
     * 删除问卷
     * @return
     */
    @DeleteMapping("/{id}")
    public ResultVo delete(@PathVariable("id") Long id){
        boolean b = paperService.removeById(id);
        if(b){
            return ResultUtils.success("删除问卷成功!");
        }
        return ResultUtils.error("删除问卷失败!");
    }

    /**
     * 查找问卷
     */
    @GetMapping("/{id}")
    public ResultVo getPaper(@PathVariable("id") Long id){
        Paper paper = paperService.getById(id);
        if(paper == null){
            return ResultUtils.error("查找问卷失败!");
        }
        return ResultUtils.success("查找问卷成功!",paper);
    }

    @GetMapping("/activityId/{activityId}")
    public ResultVo getPapersByActivityId(@RequestParam(defaultValue = "") String name,
                                          @RequestParam Integer pageNum,
                                          @RequestParam Integer pageSize,
                                          @PathVariable Long activityId) {
        User currentUser = TokenUtils.getCurrentUser();                                     //当前登录的用户
        Long currentUserMUserId = currentUser.getUserId();                                  //当前登录的管理员userId - 后台
//        Long currentPartnerAppUserId = userService.getAppUserId(currentPartnerId);    //当前登录的管理员userId - 小程序

        QueryWrapper<Activity> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");

        if (!"".equals(activityId)) {
            queryWrapper.lambda().eq(Activity ::getActivityId, activityId);
        }
        if (!"".equals(name)) {
            queryWrapper.like("title", name);
        }

//        QueryWrapper<Activity> activityQueryWrapper1 = new QueryWrapper<>();
//        activityQueryWrapper1.lambda().eq(Activity::getActivityId, activityId);
//        Activity activity = activityService.getOne(activityQueryWrapper1);
//        String activityTitle = activity.getTitle();
//        QueryWrapper<Activity> activityQueryWrapper2 = new QueryWrapper<>();
//        activityQueryWrapper2.lambda().eq(Activity::getTitle, activityTitle);
//        List<Activity> activityList2 = activityService.list(activityQueryWrapper2);
//        if (!"".equals(activityTitle)) {
//            queryWrapper.like("title", activityTitle);
//        }

        if(Constants.ROLE_DEV.equals(currentUser.getRoleFlag())){

        } else if(Constants.ROLE_ADMIN.equals(currentUser.getRoleFlag())){
            queryWrapper.lambda().eq(Activity ::getCreateUid, currentUserMUserId);
        } else {
            return ResultUtils.success("无权限", null);
        }
        IPage<Activity> activityListPage = activityService.page(new Page<>(pageNum, pageSize), queryWrapper);
        List<Activity> activityList = activityListPage.getRecords();

        //  项目:活动 = 1:n
        //      活动:问卷 = n:1
        for(Activity activity : activityList){
            Long activityId2 = activity.getActivityId();
            QueryWrapper<ProjectActivity> projectActivityQueryWrapper = new QueryWrapper<>();
            projectActivityQueryWrapper.lambda().eq(ProjectActivity::getActivityId, activityId2);

            ProjectActivity projectActivity = projectActivityService.getOne(projectActivityQueryWrapper);
            if(projectActivity != null){
                Long projectId = projectActivity.getProjectId();
                QueryWrapper<Project> projectQueryWrapper = new QueryWrapper<>();
                projectQueryWrapper.lambda().eq(Project::getProjectId, projectId);
                Project project = projectService.getOne(projectQueryWrapper);
                activity.setProject(project);
            }

            //Project-Activity-ActivityAreaLimit 项目涉及的区域
            List<String> activityAreaLimitCityCodeList = new ArrayList<>();
            QueryWrapper<ActivityAreaLimit> activityAreaLimitQueryWrapper = new QueryWrapper<>();
            activityAreaLimitQueryWrapper.lambda().eq(ActivityAreaLimit::getActivityId, activityId2);
            List<ActivityAreaLimit> activityAreaLimits = activityAreaLimitService.list(activityAreaLimitQueryWrapper);
            for(ActivityAreaLimit activityAreaLimit : activityAreaLimits){
                String cityCode = activityAreaLimit.getCityCode();
                activityAreaLimitCityCodeList.add(cityCode);
            }

            //去重
            List<String> distinctCityCodeList = activityAreaLimitCityCodeList.stream().distinct().collect(Collectors.toList());
            List<String> distinctAreaNameList = new ArrayList<>();
            for( String cityCode : distinctCityCodeList){
                QueryWrapper<ChinaRegion> chinaRegionQueryWrapper = new QueryWrapper<>();
                chinaRegionQueryWrapper.lambda().eq(ChinaRegion::getAreaCode, cityCode);
                ChinaRegion chinaRegion = chinaRegionService.getOne(chinaRegionQueryWrapper);
                if(chinaRegion!=null){
                    distinctAreaNameList.add(chinaRegion.getAreaName());
                }
            }
            activity.setAreaNameList(distinctAreaNameList);

            //答卷数量
            QueryWrapper<Answer> answerQueryWrapper = new QueryWrapper<>();
            answerQueryWrapper.lambda().eq(Answer::getActivityId, activityId2);
            List<Answer> answerList = answerService.list(answerQueryWrapper);
            activity.setAnswerCount(answerList.size());
        }

        activityList = activityList.stream().sorted(Comparator.comparing(Activity::getActivityId)).collect(Collectors.toList());
        activityListPage.setRecords(activityList);
        return ResultUtils.success("查询成功", activityListPage);
    }

//    /**
//     * 问卷列表
//     */
//    @GetMapping("/list")
//    public ResultVo getPaperList(PaperParm paperParm){
//        //构造分页对象
//        IPage<Paper> page = new Page<>();
//        page.setSize(paperParm.getPageSize());
//        page.setCurrent(paperParm.getCurrentPage());
//        //构造查询条件
//        QueryWrapper<Paper> paperQuery = new QueryWrapper<>();
//        //模糊查询
//        if(StringUtils.isNotEmpty(paperParm.getTitle())){
//            paperQuery.lambda().like(Paper::getTitle,paperParm.getTitle());
//        }
//        //排序
//        paperQuery.lambda().orderByAsc(Paper::getId);
//        IPage<Paper> paperListPage = paperService.page(page, paperQuery);
//        List<Paper> paperList = paperService.list(paperQuery);
//
//                                                                    //项目:活动 = 1:n
//                                                                    //    活动:问卷 = n:1
//        //查询paper所属的activity和project信息
//        for(Paper paper : paperList){
//            Long paperId = paper.getPaperId();
//
//            //1.activity
//            QueryWrapper<ActivityPaper> activityPaperQueryWrapper = new QueryWrapper<>();
//            activityPaperQueryWrapper.lambda().eq(ActivityPaper::getPaperId,paperId);
////            ActivityPaper activityPaper = activityPaperService.getOne(activityPaperQueryWrapper);
//            List<ActivityPaper> activityPaperList = activityPaperService.list(activityPaperQueryWrapper);
//
//            List<Activity> activityList = new ArrayList<>();
//            for( ActivityPaper activityPaper : activityPaperList){
//                Long activityId = activityPaper.getActivityId();
//                QueryWrapper<Activity> activityQueryWrapper = new QueryWrapper<>();
//                activityQueryWrapper.lambda().eq(Activity::getActivityId, activityId);
//                Activity activity = activityService.getOne(activityQueryWrapper);
//
//                //2.project
//                QueryWrapper<ProjectActivity> projectActivityQueryWrapper = new QueryWrapper<>();
//                projectActivityQueryWrapper.lambda().eq(ProjectActivity::getActivityId,activityId);
//                ProjectActivity projectActivity = projectActivityService.getOne(projectActivityQueryWrapper);
//                if(projectActivity!=null) {                                 //活动已关联到项目
//                    Long projectId = projectActivity.getProjectId();
//                    QueryWrapper<Project> projectQueryWrapper = new QueryWrapper<>();
//                    projectQueryWrapper.lambda().eq(Project::getProjectId, projectId);
//                    Project project = projectService.getOne(projectQueryWrapper);
//                    activity.setProject(project);
//                }
//                activityList.add(activity);
//            }
//            paper.setActivityList(activityList);
//
//        }
//
//        paperListPage.setRecords(paperList);
//        return ResultUtils.success("查询成功",paperListPage);
//    }

    /**
     * 问卷列表 - 问卷按活动
     */
    @GetMapping("/list")
    public ResultVo getPaperList(@RequestParam(defaultValue = "") String title, @RequestParam Integer pageNum, @RequestParam Integer pageSize){
        //构造分页对象
        IPage<Activity> page = new Page<>();
        page.setSize(pageSize);
        page.setCurrent(pageNum);
        //构造查询条件
        QueryWrapper<Activity> activityQuery = new QueryWrapper<>();
        //模糊查询
        if(StringUtils.isNotEmpty(title)){
            activityQuery.lambda().like(Activity::getTitle,title);
        }
        //排序
        activityQuery.lambda().orderByDesc(Activity::getId);
        IPage<Activity> activityListPage = activityService.page(page, activityQuery);
        List<Activity> activityList = activityListPage.getRecords();

        //项目:活动 = 1:n
        //    活动:问卷 = n:1
        //查询paper所属的activity和project信息
        for(Activity activity : activityList){
            Long activityId = activity.getActivityId();

            //1.paper
            QueryWrapper<ActivityPaper> activityPaperQueryWrapper = new QueryWrapper<>();
            activityPaperQueryWrapper.lambda().eq(ActivityPaper::getActivityId,activityId);
            ActivityPaper activityPaper = activityPaperService.getOne(activityPaperQueryWrapper);
            if(activityPaper!=null){
                Long paperId = activityPaper.getPaperId();
                QueryWrapper<Paper> paperQueryWrapper = new QueryWrapper<>();
                paperQueryWrapper.lambda().eq(Paper::getPaperId, paperId);
                Paper paper = paperService.getOne(paperQueryWrapper);
                activity.setPaper(paper);
            }

            //2.Activity涉及区域
            List<String> activityAreaLimitCityCodeList = new ArrayList<>();
            QueryWrapper<ActivityAreaLimit> activityAreaLimitQueryWrapper = new QueryWrapper<>();
            activityAreaLimitQueryWrapper.lambda().eq(ActivityAreaLimit::getActivityId, activityId);
            List<ActivityAreaLimit> activityAreaLimits = activityAreaLimitService.list(activityAreaLimitQueryWrapper);
            for(ActivityAreaLimit activityAreaLimit : activityAreaLimits){
                String cityCode = activityAreaLimit.getCityCode();
                activityAreaLimitCityCodeList.add(cityCode);
            }
            //Activity-ActivityAreaLimit-ChinaRegion 项目涉及区域的名字
            List<String> distinctCityCodeList = activityAreaLimitCityCodeList.stream().distinct().collect(Collectors.toList());
            List<String> distinctAreaNameList = new ArrayList<>();
            for( String cityCode : distinctCityCodeList){
                QueryWrapper<ChinaRegion> chinaRegionQueryWrapper = new QueryWrapper<>();
                chinaRegionQueryWrapper.lambda().eq(ChinaRegion::getAreaCode, cityCode);
                ChinaRegion chinaRegion = chinaRegionService.getOne(chinaRegionQueryWrapper);
                if(chinaRegion!=null){
                    distinctAreaNameList.add(chinaRegion.getAreaName());
                }
            }
//            activity.setAreaNameList(activityService.getActivityAreaNameListByActivityId(activityId));
            activity.setAreaNameList(distinctAreaNameList);

            //3.project
            QueryWrapper<ProjectActivity> projectActivityQueryWrapper = new QueryWrapper<>();
            projectActivityQueryWrapper.lambda().eq(ProjectActivity::getActivityId,activityId);
            ProjectActivity projectActivity = projectActivityService.getOne(projectActivityQueryWrapper);
            if(projectActivity!=null) {                                 //活动已关联到项目
                Long projectId = projectActivity.getProjectId();
                QueryWrapper<Project> projectQueryWrapper = new QueryWrapper<>();
                projectQueryWrapper.lambda().eq(Project::getProjectId, projectId);
                Project project = projectService.getOne(projectQueryWrapper);
                activity.setProject(project);
            }
        }
        activityListPage.setRecords(activityList);
        return ResultUtils.success("查询成功",activityListPage);
    }

    /**
     * 问卷统计查询
     */
    @Deprecated
    @GetMapping("/getTotalList")
    public ResultVo getTotalList(Long paperId){
        //1、获取试卷 paper
        QueryWrapper<Paper> query = new QueryWrapper<>();
        query.lambda().eq(Paper::getPaperId,paperId);
        Paper paper = paperService.getOne(query);

        //2、获取试卷中的试题列表 questionList
        List<Long> questionIdList = paperQuestionService.getQuestionIdListByPaperId(paperId);
        List<Question> questionList = new ArrayList<>();
        for (Long questionId : questionIdList){
            Question question = questionService.getQuestionByQuestionId(questionId);
            questionList.add(question);
        }
        //3 为每道试题设置选项 - 获取答卷中的所有试题的所有选项,包含统计total,最终拿到试卷中的试题列表，每道试题都包含选项列表 - paper+questionList+returnQuestionOptionList
        List<QuestionOption> questionOptionList = questionOptionService.getQuestionOptionList(paperId); //自定义sql-QuestionOptionMapper.getQuestionOptionList
        for (int i=0;i<questionList.size();i++){
            List<QuestionOption> returnQuestionOptionList = new ArrayList<>();
            Long questionId = questionList.get(i).getQuestionId();
            questionOptionList.stream().filter(item -> item.getQuestionId().equals(questionId)).forEach(item->{
                QuestionOption questionOption = new QuestionOption();
                BeanUtils.copyProperties(item,questionOption);
                returnQuestionOptionList.add(questionOption);
            });
            //设置单选、多选的选项
            questionList.get(i).setQuestionOptionList(returnQuestionOptionList);
        }

        List<Question> selectQuestionList = new ArrayList<>();
        List<Question> blankQuestionList = new ArrayList<>();

//        questions.stream().filter(item -> item.getType()==1 || item.getType()==2 ).forEach(item->{
//          Question question = new Question();
//            BeanUtils.copyProperties(item,question);
//            questionList.add(question);
//        });

        for(Question question : questionList){
            if(question.getType()==1 || question.getType()==2 ){
                selectQuestionList.add(question);
            } else if (question.getType()==3) {
                blankQuestionList.add(question);
            }
        }
        paper.setSelectQuestionList(selectQuestionList);
        paper.setBlankQuestionList(blankQuestionList);

//        paper.setQuestionList(questionList);
        return ResultUtils.success("查询成功",paper);
    }



    @GetMapping("/getTotalListEchart")
    @ResponseBody
    public ResultVo getTotalListEchart(Long paperId) {
        //根据问卷id查询所有的试题
        List<QuestionParm> questionList = questionService.getQuestionParmList(paperId);
        //存放返回值
        List<ResultQuestion> resultQuestionList = new ArrayList<>();
        //查询试题对应的选项
        List<ResultQuestionOption> questionOptionList = questionOptionService.getQuestionOptionResultList(paperId);
        if (questionList.size() > 0) {
            for (int i = 0; i < questionList.size(); i++) {
                //定义返回值
                ResultQuestion resultQuestion = new ResultQuestion();
                //定义存放试题选项
                List<ResultQuestionOption> resultQuestionOptionList = new ArrayList<>();
                //获取试题的id
                Long questionId = questionList.get(i).getQuestionId();

                questionOptionList.stream().filter(item -> item.getQuestionId().equals(questionId)).forEach(item -> {
                    ResultQuestionOption questionOption = new ResultQuestionOption();
                    BeanUtils.copyProperties(item, questionOption);
                    //把选项添加到试题
                    resultQuestionOptionList.add(questionOption);
                });
                BeanUtils.copyProperties(questionList.get(i), resultQuestion);
                resultQuestion.setQuestionOptionResultList(resultQuestionOptionList);
                resultQuestion.setPaperId(paperId);
                resultQuestionList.add(resultQuestion);
            }
        }
        return ResultUtils.success("查询成功", resultQuestionList);
    }

    /**
     * 获取问卷所属项目信息
     */
    @GetMapping("/getProjectByPaperId")
    public ResultVo getProjectByPaperId(Long paperId){

        QueryWrapper<ActivityPaper> activityPaperQueryWrapper = new QueryWrapper<>();
        activityPaperQueryWrapper.lambda().like(ActivityPaper::getActivityId,paperId);
        ActivityPaper activityPaper = activityPaperService.getOne(activityPaperQueryWrapper);
        Long activityId = activityPaper.getActivityId();

        QueryWrapper<ProjectActivity> projectActivityQueryWrapper = new QueryWrapper<>();
        projectActivityQueryWrapper.lambda().like(ProjectActivity::getActivityId,activityId);
        ProjectActivity projectActivity = projectActivityService.getOne(projectActivityQueryWrapper);
        Long projectId = projectActivity.getProjectId();

        QueryWrapper<Project> projectQueryWrapper = new QueryWrapper<>();
        projectQueryWrapper.lambda().like(Project::getProjectId, projectId);
        Project project = projectService.getOne(projectQueryWrapper);

        return ResultUtils.success("查询成功",project);
    }


}
