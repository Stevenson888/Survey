package com.xwl.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelWriter;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;

import cn.hutool.poi.excel.StyleSet;
import cn.hutool.poi.excel.cell.CellSetter;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xwl.entity.*;
import com.xwl.service.*;
import com.xwl.utils.*;
import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.XSlf4j;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 
 * @since 2023-08-03
 */
@Slf4j
@RestController
@RequestMapping("/api/activity")
public class ActivityController {

    @Resource
    private IActivityService activityService;
    @Resource
    private IQuestionService questionService;
    @Resource
    private IPaperService paperService;
    @Resource
    private IQuestionOptionService questionOptionService;
    @Resource
    private IProjectActivityService projectActivityService;
    @Resource
    private IProjectService projectService;
    @Resource
    private IPaperQuestionService paperQuestionService;
    @Resource
    private IActivityAreaLimitService activityAreaLimitService;
    @Resource
    private IChinaRegionService chinaRegionService;
    @Resource
    private IAnswerService answerService;
    @Resource
    private IAnswerDetailService answerDetailService;

    private final DateTime now = DateUtil.date();
    private final User currentUser = TokenUtils.getCurrentUser();

//    Integer counter1 = 0;
//    Integer counter2 = 0;
//    Integer counter3 = 0;
//    Integer counter4 = 0;
//    Integer counter5 = 0;
//    Integer counterSum = 0;
//    double minValue  = 0;
//    double num1 =  0;
//    double num2 =  0;
//    double num3 =  0;
//    double num4 =  0;
//    double  maxValue  = 0;
//    double interval =  0;
//    double inputNum =  0;
//
////    BigDecimal minValue  = new BigDecimal(0);
////    BigDecimal num1 =  new BigDecimal(0);
////    BigDecimal num2 =  new BigDecimal(0);
////    BigDecimal num3 =  new BigDecimal(0);
////    BigDecimal num4 =  new BigDecimal(0);
////    BigDecimal  maxValue  = new BigDecimal(0);
////    BigDecimal interval =  new BigDecimal(0);

    // 新增或者更新
    @PostMapping
    public ResultVo save(@RequestBody Activity activity) {
        if (activity.getId() == null) {
            Long activityId = SnowflakeIdWorker.getNextId();
            activity.setActivityId(activityId);

            List<List> objList = activity.getSelectedAreaOptions();
//            List<ActivityAreaLimit> activityAreaLimitList = new ArrayList<>();
            for( List<String> obj : objList) {
//                System.out.println("===obj===" + obj);
                String cityCode = obj.get(2);
//                System.out.println("===cityCode===" + cityCode);

                ActivityAreaLimit activityAreaLimit = new ActivityAreaLimit();
                activityAreaLimit.setActivityId(activityId);
                activityAreaLimit.setCityCode(cityCode);
                activityAreaLimit.setType("WHITE");
                activityAreaLimitService.saveOrUpdate(activityAreaLimit);
            }

//            activity.setAnswerCount(0);
            activity.setCreateTime(now);
            activity.setUpdateTime(now);
            activity.setCreateUid(TokenUtils.getCurrentUser().getUserId());
            activity.setUpdateUid(TokenUtils.getCurrentUser().getUserId());
        } else { //更新
            Long activityId = activity.getActivityId();

            activity.setUpdateUid(TokenUtils.getCurrentUser().getUserId());
            activity.setUpdateTime(DateUtil.date());
        }

        activityService.saveOrUpdate(activity);
        return ResultUtils.success("操作成功");
    }

    @DeleteMapping("/{id}")
    public ResultVo delete(@PathVariable Integer id) {
        //1.级联删除，先删两子
        Activity activity = activityService.getById(id);
        Long activityId = activity.getActivityId();
        projectActivityService.deleteProjectActivityByActivityId(activityId);
        activityAreaLimitService.deleteAllActivityAreaLimitByActivityId(activityId);

        //2.级联删除，再删父
        activityService.removeById(id);
        return ResultUtils.success("删除成功");
    }

    @PostMapping("/del/batch")
    public ResultVo deleteBatch(@RequestBody List<Integer> ids) {
        activityService.removeByIds(ids);
        return ResultUtils.success("批量删除成功");
    }

    @GetMapping
    public ResultVo findAll() {
        return ResultUtils.success("查询成功",activityService.list());
    }

    @GetMapping("/getAllActivityVoList")
    public ResultVo getAllActivityVoList() {
        List<Activity> activityList = activityService.list();
        List<ActivityVo> activityVoList = new ArrayList<>();
        for( Activity activity : activityList ){
            ActivityVo activityVo = new ActivityVo();
            BeanUtils.copyProperties(activity,activityVo);
            activityVoList.add(activityVo);
        }
        return ResultUtils.success("查询成功", activityVoList);
    }

    @GetMapping("/{id}")
    public ResultVo findOne(@PathVariable Integer id) {
        return ResultUtils.success("查询成功",activityService.getById(id));
    }

