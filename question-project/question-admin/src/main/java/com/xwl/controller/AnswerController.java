package com.xwl.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.poi.excel.StyleSet;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xwl.entity.*;
import com.xwl.service.*;
import com.xwl.utils.Constants;
import com.xwl.utils.ResultUtils;
import com.xwl.utils.ResultVo;
import com.xwl.utils.TokenUtils;
import org.apache.ibatis.cursor.Cursor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author
 * @since 2023-07-31
 */
@RestController
@RequestMapping("/api/answer")
public class AnswerController {

    @Resource
    private IAnswerService answerService;
    @Resource
    private IActivityService activityService;
    @Resource
    private IProjectActivityService projectActivityService;
    @Resource
    private IProjectService projectService;
    @Resource
    private IAppUserService appUserService;
    @Resource
    private IStoreService storeService;

    private final String now = DateUtil.now();

    // 新增或者更新
    @PostMapping
    public ResultVo save(@RequestBody Answer answer) {
        if (answer.getId() == null) {
            //answer.setTime(DateUtil.now());
            //answer.setUser(TokenUtils.getCurrentUser().getUsername());
        }
        answerService.saveOrUpdate(answer);
        return ResultUtils.success("操作成功");
    }

    @DeleteMapping("/{id}")
    public ResultVo delete(@PathVariable Integer id) {
        answerService.removeById(id);
        return ResultUtils.success("删除成功");
    }

    @PostMapping("/del/batch")
    public ResultVo deleteBatch(@RequestBody List<Integer> ids) {
        answerService.removeByIds(ids);
        return ResultUtils.success("批量删除成功");
    }

    @GetMapping
    public ResultVo findAll() {
        return ResultUtils.success("查询成功",answerService.list());
    }

    @GetMapping("/{id}")
    public ResultVo findOne(@PathVariable Integer id) {
        return ResultUtils.success("查询成功",answerService.getById(id));
    }

