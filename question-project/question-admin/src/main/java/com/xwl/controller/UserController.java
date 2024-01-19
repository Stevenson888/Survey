package com.xwl.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xwl.annotation.AutoLog;
import com.xwl.entity.*;
import com.xwl.exception.ServiceException;
import com.xwl.mapper.CustomerStoreMapper;
import com.xwl.mapper.RoleMapper;
import com.xwl.mapper.RoleMenuMapper;
import com.xwl.mapper.UserMapper;
import com.xwl.service.*;
import com.xwl.service.impl.CustomerStoreServiceImpl;
import com.xwl.utils.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 用户管理模块控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final static org.slf4j.Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private IUserService userService;
    @Autowired
    private IAppUserService appUserService;
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private RoleMenuMapper roleMenuMapper;
    @Resource
    private CustomerStoreMapper customerStoreMapper;
    @Resource
    private IMenuService menuService;
    @Resource
    private IPartnerService partnerService;
    @Resource
    private IAnswerService answerService;
    @Resource
    private IStoreService storeService;
    @Resource
    private ICustomerStoreService customerStoreService;


    /**
     * 用户登录
     */
//    @PostMapping("/login")
//    public ResultVo login(@RequestBody User sysUser){
//        QueryWrapper<User> query = new QueryWrapper<>();
//
////        query.lambda().eq(SysUser::getUsername,sysUser.getUsername())
////                .eq(SysUser::getPassword,sysUser.getPassword());
//        query.lambda().eq(User::getUsername,sysUser.getUsername());
//
//        User one = userService.getOne(query);
//        if(one == null){
//            return ResultUtils.error("用户名或密码错误!");
//        }
////        return ResultUtils.success("登录成功", one.getId());
//        return ResultUtils.success("登录成功", one);
//    }

    public String insertPartnersFromAppUserToMUser() {
        User returnMUser = new User();

        QueryWrapper<AppUser> appUserQueryWrapper = new QueryWrapper<>();
        appUserQueryWrapper.lambda().eq(AppUser::getUserType, 8);
        List<AppUser> appUserList = appUserService.list(appUserQueryWrapper);
        for (AppUser appUser : appUserList) {
            returnMUser.setUserId(SnowflakeIdWorker.getNextId());
            returnMUser.setPassword("96e79218965eb72c92a549dd5a330112");
            returnMUser.setCreateUid(new Long(0));
            returnMUser.setRoleFlag("ROLE_PARTNER");

            returnMUser.setAppUserId(appUser.getUserId());
            returnMUser.setUsername(StrUtil.isNotEmpty(appUser.getNickname()) ? appUser.getNickname() : appUser.getRealName());
            returnMUser.setRealName(appUser.getRealName());

            returnMUser.setProvinceName(appUser.getProvinceName());
            returnMUser.setProvinceCode(appUser.getProvinceCode());
            returnMUser.setCityName(appUser.getCityName());
            returnMUser.setCityCode(appUser.getCityCode());
            returnMUser.setDistrictName(appUser.getDistrictName());
            returnMUser.setDistrictCode(appUser.getDistrictCode());

            returnMUser.setGender(appUser.getGender());
            returnMUser.setMobile(appUser.getMobile());
            returnMUser.setStatus(appUser.getStatus());
            returnMUser.setAvatarUrl(appUser.getAvatar());
            returnMUser.setEmail("");
            returnMUser.setAddress("");

            returnMUser.setCreateTime(new Date());
            returnMUser.setUpdateTime(new Date());
            returnMUser.setLastLoginTime(new Date());

            boolean save = userService.save(returnMUser);
        }

        return "Good";
    }

    @PostMapping("/login")
    public ResultVo login(@RequestBody User user) {
        QueryWrapper<User> query = new QueryWrapper<>();
        query.lambda().eq(User::getUsername,user.getUsername());
        User userTemp1 = userService.getOne(query);
        if(userTemp1 == null){
            throw new ServiceException( Constants.CODE_600 , "用户不存在");
        }

        QueryWrapper<User> query2 = new QueryWrapper<>();
        query2.lambda().eq(User::getUsername,user.getUsername())
                        .eq(User::getPassword,SecureUtil.md5(user.getPassword()));
        User userTemp2 = userService.getOne(query2);
        if(userTemp2 == null){
            throw new ServiceException(Constants.CODE_601, "密码错误");
        }
        if(userTemp2.getStatus() == 0){
            throw new ServiceException(Constants.CODE_607, "此用户未激活");
        }

        User returnUser = new User() ;
        BeanUtil.copyProperties(userTemp2, returnUser, true);

        // 设置token
        String token = TokenUtils.genToken(returnUser.getId().toString(), SecureUtil.md5(user.getPassword()));
//        String token = TokenUtils.genToken(one.getId().toString(), "111111");
        returnUser.setToken(token);

        // 设置Menu
        String roleFlag = returnUser.getRoleFlag(); // ROLE_ADMIN
        List<Menu> roleMenus = getRoleMenus(roleFlag);
        returnUser.setMenus(roleMenus);

//        this.insertPartnersFromAppUserToMUser();

        return ResultUtils.success("登录成功", returnUser);
    }

    @PostMapping("/register")
    public ResultVo register(@RequestBody User user) {
//        String username = user.getUsername();
//        String password = user.getPassword();
//        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
//            throw new ServiceException( Constants.CODE_602 , "请输入用户名密码");
////            return ResultUtils.error("请输入用户名密码", 400, null);
//        }
        userService.register(user);
//        userDTO.setNickname(userDTO.getUsername());
        return ResultUtils.success("注册成功", user);
    }

    /**
     * 修改本人密码
     *
     * @param user
     * @return
     */
    @PutMapping("/updatePassword")
    public ResultVo updatePassword(@RequestBody User user) {
        QueryWrapper<User> query = new QueryWrapper<>();
        query.lambda().eq(User::getUsername, user.getUsername())
                .eq(User::getPassword, SecureUtil.md5(user.getPassword()));
        query.lambda().eq(User::getUsername,user.getUsername());
        User one = userService.getOne(query);
        if(one == null){
            throw new ServiceException(Constants.CODE_601, "密码错误");
        }

        User updateUser = new User();
        BeanUtils.copyProperties(user,updateUser);
        updateUser.setPassword(SecureUtil.md5(user.getPassword()));
        updateUser.setNewPassword(SecureUtil.md5(user.getNewPassword()));

        userService.updatePassword(updateUser);
        return ResultUtils.success("更新密码成功", updateUser);
    }

