package com.xwl.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.poi.excel.StyleSet;
import com.alibaba.fastjson.support.geo.Geometry;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xwl.annotation.AutoLog;
import com.xwl.entity.*;
import com.xwl.exception.ServiceException;
import com.xwl.mapper.CustomerStoreMapper;
import com.xwl.service.*;
import com.xwl.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
//import org.locationtech.jts.geom.Point;
//import org.locationtech.jts.geom.Geometry;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 
 * @since 2023-07-30
 */
@Slf4j
@RestController
@RequestMapping("/api/store")
public class StoreController {

    @Resource
    private IStoreService storeService;
    @Resource
    private IRetailerStoreService retailerStoreService;
    @Resource
    private IUserService userService;
    @Resource
    private IAppUserService appUserService;
    @Resource
    private IAnswerService answerService;
    @Resource
    private CustomerStoreMapper customerStoreMapper;
    @Resource
    private IChinaRegionService chinaRegionService;

    private final String now = DateUtil.now();


    @GetMapping("/getStoreById/{storeId}")
    public ResultVo getStoreById( @PathVariable Long storeId){
        Long currentUserId = TokenUtils.getCurrentUser().getUserId(); //当前登录的Admin
//        List<Store> storeList = storeService.getStoresByAdminUserId(adminUserId);

        QueryWrapper<Store> storeQuery = new QueryWrapper<>();
        storeQuery.lambda().eq(Store::getStoreId, storeId);
        Store store = storeService.getOne(storeQuery);

//        List<Store> newStoreList = new ArrayList<>();
//        storeList.stream().filter(item -> item.getCreateUserName().equals(realName)).forEach(item->{
//            Store newStore = new Store();
//            BeanUtils.copyProperties(item,newStore);
//            newStoreList.add(newStore);
//        });
//        storeList = newStoreList;

//        storeList.stream().filter(item -> item.getCreateUserName().equals(realName)).collect(Collectors.toList());

//        questionOptionList.stream().filter(item -> item.getQuestionId().equals(questionId)).forEach(item->{
//            QuestionOption questionOption = new QuestionOption();
//            BeanUtils.copyProperties(item,questionOption);
//            returnQuestionOptionList.add(questionOption);
//        });

//        IPage<Store> page = new Page<>();
//        page.setCurrent(currentPage);
//        page.setSize(pageSize);
//        QueryWrapper<Store> queryWrapper = new QueryWrapper<>();
//        IPage<Store> storeListPage = storeService.page(page,queryWrapper);

//        for(Store store: storeList){

        //零售店店主
        Long userId = store.getUserId();
        if(userId!=0){
            QueryWrapper<AppUser> userQuery = new QueryWrapper<>();
            userQuery.lambda().eq(AppUser::getUserId, userId);
            AppUser storeOwner = appUserService.getOne(userQuery);
            storeOwner.setUserId(store.getCreateUid());
            store.setStoreOwner(storeOwner);
        }

        //零售店所属客户经理
        Long createUserIdInApp = store.getCreateUid();
        AppUser createPartner = null;
        if(createUserIdInApp!=0){
            QueryWrapper<AppUser> createUserQuery = new QueryWrapper<>();
            createUserQuery.lambda().eq(AppUser::getUserId, createUserIdInApp);
            createPartner = appUserService.getOne(createUserQuery);
            //零售店所属客户经理 - 真名从MUser中获取
            Long createUserIdInMUser = userService.getMUserIdFromAppUserId(createUserIdInApp);
            QueryWrapper<User> createMUserQuery = new QueryWrapper<>();
            createMUserQuery.lambda().eq(User::getUserId, createUserIdInMUser);
            User createPartnerInMUser = userService.getOne(createMUserQuery);
            createPartner.setRealName(createPartnerInMUser!=null ? createPartnerInMUser.getRealName() : "");
        } else {
            createPartner.setRealName("");
        }
        store.setCreatePartner(createPartner);

        //零售店对应的消费者数量
        List<Long> userIdList = customerStoreMapper.getCustomerIdListByStoreId(store.getStoreId());
        Integer customerAmount = userIdList.size();
        store.setCustomerAmountOfStore(customerAmount);

        //已完成门店问卷
        Integer finishedStoreAnswerAmount = storeService.getFinishedStoreAnswerByStoreId(storeId);
        store.setFinishedStoreAnswer(finishedStoreAnswerAmount);

        //完成消费者问卷
        Integer finishedCustomerAnswerAmount = storeService.getFinishedCustomerAnswerByStoreId(storeId);
        store.setFinishedCustomerAnswer(finishedCustomerAnswerAmount);

        //可参与门店问卷
            //没有区域限制的零售户问卷数量 + 限制在门店所在区域的零售户问卷数量
            //零售户问卷：activity.for_user_type = 2 （1是消费者问卷，2是零售户问卷）
            //问卷区域限制表：activity_area_limit
            //零售户门店区域编号：store.district_code
//        String cityCode = store.getCityCode();

//        String cityCode = store.getCityCode();
//        List<Answer> availableStoreAnswerList = storeService.getAvailableStoreAnswerByCityCode(cityCode);
        String districtCode = store.getDistrictCode();
        List<Answer> availableStoreAnswerList = storeService.getAvailableStoreAnswerByDistrictCode(districtCode);
        store.setAvailableStoreAnswer(availableStoreAnswerList.size());


//            Long storeIdForAnswer = store.getStoreId();
//            QueryWrapper<Answer> answerQuery1 = new QueryWrapper<>();
//            answerQuery1.lambda().eq(Answer::getStoreId, storeIdForAnswer);
//            List<Answer> answerList1 = answerService.list(answerQuery1);
//            Integer answerAmountOfStore = answerList1.size();
//            store.setAnswerAmountOfStore(answerAmountOfStore);
//
//            Long userIdForAnswer = store.getUserId();
//            QueryWrapper<Answer> answerQuery2 = new QueryWrapper<>();
//            answerQuery2.lambda().eq(Answer::getUserId, userIdForAnswer);
//            List<Answer> answerList2 = answerService.list(answerQuery2);
//            Integer answerAmountOfUser = answerList2.size();
//            store.setAnswerAmountOfUser(answerAmountOfUser);
//
//            Integer answerTotalAmount = answerAmountOfStore + answerAmountOfUser;
//            store.setAnswerTotalAmount(answerTotalAmount);
//        }

//        storeList.stream().filter(item -> item.getCreateUserName().contains(realName)).collect(Collectors.toList());

//        List<Store> newStoreList = new ArrayList<>();
//        storeList.stream().filter(item -> item.getCreateUserName().contains(realName)).forEach(item->{
//            Store newStore = new Store();
//            BeanUtils.copyProperties(item,newStore);
//            newStoreList.add(newStore);
//        });
//        storeList = newStoreList;

//        storeListPage.setRecords(newStoreList);

        return ResultUtils.success("查询成功", store);
    }

    @PutMapping("/deleteAuthByStoreId/{storeId}")
    public ResultVo deleteAuth(@PathVariable Long storeId){

        QueryWrapper<Store> storeQuery = new QueryWrapper<>();
        storeQuery.lambda().eq(Store::getStoreId, storeId);
        Store store = storeService.getOne(storeQuery);

        Long userId = store.getUserId();

        //20240113 Start
        QueryWrapper<RetailerStore> retailerStoreQuery = new QueryWrapper<>();
        retailerStoreQuery.lambda().eq(RetailerStore::getStoreId, storeId)
                                .eq(RetailerStore::getUserId, userId)
                                 .eq(RetailerStore::getIsDelete, 0);
        RetailerStore retailerStore = retailerStoreService.getOne(retailerStoreQuery);
        retailerStore.setIsDelete(1);
        retailerStore.setDeleteTime(DateUtil.date());
        retailerStoreService.updateById(retailerStore);

        QueryWrapper<RetailerStore> retailerStoreQuery2 = new QueryWrapper<>();
        retailerStoreQuery2.lambda().eq(RetailerStore::getUserId, userId)
                                    .eq(RetailerStore::getIsDelete, 0);
        List<RetailerStore> retailerStoreList = retailerStoreService.list(retailerStoreQuery2);
        if(retailerStoreList==null || retailerStoreList.size()==0){
            QueryWrapper<AppUser> appUserQuery = new QueryWrapper<>();
            appUserQuery.lambda().eq(AppUser::getUserId, userId);
            AppUser appUser = appUserService.getOne(appUserQuery);
            appUser.setUserType(1);
            appUserService.updateById(appUser);
        }
        //20240113 End

//        storeService.deleteAuthForUser(userId);

        storeService.deleteAuthForStore(storeId);

        return ResultUtils.success("操作成功");
    }


