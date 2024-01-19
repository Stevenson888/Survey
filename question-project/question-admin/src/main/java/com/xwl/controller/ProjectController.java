package com.xwl.controller;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xwl.entity.*;
import com.xwl.service.*;
import com.xwl.utils.*;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 
 * @since 2023-08-12
 */
@RestController
@RequestMapping("/api/project")
public class ProjectController {

    @Resource
    private IProjectService projectService;
    @Resource
    private IUserService userService;
    @Resource
    private IProjectActivityService projectActivityService;
    @Resource
    private IActivityService activityService;
    @Resource
    private IActivityAreaLimitService activityAreaLimitService;
    @Resource
    private IChinaRegionService chinaRegionService;
    @Resource
    private IFileService fileService;

    private final DateTime now = DateUtil.date();

//    // 新增或者更新
//    @PostMapping
//    public ResultVo save(@RequestBody Project project) {
//        User currentUser = TokenUtils.getCurrentUser();
//
//        if (project.getId() == null) {
//            Long projectId = SnowflakeIdWorker.getNextId();
//            project.setProjectId(projectId);
//
//            // 202308
//            String currentDate = DateUtil.format(DateUtil.date(), "yyyyMMdd");
//            String currentYearAndMonth =currentDate.substring(0,6);
//            System.out.println("======currentYearAndMonth======" + currentYearAndMonth);
//            // 23 - TODO: 生成2位随机数，每个月清零
//            String str = new String("23");
//            System.out.println("======str2======" + str);
//            //拼接202308 + 23
//            String projectCode = currentYearAndMonth + str;
//            project.setCode(projectCode);
//
//            project.setCreateMuid(currentUser.getUserId());
//            project.setUpdateMuid(currentUser.getUserId());
//            project.setCreateTime(DateUtil.date());
//            project.setUpdateTime(DateUtil.date());
//            project.setProductCategory("0");
//
//            //每个projectId-activityId插入到 ProjectActivity表
//            List<Object> selectActivityList = project.getSelectedActivityList();
//            System.out.println("===project.getSelectedActivityList()===" + project.getSelectedActivityList());
//
//            for(Object obj: selectActivityList){
//                ProjectActivity projectActivity = new ProjectActivity();
//                projectActivity.setProjectId(projectId);
//
//                LinkedHashMap newObj = (LinkedHashMap)obj;
//                Object activityIdObj = newObj.get("oneId");
//                Long activityId = Long.valueOf(String.valueOf(activityIdObj));
//                System.out.println("======项目projectId " + projectId + " 关联活动activityId " + activityId);
//                projectActivity.setActivityId(activityId);
//
//                projectActivityService.save(projectActivity);
//            }
//
//        } else {
//            project.setUpdateMuid(currentUser.getUserId());
//            project.setUpdateTime(DateUtil.date());
//        }
//        projectService.saveOrUpdate(project);
//        return ResultUtils.success("操作成功");
//    }


