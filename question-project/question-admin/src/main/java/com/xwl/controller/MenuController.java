package com.xwl.controller;


import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xwl.entity.Menu;
import com.xwl.service.IMenuService;
import com.xwl.utils.ResultUtils;
import com.xwl.utils.ResultVo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author
 * @since 2022-02-10
 */
@RestController
@RequestMapping("/api/menu")
public class MenuController {

    @Resource
    private IMenuService menuService;

//    @Resource
//    private DictMapper dictMapper;

    // 新增或者更新
    @PostMapping
    public ResultVo save(@RequestBody Menu menu) {
        menuService.saveOrUpdate(menu);
        return ResultUtils.success("保存成功");
    }

    @DeleteMapping("/{id}")
    public ResultVo delete(@PathVariable Integer id) {
        menuService.removeById(id);
        return ResultUtils.success("删除成功");
    }

    @PostMapping("/del/batch")
    public ResultVo deleteBatch(@RequestBody List<Integer> ids) {
        menuService.removeByIds(ids);
        return ResultUtils.success("批量删除成功");
    }

    @GetMapping("/ids")
    public ResultVo findAllIds() {
        return ResultUtils.success("查询成功", menuService.list().stream().map(Menu::getId));
    }

    @GetMapping
    public ResultVo findAll(@RequestParam(defaultValue = "") String name) {
        return ResultUtils.success("查询成功", menuService.findMenus(name));
    }

    @GetMapping("/{id}")
    public ResultVo findOne(@PathVariable Integer id) {

        return ResultUtils.success("查询成功", menuService.getById(id));
    }

    @GetMapping("/page")
    public ResultVo findPage(@RequestParam String name,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name", name);
        queryWrapper.orderByAsc("id");
        return ResultUtils.success("查询成功", menuService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

//    @GetMapping("/icons")
//    public Result getIcons() {
//        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("type", Constants.DICT_TYPE_ICON);
//        return Result.success(dictMapper.selectList(queryWrapper));
//    }

}