    @GetMapping("/page")
    public ResultVo findPage(@RequestParam(defaultValue = "") String name,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        User currentUser = TokenUtils.getCurrentUser();                                     //当前登录的用户
        Long currentUserMUserId = currentUser.getUserId();                                  //当前登录的管理员userId - 后台
//        Long currentPartnerAppUserId = userService.getAppUserId(currentPartnerId);    //当前登录的管理员userId - 小程序

        QueryWrapper<Activity> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        if (!"".equals(name)) {
            queryWrapper.like("title", name);
        }

        if(Constants.ROLE_DEV.equals(currentUser.getRoleFlag())){

        } else if(Constants.ROLE_ADMIN.equals(currentUser.getRoleFlag())){
            queryWrapper.lambda().eq(Activity ::getCreateUid, currentUserMUserId);
        } else {
            return ResultUtils.success("无权限", null);
        }
        IPage<Activity> activityListPage = activityService.page(new Page<>(pageNum, pageSize), queryWrapper);
        List<Activity> activityList = activityListPage.getRecords();

//        if(Constants.ROLE_ADMIN.equals(currentUser.getRoleFlag())){                         //当前登录的用户是管理员
//            activityList = activityList.stream().filter( activity -> activity.getCreateUid().equals(currentUserMUserId) ).collect(Collectors.toList());
//        }


        //  项目:活动 = 1:n
        //      活动:问卷 = n:1
        for(Activity activity : activityList){
            Long activityId = activity.getActivityId();
            QueryWrapper<ProjectActivity> projectActivityQueryWrapper = new QueryWrapper<>();
            projectActivityQueryWrapper.lambda().eq(ProjectActivity::getActivityId, activityId);

            ProjectActivity projectActivity = projectActivityService.getOne(projectActivityQueryWrapper);
            if(projectActivity != null){
                Long projectId = projectActivity.getProjectId();
                QueryWrapper<Project> projectQueryWrapper = new QueryWrapper<>();
                projectQueryWrapper.lambda().eq(Project::getProjectId, projectId);
                Project project = projectService.getOne(projectQueryWrapper);
                activity.setProject(project);
            }

//            List<ProjectActivity> projectActivityList = projectActivityService.list(projectActivityQueryWrapper);
//            for(ProjectActivity projectActivity : projectActivityList){
//                Long projectId = projectActivity.getProjectId();
//                QueryWrapper<Project> projectQueryWrapper = new QueryWrapper<>();
//                projectQueryWrapper.lambda().eq(Project::getProjectId, projectId);
//                Project project = projectService.getOne(projectQueryWrapper);
//            }

            //Project-Activity-ActivityAreaLimit 项目涉及的区域
            List<String> activityAreaLimitCityCodeList = new ArrayList<>();
//            for(ProjectActivity projectActivity : selectedProjectActivityList){
//                Long activityId = projectActivity.getActivityId();
                QueryWrapper<ActivityAreaLimit> activityAreaLimitQueryWrapper = new QueryWrapper<>();
                activityAreaLimitQueryWrapper.lambda().eq(ActivityAreaLimit::getActivityId, activityId);
                List<ActivityAreaLimit> activityAreaLimits = activityAreaLimitService.list(activityAreaLimitQueryWrapper);
                for(ActivityAreaLimit activityAreaLimit : activityAreaLimits){
                    String cityCode = activityAreaLimit.getCityCode();
                    activityAreaLimitCityCodeList.add(cityCode);
                }
//            }

            //For Testing
            //activityAreaLimitCityCodeList.add("110107");
            //activityAreaLimitCityCodeList.add("110108");
            //activityAreaLimitCityCodeList.add("110108");
            //activityAreaLimitCityCodeList.add("110109");
            //activityAreaLimitCityCodeList.add("110109");
            //activityAreaLimitCityCodeList.add("110113");
            //activityAreaLimitCityCodeList.add("110117");

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
            answerQueryWrapper.lambda().eq(Answer::getActivityId, activityId);
            List<Answer> answerList = answerService.list(answerQueryWrapper);
            activity.setAnswerCount(answerList.size());
        }

        activityList = activityList.stream().sorted(Comparator.comparing(Activity::getActivityId)).collect(Collectors.toList());
        activityListPage.setRecords(activityList);
        return ResultUtils.success("查询成功", activityListPage);
    }



    //获取没有被project关联上的所有activity(获取闲置的活动)
    @GetMapping("/getAllUnselectedActivityVoList")
    public ResultVo getAllUnselectedActivityVoList() {
        List<ActivityVo> unselectedActivityVoList = activityService.getAllUnselectedActivityVoList();
        return ResultUtils.success("查询成功", unselectedActivityVoList);
    }

    /**
     * 1.1 获取答卷 所有题目
     */
    @GetMapping("/getTotalListByActivityId/{activityId}")
    public ResultVo getTotalListByActivityIdApi(@PathVariable Long activityId ){
        QueryWrapper<Activity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Activity::getActivityId, activityId);
        Activity activity = activityService.getOne(queryWrapper);

        //问卷 所有题目 - 单选+多选+填空+...
        List<Question> allQuestionList = activityService.getQuestionListByActivityId(activity.getActivityId());

        //答卷 选择题 - 单选+多选
        List<Question> allSelectQuestionList = allQuestionList.stream().filter(item -> item.getType()==1 || item.getType()==2).collect(Collectors.toList());   //activityId 下的所有选择题 - 1单选2多选
        activity.setSelectQuestionList(getSelectTotalListByActivityId(activityId, allSelectQuestionList));

        //答卷 填空题 - 填空
//        List<Question> allBlankQuestionList = allQuestionList.stream().filter(item -> item.getType()==3).collect(Collectors.toList());   //activityId 下的所有选择题 - 1单选2多选
//        activity.setBlankQuestionList(getBlankTotalListByActivityId(activityId, allBlankQuestionList));

