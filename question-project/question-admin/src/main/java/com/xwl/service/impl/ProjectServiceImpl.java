package com.xwl.service.impl;

import cn.hutool.core.date.DateUtil;
import com.xwl.entity.Project;
import com.xwl.mapper.ProjectMapper;
import com.xwl.service.IProjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 
 * @since 2023-08-12
 */
@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements IProjectService {

    public String getProjectCode(){

        List<Project> projectList = list();
        String currentDate = DateUtil.format(DateUtil.date(), "yyyyMMdd");
        String currentDateStr =  currentDate.substring(0,8);         //currentDateStr:20230818

        if(projectList==null || projectList.size()<1){
            String returnProjectCode = currentDateStr+"01";  //20230818+01
            return returnProjectCode;
        }

        List<String> projectCodeList = projectList.stream().map(Project::getCode).collect(Collectors.toList());

        String maxProjectCode =  Collections.max(projectCodeList);      //maxProjectCode:20230818XX
        String maxYrMonStr = maxProjectCode.substring(0,6);          //maxDateStr:202308
        String currentYrMonStr =  currentDate.substring(0,6);        //currentYrMonStr:202308
        String returnProjNoStr = "";

        if( currentYrMonStr.equals(maxYrMonStr) ){                      //当前月已有合同: 2023081866
            Integer returnProjNoInt = Integer.valueOf(maxProjectCode.substring(8,10))+1;    //66+1=67
            returnProjNoStr = String.format("%02d",returnProjNoInt);                        //67
        } else {                                                        //当前月还没有合同: 2023081801
            returnProjNoStr = "01";                                                         //01
        }
        String returnProjectCode = currentDateStr+returnProjNoStr;  //20230818+67=2023081867 or 20230818+01=2023081801

        return returnProjectCode;

    }

}