    @GetMapping("/page")
    public ResultVo findPage(@RequestParam(defaultValue = "") String name,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<Answer> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        if (!"".equals(name)) {
            queryWrapper.like("name", name);
        }
//        User currentUser = TokenUtils.getCurrentUser();
//        if (currentUser.getRole().equals("ROLE_USER")) {
//            queryWrapper.eq("user", currentUser.getUsername());
//        }
        return ResultUtils.success("查询成功",answerService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

//    @GetMapping("/getAnswerListByActivityId/{activityId}")
//    public ResultVo getAnswerListByActivityId(@PathVariable Integer activityId,
//                                              @RequestParam(defaultValue = "") String name,
//                                              @RequestParam Integer pageNum,
//                                              @RequestParam Integer pageSize) {
//        QueryWrapper<Answer> answerQueryWrapper = new QueryWrapper<>();
//        answerQueryWrapper.lambda().eq(Answer::getActivityId, activityId);
////        if (!"".equals(name)) {
////            answerQueryWrapper.like("name", name);
////        }
//        IPage<Answer> answerListPage = answerService.page(new Page<>(pageNum, pageSize), answerQueryWrapper);
//        List<Answer> answerList = answerListPage.getRecords();
//
//        if(answerList==null || answerList.size()==0){
//            return ResultUtils.success("无数据记录", null);  // no data 没有数据
//        }
//
//        for(Answer answer : answerList){
//            Long userId = answer.getUserId();
//            QueryWrapper<AppUser> appUserQueryWrapper = new QueryWrapper<>();
//            appUserQueryWrapper.lambda().eq(AppUser::getUserId, userId);
//            AppUser appUser = appUserService.getOne(appUserQueryWrapper);
//            answer.setAppUser(appUser);
//
//            Long storeId = answer.getStoreId();
//            QueryWrapper<Store> storeQueryWrapper = new QueryWrapper<>();
//            storeQueryWrapper.lambda().eq(Store::getStoreId, storeId);
//            Store store = storeService.getOne(storeQueryWrapper);
//            answer.setStore(store);
//
////            Long activityId = answer.getActivityId();
//            QueryWrapper<Activity> activityQueryWrapper = new QueryWrapper<>();
//            activityQueryWrapper.lambda().eq(Activity::getActivityId, activityId);
//            Activity activity = activityService.getOne(activityQueryWrapper);
////            answer.setTitle(activity.getTitle());
//
//            answer.setActivity(activity);
//
//        }
//        answerListPage.setRecords(answerList);
//        return ResultUtils.success("查询成功", answerListPage);
//    }


    @GetMapping("/getAnswerListByActivityId/{activityId}")
    public ResultVo getAnswerListByActivityId(@PathVariable Long activityId,
                                              @RequestParam(defaultValue = "") String name,
                                              @RequestParam(defaultValue = "") String licenseNo,
                                              @RequestParam Integer pageNum,
                                              @RequestParam Integer pageSize) {
        Answer answer = new Answer();
        answer.setActivityId(activityId);
        if( name!=null && StrUtil.isNotEmpty(name)){
            answer.setName(name);
        }
        if( licenseNo!=null && StrUtil.isNotEmpty(licenseNo)){
            answer.setLicenseNo(licenseNo);
        }
        IPage<Answer> answerListPage = answerService.getAnswersByActivityIdWithParm(new Page<>(pageNum, pageSize), answer);
        return ResultUtils.success("查询成功", answerListPage);
    }

    /**
     * 导出接口   http://local host:8089/api/store/exportStoreList
     */
    @GetMapping("/exportAnswerList/{activityId}")
    public void exportAnswersByActivityIdWithParm(HttpServletResponse response,
                                                  @PathVariable Long activityId,
                                                  @RequestParam(defaultValue = "") String name,
                                                  @RequestParam(defaultValue = "") String licenseNo) throws Exception {
        Answer answer = new Answer();
        answer.setActivityId(activityId);
        if( name!=null && StrUtil.isNotEmpty(name)){
            answer.setName(name);
        }
        if( licenseNo!=null && StrUtil.isNotEmpty(licenseNo)){
            answer.setLicenseNo(licenseNo);
        }
//        Cursor<Answer> answerList = answerService.exportAnswersByActivityIdWithParm(answer);
        List<Answer> answerList = answerService.exportAnswersByActivityIdWithParm(answer);

        //filename
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        String dateStr = sdf.format(date);

        // 1.问卷 选择题
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //设置要导出到的sheet
        writer.renameSheet(0,"批量更新客户信息");
        //写入表头
//        List<String> rowHead = CollUtil.newArrayList("答卷编号", "答卷门店专卖证号", "答卷门店名称", "答卷消费者昵称", "答卷消费者手机号", "答卷地点", "答卷完成时间", "答卷用时(秒)" );
//        writer.writeHeadRow(rowHead);

        // 设置单元格格式为文本
        Workbook workbook = writer.getWorkbook();
        workbook.getSheetAt(0).setColumnWidth(0, 6000);     //answer_id  答卷编号
        workbook.getSheetAt(0).setColumnWidth(1, 8000);    //licenseNo 答卷门店专卖证号
        workbook.getSheetAt(0).setColumnWidth(2, 8000);     //name 答卷门店名称
        workbook.getSheetAt(0).setColumnWidth(3, 8000);     //nickname 答卷消费者昵称
        workbook.getSheetAt(0).setColumnWidth(4, 8000);     //mobile 答卷消费者手机号
        workbook.getSheetAt(0).setColumnWidth(5, 8000);     //location 答卷地点
        workbook.getSheetAt(0).setColumnWidth(6, 8000);     //endTime 答卷完成时间
        workbook.getSheetAt(0).setColumnWidth(7, 4000);     //duration 答卷用时(秒)

        StyleSet styleSet = new StyleSet(workbook);
        CellStyle cellStyle = styleSet.getCellStyle();
        DataFormat format = writer.getWorkbook().createDataFormat();
        cellStyle.setDataFormat(format.getFormat("@"));
        writer.setStyleSet(styleSet);

        // 用个List<Map>装表格的所有内容
        ArrayList<Map<String, Object>> rows = new ArrayList<>();
        // 拼接上列明。我这里用的最土的方法，很不优雅别学我
        for(int i = 0; i < answerList.size(); i++){
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("答卷编号", answerList.get(i).getAnswerId().toString());
            row.put("答卷门店专卖证号", answerList.get(i).getLicenseNo());
            row.put("答卷门店名称", answerList.get(i).getName());
            row.put("答卷消费者昵称", answerList.get(i).getNickname());
            row.put("答卷消费者手机号", answerList.get(i).getMobile().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
            row.put("答卷地点", answerList.get(i).getLocation());
            row.put("答卷完成时间", answerList.get(i).getEndTime().toString());
            row.put("答卷用时(秒)", answerList.get(i).getDuration().toString());
            rows.add(row);
        }
        writer.write(rows, true);
        //sheet1 END

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
//        String fileName = URLEncoder.encode("store-" + dateStr, "UTF-8");
        String fileName = URLEncoder.encode("答卷清单-" + dateStr, "UTF-8");
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


    @GetMapping("/getAnswersProcessByActivityId/{activityId}")
    public ResultVo getAnswersProcessByActivityId(@PathVariable Long activityId,
                                              @RequestParam(defaultValue = "") String name,
                                              @RequestParam Integer pageNum,
                                              @RequestParam Integer pageSize) {

        Activity returnActivity = new Activity();
        Project returnProject = new Project();

        QueryWrapper<ProjectActivity> projectActivityQueryWrapper = new QueryWrapper<>();
        projectActivityQueryWrapper.lambda().eq(ProjectActivity::getActivityId, activityId);
        ProjectActivity projectActivity = projectActivityService.getOne(projectActivityQueryWrapper);
        if(projectActivity!=null){
            Long projectId = projectActivity.getProjectId();
            QueryWrapper<Project> projectQueryWrapper = new QueryWrapper<>();
            projectQueryWrapper.lambda().eq(Project::getProjectId, projectId);
            Project project = projectService.getOne(projectQueryWrapper);
            if(project!=null){
                String projectName = project.getName();
                returnProject.setName(projectName);
            }else{
                returnProject.setName("");
            }
        } else {
            returnProject.setName("");
        }
        returnActivity.setProject(returnProject);

        returnActivity.setActivityId(activityId);

        QueryWrapper<Activity> activityQueryWrapper = new QueryWrapper<>();
        activityQueryWrapper.lambda().eq(Activity::getActivityId, activityId);
        Activity activity = activityService.getOne(activityQueryWrapper);
        returnActivity.setTitle(activity.getTitle());

        //答卷数量
        QueryWrapper<Answer> answerQueryWrapper = new QueryWrapper<>();
        answerQueryWrapper.lambda().eq(Answer::getActivityId, activityId);
        List<Answer> answerList = answerService.list(answerQueryWrapper);
        returnActivity.setAnswerCount(answerList.size());

        return ResultUtils.success("查询成功", returnActivity);
    }

    /**
    * 导出接口
    */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        // 从数据库查询出所有的数据
        List<Answer> list = answerService.list();
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);

        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list, true);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("Answer信息表", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();

        }

    /**
     * excel 导入
     * @param file
     * @throws Exception
     */
    @PostMapping("/import")
    public ResultVo imp(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        // 通过 javabean的方式读取Excel内的对象，但是要求表头必须是英文，跟javabean的属性要对应起来
        List<Answer> list = reader.readAll(Answer.class);

        answerService.saveBatch(list);
        return ResultUtils.success();
    }

    private User getUser() {
        return TokenUtils.getCurrentUser();
    }


    //1.1 累计问卷数量
    @GetMapping("/getAnswerAmountByDevUserId")
    public ResultVo getAnswerAmountByDevUserId(@RequestParam Long userId) {
        Integer answersCount= answerService.getAnswerAmountByDevUserId();
        return ResultUtils.success("查询成功", answersCount);
    }
    //1.2 累计参与门店
    @GetMapping("/getStoreAmountByDevUserId")
    public ResultVo getStoreAmountByDevUserId(@RequestParam Long userId) {
        Integer storeAmount = answerService.getStoreAmountByDevUserId(userId);
        return ResultUtils.success("查询成功", storeAmount );
    }
    //1.3 当前管理员下，昨日新增问卷数量
    @GetMapping("/getLastdayAnswerAmountByDevUserId")
    public ResultVo getLastdayAnswerAmountByDevUserId(@RequestParam Long userId) {
        DateTime yesterday = DateUtil.yesterday();
        DateTime startOfYesterday = DateUtil.beginOfDay(yesterday);
        DateTime  endOfYesterday = DateUtil.endOfDay(yesterday);

        Integer answerAmount = answerService.getLastdayAnswerAmountByDevUserId(userId,startOfYesterday,endOfYesterday);
        return ResultUtils.success("查询成功", answerAmount);
    }
    //1.4 当前管理员下，昨日参与门店
    @GetMapping("/getLastdayAnswerListStoreAmountByDevUserId")
    public ResultVo getLastdayAnswerListStoreAmountByDevUserId(@RequestParam Long userId) {
        DateTime yesterday = DateUtil.yesterday();
        DateTime startOfYesterday = DateUtil.beginOfDay(yesterday);
        DateTime  endOfYesterday = DateUtil.endOfDay(yesterday);

        Integer storeAmount = answerService.getLastdayAnswerListStoreAmountByDevUserId(userId,startOfYesterday,endOfYesterday);
        return ResultUtils.success("查询成功", storeAmount);
    }
    //1.5 当前管理员下，昨日新增问卷来源 answerTotalLastday
    @GetMapping("/getGroupLastdayAnswerListByDevUserId")
    public ResultVo getGroupLastdayAnswerListByDevUserId(@RequestParam Long userId) {
        DateTime yesterday = DateUtil.yesterday();
        DateTime startOfYesterday = DateUtil.beginOfDay(yesterday);
        DateTime  endOfYesterday = DateUtil.endOfDay(yesterday);

        List<Answer> answerList = answerService.getGroupLastdayAnswerListByDevUserId(userId,startOfYesterday,endOfYesterday);     // for activity.total as total
        for(Answer answer: answerList){
            Long activityId = answer.getActivityId();
            QueryWrapper<Activity> activityQueryWrapper = new QueryWrapper<>();
            activityQueryWrapper.lambda().eq(Activity::getActivityId, activityId);
            Activity activity = activityService.getOne(activityQueryWrapper);
            answer.setActivity(activity);                                                           // for activity.title
        }
        return ResultUtils.success("查询成功", answerList);
    }
    //1.6 当前管理员下，昨日门店来源 storeTotalLastday
    @GetMapping("/getGroupLastdayAnswerListStoreAmountByDevUserId")
    public ResultVo getGroupLastdayAnswerListStoreAmountByDevUserId(@RequestParam Long userId) {
        DateTime yesterday = DateUtil.yesterday();
        DateTime startOfYesterday = DateUtil.beginOfDay(yesterday);
        DateTime  endOfYesterday = DateUtil.endOfDay(yesterday);

        List<Answer> answerList = answerService.getGroupLastdayAnswerListStoreAmountByDevUserId(userId,startOfYesterday,endOfYesterday); // for store.total as total
        for(Answer answer: answerList){
            Long activityId = answer.getActivityId();
            QueryWrapper<Activity> activityQueryWrapper = new QueryWrapper<>();
            activityQueryWrapper.lambda().eq(Activity::getActivityId, activityId);
            Activity activity = activityService.getOne(activityQueryWrapper);
            answer.setActivity(activity);
        }
        return ResultUtils.success("查询成功", answerList);
    }







    //2.1 累计问卷数量
//    @GetMapping("/getAnswerListByAdminUserId")
//    public ResultVo getAnswerListByAdminUserId(@RequestParam Long userId) {
//        List<Answer> answerList = answerService.getAnswerListByAdminUserId(userId);
//        answerList = answerList.stream().distinct().collect(Collectors.toList());
//        return ResultUtils.success("查询成功", answerList.size());
//    }
    //2.1 累计问卷数量
    @GetMapping("/getAnswerAmountByAdminUserId")
    public ResultVo getAnswerAmountByAdminUserId(@RequestParam Long userId) {
        Integer answersCount= answerService.getAnswerAmountByAdminUserId(userId);
//        System.out.println("===answersCount===" + answersCount);
        return ResultUtils.success("查询成功", answersCount);
    }

    //2.2 累计参与门店
    @GetMapping("/getStoreAmountByAdminUserId")
    public ResultVo getStoreAmountByAdminUserId(@RequestParam Long userId) {

//        List<Long> idList = answerService.getAnswerListStoreAmountByAdminUserId(userId);
//        idList = idList.stream().distinct().collect(Collectors.toList());
//        return ResultUtils.success("查询成功", idList.size());

//        DateTime yesterday = DateUtil.yesterday();
//        System.out.println("000000 yesterday:"+yesterday);  //2023-09-04 18:16:49
//        String yesterdayStr = DateUtil.formatDate(yesterday);
//        System.out.println("000000 yesterdayStr:"+yesterdayStr);    //2023-09-04

//        QueryWrapper<Answer> answerQueryWrapper = new QueryWrapper<>();
//        answerQueryWrapper.apply("DATE_FORMAT(create_time, '%Y-%m-%d') == DATE_FORMAT(DateUtil.yesterday(),'%Y-%m-%d')");
//        List<Answer> answerList = answerService.list(answerQueryWrapper);

//        answerQueryWrapper.apply("DATE(createtime) >= STR_TO_DATE('"+yesterday+" 00:00:00','%Y-%m-%d %H:%i:%s')");
//        answerQueryWrapper.apply("DATE(createtime) <= STR_TO_DATE('"+yesterday+" 23:59:59','%Y-%m-%d %H:%i:%s')");
//
//        answerList.stream().filter(timestamp ->
//                timestamp.isAfter(startDateTime) && timestamp.isBefore(endDateTime))

        Integer storeAmount = answerService.getStoreAmountByAdminUserId(userId);
        return ResultUtils.success("查询成功", storeAmount );
    }

    //2.3 当前管理员下，昨日新增问卷数量
//    @GetMapping("/getLastdayAnswerListByAdminUserId")
//    public ResultVo getLastdayAnswerListByAdminUserId(@RequestParam Long userId) {
//        List<Answer> answerList = answerService.getLastdayAnswerListByAdminUserId(userId);
//        return ResultUtils.success("查询成功", answerList.size());
//    }
    //2.3 当前管理员下，昨日新增问卷数量
    @GetMapping("/getLastdayAnswerAmountByAdminUserId")
    public ResultVo getLastdayAnswerListByAdminUserId(@RequestParam Long userId) {
        DateTime yesterday = DateUtil.yesterday();
        DateTime startOfYesterday = DateUtil.beginOfDay(yesterday);
        DateTime  endOfYesterday = DateUtil.endOfDay(yesterday);

//        System.out.println("昨天的起始时间：" + startOfYesterday );
//        System.out.println("昨天的起始时间：" + endOfYesterday );

        Integer answerAmount = answerService.getLastdayAnswerAmountByAdminUserId(userId,startOfYesterday,endOfYesterday);
        return ResultUtils.success("查询成功", answerAmount);
    }
//    //2.4 当前管理员下，昨日参与门店
//    @GetMapping("/getLastdayAnswerListStoreAmountByAdminUserId")
//    public ResultVo getLastdayAnswerListStoreAmountByAdminUserId(@RequestParam Long userId) {
//        List<Long> storeIdList = answerService.getLastdayAnswerListStoreAmountByAdminUserId(userId);
//        return ResultUtils.success("查询成功", storeIdList.size());
//    }
    //2.4 当前管理员下，昨日参与门店
    @GetMapping("/getLastdayAnswerListStoreAmountByAdminUserId")
    public ResultVo getLastdayAnswerListStoreAmountByAdminUserId(@RequestParam Long userId) {
        DateTime yesterday = DateUtil.yesterday();
        DateTime startOfYesterday = DateUtil.beginOfDay(yesterday);
        DateTime  endOfYesterday = DateUtil.endOfDay(yesterday);

        Integer storeAmount = answerService.getLastdayAnswerListStoreAmountByAdminUserId(userId,startOfYesterday,endOfYesterday);
        return ResultUtils.success("查询成功", storeAmount);
    }

//    //2.5 当前管理员下，昨日新增问卷来源 answerTotalLastday
//    @GetMapping("/getGroupLastdayAnswerListByAdminUserId")
//    public ResultVo getGroupLastdayAnswerListByAdminUserId(@RequestParam Long userId) {
//        List<Answer> answerList = answerService.getGroupLastdayAnswerListByAdminUserId(userId);     // for activity.total as total
//        for(Answer answer: answerList){
//            Long activityId = answer.getActivityId();
//            QueryWrapper<Activity> activityQueryWrapper = new QueryWrapper<>();
//            activityQueryWrapper.lambda().eq(Activity::getActivityId, activityId);
//            Activity activity = activityService.getOne(activityQueryWrapper);
//            answer.setActivity(activity);                                                           // for activity.title
//        }
//        return ResultUtils.success("查询成功", answerList);
//    }
    //2.5 当前管理员下，昨日新增问卷来源 answerTotalLastday
    @GetMapping("/getGroupLastdayAnswerListByAdminUserId")
    public ResultVo getGroupLastdayAnswerListByAdminUserId(@RequestParam Long userId) {
        DateTime yesterday = DateUtil.yesterday();
        DateTime startOfYesterday = DateUtil.beginOfDay(yesterday);
        DateTime  endOfYesterday = DateUtil.endOfDay(yesterday);

        List<Answer> answerList = answerService.getGroupLastdayAnswerListByAdminUserId(userId,startOfYesterday,endOfYesterday);     // for activity.total as total
        for(Answer answer: answerList){
            Long activityId = answer.getActivityId();
            QueryWrapper<Activity> activityQueryWrapper = new QueryWrapper<>();
            activityQueryWrapper.lambda().eq(Activity::getActivityId, activityId);
            Activity activity = activityService.getOne(activityQueryWrapper);
            answer.setActivity(activity);                                                           // for activity.title
        }
        return ResultUtils.success("查询成功", answerList);
    }

//    //2.6 当前管理员下，昨日门店来源 storeTotalLastday
//    @GetMapping("/getGroupLastdayAnswerListStoreAmountByAdminUserId")
//    public ResultVo getGroupLastdayAnswerListStoreAmountByAdminUserId(@RequestParam Long userId) {
//        List<Answer> answerList = answerService.getGroupLastdayAnswerListStoreAmountByAdminUserId(userId); // for store.total as total
//        for(Answer answer: answerList){
//            Long activityId = answer.getActivityId();
//            QueryWrapper<Activity> activityQueryWrapper = new QueryWrapper<>();
//            activityQueryWrapper.lambda().eq(Activity::getActivityId, activityId);
//            Activity activity = activityService.getOne(activityQueryWrapper);
//            answer.setActivity(activity);
//        }
//        return ResultUtils.success("查询成功", answerList);
//    }
    //2.6 当前管理员下，昨日门店来源 storeTotalLastday
    @GetMapping("/getGroupLastdayAnswerListStoreAmountByAdminUserId")
    public ResultVo getGroupLastdayAnswerListStoreAmountByAdminUserId(@RequestParam Long userId) {
        DateTime yesterday = DateUtil.yesterday();
        DateTime startOfYesterday = DateUtil.beginOfDay(yesterday);
        DateTime  endOfYesterday = DateUtil.endOfDay(yesterday);

        List<Answer> answerList = answerService.getGroupLastdayAnswerListStoreAmountByAdminUserId(userId,startOfYesterday,endOfYesterday); // for store.total as total
        for(Answer answer: answerList){
            Long activityId = answer.getActivityId();
            QueryWrapper<Activity> activityQueryWrapper = new QueryWrapper<>();
            activityQueryWrapper.lambda().eq(Activity::getActivityId, activityId);
            Activity activity = activityService.getOne(activityQueryWrapper);
            answer.setActivity(activity);
        }
        return ResultUtils.success("查询成功", answerList);
    }








    //3.1 累计问卷数量
    @GetMapping("/getAnswerAmountByPartnerUserId")
    public ResultVo getAnswerAmountByPartnerUserId(@RequestParam Long userId) {
        Integer answersCount= answerService.getAnswerAmountByPartnerUserId(userId);
        return ResultUtils.success("查询成功", answersCount);
    }
    //3.2 累计参与门店
    @GetMapping("/getStoreAmountByPartnerUserId")
    public ResultVo getStoreAmountByPartnerUserId(@RequestParam Long userId) {
        Integer storeAmount = answerService.getStoreAmountByPartnerUserId(userId);
        return ResultUtils.success("查询成功", storeAmount );
    }
    //3.3 当前管理员下，昨日新增问卷数量
    @GetMapping("/getLastdayAnswerAmountByPartnerUserId")
    public ResultVo getLastdayAnswerAmountByPartnerUserId(@RequestParam Long userId) {
        DateTime yesterday = DateUtil.yesterday();
        DateTime startOfYesterday = DateUtil.beginOfDay(yesterday);
        DateTime  endOfYesterday = DateUtil.endOfDay(yesterday);

        Integer answerAmount = answerService.getLastdayAnswerAmountByPartnerUserId(userId,startOfYesterday,endOfYesterday);
        return ResultUtils.success("查询成功", answerAmount);
    }
    //3.4 当前管理员下，昨日参与门店
    @GetMapping("/getLastdayAnswerListStoreAmountByPartnerUserId")
    public ResultVo getLastdayAnswerListStoreAmountByPartnerUserId(@RequestParam Long userId) {
        DateTime yesterday = DateUtil.yesterday();
        DateTime startOfYesterday = DateUtil.beginOfDay(yesterday);
        DateTime  endOfYesterday = DateUtil.endOfDay(yesterday);

        Integer storeAmount = answerService.getLastdayAnswerListStoreAmountByPartnerUserId(userId,startOfYesterday,endOfYesterday);
        return ResultUtils.success("查询成功", storeAmount);
    }
    //3.5 当前管理员下，昨日新增问卷来源 answerTotalLastday
    @GetMapping("/getGroupLastdayAnswerListByPartnerUserId")
    public ResultVo getGroupLastdayAnswerListByPartnerUserId(@RequestParam Long userId) {
        DateTime yesterday = DateUtil.yesterday();
        DateTime startOfYesterday = DateUtil.beginOfDay(yesterday);
        DateTime  endOfYesterday = DateUtil.endOfDay(yesterday);

        List<Answer> answerList = answerService.getGroupLastdayAnswerListByPartnerUserId(userId,startOfYesterday,endOfYesterday);     // for activity.total as total
        for(Answer answer: answerList){
            Long activityId = answer.getActivityId();
            QueryWrapper<Activity> activityQueryWrapper = new QueryWrapper<>();
            activityQueryWrapper.lambda().eq(Activity::getActivityId, activityId);
            Activity activity = activityService.getOne(activityQueryWrapper);
            answer.setActivity(activity);                                                           // for activity.title
        }
        return ResultUtils.success("查询成功", answerList);
    }
    //3.6 当前管理员下，昨日门店来源 storeTotalLastday
    @GetMapping("/getGroupLastdayAnswerListStoreAmountByPartnerUserId")
    public ResultVo getGroupLastdayAnswerListStoreAmountByPartnerUserId(@RequestParam Long userId) {
        DateTime yesterday = DateUtil.yesterday();
        DateTime startOfYesterday = DateUtil.beginOfDay(yesterday);
        DateTime  endOfYesterday = DateUtil.endOfDay(yesterday);

        List<Answer> answerList = answerService.getGroupLastdayAnswerListStoreAmountByPartnerUserId(userId,startOfYesterday,endOfYesterday); // for store.total as total
        for(Answer answer: answerList){
            Long activityId = answer.getActivityId();
            QueryWrapper<Activity> activityQueryWrapper = new QueryWrapper<>();
            activityQueryWrapper.lambda().eq(Activity::getActivityId, activityId);
            Activity activity = activityService.getOne(activityQueryWrapper);
            answer.setActivity(activity);
        }
        return ResultUtils.success("查询成功", answerList);
    }

}

