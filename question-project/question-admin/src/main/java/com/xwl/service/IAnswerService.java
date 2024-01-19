package com.xwl.service;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xwl.entity.Answer;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.cursor.Cursor;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 
 * @since 2023-07-31
 */
public interface IAnswerService extends IService<Answer> {

//    List<Long> getAnswerIdListByActivityId(Long activityId);
    IPage<Answer> getAnswersByActivityIdWithParm(IPage page, Answer answer);
    List<Answer> exportAnswersByActivityIdWithParm(Answer answer);


    //1.开发人员
    //1.1 累计问卷数量
    Integer getAnswerAmountByDevUserId();
    //1.2 累计参与门店
    Integer getStoreAmountByDevUserId(Long userId);
    //1.3 当前管理员下，昨日新增问卷数量
    Integer getLastdayAnswerAmountByDevUserId(Long userId, DateTime startOfYesterday, DateTime endOfYesterday);
    //1.4 当前管理员下，昨日参与门店
    Integer getLastdayAnswerListStoreAmountByDevUserId(Long userId, DateTime startOfYesterday, DateTime endOfYesterday);
    //1.5 当前管理员下，昨日新增问卷来源 answerTotalLastday
    List<Answer> getGroupLastdayAnswerListByDevUserId(Long userId, DateTime startOfYesterday, DateTime endOfYesterday);
    //1.6 当前管理员下，昨日门店来源 storeTotalLastday
    List<Answer> getGroupLastdayAnswerListStoreAmountByDevUserId(Long userId, DateTime startOfYesterday, DateTime endOfYesterday);


    //2.管理员
    //2.1 累计问卷数量
    Integer getAnswerAmountByAdminUserId(Long userId);
    //2.2 累计参与门店
    Integer getStoreAmountByAdminUserId(Long userId);
    //2.3 当前管理员下，昨日新增问卷数量
    Integer getLastdayAnswerAmountByAdminUserId(Long userId, DateTime startOfYesterday, DateTime endOfYesterday);
    //2.4 当前管理员下，昨日参与门店
    Integer getLastdayAnswerListStoreAmountByAdminUserId(Long userId, DateTime startOfYesterday, DateTime endOfYesterday);
    //2.5 当前管理员下，昨日新增问卷来源 answerTotalLastday
    List<Answer> getGroupLastdayAnswerListByAdminUserId(Long userId, DateTime startOfYesterday, DateTime endOfYesterday);
    //2.6 当前管理员下，昨日门店来源 storeTotalLastday
    List<Answer> getGroupLastdayAnswerListStoreAmountByAdminUserId(Long userId, DateTime startOfYesterday, DateTime endOfYesterday);

    //    List<Answer> getAnswerListByAdminUserId(Long userId);
    //    List<Long> getAnswerListStoreAmountByAdminUserId(Long userId);
    //    List<Answer> getLastdayAnswerListByAdminUserId(Long userId);
    //    List<Long> getLastdayAnswerListStoreAmountByAdminUserId(Long userId);
    //    List<Answer> getGroupLastdayAnswerListByAdminUserId(@Param("userId") Long userId);
    //    List<Answer> getGroupLastdayAnswerListStoreAmountByAdminUserId(@Param("userId") Long userId);



    //3.客户经理
    //3.1 累计问卷数量
    Integer getAnswerAmountByPartnerUserId(Long userId);
    //3.2 累计参与门店
    Integer getStoreAmountByPartnerUserId(Long userId);
    //3.3 当前管理员下，昨日新增问卷数量
    Integer getLastdayAnswerAmountByPartnerUserId(Long userId, DateTime startOfYesterday, DateTime endOfYesterday);
    //3.4 当前管理员下，昨日参与门店
    Integer getLastdayAnswerListStoreAmountByPartnerUserId(Long userId, DateTime startOfYesterday, DateTime endOfYesterday);
    //3.5 当前管理员下，昨日新增问卷来源 answerTotalLastday
    List<Answer> getGroupLastdayAnswerListByPartnerUserId(Long userId, DateTime startOfYesterday, DateTime endOfYesterday);
    //3.6 当前管理员下，昨日门店来源 storeTotalLastday
    List<Answer> getGroupLastdayAnswerListStoreAmountByPartnerUserId(Long userId, DateTime startOfYesterday, DateTime endOfYesterday);

}
