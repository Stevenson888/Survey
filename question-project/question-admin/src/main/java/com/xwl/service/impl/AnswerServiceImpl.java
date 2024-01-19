package com.xwl.service.impl;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xwl.entity.Answer;
import com.xwl.entity.Menu;
import com.xwl.mapper.ActivityAreaLimitMapper;
import com.xwl.mapper.AnswerMapper;
import com.xwl.service.IAnswerService;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.cursor.Cursor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 
 * @since 2023-07-31
 */
@Service
public class AnswerServiceImpl extends ServiceImpl<AnswerMapper, Answer> implements IAnswerService {

    @Resource
    private AnswerMapper answerMapper;

//    @Override
//    public List<Long> getAnswerIdListByActivityId(Long activityId) {
//        return this.baseMapper.getAnswerIdListByActivityId(activityId);
//    }
    @Override
    public IPage<Answer> getAnswersByActivityIdWithParm(IPage page, Answer answer) {
        return this.baseMapper.getAnswersByActivityIdWithParm(page, answer);
    }
    @Override
    public List<Answer> exportAnswersByActivityIdWithParm(Answer answer) {
        return this.baseMapper.exportAnswersByActivityIdWithParm( answer);
    }


    //1.1 累计问卷数量
    @Override
    public Integer getAnswerAmountByDevUserId() {
        return this.baseMapper.getAnswerAmountByDevUserId();
    }
    //1.2 累计参与门店
    @Override
    public Integer getStoreAmountByDevUserId(Long userId) {
        return this.baseMapper.getStoreAmountByDevUserId(userId);
    }
    //1.3 当前管理员下，昨日新增问卷数量
    @Override
    public Integer getLastdayAnswerAmountByDevUserId(Long userId, DateTime startOfYesterday, DateTime endOfYesterday) {
        return this.baseMapper.getLastdayAnswerAmountByDevUserId(userId, startOfYesterday, endOfYesterday);
    }
    //1.4 当前管理员下，昨日参与门店
    @Override
    public Integer getLastdayAnswerListStoreAmountByDevUserId(Long userId, DateTime startOfYesterday, DateTime endOfYesterday) {
        return this.baseMapper.getLastdayAnswerListStoreAmountByDevUserId(userId, startOfYesterday, endOfYesterday);
    }
    //1.5 当前管理员下，昨日新增问卷来源 answerTotalLastday
    @Override
    public List<Answer> getGroupLastdayAnswerListByDevUserId(Long userId, DateTime startOfYesterday, DateTime endOfYesterday) {
        return this.baseMapper.getGroupLastdayAnswerListByDevUserId(userId, startOfYesterday, endOfYesterday);
    }
    //1.6 当前管理员下，昨日门店来源 storeTotalLastday
    @Override
    public List<Answer> getGroupLastdayAnswerListStoreAmountByDevUserId(Long userId, DateTime startOfYesterday, DateTime endOfYesterday) {
        return this.baseMapper.getGroupLastdayAnswerListStoreAmountByDevUserId(userId,startOfYesterday,endOfYesterday );
    }


    //2.1 累计问卷数量
//    @Override
//    public List<Answer> getAnswersCountByAdminUserId(Long userId) {
//        return this.baseMapper.getAnswersCountByAdminUserId(userId);
//    }
    @Override
    public Integer getAnswerAmountByAdminUserId(Long userId) {
        return this.baseMapper.getAnswerAmountByAdminUserId(userId);
    }
    //2.2 累计参与门店
//    @Override
//    public List<Long> getAnswerListStoreAmountByAdminUserId(Long userId) {
//        return this.baseMapper.getAnswerListStoreAmountByAdminUserId(userId);
//    }
    @Override
    public Integer getStoreAmountByAdminUserId(Long userId) {
        return this.baseMapper.getStoreAmountByAdminUserId(userId);
    }