/**
     * 修改下属密码
     *
     * @param user
     * @return
     */
    @PutMapping("/updateOthersPassword")
    public ResultVo updateOthersPassword(@RequestBody User user) {
//        QueryWrapper<User> query = new QueryWrapper<>();
//        query.lambda().eq(User::getUsername, user.getUsername())
//                .eq(User::getPassword, SecureUtil.md5(user.getPassword()));
//        query.lambda().eq(User::getUsername,user.getUsername());
//        User one = userService.getOne(query);
//        if(one == null){
//            throw new ServiceException(Constants.CODE_601, "密码错误");
//        }

        User updateUser = new User();
        BeanUtils.copyProperties(user,updateUser);
//        updateUser.setPassword(SecureUtil.md5(user.getPassword()));
        updateUser.setNewPassword(SecureUtil.md5(user.getNewPassword()));

        userService.updateOthersPassword(updateUser);
        return ResultUtils.success("更新密码成功", updateUser);
    }

    /**
     * 修改下属密码
     *
     * @param user
     * @return
     */
    @PutMapping("/updateUserStatus")
    public ResultVo updateUserStatus(@RequestBody User user) {
        User updateUser = new User();
        BeanUtils.copyProperties(user,updateUser);

        userService.updateUserStatus(updateUser);
        return ResultUtils.success("用户状态更新成功", updateUser);
    }

    /**
     * 获取当前角色的菜单列表
     * @param roleFlag
     * @return
     */
    private List<Menu> getRoleMenus(String roleFlag) {
        Integer roleId = roleMapper.selectByFlag(roleFlag);
        // 当前角色的所有菜单id集合
        List<Integer> menuIds = roleMenuMapper.selectByRoleId(roleId);

        // 查出系统所有的菜单(树形)
        List<Menu> menus = menuService.findMenus("");
        // new一个最后筛选完成之后的list
        List<Menu> roleMenus = new ArrayList<>();
        // 筛选当前用户角色的菜单
        for (Menu menu : menus) {
            if (menuIds.contains(menu.getId())) {
                roleMenus.add(menu);
            }
            List<Menu> children = menu.getChildren();
            // removeIf()  移除children里面不在menuIds集合中的元素
            children.removeIf(child -> !menuIds.contains(child.getId()));
        }
        return roleMenus;
    }

