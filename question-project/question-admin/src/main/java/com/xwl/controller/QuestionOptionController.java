//package com.xwl.controller;
//
//import cn.hutool.core.date.DateUtil;
//import cn.hutool.poi.excel.ExcelReader;
//import cn.hutool.poi.excel.ExcelUtil;
//import cn.hutool.poi.excel.ExcelWriter;
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.example.springboot.common.Result;
//import com.example.springboot.entity.QuestionOption;
//import com.example.springboot.entity.User;
//import com.example.springboot.service.IQuestionOptionService;
//import com.example.springboot.utils.TokenUtils;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.annotation.Resource;
//import javax.servlet.ServletOutputStream;
//import javax.servlet.http.HttpServletResponse;
//import java.io.InputStream;
//import java.net.URLEncoder;
//import java.util.List;
//
///**
// * <p>
// *  前端控制器
// * </p>
// *
// * @author
// * @since 2023-05-24
// */
//@RestController
//@RequestMapping("/questionOption")
//public class QuestionOptionController {
//
//    @Resource
//    private IQuestionOptionService questionOptionService;
//
//    private final String now = DateUtil.now();
//
//    // 新增或者更新
//    @PostMapping
//    public Result save(@RequestBody QuestionOption questionOption) {
//        if (questionOption.getId() == null) {
//            //questionOption.setTime(DateUtil.now());
//            //questionOption.setUser(TokenUtils.getCurrentUser().getUsername());
//        }
//        questionOptionService.saveOrUpdate(questionOption);
//        return Result.success();
//    }
//
//    @DeleteMapping("/{id}")
//    public Result delete(@PathVariable Integer id) {
//        questionOptionService.removeById(id);
//        return Result.success();
//    }
//
//    @PostMapping("/del/batch")
//    public Result deleteBatch(@RequestBody List<Integer> ids) {
//        questionOptionService.removeByIds(ids);
//        return Result.success();
//    }
//
//    @GetMapping
//    public Result findAll() {
//        return Result.success(questionOptionService.list());
//    }
//
//    @GetMapping("/{id}")
//    public Result findOne(@PathVariable Integer id) {
//        return Result.success(questionOptionService.getById(id));
//    }
//
//    @GetMapping("/page")
//    public Result findPage(@RequestParam(defaultValue = "") String name,
//                           @RequestParam Integer pageNum,
//                           @RequestParam Integer pageSize) {
//        QueryWrapper<QuestionOption> queryWrapper = new QueryWrapper<>();
//        queryWrapper.orderByDesc("id");
//        if (!"".equals(name)) {
//            queryWrapper.like("name", name);
//        }
////        User currentUser = TokenUtils.getCurrentUser();
////        if (currentUser.getRole().equals("ROLE_USER")) {
////            queryWrapper.eq("user", currentUser.getUsername());
////        }
//        return Result.success(questionOptionService.page(new Page<>(pageNum, pageSize), queryWrapper));
//    }
//
//    /**
//    * 导出接口
//    */
//    @GetMapping("/export")
//    public void export(HttpServletResponse response) throws Exception {
//        // 从数据库查询出所有的数据
//        List<QuestionOption> list = questionOptionService.list();
//        // 在内存操作，写出到浏览器
//        ExcelWriter writer = ExcelUtil.getWriter(true);
//
//        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
//        writer.write(list, true);
//
//        // 设置浏览器响应的格式
//        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
//        String fileName = URLEncoder.encode("QuestionOption信息表", "UTF-8");
//        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
//
//        ServletOutputStream out = response.getOutputStream();
//        writer.flush(out, true);
//        out.close();
//        writer.close();
//
//        }
//
//    /**
//     * excel 导入
//     * @param file
//     * @throws Exception
//     */
//    @PostMapping("/import")
//    public Result imp(MultipartFile file) throws Exception {
//        InputStream inputStream = file.getInputStream();
//        ExcelReader reader = ExcelUtil.getReader(inputStream);
//        // 通过 javabean的方式读取Excel内的对象，但是要求表头必须是英文，跟javabean的属性要对应起来
//        List<QuestionOption> list = reader.readAll(QuestionOption.class);
//
//        questionOptionService.saveBatch(list);
//        return Result.success();
//    }
//
//    private User getUser() {
//        return TokenUtils.getCurrentUser();
//    }
//
//}
//