    // 新增
    @PostMapping
    public ResultVo add(@RequestBody Store store) {
        //add store
        if (store.getStoreId() == null) {

//            QueryWrapper<Store> storeQuery = new QueryWrapper<>();
//            storeQuery.lambda().eq(Store::getLicenseNo, store.getLicenseNo());
//            Store storeTemp = storeService.getOne(storeQuery);
//            if( storeTemp != null ){
//                int isDelete = Integer.valueOf(storeTemp.getIsDelete() ? 1 : 0);
//                if( isDelete==0 ){
//                    throw new ServiceException( Constants.CODE_700 , "专卖证号已存在");
//                }
//            }

            User currentUser = TokenUtils.getCurrentUser();

            Long storeId = SnowflakeIdWorker.getNextId();
            store.setStoreId(storeId);

//            Long userId = SnowflakeIdWorker.getNextId();
            Long userId = new Long(0);
            store.setUserId(userId);

            if(Constants.ROLE_ADMIN.equals( currentUser.getRoleFlag()) ){   //客户经理创建门店时，store插入的create_uid为客户经理在小程序的用户id
//                Long partnerMuid = store.getCreateUid();
////                Long partnerAppuid = userService.getAppUserIdFromMUserId(partnerMuid);
//                store.setCreateUid(partnerMuid);

                if( NumberUtil.isNumber(store.getCreateUserName()) ){
                    Long createMUid = Long.valueOf(store.getCreateUserName());
                    Long createAppUid = userService.getAppUserIdFromMUserId(createMUid);
                    store.setCreateUid(createAppUid);
                }
            }

            if( Constants.ROLE_PARTNER.equals( currentUser.getRoleFlag()) ){   //客户经理创建门店时，store插入的create_uid为客户经理在小程序的用户id
//                    Long createAppUid = userService.getAppUserIdFromMUserId(createMUid);
//                    store.setCreateUid(createAppUid);

                Long partnerMuid = store.getCreateUid();
                Long partnerAppuid = userService.getAppUserIdFromMUserId(partnerMuid);
                store.setCreateUid(partnerAppuid);
            }

            store.setBusinessHours("");
            store.setLevel(0);
//            store.setTradeLevel(0);
            store.setStarLevel(0);
            store.setLocation("");

            store.setLongitude(new BigDecimal(0));
            store.setLatitude(new BigDecimal(0));
            store.setLogo("");
            store.setStatus(0);     //门店默认为'未认证'
            store.setIsEnable(true);
            store.setIsDelete(false);

            Date now = DateUtil.date();
            store.setCreateTime(now);
            store.setUpdateTime(now);

            Integer oneStoreId = storeService.selectStoreIdByLicenseNo(store.getLicenseNo());
            if(oneStoreId != null){
                throw new ServiceException( Constants.CODE_700 , "专卖证号已存在");
            }
            storeService.insertStore(store);
        }

        return ResultUtils.success("操作成功", store);
    }


    /**
     * 编辑
     */
    // 新增或者更新
    @PutMapping
    public ResultVo edit(@RequestBody Store store) {

        Store storeItem = storeService.getById(store.getId());

        storeItem.setName(store.getName());
        storeItem.setProvinceCode(store.getProvinceCode());
        storeItem.setProvinceName(store.getProvinceName());
        storeItem.setCityCode(store.getCityCode());
        storeItem.setCityName(store.getCityName());
        storeItem.setDistrictCode(store.getDistrictCode());
        storeItem.setDistrictName(store.getDistrictName());
        storeItem.setDetailAddress(store.getDetailAddress());
        storeItem.setTelephone(store.getTelephone());
        storeItem.setTradeLevel(store.getTradeLevel());

        if( NumberUtil.isNumber(store.getCreateUserName()) ){
            Long createMUid = Long.valueOf(store.getCreateUserName());
            Long createAppUid = userService.getAppUserIdFromMUserId(createMUid);
            storeItem.setCreateUid(createAppUid);
        }

        storeService.updateStore(storeItem);
        return ResultUtils.success("操作成功", storeItem);
    }


    @DeleteMapping("/{id}")
    public ResultVo delete(@PathVariable Integer id) {
        storeService.removeById(id);
        return ResultUtils.success("删除成功");
    }

    @PostMapping("/del/batch")
    public ResultVo deleteBatch(@RequestBody List<Integer> ids) {
        storeService.removeByIds(ids);
        return ResultUtils.success("批量删除成功");
    }

    @GetMapping
    public ResultVo findAll() {
        return ResultUtils.success("查询成功", storeService.list());
    }

    @GetMapping("/{id}")
    public ResultVo findOne(@PathVariable Integer id) {

        return ResultUtils.success("查询成功", storeService.getById(id));
    }