//    private User getUserInfo(UserDTO userDTO) {
//        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("username", userDTO.getUsername());
////        queryWrapper.eq("password", userDTO.getPassword());
//        User one;
//        try {
//            one = getOne(queryWrapper); // 从数据库查询用户信息
//        } catch (Exception e) {
////            LOG.error(e);
////            throw new ServiceException(Constants.CODE_500, "系统错误");
//        }
//        return one;
//    }

    /**
     * 获取用户信息
     */
    @GetMapping("/getInfo")
    public ResultVo getInfo(Integer id){
        User user = userService.getById(id);
        return ResultUtils.success("查询成功",user);
    }

    @GetMapping("/{id}")
    public ResultVo getUserById(@PathVariable Long id){
        User user = userService.getById(id);
        return ResultUtils.success("查询成功",user);
    }

    /**
     * 1.1 dev获取所有管理员
     */
    @GetMapping("/getAllAdminsByDev")
    public ResultVo getAllAdminsByDev(@RequestParam(defaultValue = "") String realName,
                                    @RequestParam Integer pageNum, @RequestParam Integer pageSize){
        IPage<User> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);

        String roleFlag = Constants.ROLE_ADMIN;
        QueryWrapper<User> adminQuery = new QueryWrapper<>();
        adminQuery.lambda().eq(User::getRoleFlag, roleFlag);
        if (StrUtil.isNotBlank(realName)) {
            adminQuery.lambda().like(User::getRealName,realName);
        }

        IPage<User> userListPage = userService.page(page,adminQuery);
        return ResultUtils.success("查询成功",userListPage);
    }

    /**
     * 1.2 dev获取所有客户经理
     */
    @GetMapping("/getAllPartnersByDev")
    public ResultVo getAllPartnersByDev( @RequestParam(defaultValue = "") String realName,
                                           @RequestParam Integer pageNum, @RequestParam Integer pageSize){
        IPage<User> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        String roleFlag = Constants.ROLE_PARTNER;
        queryWrapper.lambda().eq(User::getRoleFlag, roleFlag);
        if (StrUtil.isNotBlank(realName)) {
            queryWrapper.lambda().like(User::getRealName,realName);
        }
        IPage<User> userListPage = userService.page(page,queryWrapper);

        List<User> userList = userListPage.getRecords();        //当前登录管理员名下的客户经理列表
//        if(userList==null || userList.size()==0){
//            return ResultUtils.success("无数据记录", null);  // no data 没有数据
//        }
        for(User user: userList){
            AppUser appUser = getUserFromApp(user.getAppUserId());
            if(appUser!=null){
                user.setAppNickname(StringUtils.isNotBlank( appUser.getNickname()) ? appUser.getNickname() : "");
                user.setAppCreateTime(appUser.getCreateTime());
                List<Store> storeList = getStoresByPartnerUid(user.getUserId());    //当前登录管理员名下的客户经理，名下的门店列表
                user.setStoreList(storeList);
                user.setStoreNumber(storeList.size());
            }
        }
        userListPage.setRecords(userList);

        return ResultUtils.success("查询成功",userListPage);
    }

    /**
     * 1.4 消费者页 - dev看所有消费者列表
     * @param name
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/getCustomersByDev")
    public ResultVo getCustomersByDev( @RequestParam(defaultValue = "") String name,
                                       @RequestParam Integer pageNum,
                                       @RequestParam Integer pageSize){

//        String roleFlag = Constants.ROLE_ADMIN;
//        QueryWrapper<User> adminQuery = new QueryWrapper<>();
//        adminQuery.lambda().eq(User::getRoleFlag, roleFlag);
//        List<User> adminList = userService.list(adminQuery);                                //所有管理员

//        List<Long> customerIds = new ArrayList<>();
//        if(customerIds==null || customerIds.size()==0){
//            return ResultUtils.success("无数据记录", null);  // no data 没有数据
//        }
//        for ( User admin : adminList){
//            Long adminUserId = admin.getUserId();
//            List<Long> storeIds = storeService.getStoreIdListByAdminUserId(adminUserId);    //每个管理员名下所有客户经理名下的所有零售店
//            for(Long storeId: storeIds){
//                List<Long> userIdList = customerStoreMapper.getCustomerIdListByStoreId(storeId);    //每个零售店关联的消费者
//                customerIds.addAll(userIdList);                                                     //customerIds：所有消费者
//            }
//        }

//        List<Long> customerIds = new ArrayList<>();
//        List<Long> storeIds = storeService.getStoreIdListByAllAdmin();    //每个管理员名下所有客户经理名下的所有零售店
//        for(Long storeId: storeIds){
//            List<Long> userIdList = customerStoreMapper.getCustomerIdListByStoreId(storeId);    //每个零售店关联的消费者
//            customerIds.addAll(userIdList);                                                     //customerIds：所有消费者
//        }

        List<Long> customerIds = new ArrayList<>();
        List<Long> userIdList = storeService.getCustomerIdListByAllAdmin();    //每个管理员名下所有客户经理名下的所有零售店
        customerIds.addAll(userIdList);

        //构造分页对象
        IPage<AppUser> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        //构造查询条件
        QueryWrapper<AppUser> query = new QueryWrapper<>();
        if (StrUtil.isNotBlank(name)) {
            query.lambda().like(AppUser::getNickname,name);
        }
        query.lambda().in(AppUser::getUserId, customerIds);                 //从所有pageList中，过滤出之前自定义sql的结果集
        IPage<AppUser> appUserListPage = appUserService.page(page,query);
        List<AppUser> appUserList = appUserListPage.getRecords();

        for(AppUser appUser : appUserList){
            Long userId = appUser.getUserId();

            List<Long> storeIdList = customerStoreMapper.getStoreIdByCustomerId(userId);
            List<Store> stores = new ArrayList<>();
            for(Long storeId : storeIdList){
                QueryWrapper<Store> storeQuery2 = new QueryWrapper<>();
                storeQuery2.lambda().eq(Store::getStoreId,storeId);
                Store store = storeService.getOne(storeQuery2);
                stores.add(store);
            }
            appUser.setStoreList(stores);

            QueryWrapper<Answer> answerQuery = new QueryWrapper<>();
            answerQuery.lambda().eq(Answer::getUserId, userId);
            List<Answer> answerList = answerService.list(answerQuery);
            appUser.setAnswerAmount( CollUtil.isNotEmpty(answerList) ? answerList.size() : 0 );

        }
//        }
        appUserListPage.setRecords(appUserList);
        return ResultUtils.success("查询成功",appUserListPage);

    }


//    /**
//     * 1.客户经理页 - 管理员看自己创建的客户经理列表
//     * @param realName
//     * @param currentPage
//     * @param pageSize
//     * @return
//     */
//    @GetMapping("/getPartnerUserList")
//    public ResultVo getPartnerUserList(@RequestParam(defaultValue = "") String realName,
//                                    @RequestParam Integer currentPage, @RequestParam Integer pageSize){
//        User currentUser = TokenUtils.getCurrentUser();
//
//        //1.构造分页对象page
//        IPage<User> page = new Page<>();
//        page.setCurrent(currentPage);
//        page.setSize(pageSize);
//
//        //2.构造userList
//        //2.1构造查询条件queryWrapper,得到userList
//        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//        queryWrapper.lambda().eq(User::getRoleFlag, "ROLE_PARTNER");
//        queryWrapper.lambda().eq(User::getCreateUid, currentUser.getUserId());
//        queryWrapper.lambda().like(User::getRealName,realName);
//        List<User> userList = userService.list(queryWrapper);
//
//        //2.2继续给userList赋值
////        userList.stream().map(user -> {
////            User newUser = new User();
////            BeanUtils.copyProperties(user, newUser);
////            AppUser appUser = getUserFromApp(user.getUserId());
////            newUser.setAppNickname(appUser.getNickname());
////            List<Store> storeList = getStoresByPartnerUid(user.getUserId());
////            newUser.setStoreList(storeList);
////            return newUser;
////        }).collect(Collectors.toList());
//
////        List<User> userList = userService.list(queryWrapper);
//        for(User user: userList){
//            AppUser appUser = getUserFromApp(user.getUserId());
//            user.setAppNickname(StringUtils.isNotBlank( appUser.getNickname()) ? appUser.getNickname() : "");
//            user.setAppCreateTime(appUser.getCreateTime());
//            List<Store> storeList = getStoresByPartnerUid(user.getUserId());
//            user.setStoreList(storeList);
//            user.setStoreNumber(storeList.size());
//        }
//
//        //3.构造最终userListPage
//        IPage<User> userListPage = userService.page(page,queryWrapper);
//        userListPage.setRecords(userList);
//
//        return ResultUtils.success("查询成功",userListPage);
//
////        List<User> userList = userService.list(query);
////        for(User user: userList){
////            AppUser appUser = getUserFromApp(user.getUserId());
////            user.setAppNickname(StringUtils.isNotBlank( appUser.getNickname()) ? appUser.getNickname() : "");
////            user.setAppCreateTime(appUser.getCreateTime());
////        }
//
////        Empty "";  Blank "  "
//
//    }



    /**
     * 2.1 客户经理页 - 管理员看自己创建的客户经理列表
     * @param realName
     * @param pageNum
     * @param pageSize
     * @return
     */
//    @AutoLog("")
    @GetMapping("/getPartnerListByAdmin")
    public ResultVo getPartnerListByAdmin( @RequestParam(defaultValue = "") String realName,
                                    @RequestParam Integer pageNum, @RequestParam Integer pageSize){

        Long adminId = TokenUtils.getCurrentUser().getUserId(); //当前登录的Admin
        String roleFlag = Constants.ROLE_PARTNER;
//        IPage<User> userListPage = getPartnersByUserId(roleFlag, createUserId, realName, currentPage,  pageSize);

//        log.info("getPartnerListByAdmin adminId: " + adminId);

        //1.构造分页对象page
        IPage<User> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);

        //2.构造userList
        //2.1构造查询条件queryWrapper,得到userList
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StrUtil.isNotBlank(roleFlag)) {
            queryWrapper.lambda().eq(User::getRoleFlag, roleFlag);
        }
        queryWrapper.lambda().eq(User::getCreateUid, adminId);  //创建人为当前登录管理员的user，即客户经理
        if (StrUtil.isNotBlank(realName)) {
            queryWrapper.lambda().like(User::getRealName,realName);
        }

        IPage<User> userListPage = userService.page(page,queryWrapper);
        List<User> userList = userListPage.getRecords();        //当前登录管理员名下的客户经理列表

//        if(userList==null || userList.size()==0){
//            return ResultUtils.success("无数据记录", null);  // no data 没有数据
//        }

        for(User user: userList){
            AppUser appUser = getUserFromApp(user.getAppUserId());
            if(appUser!=null){
                user.setAppNickname(StringUtils.isNotBlank( appUser.getNickname()) ? appUser.getNickname() : "");
                user.setAppCreateTime(appUser.getCreateTime());
                List<Store> storeList = getStoresByPartnerUid(user.getUserId());    //当前登录管理员名下的客户经理，名下的门店列表
                user.setStoreList(storeList);
                user.setStoreNumber(storeList.size());
            }
        }

        //3.构造userListPage
        userListPage.setRecords(userList);

        return ResultUtils.success("查询成功",userListPage);
    }