    // 新增或者更新
    @PostMapping
    public ResultVo save(@RequestBody Project project) {
        User currentUser = TokenUtils.getCurrentUser();

        //新增
        if (project.getId() == null) {
            Long projectId = SnowflakeIdWorker.getNextId();
            project.setProjectId(projectId);

            String projectCode = projectService.getProjectCode();
            project.setCode(projectCode);

            project.setCreateMuid(currentUser.getUserId());
            project.setUpdateMuid(currentUser.getUserId());
            project.setCreateTime(DateUtil.date());
            project.setUpdateTime(DateUtil.date());
            project.setProductCategory("0");

            //每个projectId-activityId插入到 ProjectActivity表
//            List<Object> selectActivityList = project.getSelectedActivityList();
//            System.out.println("===project.getSelectedActivityList()===" + project.getSelectedActivityList());
//
//            for(Object obj: selectActivityList){
//                ProjectActivity projectActivity = new ProjectActivity();
//                projectActivity.setProjectId(projectId);
//
//                LinkedHashMap newObj = (LinkedHashMap)obj;
//                Object activityIdObj = newObj.get("oneId");
//                Long activityId = Long.valueOf(String.valueOf(activityIdObj));
//                System.out.println("======项目projectId " + projectId + " 关联活动activityId " + activityId);
//                projectActivity.setActivityId(activityId);
//
//                projectActivityService.save(projectActivity);
//            }

            //Activity有与Project的关联关系时(有闲置的活动可以绑定到项目), 把每个projectId-activityId关联关系，插入到关联表ProjectActivity表
            List<Integer> activityIdList = project.getSelectedActivityIdList();
            if(activityIdList != null && !activityIdList.isEmpty()){
                //activityIdList.stream().sorted(Comparator.comparing(ProjectActivity::getActivityId)).collect(Collectors.toList());
                activityIdList = activityIdList.stream().sorted().collect(Collectors.toList());
                for(Integer id: activityIdList){
                    ProjectActivity projectActivity = new ProjectActivity();
                    Activity activity = activityService.getById(id);
                    Long activityId = activity.getActivityId(); //id不是activityId， 所以这里要获取activityId

                    projectActivity.setProjectId(projectId);
                    projectActivity.setActivityId(activityId);

                    projectActivityService.save(projectActivity);
                }
            } else {    //没有闲置的活动可以绑定到项目

            }
        } else {  //更新
            Long projectId = project.getProjectId();

            //更新project-Activity的关联关系：页面所选关联活动与DB中不一样，才更新
            //DB
            List<Long> activityIdsFromDB = projectActivityService.getActivityIdsByProjectId(projectId);
            activityIdsFromDB = activityIdsFromDB.stream().sorted().collect(Collectors.toList());   //升序排序
            //页面： id-activityId
            List<Integer> selectedActivityIdsFromPage = project.getSelectedActivityIdList();
            List<Long> selectedActivityLongIdsFromPage = new ArrayList<>();
            for (Integer id : selectedActivityIdsFromPage){
                ProjectActivity projectActivity = new ProjectActivity();
                Activity activity = activityService.getById(id);
                Long activityId = activity.getActivityId(); //id不是activityId， 所以这里要获取activityId
                selectedActivityLongIdsFromPage.add(activityId);
            }
            selectedActivityLongIdsFromPage = selectedActivityLongIdsFromPage.stream().sorted().collect(Collectors.toList()); //升序排序
            if (!activityIdsFromDB.equals(selectedActivityLongIdsFromPage) ){  //所选关联活动与DB中不一样，才更新project-Activity的关联关系
                //1.先删除
                projectActivityService.deleteProjectActivityByProjectId(projectId);
                //2.再插入 - 每个projectId-activityId插入到 ProjectActivity表
                List<Integer> activityIdList = project.getSelectedActivityIdList();
                activityIdList = activityIdList.stream().sorted().collect(Collectors.toList());
                for(Integer id: activityIdList){
                    ProjectActivity projectActivity = new ProjectActivity();
                    Activity activity = activityService.getById(id);
                    Long activityId = activity.getActivityId(); //id不是activityId， 所以这里要获取activityId
                    projectActivity.setProjectId(projectId);
                    projectActivity.setActivityId(activityId);
                    projectActivityService.save(projectActivity);
                }
            }

            project.setUpdateMuid(currentUser.getUserId());
            project.setUpdateTime(DateUtil.date());
        }
        projectService.saveOrUpdate(project);
        return ResultUtils.success("操作成功");
    }

    @DeleteMapping("/{id}")
    public ResultVo delete(@PathVariable Integer id) {
        //1.级联删除，先删子 projectActivity
        Project project = projectService.getById(id);
        Long projectId = project.getProjectId();
        projectActivityService.deleteProjectActivityByProjectId(projectId);

        //2.级联删除，再删父 project
        projectService.removeById(id);
        return ResultUtils.success("删除成功");
    }

