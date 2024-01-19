package com.xwl.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelWriter;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletOutputStream;
import java.net.URLEncoder;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.io.InputStream;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.web.multipart.MultipartFile;
import com.xwl.entity.User;
import com.xwl.utils.TokenUtils;
import com.xwl.utils.ResultVo;
import com.xwl.utils.ResultUtils;

import com.xwl.service.IActivityAreaLimitService;
import com.xwl.entity.ActivityAreaLimit;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 
 * @since 2023-08-13
 */
@Deprecated
@RestController
@RequestMapping("/activityAreaLimit")
public class ActivityAreaLimitController {

    @Resource
    private IActivityAreaLimitService activityAreaLimitService;

    private final String now = DateUtil.now();

    // 新增或者更新
    @PostMapping
    public ResultVo save(@RequestBody ActivityAreaLimit activityAreaLimit) {
        if (activityAreaLimit.getId() == null) {
            //activityAreaLimit.setTime(DateUtil.now());
            //activityAreaLimit.setUser(TokenUtils.getCurrentUser().getUsername());
        }
        activityAreaLimitService.saveOrUpdate(activityAreaLimit);
        return ResultUtils.success("操作成功");
    }

    @DeleteMapping("/{id}")
    public ResultVo delete(@PathVariable Integer id) {
        activityAreaLimitService.removeById(id);
        return ResultUtils.success("删除成功");
    }

    @PostMapping("/del/batch")
    public ResultVo deleteBatch(@RequestBody List<Integer> ids) {
        activityAreaLimitService.removeByIds(ids);
        return ResultUtils.success("批量删除成功");
    }

    @GetMapping
    public ResultVo findAll() {
        return ResultUtils.success("查询成功",activityAreaLimitService.list());
    }

    @GetMapping("/{id}")
    public ResultVo findOne(@PathVariable Integer id) {
        return ResultUtils.success("查询成功",activityAreaLimitService.getById(id));
    }

    @GetMapping("/page")
    public ResultVo findPage(@RequestParam(defaultValue = "") String name,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<ActivityAreaLimit> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("id");
        if (!"".equals(name)) {
            queryWrapper.like("name", name);
        }
//        User currentUser = TokenUtils.getCurrentUser();
//        if (currentUser.getRole().equals("ROLE_USER")) {
//            queryWrapper.eq("user", currentUser.getUsername());
//        }
        return ResultUtils.success("查询成功",activityAreaLimitService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    /**
    * 导出接口
    */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        // 从数据库查询出所有的数据
        List<ActivityAreaLimit> list = activityAreaLimitService.list();
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);

        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list, true);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("ActivityAreaLimit信息表", "UTF-8");
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
        List<ActivityAreaLimit> list = reader.readAll(ActivityAreaLimit.class);

        activityAreaLimitService.saveBatch(list);
        return ResultUtils.success("导入成功");
    }

    private User getUser() {
        return TokenUtils.getCurrentUser();
    }

}