//    public IPage<User> getPartnersByUserId(String roleFlag, Long createUserId, String realName, Integer currentPage, Integer pageSize){
//        //1.构造分页对象page
//        IPage<User> page = new Page<>();
//        page.setCurrent(currentPage);
//        page.setSize(pageSize);
//
//        //2.构造userList
//        //2.1构造查询条件queryWrapper,得到userList
//        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//        if (StrUtil.isNotBlank(roleFlag)) {
//            queryWrapper.lambda().eq(User::getRoleFlag, roleFlag);
//        }
//        queryWrapper.lambda().eq(User::getCreateUid, createUserId);
//        if (StrUtil.isNotBlank(realName)) {
//            queryWrapper.lambda().like(User::getRealName,realName);
//        }
//
//        List<User> userList = userService.list(queryWrapper);
//        IPage<User> userListPage = userService.page(page,queryWrapper);
//
//        for(User user: userList){
//            AppUser appUser = getUserFromApp(user.getUserId());
//            user.setAppNickname(StringUtils.isNotBlank( appUser.getNickname()) ? appUser.getNickname() : "");
//            user.setAppCreateTime(appUser.getCreateTime());
//            List<Store> storeList = getStoresByPartnerUid(user.getUserId());
//            user.setStoreList(storeList);
//            user.setStoreNumber(storeList.size());
//        }
//
//        //3.构造userListPage
//        userListPage.setRecords(userList);
//
//        return userListPage;
//    }



//    /**
//     * 1.3 消费者页 - 管理员看自己创建的消费者列表
//     * @param realName
//     * @param pageNum
//     * @param pageSize
//     * @return
//     */
//    @GetMapping("/getCustomerListByAdmin")
//    public ResultVo getCustomerListByAdmin( @RequestParam(defaultValue = "") String realName,
//                                           @RequestParam Integer pageNum, @RequestParam Integer pageSize){
//        Long adminUserId = TokenUtils.getCurrentUser().getUserId(); //当前登录的Admin
//        List<Store> storeList = storeService.getStoresByAdminUserId(adminUserId);
////        IPage<Store> storeListPage = storeService.getStoresByAdminUserId(adminUserId);
////        List<Store> storeList = storeListPage.getRecords();
//
//        List<AppUser> customers = new ArrayList<>();
//        for(Store store: storeList){
//            List<Long> userIdList = customerStoreMapper.getCustomersByStoreId(store.getStoreId());
//            for(Long userId: userIdList){
//                QueryWrapper<AppUser> appUserQuery = new QueryWrapper<>();
//                appUserQuery.lambda().eq(AppUser::getUserId, userId);
//                AppUser appUser = appUserService.getOne(appUserQuery);
//
////                Long appUserId =  appUser.getUserId();
////                QueryWrapper<CustomerStore> customerStoreQuery = new QueryWrapper<>();
////                customerStoreQuery.lambda().eq(CustomerStore::getUserId,appUserId);
////                CustomerStore customerStore = customerStoreService.getOne(customerStoreQuery);
////                Long storeId = customerStore.getStoreId();
////                QueryWrapper<Store> storeQuery = new QueryWrapper<>();
////                storeQuery.lambda().eq(Store::getStoreId, storeId);
////                Store store2 = storeService.getOne(storeQuery);
//
//                appUser.setStore(store);
//
//                QueryWrapper<Answer> answerQuery = new QueryWrapper<>();
//                answerQuery.lambda().eq(Answer::getUserId, userId);
//                List<Answer> answerList = answerService.list(answerQuery);
//                appUser.setAnswerAmount( CollUtil.isNotEmpty(answerList) ? answerList.size() : 0 );
//
//                customers.add(appUser);
//            }
//        }
//
////        List<User> customers = new ArrayList<>();
////        BeanUtils.copyProperties(appCustomers, customers);
//
//        //根据字段 getLabelsName 去重
////        List<User> customerList = customers.stream().distinct().collect(Collectors.toList());
////        List<User> customerList = customers.stream().filter(distinctByKey(User::getUserId)).collect(Collectors.toList());
//
//
////        //构造分页对象
////        IPage<AppUser> page = new Page<>();
////        page.setCurrent(pageNum);
////        page.setSize(pageSize);
////        //构造查询条件
////        QueryWrapper<AppUser> query = new QueryWrapper<>();
////        if (StrUtil.isNotBlank(realName)) {
////            query.lambda().like(AppUser::getRealName,realName);
////        }
////        IPage<AppUser> userListPage = appUserService.page(page,query);
////        userListPage.getRecords().clear();
////        userListPage.setRecords(customers);
////        userListPage.setTotal(customers.size());
//////        page.setRecords(customers);
//////        page.setTotal(customers.size());
//////        return ResultUtils.success("查询成功",page);
////        return ResultUtils.success("查询成功",userListPage);
//
//
//        QueryWrapper<AppUser> queryWrapper = new QueryWrapper<>();
//        if (StrUtil.isNotBlank(realName)) {
//            queryWrapper.lambda().like(AppUser::getRealName,realName);
//        }
//        IPage<AppUser> userListPage = appUserService.page(new Page<>(pageNum, pageSize), queryWrapper);
//        userListPage.getRecords().clear();
//        userListPage.setRecords(customers);
//        userListPage.setTotal(customers.size());
//        return ResultUtils.success("查询成功",userListPage);
//
//    }


    /**
     * 2.3 消费者页 - 管理员看自己创建的消费者列表
     * @param name
     * @param pageNum
     * @param pageSize
     * @return
     */
