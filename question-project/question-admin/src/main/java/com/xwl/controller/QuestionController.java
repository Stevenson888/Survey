package com.xwl.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelWriter;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletOutputStream;
import java.net.URLEncoder;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xwl.entity.Question;
import com.xwl.entity.User;
import com.xwl.service.IQuestionService;
import com.xwl.utils.ResultUtils;
import com.xwl.utils.ResultVo;
import com.xwl.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.io.InputStream;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author
 * @since 2023-05-23
 */
@RestController
@RequestMapping("/api/question")
public class QuestionController {


    @Autowired
    private IQuestionService questionService;

    //保存试题
    @PostMapping("/saveQuestion/{paperId}")
    public ResultVo saveQuestion(@PathVariable Long paperId, @RequestBody List<Question> questionList){
        questionService.saveQuestion(paperId, questionList);
        return ResultUtils.success("设计试题成功!");
    }
    //编辑回显
    @GetMapping("/getQuestionList")
    public ResultVo getQuestionList(Long paperId){
        List<Question> questionList = questionService.getQuestionList(paperId);
        return ResultUtils.success("查询成功",questionList);
    }



//    @Resource
//    private IQuestionService questionService;
//
//    private final String now = DateUtil.now();
//
//    // 新增或者更新
//    @PostMapping
//    public ResultVo save(@RequestBody Question question) {
//        if (question.getId() == null) {
//            //question.setTime(DateUtil.now());
//            //question.setUser(TokenUtils.getCurrentUser().getUsername());
//        }
//        questionService.saveOrUpdate(question);
//        return ResultUtils.success();
//    }
//
//    @DeleteMapping("/{id}")
//    public ResultVo delete(@PathVariable Integer id) {
//        questionService.removeById(id);
//        return ResultUtils.success();
//    }
//
//    @PostMapping("/del/batch")
//    public ResultVo deleteBatch(@RequestBody List<Integer> ids) {
//        questionService.removeByIds(ids);
//        return ResultUtils.success();
//    }
//
//    @GetMapping
//    public ResultVo findAll() {
//        return ResultUtils.success("查询成功", questionService.list());
//    }
//
//    @GetMapping("/{id}")
//    public ResultVo findOne(@PathVariable Integer id) {
//        return ResultUtils.success("查询成功", questionService.getById(id));
//    }
//
//    @GetMapping("/page")
//    public ResultVo findPage(@RequestParam(defaultValue = "") String name,
//                           @RequestParam Integer pageNum,
//                           @RequestParam Integer pageSize) {
//        QueryWrapper<Question> queryWrapper = new QueryWrapper<>();
//        queryWrapper.orderByDesc("id");
//        if (!"".equals(name)) {
//            queryWrapper.like("name", name);
//        }
////        User currentUser = TokenUtils.getCurrentUser();
////        if (currentUser.getRole().equals("ROLE_USER")) {
////            queryWrapper.eq("user", currentUser.getUsername());
////        }
//        return ResultUtils.success("查询成功",questionService.page(new Page<>(pageNum, pageSize), queryWrapper));
//    }
//
//    /**
//    * 导出接口
//    */
//    @GetMapping("/export")
//    public void export(HttpServletResponse response) throws Exception {
//        // 从数据库查询出所有的数据
//        List<Question> list = questionService.list();
//        // 在内存操作，写出到浏览器
//        ExcelWriter writer = ExcelUtil.getWriter(true);
//
//        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
//        writer.write(list, true);
//
//        // 设置浏览器响应的格式
//        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
//        String fileName = URLEncoder.encode("question信息表", "UTF-8");
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
//        List<Question> list = reader.readAll(Question.class);
//
//        questionService.saveBatch(list);
//        return ResultUtils.success();
//    }
//
//    private User getUser() {
//        return TokenUtils.getCurrentUser();
//    }

}