    @PostMapping("/del/batch")
    public ResultVo deleteBatch(@RequestBody List<Integer> ids) {
        projectService.removeByIds(ids);
        return ResultUtils.success("批量删除成功");
    }

    @GetMapping
    public ResultVo findAll() {
        return ResultUtils.success("查询成功",projectService.list());
    }

    @GetMapping("/{id}")
    public ResultVo findOne(@PathVariable Integer id) {
        return ResultUtils.success("查询成功",projectService.getById(id));
    }

    @GetMapping("/page")
    public ResultVo findPage(@RequestParam(defaultValue = "") String name,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        User currentUser = TokenUtils.getCurrentUser();                                 //当前登录的用户
        Long currentUserMUserId = currentUser.getUserId();                              //当前登录的管理员userId - 后台
//        Long currentPartnerAppUserId = userService.getAppUserId(currentPartnerId);    //当前登录的管理员userId - 小程序

        QueryWrapper<Project> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().orderByAsc(Project::getId);
        if (!"".equals(name)) {
            queryWrapper.lambda().like(Project::getName, name);
        }
        if(Constants.ROLE_DEV.equals(currentUser.getRoleFlag())){

        } else if(Constants.ROLE_ADMIN.equals(currentUser.getRoleFlag())){
            queryWrapper.lambda().eq(Project ::getCreateMuid, currentUserMUserId);
        } else {
            return ResultUtils.success("无权限", null);
        }
        IPage<Project> projectListPage = projectService.page(new Page<>(pageNum, pageSize), queryWrapper);
        List<Project> projectList = projectListPage.getRecords();

//        if(projectList==null || projectList.size()==0){
//            return ResultUtils.success("无数据记录", null);  // no data 没有数据
//        }

//        if(Constants.ROLE_ADMIN.equals(currentUser.getRoleFlag())){                         //当前登录的用户是管理员
//            projectList = projectList.stream().filter( project -> project.getCreateMuid().equals(currentUserMUserId) ).collect(Collectors.toList());
//        }

        for( Project project : projectList){
            Long projectId = project.getProjectId();

            //1.Project-Activity
            QueryWrapper<ProjectActivity> projectActivityQueryWrapper = new QueryWrapper<>();
            projectActivityQueryWrapper.lambda().eq(ProjectActivity::getProjectId, projectId);
            List<ProjectActivity> selectedProjectActivityList = projectActivityService.list(projectActivityQueryWrapper);

            List<Activity> selectedActivityList = new ArrayList<>();
            for(ProjectActivity projectActivity : selectedProjectActivityList){
                Long activityId = projectActivity.getActivityId();
                QueryWrapper<Activity> activityQueryWrapper = new QueryWrapper<>();
                activityQueryWrapper.lambda().eq(Activity::getActivityId, activityId);
                Activity activity = activityService.getOne(activityQueryWrapper);
                selectedActivityList.add(activity);
            }
            project.setSelectedActivityList(selectedActivityList);
//            project.setSelectedProjectActivityList(selectedProjectActivityList);
            project.setSelectedProjectActivityListSize(selectedProjectActivityList.size());

            //2.Project-Activity-ActivityAreaLimit 项目涉及的区域
            List<String> activityAreaLimitCityCodeList = new ArrayList<>();
            for(ProjectActivity projectActivity : selectedProjectActivityList){
                Long activityId = projectActivity.getActivityId();
                QueryWrapper<ActivityAreaLimit> activityAreaLimitQueryWrapper = new QueryWrapper<>();
                activityAreaLimitQueryWrapper.lambda().eq(ActivityAreaLimit::getActivityId, activityId);
                List<ActivityAreaLimit> activityAreaLimits = activityAreaLimitService.list(activityAreaLimitQueryWrapper);
                for(ActivityAreaLimit activityAreaLimit : activityAreaLimits){
                    String cityCode = activityAreaLimit.getCityCode();
                    activityAreaLimitCityCodeList.add(cityCode);
                }
            }
            //Project-Activity-ActivityAreaLimit-ChinaRegion 项目涉及区域的名字
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
            project.setAreaNameList(distinctAreaNameList);

            //3.Files: 项目报告
            QueryWrapper<Files> fileQueryWrapper = new QueryWrapper<>();
            fileQueryWrapper.lambda().eq(Files::getProjectId, projectId);
            fileQueryWrapper.lambda().eq(Files::getIsDelete, false);
            List<Files> fileList = fileService.list(fileQueryWrapper);
            project.setFileList(fileList);
        }
        projectListPage.setRecords(projectList);
        return ResultUtils.success("查询成功", projectListPage);
    }


