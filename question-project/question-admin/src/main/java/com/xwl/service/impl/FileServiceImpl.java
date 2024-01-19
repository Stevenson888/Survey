package com.xwl.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xwl.entity.Files;
import com.xwl.mapper.FileMapper;
import com.xwl.mapper.PaperMapper;
import com.xwl.service.IFileService;
import com.xwl.service.IPaperService;
import org.springframework.stereotype.Service;

@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, Files> implements IFileService {
}