    /**
     * 1.3 dev获取所有零售门店
     */
    @GetMapping("/page")
    public ResultVo findPage(@RequestParam(defaultValue = "") String name,
                             @RequestParam(defaultValue = "") String licenseNo,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam Long userId ) {
        QueryWrapper<Store> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().orderByAsc(Store::getCreateTime);
        queryWrapper.lambda().eq(Store::getIsDelete, 0);

        if (StrUtil.isNotBlank(name)) {
            queryWrapper.lambda().like(Store::getName,name);
        }
        if (StrUtil.isNotBlank(licenseNo)) {
            queryWrapper.lambda().eq(Store::getLicenseNo,licenseNo);
        }
//        if (StrUtil.isNotBlank(userId.toString())) {
////            Long appUserId = userService.getAppUserIdFromMUserId(userId);
//            queryWrapper.lambda().eq(Store::getCreateUid,userId);
//        }
        if ( userId!=0 ) {
//            Long appUserId = userService.getAppUserIdFromMUserId(userId);
            queryWrapper.lambda().eq(Store::getCreateUid,userId);
        }

        IPage<Store> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        IPage<Store> storeListPage = storeService.page(page,queryWrapper);
        List<Store> storeList = storeListPage.getRecords();

//        if(storeList==null || storeList.size()==0){
//            return ResultUtils.success("无数据记录", null);
//        }

        for(Store store: storeList){
            Long createUserId = store.getCreateUid();
            if( createUserId!=0 ){
                QueryWrapper<User> userQuery = new QueryWrapper<>();
                userQuery.lambda().eq(User::getAppUserId, createUserId);
                User createUser = userService.getOne(userQuery);
                store.setCreateUserName( createUser!=null ? createUser.getRealName() : "");
            } else {
                store.setCreateUserName("");
            }

            Long storeIdForAnswer = store.getStoreId();
//            QueryWrapper<Answer> answerQuery1 = new QueryWrapper<>();
//            answerQuery1.lambda().eq(Answer::getStoreId, storeIdForAnswer);
//            List<Answer> answerList1 = answerService.list(answerQuery1);
//            Integer answerAmountOfStore = answerList1.size();
//            store.setAnswerAmountOfStore(answerAmountOfStore);

            //已完成门店问卷
            Integer answerAmountOfStore = storeService.getFinishedStoreAnswerByStoreId(storeIdForAnswer);
            store.setAnswerAmountOfStore(answerAmountOfStore);

//            Long userIdForAnswer = store.getUserId();
//            QueryWrapper<Answer> answerQuery2 = new QueryWrapper<>();
//            answerQuery2.lambda().eq(Answer::getUserId, userIdForAnswer);
//            List<Answer> answerList2 = answerService.list(answerQuery2);
//            Integer answerAmountOfUser = answerList2.size();
//            store.setAnswerAmountOfUser(answerAmountOfUser);

            //完成消费者问卷
            Integer answerAmountOfUser = storeService.getFinishedCustomerAnswerByStoreId(storeIdForAnswer);
            store.setAnswerAmountOfUser(answerAmountOfUser);

            Integer answerTotalAmount = answerAmountOfStore + answerAmountOfUser;
            store.setAnswerTotalAmount(answerTotalAmount);

//            Integer answerTotalAmount = answerAmountOfStore;
//            store.setAnswerTotalAmount(answerTotalAmount);
        }

        storeListPage.setRecords(storeList);
        return ResultUtils.success("查询成功",storeListPage);


//        return ResultUtils.success("查询成功",storeService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

//    /**
//     * 1.2 零售门店页 - 管理员看自己创建的零售门店列表
//     * @param realName
//     * @param pageNum
//     * @param pageSize
//     * @return
//     */
//    @GetMapping("/getStoreListByAdmin")
//    public ResultVo getStoreListByAdmin( @RequestParam(defaultValue = "") String realName,
//                                         @RequestParam Integer pageNum, @RequestParam Integer pageSize){
//        IPage<Store> page = new Page<>();
//        page.setCurrent(pageNum);
//        page.setSize(pageSize);
//        QueryWrapper<Store> queryWrapper = new QueryWrapper<>();
//        IPage<Store> storeListPage = storeService.page(page,queryWrapper);
////        List<Store> pageStoreList = storeListPage.getRecords();
//
//        Long adminUserId = TokenUtils.getCurrentUser().getUserId(); //当前登录的Admin
//        List<Store> realStoreList = storeService.getStoresByAdminUserId(adminUserId);
//
//        for(Store store: realStoreList){
//            QueryWrapper<AppUser> userQuery = new QueryWrapper<>();
//            Long createUserId = store.getCreateUid();
//            userQuery.lambda().eq(AppUser::getUserId, createUserId);
//            AppUser createUser = appUserService.getOne(userQuery);
//            store.setCreateUserName(createUser.getRealName());
//
//            Long storeIdForAnswer = store.getStoreId();
//            QueryWrapper<Answer> answerQuery1 = new QueryWrapper<>();
//            answerQuery1.lambda().eq(Answer::getStoreId, storeIdForAnswer);
//            List<Answer> answerList1 = answerService.list(answerQuery1);
//            Integer answerAmountOfStore = answerList1.size();
//            store.setAnswerAmountOfStore(answerAmountOfStore);
////
////            Long userIdForAnswer = store.getUserId();
////            QueryWrapper<Answer> answerQuery2 = new QueryWrapper<>();
////            answerQuery2.lambda().eq(Answer::getUserId, userIdForAnswer);
////            List<Answer> answerList2 = answerService.list(answerQuery2);
////            Integer answerAmountOfUser = answerList2.size();
////            store.setAnswerAmountOfUser(answerAmountOfUser);
////
////            Integer answerTotalAmount = answerAmountOfStore + answerAmountOfUser;
//            Integer answerTotalAmount = answerAmountOfStore;
//            store.setAnswerTotalAmount(answerTotalAmount);
//        }
//
////        storeList.stream().filter(item -> item.getCreateUserName().contains(realName)).collect(Collectors.toList());
//
//        List<Store> newRealStoreList = new ArrayList<>();
//        realStoreList.stream().filter(item -> item.getCreateUserName().contains(realName)).forEach(item->{
//            Store newStore = new Store();
//            BeanUtils.copyProperties(item,newStore);
//            newRealStoreList.add(newStore);
//        });
////        storeList = newStoreList;
//
////        pageStoreList = null;
////        pageStoreList = newRealStoreList;
//
////        pageStoreList = storeListPage.getRecords();
//
////        List<Store> pageStoreList = storeListPage.getRecords().clear();
//
//        storeListPage.setRecords(newRealStoreList);
//
//        storeListPage.setTotal(newRealStoreList.size());
//        storeListPage.setSize(pageSize);
//        storeListPage.setCurrent(pageNum);
//        return ResultUtils.success("查询成功",storeListPage);
//    }



    /**
     * 2.2 零售门店页 - 管理员看自己创建的零售门店列表
     * @param name
     * @param pageNum
     * @param pageSize
     * @return
     */
//    @AutoLog("")
    @GetMapping("/getStoreListByAdmin")
    public ResultVo getStoreListByAdmin( @RequestParam Long userId,
                                         @RequestParam(defaultValue = "") String name,
                                         @RequestParam(defaultValue = "") String licenseNo,
                                         @RequestParam Integer pageNum,
                                         @RequestParam Integer pageSize){

        Long adminUserId = TokenUtils.getCurrentUser().getUserId();                     //当前登录的Admin
//        Long adminAppUserId = userService.getAppUserIdFromMUserId(adminUserId);       //不需要此步，admin只在后台有账号，admin在小程序没有账号
        List<Long> storeIdList = storeService.getStoreIdListByAdminUserId(adminUserId); //admin的后台userId

        if(storeIdList==null || storeIdList.size()==0){
            return ResultUtils.success("无数据记录", null);  // no data 没有数据
        }

        //构造分页对象
        IPage<Store> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        //构造查询条件
        QueryWrapper<Store> storeQueryWrapper = new QueryWrapper<>();
        if (StrUtil.isNotBlank(name)) {
            storeQueryWrapper.lambda().like(Store::getName,name);
        }
        if (StrUtil.isNotBlank(licenseNo)) {
            storeQueryWrapper.lambda().eq(Store::getLicenseNo,licenseNo);
        }
        storeQueryWrapper.lambda().in(Store::getStoreId, storeIdList);
        IPage<Store> storeListPage = storeService.page(page,storeQueryWrapper);
        List<Store> storeList = storeListPage.getRecords();

//        if(storeList==null || storeList.size()==0){
//            return ResultUtils.success("无数据记录", null);
//        }

        for(Store store: storeList){
//            QueryWrapper<AppUser> userQuery = new QueryWrapper<>();
//            Long createUserId = store.getCreateUid();
//            userQuery.lambda().eq(AppUser::getUserId, createUserId);
//            AppUser createUser = appUserService.getOne(userQuery);
//            store.setCreateUserName( createUser!=null ? createUser.getRealName() : "");

//            QueryWrapper<User> userQuery = new QueryWrapper<>();
//            Long createUserId = store.getCreateUid();
//            userQuery.lambda().eq(User::getAppUserId, createUserId);
//            User createUser = userService.getOne(userQuery);
//            store.setCreateUserName( createUser!=null ? createUser.getRealName() : "");

            Long createUserId = store.getCreateUid();
            if( createUserId!=0 ){
                QueryWrapper<User> userQuery = new QueryWrapper<>();
                userQuery.lambda().eq(User::getAppUserId, createUserId);
                User createUser = userService.getOne(userQuery);
                store.setCreateUserName( createUser!=null ? createUser.getRealName() : "");
            } else {
                store.setCreateUserName("");
            }

            Long storeIdForAnswer = store.getStoreId();
            QueryWrapper<Answer> answerQuery1 = new QueryWrapper<>();
            answerQuery1.lambda().eq(Answer::getStoreId, storeIdForAnswer);
            List<Answer> answerList1 = answerService.list(answerQuery1);
            Integer answerAmountOfStore = answerList1.size();
            store.setAnswerAmountOfStore(answerAmountOfStore);

            Long userIdForAnswer = store.getUserId();
            QueryWrapper<Answer> answerQuery2 = new QueryWrapper<>();
            answerQuery2.lambda().eq(Answer::getUserId, userIdForAnswer);
            List<Answer> answerList2 = answerService.list(answerQuery2);
            Integer answerAmountOfUser = answerList2.size();
            store.setAnswerAmountOfUser(answerAmountOfUser);

            Integer answerTotalAmount = answerAmountOfStore + answerAmountOfUser;
//            Integer answerTotalAmount = answerAmountOfStore;
            store.setAnswerTotalAmount(answerTotalAmount);

//            Integer answerTotalAmount = answerAmountOfStore;
//            store.setAnswerTotalAmount(answerTotalAmount);
        }

        storeListPage.setRecords(storeList);
//        storeListPage.setTotal(storeIdList.size());
        return ResultUtils.success("查询成功",storeListPage);
    }

    /**
     * 2.2 - 2 零售门店页 - 管理员看某一个客户经理的零售门店列表
     * @param userId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/getStoreListByUserId")
    public ResultVo getStoreListByUserId( @RequestParam Long userId,
                                          @RequestParam Integer pageNum, @RequestParam Integer pageSize){
        List<Store> stores = getStoresByPartnerUid(userId);    //当前登录管理员名下的客户经理，名下的门店列表
        if(stores==null || stores.size()==0){
            return ResultUtils.success("无数据记录", null);  // no data 没有数据
        }
        List<Long> storeIdList = new ArrayList<>();
        for (Store store : stores){
            storeIdList.add(store.getStoreId());
        }

        //构造分页对象
        IPage<Store> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        //构造查询条件
        QueryWrapper<Store> storeQueryWrapper = new QueryWrapper<>();
//        Long appUserId = userService.getAppUserIdFromMUserId(userId);
        storeQueryWrapper.lambda().eq(Store::getCreateUid, userId);
        storeQueryWrapper.lambda().in(Store::getStoreId, storeIdList);
        IPage<Store> storeListPage = storeService.page(page,storeQueryWrapper);
        List<Store> storeList = storeListPage.getRecords();

        for(Store store: storeList){
//            Long createUserIdInApp = store.getCreateUid();
//            if(createUserIdInApp!=0){
//    //            QueryWrapper<AppUser> userQuery = new QueryWrapper<>();
//    //            userQuery.lambda().eq(AppUser::getUserId, createUserIdInApp);
//    //            AppUser createUser = appUserService.getOne(userQuery);
//                Long createUserIdInMUser = userService.getMUserIdFromAppUserId(createUserIdInApp);
//                QueryWrapper<User> createMUserQuery = new QueryWrapper<>();
//                createMUserQuery.lambda().eq(User::getUserId, createUserIdInMUser);
//                User createPartnerInMUser = userService.getOne(createMUserQuery);
//    //            createUser.setRealName(createPartnerInMUser.getRealName());
//    //            store.setCreateUserName(createUser.getRealName());
//                store.setCreateUserName(createPartnerInMUser.getRealName());
//            } else {
//                store.setCreateUserName("");
//            }

            Long createUserId = store.getCreateUid();
            if( createUserId!=0 ){
                QueryWrapper<User> userQuery = new QueryWrapper<>();
                userQuery.lambda().eq(User::getAppUserId, createUserId);
                User createUser = userService.getOne(userQuery);
                store.setCreateUserName( createUser!=null ? createUser.getRealName() : "");
            } else {
                store.setCreateUserName("");
            }

            Long storeIdForAnswer = store.getStoreId();
            QueryWrapper<Answer> answerQuery1 = new QueryWrapper<>();
            answerQuery1.lambda().eq(Answer::getStoreId, storeIdForAnswer);
            List<Answer> answerList1 = answerService.list(answerQuery1);
            Integer answerAmountOfStore = answerList1.size();
            store.setAnswerAmountOfStore(answerAmountOfStore);

            Integer answerTotalAmount = answerAmountOfStore;
            store.setAnswerTotalAmount(answerTotalAmount);
        }

        storeListPage.setRecords(storeList);
//        storeListPage.setTotal(storeList.size());
        return ResultUtils.success("查询成功",storeListPage);
    }

//    //通过userId，查找小程序User表的记录信息
//    public AppUser getUserFromApp(Long userId){
//        QueryWrapper<AppUser> queryWrapper = new QueryWrapper<>();
//        queryWrapper.lambda().eq(AppUser::getUserId, userId);
//        AppUser appUser = appUserService.getOne(queryWrapper);
//        return appUser;
//    }

    //通过客户经理的userId，查找此人旗下创建的门店
    public List<Store> getStoresByPartnerUid(Long userId){
        QueryWrapper<Store> queryWrapper = new QueryWrapper<>();
//        Long appUserId = userService.getAppUserIdFromMUserId(userId);
        queryWrapper.lambda().eq(Store::getCreateUid, userId)
                .eq(Store::getIsDelete, 0);     //20231206 已删除的门店不要查不出来
        List<Store> storeList = storeService.list(queryWrapper);
        return storeList;
    }

    /**
     * 3.1  零售门店页 - 客户经理登录，看自己创建的零售门店列表
     * @param name
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/getStoreListByPartner")
    public ResultVo getStoreListByPartner( @RequestParam(defaultValue = "") String name,
                                           @RequestParam(defaultValue = "") String licenseNo,
                                           @RequestParam Integer pageNum,
                                           @RequestParam Integer pageSize){
        User currentPartner = TokenUtils.getCurrentUser();   //当前登录的客户经理
        Long currentPartnerId = currentPartner.getUserId();   //当前登录的客户经理
        Long currentPartnerAppUserId = userService.getAppUserIdFromMUserId(currentPartnerId);   //客户经理有小程序的UserId

        //构造分页对象
        IPage<Store> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        //构造查询条件
        QueryWrapper<Store> query = new QueryWrapper<>();
//        query.lambda().like(Store::getName, storeName);
        if (StrUtil.isNotBlank(name)) {
            query.lambda().like(Store::getName,name);
        }
        if (StrUtil.isNotBlank(licenseNo)) {
            query.lambda().eq(Store::getLicenseNo,licenseNo);
        }
        query.lambda().eq(Store::getCreateUid, currentPartnerAppUserId);  //当前登录的客户经理名下的门店， store的创建者是客户经理，userId为小程序中的
        IPage<Store> storeListPage = storeService.page(page,query);
        List<Store> storeList = storeListPage.getRecords();

        if(storeList==null || storeList.size()==0){
            return ResultUtils.success("无数据记录", null);  // no data 没有数据
        }

        for(Store store: storeList){
//            QueryWrapper<User> userQuery = new QueryWrapper<>();
//            Long createUserId = store.getCreateUid();
//            userQuery.lambda().eq(User::getUserId, createUserId);
//            User createUser = userService.getOne(userQuery);
//            store.setCreateUserName(createUser.getRealName());

            store.setCreateUserName(currentPartner.getRealName());

            Long storeIdForAnswer = store.getStoreId();
            QueryWrapper<Answer> answerQuery = new QueryWrapper<>();
            answerQuery.lambda().eq(Answer::getStoreId, storeIdForAnswer);
            List<Answer> answerList1 = answerService.list(answerQuery);
            Integer answerAmountOfStore = answerList1.size();
            store.setAnswerAmountOfStore(answerAmountOfStore);

            Integer answerTotalAmount = answerAmountOfStore;
            store.setAnswerTotalAmount(answerTotalAmount);
        }

        storeListPage.setRecords(storeList);
        return ResultUtils.success("查询成功",storeListPage);
    }

    /**
    * 导出接口
    */
//    @GetMapping("/export")
//    public void export(HttpServletResponse response) throws Exception {
//        // 从数据库查询出所有的数据
//        List<Store> list = storeService.list();
//        // 在内存操作，写出到浏览器
//        ExcelWriter writer = ExcelUtil.getWriter(true);
//
//        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
//        writer.write(list, true);
//
//        // 设置浏览器响应的格式
//        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
//        String fileName = URLEncoder.encode("Store信息表", "UTF-8");
//        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
//
//        ServletOutputStream out = response.getOutputStream();
//        writer.flush(out, true);
//        out.close();
//        writer.close();
//
//        }

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
//        List<Store> list = reader.readAll(Store.class);
//
//        storeService.saveBatch(list);
//        return ResultUtils.success();
//    }


    public List<Integer> getIdsFromStoreIdList( List<Long> storeIdList ){
        List<Integer> idList = new ArrayList<>();
        for ( Long storeId : storeIdList ){
            QueryWrapper<Store> storeQueryWrapper = new QueryWrapper<>();
            storeQueryWrapper.lambda().eq(Store::getStoreId, storeId);
            Store store = storeService.getOne(storeQueryWrapper);
            idList.add(store.getId());
        }
        return idList;
    }

    /**
     * 导出接口   http://local host:8089/api/store/exportStoreList
     */
    @GetMapping("/export/exportStoreList")
    public void exportStores(HttpServletResponse response,
                             @RequestParam Long userId,
                             @RequestParam(defaultValue = "") String name,
                             @RequestParam(defaultValue = "") String licenseNo ) throws Exception {
        List<Store> storeList = new ArrayList<>();
        List<Long> storeIdList = new ArrayList<>();
        List<Integer> idList = new ArrayList<>();

//        User currentUser = TokenUtils.getCurrentUser();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(User::getUserId, userId);
        User currentUser = userService.getOne(queryWrapper);
        String roleFlag = currentUser.getRoleFlag();

        Store storeCriteria = new Store();
        storeCriteria.setUserId(userId);
        if( name!=null && StrUtil.isNotEmpty(name)){
            storeCriteria.setName(name);
        }
        if( licenseNo!=null && StrUtil.isNotEmpty(licenseNo)){
            storeCriteria.setLicenseNo(licenseNo);
        }

        if(Constants.ROLE_DEV.equals(roleFlag)){
//            Long devMUserId = currentUser.getUserId();                     //当前登录的Dev
//            Long devAppUserId = userService.getAppUserIdFromMUserId(devMUserId);
//            storeIdList = storeService.getStoreListByUserId(devAppUserId);

            storeList = storeService.getStoreListByDevWithParm(storeCriteria);

//            idList = getIdsFromStoreIdList(storeIdList);
            if(storeList!=null || storeList.size()!=0){
//                storeList = storeService.listByIds(idList);
                for(Store store: storeList){
                    QueryWrapper<User> userQuery = new QueryWrapper<>();
                    Long createUserId = store.getCreateUid();
                    if(createUserId==0){
                        store.setCreateUserName("");
                    } else {
                        userQuery.lambda().eq(User::getAppUserId, createUserId);
                        User createUser = userService.getOne(userQuery);
                        store.setCreateUserName( createUser!=null ? createUser.getRealName() : "");
                    }
                }
            }
        }else if(Constants.ROLE_ADMIN.equals(roleFlag)){
//            Long adminMUserId = currentUser.getUserId();                                    //当前登录的Admin
//            Long adminAppUserId = userService.getAppUserIdFromMUserId(adminMUserId);
//            storeIdList = storeService.getStoreIdListByAdminUserId(adminMUserId);            //admin的后台userId
            storeList = storeService.getStoreListByAdminWithParm(storeCriteria);            //admin的后台userId
//            idList = getIdsFromStoreIdList(storeIdList);
            if(storeList!=null || storeList.size()!=0){
//                storeList = storeService.listByIds(idList);
                for(Store store: storeList){
                    QueryWrapper<User> userQuery = new QueryWrapper<>();
                    Long createUserId = store.getCreateUid();
                    userQuery.lambda().eq(User::getAppUserId, createUserId);
                    User createUser = userService.getOne(userQuery);
                    store.setCreateUserName( createUser!=null ? createUser.getRealName() : "");
                }
            }
        }
//        else if(Constants.ROLE_PARTNER.equals(roleFlag)){
////            User currentPartner = currentUser;   //当前登录的客户经理
//            Long currentPartnerId = currentUser.getUserId();   //当前登录的客户经理
//            Long currentPartnerAppUserId = userService.getAppUserIdFromMUserId(currentPartnerId);   //客户经理有小程序的UserId
//
//            QueryWrapper<Store> query = new QueryWrapper<>();
//            query.lambda().eq(Store::getCreateUid, currentPartnerAppUserId);  //当前登录的客户经理名下的门店， store的创建者是客户经理，userId为小程序中的
//            storeList = storeService.list(query);
//
//            if(storeList!=null || storeList.size()!=0){
//                for(Store store: storeList){
//                    QueryWrapper<User> userQuery = new QueryWrapper<>();
//                    Long createUserId = store.getCreateUid();
//                    userQuery.lambda().eq(User::getAppUserId, createUserId);
//                    User createUser = userService.getOne(userQuery);
//                    store.setCreateUserName( createUser!=null ? createUser.getRealName() : "");
//                }
//            }
//        }

        //filename
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        String dateStr = sdf.format(date);

        // 1.问卷 选择题

        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //设置要导出到的sheet
        writer.renameSheet(0,"批量更新客户信息");
        //写入表头
//        List<String> rowHead = CollUtil.newArrayList("专卖证号", "门店名称", "门店电话", "省份", "城市", "区县", "地址", "经度", "纬度", "档位", "客户经理" );
//        writer.writeHeadRow(rowHead);

        // 设置单元格格式为文本
        Workbook workbook = writer.getWorkbook();
        workbook.getSheetAt(0).setColumnWidth(0, 4000);     //licenseNo
        workbook.getSheetAt(0).setColumnWidth(1, 13000);    //store name
        workbook.getSheetAt(0).setColumnWidth(2, 4000);     //telephone
        workbook.getSheetAt(0).setColumnWidth(3, 3500);     //province
        workbook.getSheetAt(0).setColumnWidth(4, 3500);     //city
        workbook.getSheetAt(0).setColumnWidth(5, 3500);     //district
        workbook.getSheetAt(0).setColumnWidth(6, 13000);     //address
        workbook.getSheetAt(0).setColumnWidth(7, 4000);     //longitude
        workbook.getSheetAt(0).setColumnWidth(8, 4000);     //latitude
        workbook.getSheetAt(0).setColumnWidth(9, 3000);     //stall
        workbook.getSheetAt(0).setColumnWidth(10, 4000);     //partner

        StyleSet styleSet = new StyleSet(workbook);
        CellStyle cellStyle = styleSet.getCellStyle();
        DataFormat format = writer.getWorkbook().createDataFormat();
        cellStyle.setDataFormat(format.getFormat("@"));
        writer.setStyleSet(styleSet);

//        //sheet1 选择题 Start
//        Iterator<Store> iterator = storeList.iterator();
//        int currentRow = 1;
//        while (iterator.hasNext()) {
//            Store store = iterator.next();
//            int startRow = currentRow;
//            for(int j=0; j<storeList.size(); j++,currentRow++){
//                writer.writeCellValue(0, currentRow, storeList.get(j).getLicenseNo());                  //licenseNo
//                writer.writeCellValue(1, currentRow, storeList.get(j).getName());                       //store name
//                writer.writeCellValue(2, currentRow, storeList.get(j).getTelephone());                  //门店电话
//                writer.writeCellValue(3, currentRow, storeList.get(j).getProvinceName());                //Province name
//                writer.writeCellValue(4, currentRow, storeList.get(j).getCityName());                   //city name
//                writer.writeCellValue(5, currentRow, storeList.get(j).getDistrictName());               //district name
//                writer.writeCellValue(6, currentRow, storeList.get(j).getDetailAddress());              //address
//                writer.writeCellValue(7, currentRow, storeList.get(j).getLongitude().toString());       //longitude
//                writer.writeCellValue(8, currentRow, storeList.get(j).getLatitude().toString());        //latitude
//                writer.writeCellValue(9, currentRow, storeList.get(j).getTradeLevel());                 //stall档位 = tradeLevel
//                writer.writeCellValue(10, currentRow, storeList.get(j).getCreateUserName());             //partner
//            }
//        }
//        //sheet1 选择题 END


        //sheet1 选择题 Start
//        Iterator<Store> iterator = storeList.iterator();
//        int currentRow = 1;
//        while (iterator.hasNext()) {
//            Store store = iterator.next();
//            int startRow = currentRow;
//            for(int j=0; j<storeList.size(); j++,currentRow++){
//                writer.writeCellValue(0, currentRow, storeList.get(j).getLicenseNo());                  //licenseNo
//                writer.writeCellValue(1, currentRow, storeList.get(j).getName());                       //store name
//                writer.writeCellValue(2, currentRow, storeList.get(j).getTelephone());                  //门店电话
//                writer.writeCellValue(3, currentRow, storeList.get(j).getProvinceName());                //Province name
//                writer.writeCellValue(4, currentRow, storeList.get(j).getCityName());                   //city name
//                writer.writeCellValue(5, currentRow, storeList.get(j).getDistrictName());               //district name
//                writer.writeCellValue(6, currentRow, storeList.get(j).getDetailAddress());              //address
//                writer.writeCellValue(7, currentRow, storeList.get(j).getLongitude().toString());       //longitude
//                writer.writeCellValue(8, currentRow, storeList.get(j).getLatitude().toString());        //latitude
//                writer.writeCellValue(9, currentRow, storeList.get(j).getTradeLevel());                 //stall档位 = tradeLevel
//                writer.writeCellValue(10, currentRow, storeList.get(j).getCreateUserName());             //partner
//            }
//        }

//        List<String> rowHead = CollUtil.newArrayList("专卖证号", "门店名称", "门店电话", "省份", "城市", "区县", "地址", "经度", "纬度", "档位", "客户经理" );

        // 用个List<Map>装表格的所有内容
        ArrayList<Map<String, Object>> rows = new ArrayList<>();
        // 拼接上列明。我这里用的最土的方法，很不优雅别学我
        for(int i = 0; i < storeList.size(); i++){
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("专卖证号", storeList.get(i).getLicenseNo());
            row.put("门店名称", storeList.get(i).getName());
            row.put("门店电话", storeList.get(i).getTelephone());
            row.put("省份", storeList.get(i).getProvinceName());
            row.put("城市", storeList.get(i).getCityName());
            row.put("区县", storeList.get(i).getDistrictName());
            row.put("地址", storeList.get(i).getDetailAddress());
            row.put("经度", storeList.get(i).getLongitude().toString());
            row.put("纬度", storeList.get(i).getLatitude().toString());
            row.put("档位", storeList.get(i).getTradeLevel());
            row.put("客户经理", storeList.get(i).getCreateUserName());
            rows.add(row);
        }
        writer.write(rows, true);

        //sheet1 选择题 END


        //sheet1 选择题 Start
//        writer.write(storeList, true);
        //sheet1 选择题 END

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("门店列表-" + dateStr, "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        //将excel文件信息写入输出流，返回给调用者
        ServletOutputStream excelOut = null;
        try {
            excelOut = response.getOutputStream();
            writer.flush(excelOut,true);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            writer.close();
        }
        IoUtil.close(excelOut);

    }

    /**
     * 导出接口   http://local host:8089/api/store/exportStoreHeadList
     */
    @GetMapping("/export/exportStoreHeadList")
    public void exportStoreHeadList(HttpServletResponse response,
                                    @RequestParam Long userId) throws Exception {
        //filename
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        String dateStr = sdf.format(date);

        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //设置要导出到的sheet
        writer.renameSheet(0,"批量更新客户信息");
        //写入表头
        List<String> rowHead = CollUtil.newArrayList("专卖证号", "门店名称", "门店电话", "省份", "城市", "区县", "地址", "经度", "纬度", "档位", "客户经理" );
        writer.writeHeadRow(rowHead);

        // 设置单元格格式为文本
        Workbook workbook = writer.getWorkbook();
        workbook.getSheetAt(0).setColumnWidth(0, 4000);     //licenseNo
        workbook.getSheetAt(0).setColumnWidth(1, 13000);    //store name
        workbook.getSheetAt(0).setColumnWidth(2, 4000);     //telephone
        workbook.getSheetAt(0).setColumnWidth(3, 3500);     //province
        workbook.getSheetAt(0).setColumnWidth(4, 3500);     //city
        workbook.getSheetAt(0).setColumnWidth(5, 3500);     //district
        workbook.getSheetAt(0).setColumnWidth(6, 13000);     //address
        workbook.getSheetAt(0).setColumnWidth(7, 4000);     //longitude
        workbook.getSheetAt(0).setColumnWidth(8, 4000);     //latitude
        workbook.getSheetAt(0).setColumnWidth(9, 3000);     //stall
        workbook.getSheetAt(0).setColumnWidth(10, 4000);     //partner

        StyleSet styleSet = new StyleSet(workbook);
        CellStyle cellStyle = styleSet.getCellStyle();
        DataFormat format = writer.getWorkbook().createDataFormat();
        cellStyle.setDataFormat(format.getFormat("@"));
        writer.setStyleSet(styleSet);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("门店信息模板", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        //将excel文件信息写入输出流，返回给调用者
        ServletOutputStream excelOut = null;
        try {
            excelOut = response.getOutputStream();
            writer.flush(excelOut,true);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            writer.close();
        }
        IoUtil.close(excelOut);
    }


    /**
     * excel 导入
     *
     * @param
     * @throws Exception
     */
//    @PostMapping("/importStoreList")
//    public ResultVo importStoreList(MultipartFile file) throws Exception {
//        InputStream inputStream = file.getInputStream();
//        ExcelReader reader = ExcelUtil.getReader(inputStream);
//
////        List<List<Object>> list = reader.read(1);
//        List<List<Object>> list = reader.read(1, Integer.MAX_VALUE, false);
//        List<Map<String, Object>> maps = reader.readAll();
//        System.out.println("maps = " + maps);
//
//        String str = maps.get(0);
//        System.out.println("maps = " + maps);
//
//
//
//        String errMsg = null;
//        String errVarName = null;
//        for (int i = 0; i < list.size(); i++){
//            errMsg = "";
//            errVarName = "";
//
//            List<Object> row = list.get(i);
//            String licenseNo = row.get(0).toString().trim();            //licenseNo
//            String name = row.get(1).toString().trim();                 //store name
//            String telephone = row.get(2).toString().trim();            //telephone
//            String provinceName = row.get(3).toString().trim();         //province name
//            String cityName = row.get(4).toString().trim();             //city name
//            String districtName = row.get(5).toString().trim();         //district name
//            String detailAddress = row.get(6).toString().trim();        //detailAddress
//            String longitudeStr = row.get(7).toString().trim();         //longitudeStr
//            String latitudeStr = row.get(8).toString().trim();          //latitudeStr
//            String tradeLevelStr = row.get(9).toString().trim();         //tradeLevel
//            String createUserName = row.get(10).toString().trim();        //客户经理
//
//
//            if(StrUtil.isEmpty(licenseNo)){
//                errVarName = "'专卖证号'";
//            }
//            if(StrUtil.isEmpty(name)){
//                errVarName += "'门店名称'";
//            }
//            if(StrUtil.isEmpty(telephone)){
//                errVarName += "'门店电话'";
//            }
//            if(StrUtil.isEmpty(provinceName)){
//                errVarName += "'省'";
//            }
//            if(StrUtil.isEmpty(cityName)){
//                errVarName += "'市'";
//            }
//            if(StrUtil.isEmpty(districtName)){
//                errVarName += "'区'";
//            }
//            if(StrUtil.isEmpty(detailAddress)){
//                errVarName += "'地址'";
//            }
//            if(StrUtil.isEmpty(longitudeStr)){
//                errVarName += "'经度'";
//            }
//            if(StrUtil.isEmpty(latitudeStr)){
//                errVarName += "'维度'";
//            }
//            if(StrUtil.isEmpty(tradeLevelStr)){
//                errVarName += "'档位'";
//            }
//            if(StrUtil.isEmpty(createUserName)){
//                errVarName += "'客户经理'";
//            }
//            //有错误信息，即有字段为空
//            if(StrUtil.isNotEmpty(errVarName)){
//                errMsg = "第" + (i+2) + "行" + errVarName + "有误";
//                throw new ServiceException( Constants.CODE_606 , errMsg);
//            }
//        }
//
//        for (int i = 0; i < list.size(); i++) {
//            List<Object> row = list.get(i);
////            Map<String, Object> row = list.get(i);
//            Store store = new Store();
//            String licenseNo = row.get(0).toString().trim();                                       //1.licenseNo
//            String name = row.get(1).toString().trim();                                            //2.store name
//            String telephone = row.get(2).toString().trim();                                       //3.telephone
//            String provinceName = row.get(3).toString().trim();                                    //4.province name
//            String cityName = row.get(4).toString().trim();                                        //5.city name
//            String districtName = row.get(5).toString().trim();                                    //6.district name
//            String detailAddress = row.get(6).toString().trim();                                   //7.detail Address
//            String longitudeStr = "";
//            BigDecimal longitudeBigDecimal = new BigDecimal(0);
//            if (row.get(7) != null) {
//                longitudeStr = row.get(7).toString().trim();                                    //8.longitude
//                longitudeBigDecimal = new BigDecimal(longitudeStr);
//            }
//            String latitudeStr = "";
//            BigDecimal latitudeBigDecimal = new BigDecimal(0);
//            if (row.get(8) != null) {
//                latitudeStr = row.get(8).toString().trim();                                     //9.latitude
//                latitudeBigDecimal = new BigDecimal(latitudeStr);
//            }
//            Integer tradeLevel = Integer.valueOf(row.get(9).toString().trim());                    //10.stall档位 = tradeLevel
//
//            Long createUid = new Long(0);                                             //partner客户经理id是小程序userid
//            String createUserName = "";
//            if (row.get(10) != null) {
//                createUserName = row.get(10).toString().trim();                                 //11.create partner
//                QueryWrapper<User> userQuery = new QueryWrapper<>();
//                userQuery.lambda().eq(User::getRealName, createUserName);
//                User createUser = userService.getOne(userQuery);
//                if (createUser != null) {
//                    createUid = createUser.getAppUserId();
//                }
//            }
//
//            QueryWrapper<ChinaRegion> chinaRegionQuery = new QueryWrapper<>();
//            chinaRegionQuery.lambda().eq(ChinaRegion::getAreaName, districtName);
//            ChinaRegion chinaRegion = chinaRegionService.getOne(chinaRegionQuery);
//            String areaCode = chinaRegion.getAreaCode();
//            String provinceCode = areaCode.substring(0, 2) + "0000";
//            String cityCode = areaCode.substring(0, 4) + "00";
//            String districtCode = areaCode;
//
////            System.out.println("===000 licenseNo===" + licenseNo);
//
//            Integer oneStoreId = storeService.selectStoreIdByLicenseNo(licenseNo);
//            if (oneStoreId == null) {                     //licenseNo不存在的，进行新增
//                store.setLicenseNo(licenseNo);
//                store.setName(name);
//                store.setTelephone(telephone);
//                store.setProvinceName(provinceName);
//                store.setCityName(cityName);
//                store.setDistrictName(districtName);
//                store.setDetailAddress(detailAddress);
//                store.setLongitude(longitudeBigDecimal);
//                store.setLatitude(latitudeBigDecimal);
//                store.setTradeLevel(tradeLevel);
//                store.setCreateUserName(createUserName);
//                store.setCreateUid(createUid);
//
//                User currentUser = TokenUtils.getCurrentUser();
//                Long storeId = SnowflakeIdWorker.getNextId();
//                store.setStoreId(storeId);
////                Long userId = SnowflakeIdWorker.getNextId();
//                Long userId = new Long(0);
//                store.setUserId(userId);
//                if (Constants.ROLE_ADMIN.equals(currentUser.getRoleFlag()) || Constants.ROLE_PARTNER.equals(currentUser.getRoleFlag())) {   //客户经理创建门店时，store插入的create_uid为客户经理在小程序的用户id
//                    Long partnerMuid = store.getCreateUid();
//                    store.setCreateUid(partnerMuid);
//                }
//                store.setBusinessHours("");
//                store.setLevel(0);
//                store.setStarLevel(0);
//                store.setLocation("");
////                store.setLongitude(new BigDecimal(0));
////                store.setLatitude(new BigDecimal(0));
//                store.setLogo("");
//                store.setStatus(0);     //门店默认为'未认证'
//                store.setIsEnable(true);
//                store.setIsDelete(false);
//                Date now = DateUtil.date();
//                store.setCreateTime(now);
//                store.setUpdateTime(now);
//
//                store.setProvinceCode(provinceCode);
//                store.setCityCode(cityCode);
//                store.setDistrictCode(districtCode);
//
//                storeService.insertStore(store);
//
//            } else {                //licenseNo已存在的，进行更新
//                QueryWrapper<Store> storeQuery = new QueryWrapper<>();
//                storeQuery.lambda().eq(Store::getLicenseNo, licenseNo)
//                        .eq(Store::getIsDelete, 0);
//                Store oneStore = storeService.getOne(storeQuery);
//
//                oneStore.setLicenseNo(licenseNo);
////              if(StrUtil.isNotBlank(name)){
//                oneStore.setName(name);
////              }
//                oneStore.setTelephone(telephone);
//                oneStore.setProvinceName(provinceName);
//                oneStore.setCityName(cityName);
//                oneStore.setDistrictName(districtName);
//                oneStore.setDetailAddress(detailAddress);
//                if (StrUtil.isNotBlank(longitudeStr) && StrUtil.isNotBlank(latitudeStr)) {
//                    oneStore.setLongitude(longitudeBigDecimal);
//                    oneStore.setLatitude(latitudeBigDecimal);
//                }
//                oneStore.setTradeLevel(tradeLevel);
//                oneStore.setCreateUid(createUid);
//                Date now = DateUtil.date();
//                oneStore.setUpdateTime(now);
//
////                System.out.println("===111 StoreId: " + oneStore.getStoreId() + " === LicenseNo: " + oneStore.getLicenseNo() );
//                storeService.updateStore(oneStore);
//
//            }
//
//
//        }
//
////        userService.saveBatch(users);
//        return ResultUtils.success("导入成功");
//    }




    @PostMapping("/importStoreList")
    public ResultVo importStoreList(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);

//        List<List<Object>> list = reader.read(1);
        List<List<Object>> list = reader.read(1, Integer.MAX_VALUE, false);

//        String errCol = null;
        String errMsg = null;

        for (int i = 0; i < list.size(); i++){
//            errCol = "";
            errMsg = "";

            List<Object> row = list.get(i);

            if( row.size()<11 ){            //判断列数:一共11列
                errMsg = "数据不完整";
                throw new ServiceException( Constants.CODE_606 , errMsg);
            }

            if( row.get(0)==null || StrUtil.isEmpty(row.get(0).toString().trim()) ){      //licenseNo
//                errCol = "'专卖证号'";
                errMsg = "第" + (i+2) + "行'专卖证号'为空";
                throw new ServiceException( Constants.CODE_606 , errMsg);
            }
            if( row.get(1)==null || StrUtil.isEmpty(row.get(1).toString().trim()) ){      //name
//                errCol = "'门店名称'";
                errMsg = "第" + (i+2) + "行'门店名称'为空";
                throw new ServiceException( Constants.CODE_606 , errMsg);
            }
            if( row.get(3)==null || StrUtil.isEmpty(row.get(3).toString().trim()) ){      //provinceName
//                errCol = "'省'";
                errMsg = "第" + (i+2) + "行'省'为空";
                throw new ServiceException( Constants.CODE_606 , errMsg);
            }
            if( row.get(4)==null || StrUtil.isEmpty(row.get(4).toString().trim()) ){      //cityName
//                errCol = "'市'";
                errMsg = "第" + (i+2) + "行'市'为空";
                throw new ServiceException( Constants.CODE_606 , errMsg);
            }
            if( row.get(5)==null || StrUtil.isEmpty(row.get(5).toString().trim()) ){      //districtName
//                errCol = "'区'";
                errMsg = "第" + (i+2) + "行'区'为空";
                throw new ServiceException( Constants.CODE_606 , errMsg);
            }
            if( row.get(6)==null || StrUtil.isEmpty(row.get(6).toString().trim()) ){      //detailAddress
//                errCol = "'地址'";
                errMsg = "第" + (i+2) + "行'地址'为空";
                throw new ServiceException( Constants.CODE_606 , errMsg);
            }
            if( row.get(9)==null || StrUtil.isEmpty(row.get(9).toString().trim()) ){      //tradeLevelStr
//                errCol = "'档位'";
                errMsg = "第" + (i+2) + "行'档位'为空";
                throw new ServiceException( Constants.CODE_606 , errMsg);
            }
            if( row.get(10)==null || StrUtil.isEmpty(row.get(10).toString().trim()) ){      //客户经理 createUserName
//                errCol = "'客户经理'";
                errMsg = "第" + (i+2) + "行'客户经理'为空";
                throw new ServiceException( Constants.CODE_606 , errMsg);
            }

            String partnerName = row.get(10).toString().trim();                         //客户经理
            QueryWrapper<User> userQuery = new QueryWrapper<>();
            userQuery.lambda().eq(User::getRealName, partnerName);
            User oneUser = userService.getOne(userQuery);
            if(oneUser == null){
                errMsg = "第" + (i+2) + "行客户经理不存在";
                throw new ServiceException( Constants.CODE_606 , errMsg);
            } else {
                if(oneUser.getAppUserId() == 0){
                    errMsg = "第" + (i+2) + "行客户经理未注册小程序";
                    throw new ServiceException( Constants.CODE_606 , errMsg);
                }
            }

            if( !StrUtil.isNumeric(row.get(7).toString()) ){        //longitudeStr
//                errCol = "'经度'";
                errMsg = "第" + (i+2) + "行'经度'应为数字";
                throw new ServiceException( Constants.CODE_606 , errMsg);
            } else {                                                //longitudeStr
                String longitudeStr = row.get(7).toString().trim();
                BigDecimal longitudeBigDecimal = new BigDecimal(longitudeStr);
                if (longitudeBigDecimal.compareTo(new BigDecimal(-180))==-1 || longitudeBigDecimal.compareTo(new BigDecimal(180))==1){
                    errMsg = "第" + (i+2) + "行'经度'应在区间范围-180～180内";
                    throw new ServiceException( Constants.CODE_606 , errMsg);
                }
            }

            if(  !StrUtil.isNumeric(row.get(8).toString()) ){      //latitudeStr
//                errCol = "'纬度'";
                errMsg = "第" + (i+2) + "行'纬度'应为数字";
                throw new ServiceException( Constants.CODE_606 , errMsg);
            } else {                                                //latitudeStr
                String latitudeStr = row.get(8).toString().trim();
                BigDecimal latitudeBigDecimal = new BigDecimal(latitudeStr);
                if (latitudeBigDecimal.compareTo(new BigDecimal(-180))==-1 || latitudeBigDecimal.compareTo(new BigDecimal(180))==1){
                    errMsg = "第" + (i+2) + "行'纬度'应在区间范围-180～180内";
                    throw new ServiceException( Constants.CODE_606 , errMsg);
                }
            }

            if(  !StrUtil.isNumeric(row.get(9).toString()) ){      //tradeLevelStr
//                errCol = "'档位数字'";
                errMsg = "第" + (i+2) + "行'档位'应为数字";
                throw new ServiceException( Constants.CODE_606 , errMsg);
            }

//                //有错误信息，即有字段为空
//            if(StrUtil.isNotEmpty(errCol)){
////                errMsg = "第" + (i+2) + "行" + errCol + "有误";
//                System.out.println("===errMsg==" + errMsg);
//                throw new ServiceException( Constants.CODE_606 , errMsg);
//            }

        }

//        String licenseNo = row.get(0).toString().trim();            //licenseNo
//        String name = row.get(1).toString().trim();                 //store name
//        String telephone = row.get(2).toString().trim();            //telephone
//        String provinceName = row.get(3).toString().trim();         //province name
//        String cityName = row.get(4).toString().trim();             //city name
//        String districtName = row.get(5).toString().trim();         //district name
//        String detailAddress = row.get(6).toString().trim();        //detailAddress
//        String longitudeStr = row.get(7).toString().trim();         //longitudeStr
//        String latitudeStr = row.get(8).toString().trim();          //latitudeStr
//        String tradeLevelStr = row.get(9).toString().trim();         //tradeLevel
//        String createUserName = row.get(10).toString().trim();        //客户经理


        for (int i = 0; i < list.size(); i++) {
            List<Object> row = list.get(i);
            Store store = new Store();
            String licenseNo = row.get(0).toString().trim();                                       //1.licenseNo
            String name = row.get(1).toString().trim();                                            //2.store name
            String telephone = row.get(2).toString().trim();                                       //3.telephone
            String provinceName = row.get(3).toString().trim();                                    //4.province name
            String cityName = row.get(4).toString().trim();                                        //5.city name
            String districtName = row.get(5).toString().trim();                                    //6.district name
            String detailAddress = row.get(6).toString().trim();                                   //7.detail Address
            String longitudeStr = "";
            BigDecimal longitudeBigDecimal = new BigDecimal(0);
            if (row.get(7) != null) {
                longitudeStr = row.get(7).toString().trim();                                    //8.longitude
                longitudeBigDecimal = new BigDecimal(longitudeStr);
            }
            String latitudeStr = "";
            BigDecimal latitudeBigDecimal = new BigDecimal(0);
            if (row.get(8) != null) {
                latitudeStr = row.get(8).toString().trim();                                     //9.latitude
                latitudeBigDecimal = new BigDecimal(latitudeStr);
            }
            Integer tradeLevel = Integer.valueOf(row.get(9).toString().trim());                    //10.stall档位 = tradeLevel

            Long createUid = new Long(0);                                             //partner客户经理id是小程序userid
            String createUserName = "";
            if (row.get(10) != null) {
                createUserName = row.get(10).toString().trim();                                 //11.create partner
                QueryWrapper<User> userQuery = new QueryWrapper<>();
                userQuery.lambda().eq(User::getRealName, createUserName);
                User createUser = userService.getOne(userQuery);
                if (createUser != null) {
                    createUid = createUser.getAppUserId();
                }
            }

            QueryWrapper<ChinaRegion> chinaRegionQuery = new QueryWrapper<>();
            chinaRegionQuery.lambda().eq(ChinaRegion::getAreaName, districtName);
            ChinaRegion chinaRegion = chinaRegionService.getOne(chinaRegionQuery);
            String areaCode = chinaRegion.getAreaCode();
            String provinceCode = areaCode.substring(0, 2) + "0000";
            String cityCode = areaCode.substring(0, 4) + "00";
            String districtCode = areaCode;

//            System.out.println("===000 licenseNo===" + licenseNo);

            Integer oneStoreId = storeService.selectStoreIdByLicenseNo(licenseNo);
            if (oneStoreId == null) {                     //licenseNo不存在的，进行新增
                store.setLicenseNo(licenseNo);
                store.setName(name);
                store.setTelephone(telephone);
                store.setProvinceName(provinceName);
                store.setCityName(cityName);
                store.setDistrictName(districtName);
                store.setDetailAddress(detailAddress);
                store.setLongitude(longitudeBigDecimal);
                store.setLatitude(latitudeBigDecimal);
                store.setTradeLevel(tradeLevel);
                store.setCreateUserName(createUserName);
                store.setCreateUid(createUid);

                User currentUser = TokenUtils.getCurrentUser();
                Long storeId = SnowflakeIdWorker.getNextId();
                store.setStoreId(storeId);
//                Long userId = SnowflakeIdWorker.getNextId();
                Long userId = new Long(0);
                store.setUserId(userId);
                if (Constants.ROLE_ADMIN.equals(currentUser.getRoleFlag()) || Constants.ROLE_PARTNER.equals(currentUser.getRoleFlag())) {   //客户经理创建门店时，store插入的create_uid为客户经理在小程序的用户id
                    Long partnerMuid = store.getCreateUid();
                    store.setCreateUid(partnerMuid);
                }
                store.setBusinessHours("");
                store.setLevel(0);
                store.setStarLevel(0);
                store.setLocation("");
//                store.setLongitude(new BigDecimal(0));
//                store.setLatitude(new BigDecimal(0));
                store.setLogo("");
                store.setStatus(0);     //门店默认为'未认证'
                store.setIsEnable(true);
                store.setIsDelete(false);
                Date now = DateUtil.date();
                store.setCreateTime(now);
                store.setUpdateTime(now);

                store.setProvinceCode(provinceCode);
                store.setCityCode(cityCode);
                store.setDistrictCode(districtCode);

                storeService.insertStore(store);

            } else {                //licenseNo已存在的，进行更新
                QueryWrapper<Store> storeQuery = new QueryWrapper<>();
                storeQuery.lambda().eq(Store::getLicenseNo, licenseNo)
                        .eq(Store::getIsDelete, 0);
                Store oneStore = storeService.getOne(storeQuery);

                oneStore.setLicenseNo(licenseNo);
//              if(StrUtil.isNotBlank(name)){
                oneStore.setName(name);
//              }
                oneStore.setTelephone(telephone);
                oneStore.setProvinceName(provinceName);
                oneStore.setCityName(cityName);
                oneStore.setDistrictName(districtName);
                oneStore.setProvinceCode(provinceCode);
                oneStore.setCityCode(cityCode);
                oneStore.setDistrictCode(districtCode);
                oneStore.setDetailAddress(detailAddress);
                if (StrUtil.isNotBlank(longitudeStr) && StrUtil.isNotBlank(latitudeStr)) {
                    oneStore.setLongitude(longitudeBigDecimal);
                    oneStore.setLatitude(latitudeBigDecimal);
                }
                oneStore.setTradeLevel(tradeLevel);
                oneStore.setCreateUid(createUid);
                Date now = DateUtil.date();
                oneStore.setUpdateTime(now);

//                System.out.println("===111 StoreId: " + oneStore.getStoreId() + " === LicenseNo: " + oneStore.getLicenseNo() );
                storeService.updateStore(oneStore);

            }


        }

//        userService.saveBatch(users);
        return ResultUtils.success("导入成功");
    }


    private User getUser() {
        return TokenUtils.getCurrentUser();
    }

//    @GetMapping("/getStoresByUserId")
//    public ResultVo getStoresByUserId() {
//        User currentUser = getUser();
//        List<Store> storeList = storeService.getStoresByUserId(currentUser.getId());
//        return ResultUtils.success("查找成功", storeList );
//    }

//    @GetMapping("/getStoresByUserId")
//    public ResultVo getStoresByUserId(@RequestParam String name) {
//        List<Store> storeList = storeService.getStoresByUserId(getUser().getId(), name);
//        return ResultUtils.success("查找成功", storeList );
//    }


}