        return ResultUtils.success("查询成功",activity);
    }

    /**
     * 1.2.1 答卷 选择 - 获取问卷题目+选项 对应所有答卷的 题目+选项 (选择题)
     */
    public List<Question> getSelectTotalListByActivityId(Long activityId, List<Question> allSelectQuestionList) {
        java.text.NumberFormat numberformat = java.text.NumberFormat.getInstance();
        numberformat.setMaximumFractionDigits(2); //设置精确到小数点后2位

        //1.问卷中,获取所有选择题 - 单选+多选
        //参数：allSelectQuestionList

        //2.所有答卷中,获取所有选中的选择题选项 - 单选+多选
        List<QuestionOption> selectedQuestionOptionList = questionOptionService.getSelectQuestionOptionResultsByActivityId(activityId);   //activityId 下的所有选中的选择题 - 1单选2多选

        //3.未选中的选择题 - 单选+多选
//        List<Long> allQuestionIds = allQuestionList.stream().map(Question::getQuestionId).distinct().sorted().collect(Collectors.toList());
//        List<Long> selectedQuestionIds = selectedQuestionOptionList.stream().map(QuestionOption::getQuestionId).distinct().sorted().collect(Collectors.toList());
//        List<Long> addQuestionIds = new ArrayList<>();
//        List<Question> addQuestions = new ArrayList<>();
//        for (Long questionId : allQuestionIds) {
//            if (!selectedQuestionIds.contains(questionId)) {
//                //addQuestionIds.add(questionId);
//                QueryWrapper<Question> questionQueryWrapper = new QueryWrapper<>();
//                questionQueryWrapper.lambda().eq(Question::getQuestionId, questionId);
//                Question question = questionService.getOne(questionQueryWrapper);
//                addQuestions.add(question);
////                if(question.getType()==1 || question.getType()==2){     //单选 多选
////                    addQuestions.add(question);
////                }
//            }
//        }
//        allQuestionList.addAll(addQuestions);

        //4. 计算每个选项的选中次数optionTotal, 比例questionTotal
        for (Question question : allSelectQuestionList) {
            Long questionId = question.getQuestionId();

            //4.1 问卷中,每道题目的所有选项
            QueryWrapper<QuestionOption> questionOptionQueryWrapper = new QueryWrapper<>();
            questionOptionQueryWrapper.lambda().eq(QuestionOption::getQuestionId, questionId);
            List<QuestionOption> allQuestionOptionList = questionOptionService.list(questionOptionQueryWrapper);   //问卷中，所有选中的选项
            List<Long> allOptionIds = allQuestionOptionList.stream().map(QuestionOption::getOptionId).distinct().sorted().collect(Collectors.toList());

            //4.2 答卷中,每道题目被选中选项的总和 questionTotal
            QueryWrapper<AnswerDetail> answerDetailQueryWrapper = new QueryWrapper<>();
            answerDetailQueryWrapper.lambda().eq(AnswerDetail::getQuestionId, questionId);
            List<AnswerDetail> selectedOptionList = answerDetailService.list(answerDetailQueryWrapper);   //答卷中，所有选中的选项

            int questionTotal = selectedOptionList.size();
            question.setQuestionTotal(questionTotal);

            //4.3 答卷中,未被选中的选项,添加进去
            List<Long> selectedOptionIds = selectedOptionList.stream().map(AnswerDetail::getOptionId).distinct().sorted().collect(Collectors.toList());
            List<Long> addOptionIds = new ArrayList<>();
            List<QuestionOption> addQuestionOptions = new ArrayList<>();
            for (Long optionId : allOptionIds) {
                if (!selectedOptionIds.contains(optionId)) {
                    //addQuestionIds.add(questionId);
                    QueryWrapper<QuestionOption> questionOptionQueryWrapper2 = new QueryWrapper<>();
                    questionOptionQueryWrapper2.lambda().eq(QuestionOption::getOptionId, optionId);
                    QuestionOption questionOption = questionOptionService.getOne(questionOptionQueryWrapper2);
                    addQuestionOptions.add(questionOption);
                }
            }

            //4.4 题目中设置选项: returnQuestionOptionList = 选中的selectedQuestionOptionList + 没选中的addQuestionOptions
            // 答卷中,选中选项的比例optionProportion (选中选项optionTotal / 选项总数questionTotal )
            List<QuestionOption> returnQuestionOptionList = new ArrayList<>();
            selectedQuestionOptionList.stream().filter(item -> item.getQuestionId().equals(questionId)).forEach(item -> {
                QuestionOption questionOption = new QuestionOption();

//                int optionTotal = item.getOptionTotal();
//                String optionProportionStr = numberformat.format((float) optionTotal / (float) questionTotal * 100);
//                double optionProportion = Double.valueOf(optionProportionStr);
//                item.setOptionProportion(optionProportion);

//                item.setOptionProportion(Double.valueOf(numberformat.format((float) item.getOptionTotal() / (float) questionTotal * 100)));

                BeanUtils.copyProperties(item, questionOption);
                questionOption.setOptionProportion(Double.valueOf(numberformat.format((float) item.getOptionTotal() / (float) questionTotal * 100)));
                returnQuestionOptionList.add(questionOption);

            });
            returnQuestionOptionList.addAll(addQuestionOptions);
            returnQuestionOptionList.sort(Comparator.comparing(QuestionOption::getSort));

            question.setQuestionOptionList(returnQuestionOptionList);
        }
        return allSelectQuestionList;
    }

    /**
     * 1.2.2 答卷 填空 - 获取问卷题目+选项 对应所有答卷的 题目+选项 (填空题) - 选项id=0:无选项无前缀; 选项id!=0:有选项有前缀
     */
    public List<Question> getBlankTotalListByActivityId(Long activityId, List<Question> allBlankQuestionList) {

        //消费者问卷则Excel导出userId, 零售户问卷不导出
        QueryWrapper<Activity> activityQueryWrapper = new QueryWrapper<>();
        activityQueryWrapper.lambda().eq(Activity::getActivityId, activityId);
        Activity activity = activityService.getOne(activityQueryWrapper);


        java.text.NumberFormat numberformat = java.text.NumberFormat.getInstance();
        numberformat.setMaximumFractionDigits(2); //设置精确到小数点后2位

        //1.问卷中,获取所有填空
        //参数：allBlankQuestionList

        //2.获取所有作答了的填空
        List<QuestionOption> selectedBlankQuestionOptionList = questionOptionService.getBlankQuestionOptionResultsByActivityId(activityId);   //activityId 下的所有选中的选择题 - 1单选2多选

        //3. 计算每个选项的选中次数optionTotal, 比例questionTotal
        for (Question question : allBlankQuestionList) {
            Long questionId = question.getQuestionId();
//            System.out.println(" === 0000000000questionId === " + questionId);

//            counter1=0;
//            counter2=0;
//            counter3=0;
//            counter4=0;
//            counter5=0;
//            counterSum=0;
//            minValue  = 0;
//            num1 =  0;
//            num2 =  0;
//            num3 =  0;
//            num4 =  0;
//            maxValue  = 0;
//            interval =  0;

            //3.1 问卷中,每道填空题的选项：0或1条记录                           -- 1
            QueryWrapper<QuestionOption> questionOptionQueryWrapper = new QueryWrapper<>();
            questionOptionQueryWrapper.lambda().eq(QuestionOption::getQuestionId, questionId);
            QuestionOption questionOption = questionOptionService.getOne(questionOptionQueryWrapper);   //问卷中, 填空题有无option_id(有无前后缀),0或1条记录
//            List<Long> allOptionIds = allQuestionOptionList.stream().map(QuestionOption::getOptionId).distinct().sorted().collect(Collectors.toList());

//            if(questionOption.getOptionId() != null){
//                System.out.println(" === 0000000000optionId === " + questionOption.getOptionId());
//            }

            //3.2 问卷中,每道填空题的0或1个选项,在所有答卷中,所有被作答的填空输入    -- 2388
//            QueryWrapper<AnswerDetail> answerDetailQueryWrapper = new QueryWrapper<>();
//            answerDetailQueryWrapper.lambda().eq(AnswerDetail::getQuestionId, questionId);
//            List<AnswerDetail> selectedOptionList = answerDetailService.list(answerDetailQueryWrapper);   //答卷中, 所有作答的填空, 所输入的答案

//            int questionTotal = selectedOptionList.size();
//            question.setQuestionTotal(questionTotal);

            //3.3 答卷中,未被选中的选项,添加进去
//            List<Long> selectedOptionIds = selectedOptionList.stream().map(AnswerDetail::getOptionId).distinct().sorted().collect(Collectors.toList());
//            List<Long> addOptionIds = new ArrayList<>();
//            List<QuestionOption> addQuestionOptions = new ArrayList<>();
//            for (Long optionId : allOptionIds) {
//                if (!selectedOptionIds.contains(optionId)) {
//                    //addQuestionIds.add(questionId);
//                    QueryWrapper<QuestionOption> questionOptionQueryWrapper2 = new QueryWrapper<>();
//                    questionOptionQueryWrapper2.lambda().eq(QuestionOption::getOptionId, optionId);
//                    QuestionOption questionOption = questionOptionService.getOne(questionOptionQueryWrapper2);
//                    addQuestionOptions.add(questionOption);
//                }
//            }

//            //3.4 答卷中,选中选项的比例optionProportion (选中选项optionTotal / 选项总数questionTotal )
            List<QuestionOption> returnQuestionOptionList = new ArrayList<>();

//            Comparator<Integer> comparator = Comparator.comparing(Integer::intValue);   //.collect(Collectors.minBy(comparator))
//            IntStream mapToInt(ToIntFunction mapper);
//            Optional<Integer> minOptional =  selectedBlankQuestionOptionList.stream().map(QuestionOption::getAnswerDetailContent).min(comparator);
//            Optional<Integer> maxOptional = selectedBlankQuestionOptionList.stream().max(comparator);

//            Comparator<Integer> comparator = Comparator.comparing(Integer::intValue);   //.collect(Collectors.minBy(comparator))

//            List<Integer> min = selectedBlankQuestionOptionList.stream().map(QuestionOption::getAnswerDetailContent).mapToInt(Integer::parseInt).collect(Collectors.toList()) ;
//            OptionalInt maxOpt = selectedBlankQuestionOptionList.stream().map(QuestionOption::getAnswerDetailContent).mapToInt(Integer::parseInt) ;
//            BigDecimal max = BigDecimal.valueOf(Long.parseLong(String.valueOf(maxOpt)));
//            BigDecimal min = BigDecimal.valueOf(Long.parseLong(String.valueOf(minOpt)));
//            BigDecimal interval =  (max.subtract(min)).divide(BigDecimal.valueOf(5));

            List<QuestionOption> optionListOfOneQuestion = selectedBlankQuestionOptionList.stream().filter(item -> item.getQuestionId().equals(questionId)).collect(Collectors.toList());

            //有选项 有前后缀
//            if( questionOption != null ){
//                List<Double> list2 = new ArrayList<>();
//                for(QuestionOption record : optionListOfOneQuestion){
//                    String answerDetailContent = record.getAnswerDetailContent();
//                    if(!NumberUtil.isNumber(answerDetailContent)){
//                        list2.add(new Double( filterNonDigitCharacters(record.getAnswerDetailContent())));
//                    } else {
//                        list2.add(new Double( record.getAnswerDetailContent()));
//                    }
//                }
////                maxValue  =  !CollUtil.isEmpty(list2)  ? Collections.max(list2) : new BigDecimal(0);
////                minValue  = !CollUtil.isEmpty(list2)  ? Collections.min(list2) : new BigDecimal(0);
////                interval =  (maxValue.subtract(minValue)).divide(BigDecimal.valueOf(5));
//
//                maxValue  =  !CollUtil.isEmpty(list2)  ? Collections.max(list2) : 0;
//                minValue  = !CollUtil.isEmpty(list2)  ? Collections.min(list2) : 0;
//                interval =  (maxValue-(minValue))/5;
//
//                //min
//                num1 = minValue+(interval*1);
//                num2 = minValue+(interval*2);
//                num3 = minValue+(interval*3);
//                num4 = minValue+(interval*4);
////                num1 = minValue.add(interval.multiply(BigDecimal.valueOf(1))) ;
////                num2 = minValue.add(interval.multiply(BigDecimal.valueOf(2))) ;
////                num3 = minValue.add(interval.multiply(BigDecimal.valueOf(3))) ;
////                num4 = minValue.add(interval.multiply(BigDecimal.valueOf(4))) ;
//                //max
//            }

            try{

//            minValue  = BigDecimal.valueOf(list2.stream().map(QuestionOption::getAnswerDetailContent).mapToDouble(Double::parseDouble).min().getAsDouble());
//            maxValue  = BigDecimal.valueOf(list2.stream().map(QuestionOption::getAnswerDetailContent).mapToDouble(Double::parseDouble).max().getAsDouble());

//            OptionalInt minOpt = selectedBlankQuestionOptionList.stream().map(QuestionOption::getAnswerDetailContent).mapToInt(Integer::parseInt).min() ;
//            OptionalInt maxOpt = selectedBlankQuestionOptionList.stream().map(QuestionOption::getAnswerDetailContent).mapToInt(Integer::parseInt).max() ;
//            BigDecimal max = BigDecimal.valueOf(Long.parseLong(String.valueOf(maxOpt)));
//            BigDecimal min = BigDecimal.valueOf(Long.parseLong(String.valueOf(minOpt)));
//            BigDecimal interval =  (max.subtract(min)).divide(BigDecimal.valueOf(5));

//            if(a.compareTo(b) == -1){ System.out.println(“a小于b”);  }
//            if(a.compareTo(b) == 0){  System.out.println(“a等于b”);  }
//            if(a.compareTo(b) == 1){  System.out.println(“a大于b”);  }
//            if(a.compareTo(b) > -1){  System.out.println(“a大于等于b”);  }
//            if(a.compareTo(b) < 1){   System.out.println(“a小于等于b”);  }

            selectedBlankQuestionOptionList.stream().filter(item -> item.getQuestionId().equals(questionId)).forEach(item -> {
                if (questionOption != null){    //-- 答卷 option_id不=0 有选项/有前后缀
                    QuestionOption questionOption2 = new QuestionOption();

                        questionOption2.setQuestionTitle(item.getQuestionTitle());
                        String questionOptionContent = item.getContent();               //%i条
                        String answerDetailContent = item.getAnswerDetailContent();     //453
                        answerDetailContent = !NumberUtil.isNumber(answerDetailContent) ? filterNonDigitCharacters(answerDetailContent) : answerDetailContent;

                        questionOption2.setContent(questionOptionContent.replace("%i", answerDetailContent));
                        questionOption2.setStoreId(item.getStoreId());

//                        //消费者问卷则Excel导出userId, 零售户问卷不导出
//                        Long actvtyId = item.getActivityId();
//                        QueryWrapper<Activity> activityQueryWrapper2 = new QueryWrapper<>();
//                        activityQueryWrapper2.lambda().eq(Activity::getActivityId, actvtyId);
//                        Activity activity = activityService.getOne(activityQueryWrapper2);
                        if(activity.getForUserType()==1){
                            questionOption2.setUserId(item.getUserId());
                        } else {
                            questionOption2.setUserId(new Long(0));
                        }

//                        log.info("888888999999answerDetailContent"+answerDetailContent);
//                        inputNum = new Double(answerDetailContent);
//                        log.info("888888999999inputNum"+inputNum);
////                        if(inputNum.compareTo(minValue) == 1 && inputNum.compareTo(num1) == -1){
////                            counter1++;
////                        } else if (inputNum.compareTo(num1) == 1 && inputNum.compareTo(num2) == -1){
////                            counter2++;
////                        } else if (inputNum.compareTo(num2) == 1 && inputNum.compareTo(num3) == -1){
////                            counter3++;
////                        } else if (inputNum.compareTo(num3) == 1 && inputNum.compareTo(num4) == -1){
////                            counter4++;
////                        } else if (inputNum.compareTo(num4) == 1 && inputNum.compareTo(maxValue) == -1){
////                            counter5++;
////                        }
//                    if( inputNum>=minValue  && inputNum<=num1 ){
//                        counter1++;
//                    } else if (inputNum>num1 && inputNum<=num2){
//                        counter2++;
//                    } else if (inputNum>num2&& inputNum<=num3){
//                        counter3++;
//                    } else if (inputNum>num3 && inputNum<=num4){
//                        counter4++;
//                    } else if (inputNum>num4 && inputNum<=maxValue){
//                        counter5++;
//                    }

                    returnQuestionOptionList.add(questionOption2);
                } else {                        //-- 答卷 option_id=0 无选项/无前后缀
//                    log.info("11111111111111111-questionOption==null");
                    QuestionOption questionOption3 = new QuestionOption();
                    questionOption3.setContent(item.getAnswerDetailContent());
                    questionOption3.setQuestionTitle(item.getQuestionTitle());
                    questionOption3.setStoreId(item.getStoreId());
                    questionOption3.setUserId(item.getUserId());

                    returnQuestionOptionList.add(questionOption3);
                }
            });
//            returnQuestionOptionList.addAll(addQuestionOptions);
//            returnQuestionOptionList.sort(Comparator.comparing(QuestionOption::getSort));

            question.setQuestionOptionList(returnQuestionOptionList);

//            if( questionOption != null ){
//                counterSum=counter1+counter2+counter3+counter4+counter5;
//                question.setMinValue(minValue);
//                question.setNum1(num1);
//                question.setNum2(num2);
//                question.setNum3(num3);
//                question.setNum4(num4);
//                question.setMaxValue(maxValue);
//                question.setCounter1(counter1);
//                question.setCounter2(counter2);
//                question.setCounter3(counter3);
//                question.setCounter4(counter4);
//                question.setCounter5(counter5);
//                question.setCounterSum(counterSum);
//                question.setC1prop(Double.valueOf(numberformat.format((float) counter1 / (float) counterSum * 100)));
//                question.setC2prop(Double.valueOf(numberformat.format((float) counter2 / (float) counterSum * 100)));
//                question.setC3prop(Double.valueOf(numberformat.format((float) counter3 / (float) counterSum * 100)));
//                question.setC4prop(Double.valueOf(numberformat.format((float) counter4 / (float) counterSum * 100)));
//                question.setC5prop(Double.valueOf(numberformat.format((float) counter5 / (float) counterSum * 100)));
//
////                BigDecimal prop1 = new BigDecimal(counter1 / counterSum ) ;
////                BigDecimal prop2 = new BigDecimal(counter2 / counterSum ) ;
////                BigDecimal prop3 = new BigDecimal(counter3 / counterSum ) ;
////                BigDecimal prop4 = new BigDecimal(counter4 / counterSum ) ;
////                BigDecimal prop5 = new BigDecimal(counter5 / counterSum ) ;
////                question.setC1prop(new BigDecimal(numberformat.format(counter1 / counterSum * 100)));
////                question.setC2prop(new BigDecimal(numberformat.format(counter2 / counterSum * 100)));
////                question.setC3prop(new BigDecimal(numberformat.format(counter3 / counterSum * 100)));
////                question.setC4prop(new BigDecimal(numberformat.format(counter4 / counterSum * 100)));
////                question.setC5prop(new BigDecimal(numberformat.format(counter5 / counterSum * 100)));
//
//            }


            } catch (Exception e) {
                log.error("8888888 -------------->",e.getMessage());
                e.printStackTrace();
                log.error("9999999 -------------->",e);
            }

        }

        return allBlankQuestionList;
    }


    //删去非数字的字符 - 代码中的[^\\d]表示匹配任何非数字字符。replaceAll()方法将输入字符串中匹配到的非数字字符替换为空字符串，最后返回过滤后的结果。
    public static String filterNonDigitCharacters(String input) {
        return input.replaceAll("[^\\d]", "");
    }


    /**
     * 2 获取一份答卷：题目+选项 (选择题)
     */
    @GetMapping("/getSelectQuestionOptionResultsByAnswerId/{answerId}")
    public ResultVo getSelectQuestionOptionResultsByAnswerId(@PathVariable Long answerId) {
        //答卷
        QueryWrapper<Answer> answerQueryWrapper = new QueryWrapper<>();
        answerQueryWrapper.lambda().eq(Answer::getAnswerId, answerId);
        Answer answer = answerService.getOne(answerQueryWrapper);

        //问卷
        Long paperId = answer.getPaperId();
        Paper paper = new Paper();

        //问卷-所有题目  (单选+多选+填空+...)
        List<Question> allQuestionList = new ArrayList<>();
        QueryWrapper<PaperQuestion> paperQuestionQueryWrapper = new QueryWrapper<>();
        paperQuestionQueryWrapper.lambda().eq(PaperQuestion::getPaperId, paperId);
        List<PaperQuestion> paperQuestionList = paperQuestionService.list(paperQuestionQueryWrapper);
        List<Long> questionIdList = paperQuestionList.stream().map(PaperQuestion::getQuestionId).collect(Collectors.toList());

        //问卷-每道题目
        QueryWrapper<Question> questionQueryWrapper = new QueryWrapper<>();
        questionQueryWrapper.lambda().in(Question::getQuestionId, questionIdList);
        List<Question> questionList = questionService.list(questionQueryWrapper);
        Map<Long, Question> questionMap = questionList.stream().collect(Collectors.toMap(Question::getQuestionId, Function.identity()));        //List to Map

        // 问卷-每道题目-所有选项
        QueryWrapper<QuestionOption> questionOptionQueryWrapper = new QueryWrapper<>();
        questionOptionQueryWrapper.lambda().in(QuestionOption::getQuestionId, questionIdList);
        List<QuestionOption> questionOptionList = questionOptionService.list(questionOptionQueryWrapper);
        Map<Long, List<QuestionOption>> questionOptionMap = questionOptionList.stream().collect(Collectors.groupingBy(QuestionOption::getQuestionId));

        // 答卷-每道题目-选中的选项
        QueryWrapper<AnswerDetail> answerDetailQueryWrapper = new QueryWrapper<>();
        answerDetailQueryWrapper.lambda().eq(AnswerDetail::getAnswerId, answerId);
        List<AnswerDetail> answerDetailFullList = answerDetailService.list(answerDetailQueryWrapper);   //被选中选项
        Map<Long, List<AnswerDetail>> answerDetailMap = answerDetailFullList.stream().collect(Collectors.groupingBy(AnswerDetail::getQuestionId));

        for( PaperQuestion paperQuestion : paperQuestionList ){
            Long questionId = paperQuestion.getQuestionId();
            List<AnswerDetail> answerDetailList = Optional.ofNullable(answerDetailMap.get(questionId)).orElse(new ArrayList<>());
            Map<Long, AnswerDetail> detailMap = answerDetailList.stream().collect(Collectors.toMap(AnswerDetail::getOptionId, Function.identity()));
            Question question = questionMap.get(questionId);

            //一条AnswerDetail为一条作答记录：每个单选有一个AnswerDetail，每个多选有多条AnswerDetail
            if(question.getType()==1 || question.getType()==2){
                for(QuestionOption questionOption : questionOptionMap.get(questionId)){
                    extracted(answerDetailList, detailMap, questionOption);
                    for(AnswerDetail answerDetail : answerDetailList){
                        if(answerDetail.getOptionId().equals(questionOption.getOptionId())){
                            questionOption.setIsSelected(true);
                        }
                    }
                }

//                if(answerDetailList!=null && answerDetailList.size()!=0){
//                    for( AnswerDetail answerDetail : answerDetailList){
//                        Long answerDetailOptionId = answerDetail.getOptionId();     //选中选项的optionId
//                        for(QuestionOption questionOption : questionOptionMap.get(questionId)){       //给选中选项赋值
//                            String originString = questionOption.getContent();
//                            String regex = "(^%[a-zA-Z])|(%[a-zA-Z]$)";
//                            Matcher matcher= Pattern.compile(regex).matcher(originString);
//                            if(matcher.find()){
////                                System.out.println("===originString===" + originString);
//                                String str = questionOption.getContent();
//                                String lastTwoChars = str.substring(str.length() - 2);
//                                System.out.println("===lastTwoChars===" + lastTwoChars);
////                                String strReplace = null;
//                                if(answerDetailOptionId.equals(questionOption.getOptionId())){          // 如果选择了的就替换成content
//                                    questionOption.setContent(questionOption.getContent().replace(lastTwoChars, answerDetail.getContent()));
//                                } else {                                                                // 如果没选择的,就替换成""
//                                    questionOption.setContent(questionOption.getContent().replace(lastTwoChars, ""));
//                                }
//                            } else {
//                                System.out.println("===originString===" + originString);
//                            }
//
//                            if(answerDetailOptionId.equals(questionOption.getOptionId())){
//                                questionOption.setIsSelected(true);
//                            }
//                        }
//                    }
//                }
                question.setQuestionOptionList(questionOptionMap.get(questionId));
            }
            //一条AnswerDetail为一条作答记录：optionId不为0的填空questionOptionList不为null, optionId为0的填空questionOptionList等于null
            if(question.getType()==3){
                //第一种
                List<QuestionOption> questionOptions = questionOptionMap.get(questionId);
                if (questionOptions==null || questionOptions.isEmpty()) {
                    if(CollectionUtil.isNotEmpty( answerDetailList) ){
                        AnswerDetail answerDetail = answerDetailList.get(0);   //被选中选项
                        String answerDetailContent = answerDetail.getContent();
//                        System.out.println("===answerDetailContent===" + answerDetailContent);
                        question.setAnswerDetailContent(answerDetail.getContent());
                    }
                }else{
                    QuestionOption questionOption = questionOptions.get(0);
                    extracted(answerDetailList, detailMap, questionOption);
                    question.setAnswerDetailContent(questionOption.getContent());
                }

//                //第二种
//                if(CollectionUtil.isNotEmpty( answerDetailList) ){
//                    AnswerDetail answerDetail = answerDetailList.get(0);   //被选中选项
//                    String answerDetailContent = answerDetail.getContent();
////                        System.out.println("===answerDetailContent===" + answerDetailContent);
//                    question.setAnswerDetailContent(answerDetail.getContent());
//                }

            }

//            question.setQuestionOptionList(questionOptionList);
            allQuestionList.add(question);
        }

        //问卷中,获取所有选择题 - 单选+多选
        List<Question> allSelectQuestionList = allQuestionList.stream().filter(item -> item.getType()==1 || item.getType()==2).collect(Collectors.toList());   //activityId 下的所有选择题 - 1单选2多选
        //问卷中,获取所有填空题 - 填空
        List<Question> allBlankQuestionList = allQuestionList.stream().filter(item -> item.getType()==3).collect(Collectors.toList());   //activityId 下的所有选择题 - 1单选2多选

        paper.setSelectQuestionList(allSelectQuestionList);
        paper.setBlankQuestionList(allBlankQuestionList);
        return ResultUtils.success("查询成功", paper);
    }

    private void extracted(List<AnswerDetail> answerDetailList, Map<Long, AnswerDetail> detailMap, QuestionOption questionOption) {
        String originString = questionOption.getContent();
        String regex = "(^%[a-zA-Z])|(%[a-zA-Z]$)";
        Matcher matcher= Pattern.compile(regex).matcher(originString);
        if (!matcher.find()) {
            return;
        }
        AnswerDetail selectedOption = detailMap.get(questionOption.getOptionId());
        if(selectedOption != null){ //选中的
            questionOption.setIsSelected(true);
            if (questionOption.getContent().startsWith("%")){  //%c开头
                questionOption.setContent("<span class='user-input-content'>" + selectedOption.getContent() +"</span>" + questionOption.getContent().substring(2));
            }else{ //%c结尾
                questionOption.setContent(questionOption.getContent().substring(0, questionOption.getContent().length() - 2) + "<span class='user-input-content'>" +  selectedOption.getContent() +"</span>" );
            }
        }else{  //未选中的
            if (questionOption.getContent().startsWith("%")){   //%c开头
                questionOption.setContent(questionOption.getContent().substring(2));
            }else{  //%c结尾
                questionOption.setContent(questionOption.getContent().substring(0, questionOption.getContent().length() - 2));
            }
        }
    }


    /**
    * 导出接口   http://local host:8089/api/activity/export
    */
    @GetMapping("/export/getTotalListByActivityId/{activityId}")
    public void export(HttpServletResponse response, @PathVariable Long activityId) throws Exception {
        //filename
        QueryWrapper<Activity> activityQueryWrapper = new QueryWrapper<>();
        activityQueryWrapper.lambda().eq(Activity::getActivityId, activityId);
        Activity activity = activityService.getOne(activityQueryWrapper);
        String activityTitle = activity.getTitle();

        // 1.问卷 选择题
        List<Question> allQuestionList = activityService.getQuestionListByActivityId(activityId);
        List<Question> allSelectQuestionList = allQuestionList.stream().filter(item -> item.getType()==1 || item.getType()==2).collect(Collectors.toList());   //activityId 下的所有选择题 - 1单选2多选
        List<Question> allBlankQuestionList = allQuestionList.stream().filter(item -> item.getType()==3).collect(Collectors.toList());   //activityId 下的所有选择题 - 1单选2多选

        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //设置要导出到的sheet
        writer.renameSheet(0,"选择");
        //写入表头
        List<String> rowHead = CollUtil.newArrayList("题目", "选项", "票数", "比例");
        writer.writeHeadRow(rowHead);

        // 2.答卷 选择题
        List<Question> selectQuestionList = getSelectTotalListByActivityId(activityId, allSelectQuestionList);

        //sheet1 选择题 Start
        Iterator<Question> iterator = selectQuestionList.iterator();
        int currentRow = 1;
        while (iterator.hasNext()) {
            Question question = iterator.next();
            int startRow = currentRow;
            String title = question.getTitle();

            List<QuestionOption> questionOptionList = question.getQuestionOptionList();
//            questionOptionList.sort(Comparator.comparing(QuestionOption::getSort));
            for(int j=0; j<questionOptionList.size(); j++,currentRow++){
                writer.writeCellValue(1, currentRow, questionOptionList.get(j).getContent());              //第二行第二列 (1,1,'')   第三行第二列 (1,2,'')
                writer.writeCellValue(2, currentRow, questionOptionList.get(j).getOptionTotal());          //第二行第三列 (2,1,'')   第三行第三列 (2,2,'')
                writer.writeCellValue(3, currentRow, questionOptionList.get(j).getOptionProportion());     //第二行第四列 (3,1,'')   第三行第四列 (3,2,'')
            }
            //如果选项大于1条，合并试题单元格
            if (currentRow - startRow > 1) {
                writer.merge(startRow, currentRow-1, 0, 0, title,false);    //合并行
            } else {
                writer.writeCellValue( 0, startRow, title);
            }
        }
        //sheet1 选择题 END


        //sheet2 填空题 Start - 设置要导出到的sheet
        writer.setSheet("填空");

        //head
//        List<String> rowHead2 = CollUtil.newArrayList("题目", "填空内容", "storeId", "userId");
        List<String> rowHead2 = CollUtil.newArrayList("题目", "填空内容");
        writer.writeHeadRow(rowHead2);

        // 设置单元格格式为文本
        Workbook workbook = writer.getWorkbook();
        workbook.getSheetAt(0).setColumnWidth(0, 15000);
        workbook.getSheetAt(0).setColumnWidth(1, 15000);
        workbook.getSheetAt(0).setColumnWidth(2, 5000);
        workbook.getSheetAt(0).setColumnWidth(3, 5000);
        workbook.getSheetAt(1).setColumnWidth(0, 20000);
        workbook.getSheetAt(1).setColumnWidth(1, 5000);
        workbook.getSheetAt(1).setColumnWidth(2, 7500);
        workbook.getSheetAt(1).setColumnWidth(3, 7500);


        StyleSet styleSet2 = new StyleSet(workbook);
//        Font font = workbook.createFont();
//        font.setBold(true);
//        font.setColor(Font.COLOR_RED);
//        font.setFontName("微软雅黑");
//        font.setItalic(true);
//        styleSet2.setFont(font,true);
        CellStyle cellStyle2 = styleSet2.getCellStyle();
//        styleSet2.setVerticalAlignment(VerticalAlignment.BOTTOM);
//        styleSet2.setAlignment(HorizontalAlignment.LEFT);
        DataFormat format2 = writer.getWorkbook().createDataFormat();
        cellStyle2.setDataFormat(format2.getFormat("@"));
        writer.setStyleSet(styleSet2);


        List<Question> blankQuestionList = getBlankTotalListByActivityId(activityId, allBlankQuestionList);
        Iterator<Question> iterator2 = blankQuestionList.iterator();
        int currentRow2 = 1;
        while (iterator2.hasNext()) {
            Question question = iterator2.next();
            int startRow = currentRow2;
//            String title = question.getTitle();

            List<QuestionOption> questionOptionList = question.getQuestionOptionList();
//            questionOptionList.sort(Comparator.comparing(QuestionOption::getSort));
            for(int j=0; j<questionOptionList.size(); j++,currentRow2++){
                writer.writeCellValue(0, currentRow2, questionOptionList.get(j).getQuestionTitle());        //第二行第二列 (1,1,'')   第三行第二列 (1,2,'')
                writer.writeCellValue(1, currentRow2, questionOptionList.get(j).getContent());              //第二行第三列 (2,1,'')   第三行第三列 (2,2,'')
//                writer.writeCellValue(2, currentRow2, questionOptionList.get(j).getStoreId().toString());          //第二行第三列 (2,1,'')   第三行第三列 (2,2,'')
//                writer.writeCellValue(3, currentRow2, questionOptionList.get(j).getUserId().toString());     //第二行第四列 (3,1,'')   第三行第四列 (3,2,'')
            }
            //如果选项大于1条，合并试题单元格
//            if (currentRow2 - startRow > 1) {
//                writer.merge(startRow, currentRow2-1, 0, 0, title,false);    //合并行
//            } else {
//                writer.writeCellValue( 0, startRow, title);
//            }
        }
        //sheet2 填空题 END

//        //填空题统计 Start - 设置要导出到的sheet
//        writer.setSheet("填空统计");
//        List<String> rowHead3 = CollUtil.newArrayList("题目", "区间1","区间2","区间3","区间4","区间5","总和");
//        writer.writeHeadRow(rowHead3);
//        // 填空题统计 Start
//        Iterator<Question> iterator3 = blankQuestionList.iterator();
//        int currentRow3 = 1;
//        while (iterator3.hasNext()) {
//            Question question = iterator3.next();
//            int startRow = currentRow3;
//
//            for(int j=0; j<2; j++,currentRow3+=2){
//
//                writer.writeCellValue(0, currentRow3, "");                                                       //第二行第二列 (1,1,'')   第三行第二列 (1,2,'')
//                writer.writeCellValue(1, currentRow3, question.getMinValue()+"-"+question.getNum1());            //第二行第二列 (1,1,'')   第三行第二列 (1,2,'')
//                writer.writeCellValue(2, currentRow3, question.getNum1()+"-"+question.getNum2());                //第二行第二列 (1,1,'')   第三行第二列 (1,2,'')
//                writer.writeCellValue(3, currentRow3, question.getNum2()+"-"+question.getNum3());                //第二行第二列 (1,1,'')   第三行第二列 (1,2,'')
//                writer.writeCellValue(4, currentRow3, question.getNum3()+"-"+question.getNum4());                //第二行第二列 (1,1,'')   第三行第二列 (1,2,'')
//                writer.writeCellValue(5, currentRow3, question.getNum4()+"-"+question.getMaxValue());            //第二行第二列 (1,1,'')   第三行第二列 (1,2,'')
//                writer.writeCellValue(6, currentRow3, "");                                                       //第二行第二列 (1,1,'')   第三行第二列 (1,2,'')
//
//                writer.writeCellValue(0, currentRow3+1, question.getTitle());                                         //第二行第二列 (1,1,'')   第三行第二列 (1,2,'')
//                writer.writeCellValue(1, currentRow3+1, question.getC1prop()+"%");                              //第二行第二列 (1,1,'')   第三行第二列 (1,2,'')
//                writer.writeCellValue(2, currentRow3+1, question.getC2prop()+"%");                              //第二行第二列 (1,1,'')   第三行第二列 (1,2,'')
//                writer.writeCellValue(3, currentRow3+1, question.getC3prop()+"%");                              //第二行第二列 (1,1,'')   第三行第二列 (1,2,'')
//                writer.writeCellValue(4, currentRow3+1, question.getC4prop()+"%");                              //第二行第二列 (1,1,'')   第三行第二列 (1,2,'')
//                writer.writeCellValue(5, currentRow3+1, question.getC5prop()+"%");                              //第二行第二列 (1,1,'')   第三行第二列 (1,2,'')
//                writer.writeCellValue(6, currentRow3+1, question.getCounterSum());
//
//                writer.merge(currentRow3, currentRow3+1, 0, 0, question.getTitle(), false);    //合并行
//
//            }
////            List<QuestionOption> questionOptionList = question.getQuestionOptionList();
//////            questionOptionList.sort(Comparator.comparing(QuestionOption::getSort));
////            for(int j=0; j<questionOptionList.size(); j++,currentRow2++){
////                writer.writeCellValue(0, currentRow2, questionOptionList.get(j).getQuestionTitle());        //第二行第二列 (1,1,'')   第三行第二列 (1,2,'')
////                writer.writeCellValue(1, currentRow2, questionOptionList.get(j).getContent());              //第二行第三列 (2,1,'')   第三行第三列 (2,2,'')
//////                writer.writeCellValue(2, currentRow2, questionOptionList.get(j).getOptionTotal());          //第二行第三列 (2,1,'')   第三行第三列 (2,2,'')
//////                writer.writeCellValue(3, currentRow2, questionOptionList.get(j).getOptionProportion());     //第二行第四列 (3,1,'')   第三行第四列 (3,2,'')
////            }
//            //如果选项大于1条，合并试题单元格
////            if (currentRow2 - startRow > 1) {
////                writer.merge(startRow, currentRow2-1, 0, 0, title,false);    //合并行
////            } else {
////                writer.writeCellValue( 0, startRow, title);
////            }
//        }
//        // 填空题统计 END

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("答卷统计-"+activityTitle, "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        //将excel文件信息写入输出流，返回给调用者
        ServletOutputStream excelOut = null;
        try {
            excelOut = response.getOutputStream();
            writer.flush(excelOut,true);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            writer.close();
        }
        IoUtil.close(excelOut);

    }



//    /**
//     * excel 导入
//     * @param file
//     * @throws Exception
//     */
//    @PostMapping("/import")
//    public ResultVo imp(MultipartFile file) throws Exception {
//        InputStream inputStream = file.getInputStream();
//        ExcelReader reader = ExcelUtil.getReader(inputStream);
//        // 通过 javabean的方式读取Excel内的对象，但是要求表头必须是英文，跟javabean的属性要对应起来
//        List<Activity> list = reader.readAll(Activity.class);
//
//        activityService.saveBatch(list);
//        return ResultUtils.success();
//    }

    private User getUser() {
        return TokenUtils.getCurrentUser();
    }

}