    //2.3 当前管理员下，昨日新增问卷数量
//    @Override
//    public List<Answer> getLastdayAnswerListByAdminUserId(Long userId) {
//        return this.baseMapper.getLastdayAnswerListByAdminUserId(userId);
//    }
    @Override
    public Integer getLastdayAnswerAmountByAdminUserId(Long userId, DateTime startOfYesterday, DateTime endOfYesterday) {
        return this.baseMapper.getLastdayAnswerAmountByAdminUserId(userId, startOfYesterday, endOfYesterday);
    }
    //2.4 当前管理员下，昨日参与门店
//    @Override
//    public List<Long> getLastdayAnswerListStoreAmountByAdminUserId(Long userId) {
//        return this.baseMapper.getLastdayAnswerListStoreAmountByAdminUserId(userId);
//    }
    @Override
    public Integer getLastdayAnswerListStoreAmountByAdminUserId(Long userId, DateTime startOfYesterday, DateTime endOfYesterday) {
        return this.baseMapper.getLastdayAnswerListStoreAmountByAdminUserId(userId, startOfYesterday, endOfYesterday);
    }
    //2.5 当前管理员下，昨日新增问卷来源 answerTotalLastday
//    @Override
//    public List<Answer> getGroupLastdayAnswerListByAdminUserId(Long userId) {
//        return this.baseMapper.getGroupLastdayAnswerListByAdminUserId(userId);
//    }
    @Override
    public List<Answer> getGroupLastdayAnswerListByAdminUserId(Long userId, DateTime startOfYesterday, DateTime endOfYesterday) {
        return this.baseMapper.getGroupLastdayAnswerListByAdminUserId(userId, startOfYesterday, endOfYesterday);
    }
    //2.6 当前管理员下，昨日门店来源 storeTotalLastday
//    @Override
//    public List<Answer> getGroupLastdayAnswerListStoreAmountByAdminUserId(Long userId) {
//        return this.baseMapper.getGroupLastdayAnswerListStoreAmountByAdminUserId(userId);
//    }
    @Override
    public List<Answer> getGroupLastdayAnswerListStoreAmountByAdminUserId(Long userId, DateTime startOfYesterday, DateTime endOfYesterday) {
        return this.baseMapper.getGroupLastdayAnswerListStoreAmountByAdminUserId(userId,startOfYesterday,endOfYesterday );
    }



    //3.1 当前dev下，累计问卷数量
    @Override
    public Integer getAnswerAmountByPartnerUserId(Long userId) {
        return this.baseMapper.getAnswerAmountByPartnerUserId(userId);
    }
    //3.2 当前dev下，累计参与门店
    @Override
    public Integer getStoreAmountByPartnerUserId(Long userId) {
        return this.baseMapper.getStoreAmountByPartnerUserId(userId);
    }
    //3.3 当前dev下，昨日新增问卷数量
    @Override
    public Integer getLastdayAnswerAmountByPartnerUserId(Long userId, DateTime startOfYesterday, DateTime endOfYesterday) {
        return this.baseMapper.getLastdayAnswerAmountByPartnerUserId(userId, startOfYesterday, endOfYesterday);
    }
    //3.4 当前dev下，昨日参与门店
    @Override
    public Integer getLastdayAnswerListStoreAmountByPartnerUserId(Long userId, DateTime startOfYesterday, DateTime endOfYesterday) {
        return this.baseMapper.getLastdayAnswerListStoreAmountByPartnerUserId(userId, startOfYesterday, endOfYesterday);
    }
    //3.5 当前dev下，昨日新增问卷来源 answerTotalLastday
    @Override
    public List<Answer> getGroupLastdayAnswerListByPartnerUserId(Long userId, DateTime startOfYesterday, DateTime endOfYesterday) {
        return this.baseMapper.getGroupLastdayAnswerListByPartnerUserId(userId, startOfYesterday, endOfYesterday);
    }
    //3.6 当前dev下，昨日门店来源 storeTotalLastday
    @Override
    public List<Answer> getGroupLastdayAnswerListStoreAmountByPartnerUserId(Long userId, DateTime startOfYesterday, DateTime endOfYesterday) {
        return this.baseMapper.getGroupLastdayAnswerListStoreAmountByPartnerUserId(userId,startOfYesterday,endOfYesterday );
    }



}
