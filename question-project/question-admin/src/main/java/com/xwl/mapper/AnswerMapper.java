package com.xwl.mapper;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xwl.entity.Answer;
import com.xwl.entity.Store;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.cursor.Cursor;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 
 * @since 2023-07-31
 */
public interface AnswerMapper extends BaseMapper<Answer> {

    //1.1 dev - 累计问卷数量
    Integer getAnswerAmountByDevUserId();
    //1.2 dev - 累计参与门店
    Integer getStoreAmountByDevUserId(@Param("userId") Long userId);
    //1.3 dev - 当前管理员下，昨日新增问卷数量
    Integer getLastdayAnswerAmountByDevUserId(@Param("userId") Long userId, @Param("startOfYesterday") DateTime startOfYesterday, @Param("endOfYesterday") DateTime endOfYesterday);
    //1.4 dev - 当前管理员下，昨日参与门店
    Integer getLastdayAnswerListStoreAmountByDevUserId(@Param("userId") Long userId, @Param("startOfYesterday") DateTime startOfYesterday, @Param("endOfYesterday") DateTime endOfYesterday);
    //1.5 dev - 当前管理员下，昨日新增问卷来源 answerTotalLastday
    List<Answer> getGroupLastdayAnswerListByDevUserId(@Param("userId") Long userId, @Param("startOfYesterday") DateTime startOfYesterday, @Param("endOfYesterday") DateTime endOfYesterday );
    //1.6 dev - 当前管理员下，昨日门店来源 storeTotalLastday
    List<Answer> getGroupLastdayAnswerListStoreAmountByDevUserId(@Param("userId") Long userId, @Param("startOfYesterday") DateTime startOfYesterday,@Param("endOfYesterday") DateTime endOfYesterday);


    //2.1 累计问卷数量
    //    List<Answer> getAnswerListByAdminUserId(@Param("userId") Long userId);
    //2.2 累计参与门店
    //    List<Long> getAnswerListStoreAmountByAdminUserId(@Param("userId") Long userId);
    //2.3 当前管理员下，昨日新增问卷数量
    //    List<Answer> getLastdayAnswerListByAdminUserId(@Param("userId") Long userId);
    //2.4 当前管理员下，昨日参与门店
    //    List<Long> getLastdayAnswerListStoreAmountByAdminUserId(@Param("userId") Long userId);
    //2.5 当前管理员下，昨日新增问卷来源 answerTotalLastday
    //    List<Answer> getGroupLastdayAnswerListByAdminUserId(@Param("userId") Long userId);
    //2.6 当前管理员下，昨日门店来源 storeTotalLastday
    //    List<Answer> getGroupLastdayAnswerListStoreAmountByAdminUserId(@Param("userId") Long userId);


    //2.1 管理员 - 累计问卷数量
    Integer getAnswerAmountByAdminUserId(@Param("userId") Long userId);
    //2.2 管理员 - 累计参与门店
    Integer getStoreAmountByAdminUserId(@Param("userId") Long userId);
    //2.3 管理员 - 当前管理员下，昨日新增问卷数量
    Integer getLastdayAnswerAmountByAdminUserId(@Param("userId") Long userId, @Param("startOfYesterday") DateTime startOfYesterday, @Param("endOfYesterday") DateTime endOfYesterday);
    //2.4 管理员 - 当前管理员下，昨日参与门店
    Integer getLastdayAnswerListStoreAmountByAdminUserId(@Param("userId") Long userId, @Param("startOfYesterday") DateTime startOfYesterday, @Param("endOfYesterday") DateTime endOfYesterday);
    //2.5 管理员 - 当前管理员下，昨日新增问卷来源 answerTotalLastday
    List<Answer> getGroupLastdayAnswerListByAdminUserId(@Param("userId") Long userId, @Param("startOfYesterday") DateTime startOfYesterday, @Param("endOfYesterday") DateTime endOfYesterday );
    //2.6 管理员 - 当前管理员下，昨日门店来源 storeTotalLastday
    List<Answer> getGroupLastdayAnswerListStoreAmountByAdminUserId(@Param("userId") Long userId, @Param("startOfYesterday") DateTime startOfYesterday,@Param("endOfYesterday") DateTime endOfYesterday);


    //3.1 客户经理 - 累计问卷数量
    Integer getAnswerAmountByPartnerUserId(@Param("userId") Long userId);
    //3.2 客户经理 - 累计参与门店
    Integer getStoreAmountByPartnerUserId(@Param("userId") Long userId);
    //3.3 客户经理 - 当前管理员下，昨日新增问卷数量
    Integer getLastdayAnswerAmountByPartnerUserId(@Param("userId") Long userId, @Param("startOfYesterday") DateTime startOfYesterday, @Param("endOfYesterday") DateTime endOfYesterday);
    //3.4 客户经理 - 当前管理员下，昨日参与门店
    Integer getLastdayAnswerListStoreAmountByPartnerUserId(@Param("userId") Long userId, @Param("startOfYesterday") DateTime startOfYesterday, @Param("endOfYesterday") DateTime endOfYesterday);
    //3.5 客户经理 - 当前管理员下，昨日新增问卷来源 answerTotalLastday
    List<Answer> getGroupLastdayAnswerListByPartnerUserId(@Param("userId") Long userId, @Param("startOfYesterday") DateTime startOfYesterday, @Param("endOfYesterday") DateTime endOfYesterday );
    //3.6 客户经理 - 当前管理员下，昨日门店来源 storeTotalLastday
    List<Answer> getGroupLastdayAnswerListStoreAmountByPartnerUserId(@Param("userId") Long userId, @Param("startOfYesterday") DateTime startOfYesterday,@Param("endOfYesterday") DateTime endOfYesterday);




    IPage<Answer> getAnswersByActivityIdWithParm(IPage page, @Param("answer") Answer answer);
    List<Answer> exportAnswersByActivityIdWithParm(@Param("answer") Answer answer);

}
