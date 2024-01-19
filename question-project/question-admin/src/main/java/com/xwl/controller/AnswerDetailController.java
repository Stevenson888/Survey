//package com.xwl.controller;
//
//import cn.hutool.core.date.DateUtil;
//import cn.hutool.poi.excel.ExcelReader;
//import cn.hutool.poi.excel.ExcelUtil;
//import cn.hutool.poi.excel.ExcelWriter;
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.xwl.entity.AnswerDetail;
//import com.xwl.entity.User;
//import com.xwl.service.IAnswerDetailService;
//import com.xwl.utils.ResultUtils;
//import com.xwl.utils.ResultVo;
//import com.xwl.utils.TokenUtils;
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
// * @since 2023-07-31
// */
//@RestController
//@RequestMapping("/answerDetail")
//public class AnswerDetailController {
//
//    @Resource
//    private IAnswerDetailService answerDetailService;
//
//    private final String now = DateUtil.now();
//
//    // 新增或者更新
//    @PostMapping
//    public ResultVo save(@RequestBody AnswerDetail answerDetail) {
//        if (answerDetail.getId() == null) {
//            //answerDetail.setTime(DateUtil.now());
//            //answerDetail.setUser(TokenUtils.getCurrentUser().getUsername());
//        }
//        answerDetailService.saveOrUpdate(answerDetail);
//        return ResultUtils.success("操作成功");
//    }
//
//    @DeleteMapping("/{id}")
//    public ResultVo delete(@PathVariable Integer id) {
//        answerDetailService.removeById(id);
//        return ResultUtils.success("删除成功");
//    }
//
//    @PostMapping("/del/batch")
//    public ResultVo deleteBatch(@RequestBody List<Integer> ids) {
//        answerDetailService.removeByIds(ids);
//        return ResultUtils.success("批量删除成功");
//    }
//
//    @GetMapping
//    public ResultVo findAll() {
//        return ResultUtils.success("查询成功",answerDetailService.list());
//    }
//
//    @GetMapping("/{id}")
//    public ResultVo findOne(@PathVariable Integer id) {
//        return ResultUtils.success("查询成功",answerDetailService.getById(id));
//    }
//
//    @GetMapping("/page")
//    public ResultVo findPage(@RequestParam(defaultValue = "") String name,
//                           @RequestParam Integer pageNum,
//                           @RequestParam Integer pageSize) {
//        QueryWrapper<AnswerDetail> queryWrapper = new QueryWrapper<>();
//        queryWrapper.orderByDesc("id");
//        if (!"".equals(name)) {
//            queryWrapper.like("name", name);
//        }
////        User currentUser = TokenUtils.getCurrentUser();
////        if (currentUser.getRole().equals("ROLE_USER")) {
////            queryWrapper.eq("user", currentUser.getUsername());
////        }
//        return ResultUtils.success("查询成功",answerDetailService.page(new Page<>(pageNum, pageSize), queryWrapper));
//    }
//
//    /**
//    * 导出接口
//    */
//    @GetMapping("/export")
//    public void export(HttpServletResponse response) throws Exception {
//        // 从数据库查询出所有的数据
//        List<AnswerDetail> list = answerDetailService.list();
//        // 在内存操作，写出到浏览器
//        ExcelWriter writer = ExcelUtil.getWriter(true);
//
//        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
//        writer.write(list, true);
//
//        // 设置浏览器响应的格式
//        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
//        String fileName = URLEncoder.encode("AnswerDetail信息表", "UTF-8");
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
//    public ResultVo imp(MultipartFile file) throws Exception {
//        InputStream inputStream = file.getInputStream();
//        ExcelReader reader = ExcelUtil.getReader(inputStream);
//        // 通过 javabean的方式读取Excel内的对象，但是要求表头必须是英文，跟javabean的属性要对应起来
//        List<AnswerDetail> list = reader.readAll(AnswerDetail.class);
//
//        answerDetailService.saveBatch(list);
//        return ResultUtils.success("导入成功");
//    }
//
//    private User getUser() {
//        return TokenUtils.getCurrentUser();
//    }
//
//}
//