//    @AutoLog("")
    @GetMapping("/getCustomerListByAdmin")
    public ResultVo getCustomerListByAdmin( @RequestParam(defaultValue = "") String name,
                                            @RequestParam Integer pageNum, @RequestParam Integer pageSize){

//        Long adminUserId = TokenUtils.getCurrentUser().getUserId(); //当前登录的Admin
//        List<Long> storeIds = storeService.getStoreIdListByAdminUserId(adminUserId);
//        if(storeIds==null || storeIds.size()==0){
//            return ResultUtils.success("无数据记录", null);  // no data 没有数据
//        }
//        List<Long> customerIds = new ArrayList<>();
//        for(Long storeId: storeIds){
//            List<Long> userIdList = customerStoreMapper.getCustomerIdListByStoreId(storeId);
//            customerIds.addAll(userIdList);
//        }
//        if(customerIds==null || customerIds.size()==0){
//            return ResultUtils.success("无数据记录", null);  // no data 没有数据
//        }

        Long adminUserId = TokenUtils.getCurrentUser().getUserId(); //当前登录的Admin
        List<Long> customerIds = storeService.getCustomerIdListByAdminUserId(adminUserId);
        if(customerIds==null || customerIds.size()==0){
            return ResultUtils.success("无数据记录", null);  // no data 没有数据
        }

        //构造分页对象
        IPage<AppUser> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        //构造查询条件
        QueryWrapper<AppUser> query = new QueryWrapper<>();
        if (StrUtil.isNotBlank(name)) {
            query.lambda().like(AppUser::getNickname,name);
        }
        query.lambda().in(AppUser::getUserId, customerIds);
        IPage<AppUser> appUserListPage = appUserService.page(page,query);
        List<AppUser> appUserList = appUserListPage.getRecords();

        for(AppUser appUser : appUserList){
            Long userId = appUser.getUserId();

            List<Long> storeIdList = customerStoreMapper.getStoreIdByCustomerId(userId);
//            Long storeId = StoreIdList.get(0);
//            QueryWrapper<Store> storeQuery = new QueryWrapper<>();
//            storeQuery.lambda().eq(Store::getStoreId,storeId);
//            Store store = storeService.getOne(storeQuery);
//            appUser.setStore(store);
            List<Store> stores = new ArrayList<>();
            for(Long storeId : storeIdList){
                QueryWrapper<Store> storeQuery2 = new QueryWrapper<>();
                storeQuery2.lambda().eq(Store::getStoreId,storeId);
                Store store = storeService.getOne(storeQuery2);
                stores.add(store);
            }
            appUser.setStoreList(stores);

            QueryWrapper<Answer> answerQuery = new QueryWrapper<>();
            answerQuery.lambda().eq(Answer::getUserId, userId);
            List<Answer> answerList = answerService.list(answerQuery);
            appUser.setAnswerAmount( CollUtil.isNotEmpty(answerList) ? answerList.size() : 0 );

            }
        appUserListPage.setRecords(appUserList);
        return ResultUtils.success("查询成功",appUserListPage);
    }


//    @GetMapping("/getCustomerListByAdmin")
//    public ResultVo getCustomerListByAdmin( @RequestParam(defaultValue = "") String realName,
//                                            @RequestParam Integer pageNum, @RequestParam Integer pageSize){
//        Long adminUserId = TokenUtils.getCurrentUser().getUserId(); //当前登录的Admin
//        List<Store> storeList = storeService.getStoresByAdminUserId(adminUserId);
////        IPage<Store> storeListPage = storeService.getStoresByAdminUserId(adminUserId);
////        List<Store> storeList = storeListPage.getRecords();
//
//        List<AppUser> customers = new ArrayList<>();
//        for(Store store: storeList){
//            List<Long> userIdList = customerStoreMapper.getCustomersByStoreId(store.getStoreId());
//            for(Long userId: userIdList){
//                QueryWrapper<AppUser> appUserQuery = new QueryWrapper<>();
//                appUserQuery.lambda().eq(AppUser::getUserId, userId);
//                AppUser appUser = appUserService.getOne(appUserQuery);
//                appUser.setStore(store);
//
//                QueryWrapper<Answer> answerQuery = new QueryWrapper<>();
//                answerQuery.lambda().eq(Answer::getUserId, userId);
//                List<Answer> answerList = answerService.list(answerQuery);
//                appUser.setAnswerAmount( CollUtil.isNotEmpty(answerList) ? answerList.size() : 0 );
//
//                customers.add(appUser);
//            }
//        }

        //构造分页对象
//        PageHelper.startPage(pageNum, pageSize);
//        PageInfo<AppUser> appUserPageInfo = new PageInfo<AppUser>(customers);
//        appUserPageInfo.setPageNum(pageNum);
//        appUserPageInfo.setPageSize(pageSize);
//        return ResultUtils.success("查询成功",appUserPageInfo);
//        return appUserPageInfo;

