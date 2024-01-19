package com.xwl.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xwl.entity.Role;
import com.xwl.mapper.RoleMenuMapper;
import com.xwl.service.IRoleService;
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
@RequestMapping("/api/role")
public class RoleController {

    @Resource
    private IRoleService roleService;
    @Resource
    private RoleMenuMapper roleMenuMapper;

    // 新增或者更新
    @PostMapping
    public ResultVo save(@RequestBody Role role) {
        roleService.saveOrUpdate(role);
        return ResultUtils.success("修改成功");
    }

    @DeleteMapping("/{id}")
    public ResultVo delete(@PathVariable Integer id) {
        roleService.removeById(id);
        roleMenuMapper.deleteByRoleId(id);
        return ResultUtils.success("删除成功");
    }

    @PostMapping("/del/batch")
    public ResultVo deleteBatch(@RequestBody List<Integer> ids) {
        roleService.removeByIds(ids);
        return ResultUtils.success("删除成功");
    }

    @GetMapping
    public ResultVo findAll() {
        return ResultUtils.success("查询成功", roleService.list());
    }

    @GetMapping("/{id}")
    public ResultVo findOne(@PathVariable Integer id) {
        return ResultUtils.success("查询成功", roleService.getById(id));
    }

    @GetMapping("/page")
    public ResultVo findPage(@RequestParam String name,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name", name);
        queryWrapper.orderByAsc("id");
        return ResultUtils.success("查询成功", roleService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    /**
     * 绑定角色和菜单的关系
     * @param roleId 角色id
     * @param menuIds 菜单id数组
     * @return
     */
    @PostMapping("/roleMenu/{roleId}")
    public ResultVo saveRoleMenu(@PathVariable Integer roleId, @RequestBody List<Integer> menuIds) {
        roleService.setRoleMenu(roleId, menuIds);
        return ResultUtils.success();
    }

    @GetMapping("/roleMenu/{roleId}")
    public ResultVo getRoleMenu(@PathVariable Integer roleId) {
        List<Integer> ids = roleService.getRoleMenu(roleId);
        return ResultUtils.success( "查询成功", ids);
    }

}

