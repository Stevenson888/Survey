package com.xwl.service;

import cn.hutool.core.date.DateUtil;
import com.xwl.entity.Project;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 
 * @since 2023-08-12
 */
public interface IProjectService extends IService<Project> {

    String getProjectCode();

}