    @GetMapping("/getSelectedActivityListByProjectId/{projectId}")
    public ResultVo getSelectedActivityListByProjectId (@PathVariable Long projectId) {
        //Project-Activity
        QueryWrapper<ProjectActivity> projectActivityQueryWrapper = new QueryWrapper<>();
        projectActivityQueryWrapper.lambda().eq(ProjectActivity::getProjectId, projectId);
        List<ProjectActivity> selectedProjectActivityList = projectActivityService.list(projectActivityQueryWrapper);   //ids

        List<Activity> selectedActivityList = new ArrayList<>();
        for(ProjectActivity projectActivity : selectedProjectActivityList){
            Long activityId = projectActivity.getActivityId();
            QueryWrapper<Activity> activityQueryWrapper = new QueryWrapper<>();
            activityQueryWrapper.lambda().eq(Activity::getActivityId, activityId);
            Activity activity = activityService.getOne(activityQueryWrapper);
            selectedActivityList.add(activity);
        }
        return ResultUtils.success("查询成功", selectedActivityList);
    }

    @GetMapping("/getSelectedActivityIdListByProjectId/{projectId}")
    public ResultVo getSelectedActivityIdListByProjectId (@PathVariable Long projectId) {
        //Project-Activity
        QueryWrapper<ProjectActivity> projectActivityQueryWrapper = new QueryWrapper<>();
        projectActivityQueryWrapper.lambda().eq(ProjectActivity::getProjectId, projectId);
        List<ProjectActivity> selectedProjectActivityList = projectActivityService.list(projectActivityQueryWrapper);   //ids

        List<Integer> selectedActivityIdList = new ArrayList<>();
        for(ProjectActivity projectActivity : selectedProjectActivityList){
            Long activityId = projectActivity.getActivityId();
            QueryWrapper<Activity> activityQueryWrapper = new QueryWrapper<>();
            activityQueryWrapper.lambda().eq(Activity::getActivityId, activityId);
            Activity activity = activityService.getOne(activityQueryWrapper);
            selectedActivityIdList.add(activity.getId());
        }
//        selectedActivityIdList.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        selectedActivityIdList = selectedActivityIdList.stream().sorted().collect(Collectors.toList());
        return ResultUtils.success("查询成功", selectedActivityIdList);
    }

//    /**
//    * 导出接口
//    */
//    @GetMapping("/export")
//    public void export(HttpServletResponse response) throws Exception {
//        // 从数据库查询出所有的数据
//        List<Project> list = projectService.list();
//        // 在内存操作，写出到浏览器
//        ExcelWriter writer = ExcelUtil.getWriter(true);
//
//        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
//        writer.write(list, true);
//
//        // 设置浏览器响应的格式
//        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
//        String fileName = URLEncoder.encode("Project信息表", "UTF-8");
//        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
//
//        ServletOutputStream out = response.getOutputStream();
//        writer.flush(out, true);
//        out.close();
//        writer.close();
//
//        }

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
//        List<Project> list = reader.readAll(Project.class);
//
//        projectService.saveBatch(list);
//        return ResultUtils.success("导入成功");
//    }

    private User getUser() {
        return TokenUtils.getCurrentUser();
    }

}