//    }


    //distinctByKey自己定义
    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    /**
     * 3.2  消费者页 - 客户经理登录，看自己创建的消费者列表
     * @param name
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/getCustomerListByPartner")
    public ResultVo getCustomerListByPartner( @RequestParam(defaultValue = "") String name,
                                           @RequestParam Integer pageNum, @RequestParam Integer pageSize){
        User currentPartner = TokenUtils.getCurrentUser();                          //当前登录的客户经理
        Long currentPartnerId = currentPartner.getUserId();                         //当前登录的客户经理 - 后台
        Long currentPartnerAppUserId = userService.getAppUserIdFromMUserId(currentPartnerId);  //当前登录的客户经理 - 小程序

        //构造查询条件
        QueryWrapper<Store> storeQuery = new QueryWrapper<>();
        storeQuery.lambda().eq(Store::getCreateUid, currentPartnerAppUserId);       //Store表搜索的用户id必须是小程序的userId
        List<Store> storeList = storeService.list(storeQuery);

        if(storeList==null || storeList.size()==0){
            return ResultUtils.success("无数据记录", null);  // no data 没有数据
        }

        List<Long> customerIds = new ArrayList<>();
        for(Store store: storeList){
            List<Long> userIdList = customerStoreMapper.getCustomerIdListByStoreId(store.getStoreId());
            customerIds.addAll(userIdList);
        }

        if(customerIds==null || customerIds.size()==0){
            return ResultUtils.success("无数据记录", null);  // no data 没有数据
        }

        //构造分页对象
        IPage<AppUser> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        //构造查询条件
        QueryWrapper<AppUser> appUserQuery = new QueryWrapper<>();
        if (StrUtil.isNotBlank(name)) {
            appUserQuery.lambda().like(AppUser::getNickname,name);
        }
        if (customerIds!=null && customerIds.size()>0) {
            appUserQuery.lambda().in(AppUser::getUserId, customerIds);
        } else {
            return ResultUtils.success("查询成功", null);
        }
        IPage<AppUser> appUserListPage = appUserService.page(page,appUserQuery);
        List<AppUser> appUserList = appUserListPage.getRecords();

        for(AppUser appUser : appUserList){
            Long userId = appUser.getUserId();

            List<Long> storeIdList = customerStoreMapper.getStoreIdByCustomerId(userId);
//            Long storeId = storeIdList.get(0);
//            QueryWrapper<Store> storeQuery2 = new QueryWrapper<>();
//            storeQuery2.lambda().eq(Store::getStoreId,storeId);
//            Store store = storeService.getOne(storeQuery2);

            List<Store> stores = new ArrayList<>();
            for(Long storeId : storeIdList){
                QueryWrapper<Store> storeQuery2 = new QueryWrapper<>();
                storeQuery2.lambda().eq(Store::getStoreId,storeId);
                Store store = storeService.getOne(storeQuery2);
                stores.add(store);
            }
            appUser.setStoreList(stores);

            QueryWrapper<Answer> answerQuery = new QueryWrapper<>();
            answerQuery.lambda().eq(Answer::getUserId, userId);
            List<Answer> answerList = answerService.list(answerQuery);
            appUser.setAnswerAmount( CollUtil.isNotEmpty(answerList) ? answerList.size() : 0 );
        }
        appUserListPage.setRecords(appUserList);
        return ResultUtils.success("查询成功",appUserListPage);
    }



//    /** getStoreUserList
//     * 2.1  零售门店页 - 客户经理登录，看自己创建的零售门店列表
//     * @param name
//     * @param currentPage
//     * @param pageSize
//     * @return
//     */
//    @GetMapping("/getStoreListByPartner")
//    public ResultVo getStoreListByPartner( @RequestParam(defaultValue = "") String name,
//                                           @RequestParam Integer currentPage, @RequestParam Integer pageSize){
//        Long userId = TokenUtils.getCurrentUser().getUserId();
//
//        //构造分页对象
//        IPage<Store> page = new Page<>();
//        page.setCurrent(currentPage);
//        page.setSize(pageSize);
//        //构造查询条件
//        QueryWrapper<Store> query = new QueryWrapper<>();
//        query.lambda().eq(Store::getCreateUid, userId);
//        query.lambda().like(Store::getName,name);
//        List<Store> storeList = storeService.list(query);
//        IPage<Store> storeListPage = storeService.page(page,query);
//
////        storeList.stream().map(store -> {
////            Store newStore = new Store();
////            BeanUtils.copyProperties(store, newStore);
////
////            QueryWrapper<User> userQuery = new QueryWrapper<>();
////            Long createUserId = store.getCreateUid();
////            userQuery.lambda().eq(User::getUserId, createUserId);
////            User createUser = userService.getOne(userQuery);
////            newStore.setCreateUserName(createUser.getRealName());
////
////            return newStore;
////        }).collect(Collectors.toList());
//
//        for(Store store: storeList){
//            QueryWrapper<User> userQuery = new QueryWrapper<>();
//            Long createUserId = store.getCreateUid();
//            userQuery.lambda().eq(User::getUserId, createUserId);
//            User createUser = userService.getOne(userQuery);
//            store.setCreateUserName(createUser.getRealName());
//
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
//
//        storeListPage.setRecords(storeList);
//        return ResultUtils.success("查询成功",storeListPage);
//    }
//
//

//    IPage<Store> page = new Page<>();
//    page.setCurrent(currentPage);
//    page.setSize(pageSize);
//    QueryWrapper<Store> queryWrapper = new QueryWrapper<>();
//    IPage<Store> storeListPage = storeService.page(page,queryWrapper);
//    storeListPage.setRecords(storeList);

//    public IPage<User> getPartnersByUserId(String roleFlag, Long createUserId, String realName, Integer currentPage, Integer pageSize){
//        //1.构造分页对象page
//        IPage<User> page = new Page<>();
//        page.setCurrent(currentPage);
//        page.setSize(pageSize);
//
//        //2.构造userList
//        //2.1构造查询条件queryWrapper,得到userList
//        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//        if (StrUtil.isNotBlank(roleFlag)) {
//            queryWrapper.lambda().eq(User::getRoleFlag, roleFlag);
//        }
//        queryWrapper.lambda().eq(User::getCreateUid, createUserId);
//        if (StrUtil.isNotBlank(realName)) {
//            queryWrapper.lambda().like(User::getRealName,realName);
//        }
//
//        List<User> userList = userService.list(queryWrapper);
//        IPage<User> userListPage = userService.page(page,queryWrapper);
//
//        for(User user: userList){
//            AppUser appUser = getUserFromApp(user.getUserId());
//            user.setAppNickname(StringUtils.isNotBlank( appUser.getNickname()) ? appUser.getNickname() : "");
//            user.setAppCreateTime(appUser.getCreateTime());
//            List<Store> storeList = getStoresByPartnerUid(user.getUserId());
//            user.setStoreList(storeList);
//            user.setStoreNumber(storeList.size());
//        }
//
//        //3.构造userListPage
//        userListPage.setRecords(userList);
//
//        return userListPage;
//    }


//    public List<User> getUserListByRoleFlag(String roleFlag){
//        //构造查询条件
//        QueryWrapper<User> query = new QueryWrapper<>();
//        query.lambda().eq(User::getRoleFlag, roleFlag);
//        List<User> userList = userService.list(query);
//        return userList;
//    }
//    public List<User> getUserListByCreateUid(Long createUid){
//        //构造查询条件
//        QueryWrapper<User> query = new QueryWrapper<>();
//        query.lambda().eq(User::getCreateUid, createUid);
//        List<User> userList = userService.list(query);
//        return userList;
//    }

    //通过userId，查找小程序User表的记录信息
    public AppUser getUserFromApp(Long userId){
        QueryWrapper<AppUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(AppUser::getUserId, userId);
        AppUser appUser = appUserService.getOne(queryWrapper);
        return appUser;
    }

    //通过客户经理的userId，查找此人旗下创建的门店
    public List<Store> getStoresByPartnerUid(Long userId){
        QueryWrapper<Store> queryWrapper = new QueryWrapper<>();
        Long appUserId = userService.getAppUserIdFromMUserId(userId);
        queryWrapper.lambda().eq(Store::getCreateUid, appUserId)
                            .eq(Store::getIsDelete, 0);     //20231206 已删除的门店不要查不出来
        List<Store> storeList = storeService.list(queryWrapper);
        return storeList;
    }

    @GetMapping("/getAllPartnerList")
    public ResultVo getAllPartnerList(){
        //构造查询条件
        QueryWrapper<User> query = new QueryWrapper<>();
        query.lambda().eq(User::getRoleFlag, "ROLE_PARTNER");
        List<User> allPartnerList = userService.list(query);
        return ResultUtils.success("查询成功",allPartnerList);
    }

    @GetMapping("/getAllPartnerListByArea/{districtCode}")
    public ResultVo getAllPartnerListByArea(@PathVariable String districtCode){
        //构造查询条件
        QueryWrapper<User> query = new QueryWrapper<>();
        query.lambda().eq(User::getRoleFlag, "ROLE_PARTNER")
                        .eq(User::getDistrictCode, districtCode);
        List<User> allPartnerList = userService.list(query);
        return ResultUtils.success("查询成功",allPartnerList);
    }

//    @GetMapping("/getStoreUserList")
//    public ResultVo getStoreUserList(@RequestParam(defaultValue = "") String realName,
//                                    @RequestParam Integer currentPage, @RequestParam Integer pageSize){
//        //构造分页对象
//        IPage<User> page = new Page<>();
//        page.setCurrent(currentPage);
//        page.setSize(pageSize);
//        //构造查询条件
//        QueryWrapper<User> query = new QueryWrapper<>();
//        query.lambda().eq(User::getRoleFlag, "ROLE_STORE");
//        query.lambda().like(User::getRealName,realName);
//
//        IPage<User> userList = userService.page(page,query);
//        return ResultUtils.success("查询成功",userList);
//    }





//    @GetMapping("/getStoreUserList")
//    public ResultVo getStoreUserList(@RequestParam(defaultValue = "") String realName,
//                                     @RequestParam Integer currentPage, @RequestParam Integer pageSize){
//        User currentUser = TokenUtils.getCurrentUser();
//
//        //1.构造分页对象page
//        IPage<User> page = new Page<>();
//        page.setCurrent(currentPage);
//        page.setSize(pageSize);
//        //2.构造userList
//        //2.1构造查询条件queryWrapper,得到userList
//        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//        queryWrapper.lambda().eq(User::getRoleFlag, "ROLE_STORE");
//        queryWrapper.lambda().eq(User::getCreateUid, currentUser.getUserId());
//        queryWrapper.lambda().like(User::getRealName,realName);
//        List<User> userList = userService.list(queryWrapper);
//
//        //2.2继续给userList赋值
////        for(User user: userList){
////            AppUser appUser = getUserFromApp(user.getUserId());
////            user.setAppNickname(StringUtils.isNotBlank( appUser.getNickname()) ? appUser.getNickname() : "");
////            user.setAppCreateTime(appUser.getCreateTime());
////            List<Store> storeList = getStoresByPartnerUid(user.getUserId());
////            user.setStoreList(storeList);
////            user.setStoreNumber(storeList.size());
////        }
//
//        //3.构造最终userListPage
//        IPage<User> userListPage = userService.page(page,queryWrapper);
//        userListPage.setRecords(userList);
//        return ResultUtils.success("查询成功",userListPage);
//    }



    /**
     * 新增
     */
    @PostMapping
    public ResultVo add(@RequestBody User user){

        //添加用户时，如果用户名已存在，即不能新增
        QueryWrapper<User> userQuery2 = new QueryWrapper<>();
        userQuery2.lambda().eq(User::getUsername, user.getUsername());
        User userTemp2 = userService.getOne(userQuery2);
        if( userTemp2 != null ){
            throw new ServiceException( Constants.CODE_604 , "此用户名已注册");
        }

        User currentUser = TokenUtils.getCurrentUser();

        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        user.setLastLoginTime(new Date());
        user.setStatus(1);
//        user.setAvatar("888");

        user.setUserId(SnowflakeIdWorker.getNextId());
        user.setCreateUid(currentUser.getUserId());
        user.setAppUserId(0L);

        user.setPassword(SecureUtil.md5(user.getPassword()));

//        private Long userId;
//        private String username;
//        private String realName;
//        private String roleFlag;
//        private String provinceCode;
//        private String provinceName;
//        private String cityCode;
//        private String cityName;
//        private String districtCode;
//        private String districtName;

//        private Integer gender;
//        private String mobile;
//        private String avatar;
//        private Integer userType;
//        private Integer status;

//        private Date lastLoginTime;
//        private Date createTime;
//        private Date updateTime;

        String currentRole = currentUser.getRoleFlag();
        if(Constants.ROLE_DEV.equals(currentRole)){               //权限：开发人员创建管理员
            user.setRoleFlag(Constants.ROLE_ADMIN);
        } else if(Constants.ROLE_ADMIN.equals(currentRole)){               //权限：管理员创建客户经理
            user.setRoleFlag(Constants.ROLE_PARTNER);

            //添加客户经理时，如果手机号码已存在，即不能新增
            QueryWrapper<User> userQuery = new QueryWrapper<>();
            userQuery.lambda().eq(User::getMobile, user.getMobile());
            User userTemp = userService.getOne(userQuery);
            if( userTemp != null ){
                throw new ServiceException( Constants.CODE_603 , "此手机号已注册");
            }

            //新增客户经理时，如果该手机号已绑定小程序用户，应该直接与小程序用户进行关联，同时为小程序用户增加客户经理角色
            QueryWrapper<AppUser> appUserQuery = new QueryWrapper<>();
            appUserQuery.lambda().eq(AppUser::getMobile, user.getMobile());
            AppUser appUserTemp = appUserService.getOne(appUserQuery);

            if( appUserTemp == null ){      //该手机号对应用户未注册小程序，请先注册小程序
                throw new ServiceException( Constants.CODE_605 , "该手机号对应用户未注册小程序，请先注册小程序");
            } else {
                //该手机号对应用户已注册小程序：后台与小程序用户进行关联 - user后台 appUserTemp小程序
                user.setAppUserId( appUserTemp.getUserId() );

                //为小程序用户增加客户经理角色
                AppUser appUserTemp2 = new AppUser();
                BeanUtil.copyProperties(appUserTemp, appUserTemp2, true);
                appUserTemp2.setUserType(8);    // 小程序：8客户经理
                appUserService.saveOrUpdate(appUserTemp2);

                // select * from user  where mobile = "13366051907"
                // select * from m_user  where app_user_id = "60384623661678592"

            }

        } else if (Constants.ROLE_PARTNER.equals(currentRole)){     //权限：客户经理创建零售户
            user.setRoleFlag(Constants.ROLE_STORE);
        }

        boolean save = userService.save(user);
        if(save){
            return ResultUtils.success("新增用户成功!");
        }
        return ResultUtils.error("新增用户失败!");
    }
    /**
     * 编辑
     */
    @PutMapping
    public ResultVo edit(@RequestBody User user){
        user.setUpdateTime(new Date());

        if(StrUtil.isNotBlank(user.getPassword())){
            user.setPassword( SecureUtil.md5(user.getPassword()) );
        }

        User one = userService.getById(user.getId());
        user.setPassword(one.getPassword());
        boolean b = userService.updateById(user);
        if(b){
            return ResultUtils.success("编辑用户成功!");
        }
        return ResultUtils.error("编辑用户失败!");
    }

    /**
     * 删除
     */
    @DeleteMapping("/{id}")
    public ResultVo delete(@PathVariable("id") Long id){
        boolean b = userService.removeById(id);
        if(b){
            return ResultUtils.success("删除用户成功!");
        }
        return ResultUtils.error("删除用户失败!");
    }

    /**
     * 列表查询
     */
    @GetMapping("/list")
    public ResultVo getList(UserParm userParm){
        //构造分页对象
        IPage<User> page = new Page<>();
        page.setCurrent(userParm.getCurrentPage());
        page.setSize(userParm.getPageSize());
        //构造查询条件
        QueryWrapper<User> query = new QueryWrapper<>();

//        query.lambda().like(SysUser::getUsername,userParm.getUsername());
        query.lambda().like(User::getUsername,userParm.getUsername());

        IPage<User> list = userService.page(page,query);
        return ResultUtils.success("查询成功",list);
    }

//    // 一对多查询：1个user多个store
//    @GetMapping("/getStoresByUserId")
//    public ResultVo getStoresByUserId(@RequestParam Long userId, @RequestParam(defaultValue = "") String name,
//                                      @RequestParam Integer pageNum, @RequestParam Integer pageSize) {
//        List<User> userList = userService.getStoresByUserId(userId, name);
//
//        //构造分页对象
//        IPage<User> page = new Page<>();
//        page.setCurrent(pageNum);
//        page.setSize(pageSize);
//        //构造查询条件
//        QueryWrapper<User> query = new QueryWrapper<>();
//        IPage<User> userPage = userService.page(page,query);
//        userPage.setRecords(userList);
//
//        return ResultUtils.success("查询成功", userPage );
//
//    }

    // 一对多查询：1个user多个store
//    @GetMapping("/getStoresByUserId")
//    public ResultVo getStoresByUserId(@RequestParam Long userId, @RequestParam(defaultValue = "") String name,
//                                      @RequestParam Integer pageNum, @RequestParam Integer pageSize) {
//        List<User> userList = userService.getStoresByUserId(userId, name);
//
//        //构造分页对象
//        IPage<User> page = new Page<>();
//        page.setCurrent(pageNum);
//        page.setSize(pageSize);
//        //构造查询条件
//        QueryWrapper<User> query = new QueryWrapper<>();
//        IPage<User> userPage = userService.page(page,query);
//        userPage.setRecords(userList);
//
//        return ResultUtils.success("查询成功", userPage );
//
//    }

    // 一对多查询：1个user多个Answers
    @GetMapping("/getAnswersCountByUserId")
    public ResultVo getAnswersCountByUserId() {

//        -- 客户经理有多少份答卷
//        -- user.user_id = 58243960581128192
//        select *  from user
//        where user_id = '58243960581128192'
//        -- user.mobile = '13581709061'
//        select * from user
//        where mobile = '13581709061'
//        -- partner.mobile = '13581709061'
//        select * from partner
//        where mobile = '13581709061'
//        -- partner.id = 3
//        select * from partner
//        where partner.id = 3
//        -- answer.partner_uid = 3
//        select * from answer
//        where partner_uid = 3
//        -- answerList

        User user = userService.getById(TokenUtils.getCurrentUser().getId());

        //构造查询条件
        QueryWrapper<Partner> query = new QueryWrapper<>();
        query.lambda().eq(Partner::getMobile, user.getMobile());
        Partner partner = partnerService.getOne(query);

        //构造查询条件
        QueryWrapper<Answer> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Answer::getPartnerUid, partner.getId());
        List<Answer> answerList = answerService.list(queryWrapper);
//        IPage page = answerService.page(new Page<>(pageNum, pageSize), queryWrapper);
        return ResultUtils.success("查询成功", answerList.size());
    }



    /**
     * excel 导入
     *
     * @param file
     * @throws Exception
     */
    @Deprecated
    @PostMapping("/import")
    public ResultVo imp(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        // 方式1：(推荐) 通过 javabean的方式读取Excel内的对象，但是要求表头必须是英文，跟javabean的属性要对应起来
//        List<User> list = reader.readAll(User.class);

        // 方式2：忽略表头的中文，直接读取表的内容
        List<List<Object>> list = reader.read(1);

//        List<User> users = CollUtil.newArrayList();

        for (List<Object> row : list) {

            String licenseNo = row.get(1).toString();
            logger.info("licenseNo: " + licenseNo);

            Boolean hasLeadingNumber = startWithNumber(row.get(5).toString());
            logger.info("hasLeadingNumber: " + hasLeadingNumber);
            String partnerName = row.get(5).toString();
            logger.info("partnerName:" + partnerName);
            if(hasLeadingNumber){
                String originString = row.get(5).toString();
                String regex="[^\\p{Punct}\\p{Space}\\p{Digit}]";
                Matcher matcher=Pattern.compile(regex).matcher(originString);
                if(matcher.find()){
                    int firstCharacterIndex=matcher.start();
                    logger.info("第一个不是数字标点或空格的位置是:"+ firstCharacterIndex);
                    String withoutLeadingNumberString = originString.substring(firstCharacterIndex);
                    logger.info("withoutLeadingNumberString:"+ withoutLeadingNumberString);
                    partnerName = withoutLeadingNumberString;
                    logger.info("partnerName:"+ partnerName);
                }
            }
            //构造查询条件 query1
            QueryWrapper<Partner> query1 = new QueryWrapper<>();
            query1.lambda().eq(Partner::getName, partnerName);
            Partner partner = partnerService.getOne(query1);
            //构造查询条件 query2
            QueryWrapper<User> query2 = new QueryWrapper<>();
            query2.lambda().eq(User::getMobile, partner.getMobile());
            User userInExcel = userService.getOne(query2);
            Integer UserIdInExcel = userInExcel.getId();

            //构造查询条件 query3 licenseNo
            QueryWrapper<Store> query3 = new QueryWrapper<>();
            query3.lambda().eq(Store::getLicenseNo, licenseNo);
            Store storeInExcel = storeService.getOne(query3);
            Long UserIdInDB = storeInExcel.getCreateUid();
            //构造查询条件 query4
            QueryWrapper<User> query4 = new QueryWrapper<>();
            query4.lambda().eq(User::getId, UserIdInDB);
            User userInDb = userService.getOne(query4);

//            User user = new User();
//            user.setId( Integer.valueOf((String) row.get(1)) );
//            user.setId(row.get(1).toString());
//            user.setUsername(row.get(5).toString());

//            System.out.println("userId" + user.getId());
//            logger.info("user", user);

//            users.add(user);
        }

//        userService.saveBatch(users);
        return ResultUtils.success("导入成功");
    }

    @Deprecated
    public boolean startWithNumber(String str){
        return Pattern.matches("[0-9].*", str);
    }

}
