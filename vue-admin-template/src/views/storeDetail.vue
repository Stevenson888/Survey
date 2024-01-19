<template>
  <el-main>
    <div>
<!--    <el-form :model="params" ref="searchForm" label-width="200px" :inline="true" size="small">-->
<!--      <el-form-item label="门店所属客户经理姓名搜索">-->
<!--        <el-input v-model="params.realName"></el-input>-->
<!--      </el-form-item>-->
<!--      <el-form-item>-->
<!--        <el-button icon="el-icon-search" @click="searchBtn">搜索</el-button>-->
<!--        <el-button style="color: #ff7670" @click="reset" icon="el-icon-close">重置</el-button>-->
<!--        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增</el-button>-->
<!--      </el-form-item>-->
<!--    </el-form>-->

<!--    <div style="color: #000000; font-size: large; font-weight: 600; margin-bottom:20px">-->
<!--      <span>{{ user.roleFlag }}</span>-->
<!--      <span style="color: #FF7670;">  {{ user.realName }}  </span>-->
<!--      <span>的门店列表：</span>-->
<!--    </div>-->

    <div style="color: #000000; font-size: large; font-weight: 600; margin-bottom:20px">
<!--      <span>StoreDetail页 - StoreId: {{ this.storeId }}</span>-->
    </div>

    <div class="table">

      <!--table 1-->
      <div>
          <!--r0-->
          <el-row>
            <el-col :span="24">
              <div class="grid-content bg-purple-dark title">
                门店基础信息
              </div>
            </el-col>
          </el-row>
          <!--r1-->
          <el-row >
            <el-col :span="6"><div class="header">专卖证号</div></el-col>
            <el-col :span="6"><div class="body">{{store.licenseNo}}</div></el-col>
            <el-col :span="6"><div class="header">门店档位</div></el-col>
            <el-col :span="6"><div class="body">{{store.tradeLevel}}</div></el-col>
          </el-row>
          <!--r2-->
          <el-row>
            <el-col :span="6"><div class="header">所在省/直辖</div></el-col>
            <el-col :span="6"><div class="body">{{store.provinceName}}</div></el-col>
            <el-col :span="6"><div class="header">所在地市/区</div></el-col>
            <el-col :span="6"><div class="body">{{store.cityName}}/{{store.districtName}}</div></el-col>
          </el-row>
          <!--r3-->
          <el-row>
            <el-col :span="6"><div class="header">门店名称</div></el-col>
            <el-col :span="6"><div class="body">{{store.name}}</div></el-col>
            <el-col :span="6"><div class="header">门店地址</div></el-col>
            <el-col :span="6"><div class="body">{{store.detailAddress}}</div></el-col>
          </el-row>
          <!--r4-->
          <el-row>
            <el-col :span="6"><div class="header">店主姓名</div></el-col>
            <el-col :span="6"><div class="body">{{ storeOwner!=null ? storeOwner.realName : ""}}</div></el-col>
            <el-col :span="6"><div class="header">门店电话</div></el-col>
            <el-col :span="6"><div class="body">{{storeOwner!=null ? storeOwner.mobile : ""}}</div></el-col>
          </el-row>
      </div>

      <!--table 2-->
      <div style="">
          <!--r0-->
          <el-row>
            <el-col :span="24">
              <div class="grid-content bg-purple-dark title">
                享问礼小程序信息
              </div>
            </el-col>
          </el-row>
          <!--r1-->
          <el-row>
            <el-col :span="6"><div class="header">小程序认证状态</div></el-col>
            <el-col :span="6"  style="text-align:center;">

<!--              <el-tag type="danger" effect="dark" size="medium" v-if="storeOwner==null"><div style="height: 3px">未认证</div></el-tag>-->
<!--              <el-tag type="danger" effect="dark" size="medium" v-else-if="storeOwner.userId==0"><div style="height: 3px">未认证</div></el-tag>-->
<!--              <el-tag type="success" effect="dark" size="medium" v-else="storeOwner.userId!=0"><div style="height: 3px">已认证</div></el-tag>-->

              <el-tag type="danger" effect="dark" size="medium" v-if="store.status===0"><div style="height: 3px">未认证</div></el-tag>
              <el-tag type="success" effect="dark" size="medium" v-if="store.status===1"><div style="height: 3px">已认证</div></el-tag>
<!--                <el-tag type="danger" v-if="status === 0">未认证</el-tag>-->
<!--                <el-tag type="success" v-if="status === 1">已认证</el-tag>-->
            </el-col>
            <el-col :span="6"><div class="header">归属客户经理</div></el-col>
            <el-col :span="6"><div class="body">{{ createPartner!=null?createPartner.realName:""}}</div></el-col>
          </el-row>
          <!--r2-->
          <el-row>
            <el-col :span="6"><div class="header">小程序认证时间</div></el-col>
            <el-col :span="6"><div class="body">{{storeOwner!=null?storeOwner.createTime:""}}</div></el-col>
            <el-col :span="6"><div class="header">用户ID</div></el-col>
<!--            <el-col :span="6"><div class="body">{{storeOwner!=null?storeOwner.userId:""}}</div></el-col>-->
            <el-col :span="6"><div class="body">{{store.userId}}</div></el-col>
          </el-row>
          <!--r3-->
          <el-row>
            <el-col :span="6"><div class="header">认证微信昵称</div></el-col>
            <el-col :span="6"><div class="body">{{storeOwner!=null?storeOwner.nickname:""}}</div></el-col>
            <el-col :span="6"><div class="header">微信账号电话</div></el-col>
            <el-col :span="6"><div class="body">{{storeOwner!=null?storeOwner.mobile:""}}</div></el-col>
          </el-row>
      </div>

      <!--table 3-->
      <div style="">
        <!--r0-->
        <el-row>
          <el-col :span="24">
            <div class=" title"  >
<!--                <el-button type="danger" @click="deleteAuth" disabled="storeOwner || this.readOnly">删除当前微信号与门店的关联关系</el-button>-->
                <el-button type="danger" @click="deleteAuth" :disabled="readOnly||!storeOwner">删除当前微信号与门店的关联关系</el-button>
            </div>
          </el-col>
        </el-row>
      </div>

      <!--table 4-->
      <div style="">
          <!--r0-->
          <el-row>
            <el-col :span="24">
              <div class="grid-content bg-purple-dark title">
                问卷完成情况
              </div>
            </el-col>
          </el-row>
          <!--r1-->
          <el-row>
            <el-col :span="6"><div class="header">可参与门店问卷</div></el-col>
            <el-col :span="6"><div class="body">{{0}}</div></el-col>
            <el-col :span="6"><div class="header">已完成门店问卷</div></el-col>
            <el-col :span="6"><div class="body">{{store.finishedStoreAnswer}}</div></el-col>
          </el-row>
          <!--r2-->
          <el-row>
            <el-col :span="6"><div class="header">门店关联消费者</div></el-col>
            <el-col :span="6"><div class="body">{{store.customerAmountOfStore}}</div></el-col>
            <el-col :span="6"><div class="header">完成消费者问卷</div></el-col>
            <el-col :span="6"><div class="body">{{store.finishedCustomerAnswer}}</div></el-col>
          </el-row>

      </div>
    </div>

    <div style="padding-top:50px; text-align: center">

<!--      <el-table-column prop="createUserName" label="归属客户经理" v-if="user.roleFlag === 'ROLE_ADMIN'">-->
<!--        <template slot-scope="scope" >-->
<!--          <router-link :to="{ path:'/stores',query: { realName: scope.row.createUserName } }"> {{scope.row.createUserName}} </router-link>-->
<!--        </template>-->
<!--      </el-table-column>-->

<!--      <router-link :to="{ path:'/stores', query: { userId: this.createPartner.userId } }"> 返回 </router-link>-->

<!--      <el-button type="primary" @click="$router.push({name:'/stores', params:{ userId: this.createPartner.userId } })"> 返回 </el-button>-->

      <el-button type="primary" @click="returnStoresPage()">返回</el-button>
    </div>


    <!--    <el-table :data="tableList" border stripe :header-cell-class-name="'headerBg'"  @selection-change="handleSelectionChange">-->
<!--&lt;!&ndash;    <el-table :data="store" border stripe :header-cell-class-name="'headerBg'"  @selection-change="handleSelectionChange">&ndash;&gt;-->
<!--      <el-table-column type="selection" width="55"></el-table-column>-->
<!--&lt;!&ndash;      <el-table-column prop="id" label="ID" width="80" sortable></el-table-column>&ndash;&gt;-->
<!--      <el-table-column prop="licenseNo" label="专卖证号"></el-table-column>-->
<!--&lt;!&ndash;      <el-table-column prop="roleFlag" label="角色标识" width=120></el-table-column>&ndash;&gt;-->
<!--            <el-table-column prop="storeId" label="门店id(FT)" width=160 sortable></el-table-column>-->
<!--      <el-table-column prop="name" label="门店名称" width=180></el-table-column>-->
<!--      &lt;!&ndash;      <el-table-column prop="provinceCode" label="省份编号"></el-table-column>&ndash;&gt;-->
<!--      <el-table-column prop="provinceName" label="省份名称"></el-table-column>-->
<!--      &lt;!&ndash;      <el-table-column prop="cityCode" label="城市编号"></el-table-column>&ndash;&gt;-->
<!--      <el-table-column prop="cityName" label="城市名称"></el-table-column>-->
<!--      &lt;!&ndash;      <el-table-column prop="districtCode" label="区域编号"></el-table-column>&ndash;&gt;-->
<!--      <el-table-column prop="districtName" label="区域名称"></el-table-column>-->
<!--      &lt;!&ndash;      <el-table-column prop="detailAddress" label="详细地址"></el-table-column>&ndash;&gt;-->
<!--&lt;!&ndash;      <el-table-column prop="status" label="小程序认证状态"></el-table-column>&ndash;&gt;-->
<!--      <el-table-column prop="status" label="小程序认证状态">-->
<!--        <template v-slot="scope">-->
<!--          <el-tag type="danger" v-if="scope.row.status === 0">未认证</el-tag>-->
<!--          <el-tag type="success" v-if="scope.row.status === 1">已认证</el-tag>-->
<!--        </template>-->
<!--      </el-table-column>-->
<!--&lt;!&ndash;      <el-table-column prop="answerTotalAmount" label="完成问卷数量"></el-table-column>&ndash;&gt;-->
<!--&lt;!&ndash;      <el-table-column prop="answerTotalAmount" label="完成问卷数量" @click="$router.push('/login')"></el-table-column>&ndash;&gt;-->
<!--&lt;!&ndash;      <el-table-column prop="answerTotalAmount" label="完成问卷数量"  @click="handleEdit(scope.row)" ></el-table-column>&ndash;&gt;-->

<!--      <el-table-column prop="answerTotalAmount" label="完成问卷数量" width=“120”>-->
<!--        <template slot-scope="scope">-->
<!--&lt;!&ndash; 链接跳转         <router-link v-bind:to="'/problem/'+scope.row.id">{{scope.row.id}}</router-link>&ndash;&gt;-->
<!--&lt;!&ndash;          <router-link @click="checkAnswerDetail(scope.row)"></router-link>&ndash;&gt;-->
<!--          <el-button type="text" @click="checkAnswerDetail(scope.row)" >{{ scope.row.answerTotalAmount }}</el-button>-->
<!--        </template>-->
<!--      </el-table-column>-->

<!--      &lt;!&ndash;          <router-link to="'/partnerusers/' +  scope.row.id" >{{ scope.row.createUserName }}</router-link>&ndash;&gt;-->
<!--      &lt;!&ndash;          <router-link v-bind:to="'/problem/'+scope.row.id">{{scope.row.id}}</router-link>&ndash;&gt;-->
<!--      &lt;!&ndash;          <router-link @click="checkAnswerDetail(scope.row)"></router-link>&ndash;&gt;-->
<!--      &lt;!&ndash;          <el-link href="partnerusers" >默认链接</el-link>&ndash;&gt;-->
<!--      &lt;!&ndash;          <el-button type="text" @click="checkAnswerDetail(scope.row)" >{{ scope.row.answerTotalAmount }}</el-button>&ndash;&gt;-->
<!--      <el-table-column prop="createUserName" label="归属客户经理" v-if="user.roleFlag === 'ROLE_ADMIN'">-->
<!--        <template slot-scope="scope" >-->
<!--          &lt;!&ndash;  /partnerusers?realName=xxx  &ndash;&gt;-->
<!--          <router-link :to="{path:'/partnerusers',query: { realName: scope.row.createUserName }}">{{scope.row.createUserName}}</router-link>-->
<!--        </template>-->
<!--      </el-table-column>-->
<!--      <el-table-column prop="createUserName" label="归属客户经理" v-if="user.roleFlag !== 'ROLE_ADMIN'"></el-table-column>-->

<!--      <el-table-column prop="createUid" label="创建者(FT)" width="200px"></el-table-column>-->

<!--      &lt;!&ndash;      <el-table-column prop="id" label="ID" width="80" sortable></el-table-column>&ndash;&gt;-->
<!--      &lt;!&ndash;      <el-table-column prop="storeId" label="门店id"></el-table-column>&ndash;&gt;-->
<!--      &lt;!&ndash;      <el-table-column prop="userId" label="用户id"></el-table-column>&ndash;&gt;-->
<!--      &lt;!&ndash;      <el-table-column prop="name" label="门店名称"></el-table-column>&ndash;&gt;-->
<!--&lt;!&ndash;      <el-table-column prop="logo" label="门店logo"></el-table-column>&ndash;&gt;-->
<!--&lt;!&ndash;      <el-table-column prop="telephone" label="电话号码"></el-table-column>&ndash;&gt;-->
<!--&lt;!&ndash;      <el-table-column prop="businessHours" label="营业时间"></el-table-column>&ndash;&gt;-->
<!--&lt;!&ndash;      <el-table-column prop="level" label="门店等级"></el-table-column>&ndash;&gt;-->
<!--&lt;!&ndash;      <el-table-column prop="tradeLevel" label="行业等级"></el-table-column>&ndash;&gt;-->
<!--&lt;!&ndash;      <el-table-column prop="starLevel" label="门店星级0-10，共5个满星"></el-table-column>&ndash;&gt;-->
<!--&lt;!&ndash;      <el-table-column prop="provinceCode" label="省份编号"></el-table-column>&ndash;&gt;-->
<!--&lt;!&ndash;      <el-table-column prop="provinceName" label="省份名称"></el-table-column>&ndash;&gt;-->
<!--&lt;!&ndash;      <el-table-column prop="cityCode" label="城市编号"></el-table-column>&ndash;&gt;-->
<!--&lt;!&ndash;      <el-table-column prop="cityName" label="城市名称"></el-table-column>&ndash;&gt;-->
<!--&lt;!&ndash;      <el-table-column prop="districtCode" label="区域编号"></el-table-column>&ndash;&gt;-->
<!--&lt;!&ndash;      <el-table-column prop="districtName" label="区域名称"></el-table-column>&ndash;&gt;-->
<!--&lt;!&ndash;      <el-table-column prop="detailAddress" label="详细地址"></el-table-column>&ndash;&gt;-->
<!--&lt;!&ndash;      <el-table-column prop="longitude" label="经度"></el-table-column>&ndash;&gt;-->
<!--&lt;!&ndash;      <el-table-column prop="latitude" label="纬度"></el-table-column>&ndash;&gt;-->
<!--&lt;!&ndash;      <el-table-column prop="geoPoint" label=""></el-table-column>&ndash;&gt;-->
<!--&lt;!&ndash;      <el-table-column prop="location" label="定位位置"></el-table-column>&ndash;&gt;-->
<!--&lt;!&ndash;      <el-table-column prop="status" label="门店状态 1正常"></el-table-column>&ndash;&gt;-->
<!--&lt;!&ndash;      <el-table-column prop="createUid" label="创建人id"></el-table-column>&ndash;&gt;-->
<!--&lt;!&ndash;      <el-table-column prop="createTime" label="创建时间"></el-table-column>&ndash;&gt;-->
<!--&lt;!&ndash;      <el-table-column prop="updateTime" label="最后修改时间"></el-table-column>&ndash;&gt;-->
<!--&lt;!&ndash;      <el-table-column prop="isEnable" label="是否启用,启用后可被消费者看到"></el-table-column>&ndash;&gt;-->
<!--&lt;!&ndash;      <el-table-column prop="isDelete" label="是否删除"></el-table-column>&ndash;&gt;-->

<!--&lt;!&ndash;      <el-table-column label="操作"  width="180" align="center">&ndash;&gt;-->
<!--&lt;!&ndash;        <template slot-scope="scope">&ndash;&gt;-->
<!--&lt;!&ndash;          <el-button type="success" @click="handleEdit(scope.row)">编辑 <i class="el-icon-edit"></i></el-button>&ndash;&gt;-->
<!--&lt;!&ndash;          <el-popconfirm&ndash;&gt;-->
<!--&lt;!&ndash;              class="ml-5"&ndash;&gt;-->
<!--&lt;!&ndash;              confirm-button-text='确定'&ndash;&gt;-->
<!--&lt;!&ndash;              cancel-button-text='我再想想'&ndash;&gt;-->
<!--&lt;!&ndash;              icon="el-icon-info"&ndash;&gt;-->
<!--&lt;!&ndash;              icon-color="red"&ndash;&gt;-->
<!--&lt;!&ndash;              title="您确定删除吗？"&ndash;&gt;-->
<!--&lt;!&ndash;              @confirm="del(scope.row.id)"&ndash;&gt;-->
<!--&lt;!&ndash;          >&ndash;&gt;-->
<!--&lt;!&ndash;            <el-button type="danger" slot="reference">删除 <i class="el-icon-remove-outline"></i></el-button>&ndash;&gt;-->
<!--&lt;!&ndash;          </el-popconfirm>&ndash;&gt;-->
<!--&lt;!&ndash;        </template>&ndash;&gt;-->
<!--&lt;!&ndash;      </el-table-column>&ndash;&gt;-->

<!--      <el-table-column label="操作"  align="left" width="400px">-->
<!--        <template slot-scope="scope">-->
<!--          <el-button type="success" @click="handleEdit(scope.row)">编辑 <i class="el-icon-edit"></i></el-button>-->
<!--          <el-button type="danger" @click="del(scope.row.id)">删除 <i class="el-icon-delete"></i></el-button>-->
<!--        </template>-->
<!--      </el-table-column>-->

<!--    </el-table>-->

<!--    <div style="padding: 10px 0">-->
<!--      <el-pagination-->
<!--          @size-change="handleSizeChange"-->
<!--          @current-change="handleCurrentChange"-->
<!--          :current-page="currentPage"-->
<!--          :page-sizes="[2, 5, 10, 20]"-->
<!--          :page-size="pageSize"-->
<!--          layout="total, sizes, prev, pager, next, jumper"-->
<!--          :total="total"
              background>-->
<!--      </el-pagination>-->
<!--    </div>-->

<!--    <el-dialog title="信息" :visible.sync="dialogFormVisible" width="40%" :close-on-click-modal="false">-->
<!--      <el-form label-width="120px" size="small" style="width: 80%; margin: 0 auto">-->
<!--        <el-form-item label="门店id">-->
<!--          <el-input v-model="form.storeId" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="用户id">-->
<!--          <el-input v-model="form.userId" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="门店名称">-->
<!--          <el-input v-model="form.name" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="专卖证号">-->
<!--          <el-input v-model="form.licenseNo" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="门店logo">-->
<!--          <el-input v-model="form.logo" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="电话号码">-->
<!--          <el-input v-model="form.telephone" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="营业时间">-->
<!--          <el-input v-model="form.businessHours" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="门店等级">-->
<!--          <el-input v-model="form.level" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="行业等级">-->
<!--          <el-input v-model="form.tradeLevel" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="门店星级0-10，共5个满星">-->
<!--          <el-input v-model="form.starLevel" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="省份编号">-->
<!--          <el-input v-model="form.provinceCode" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="省份名称">-->
<!--          <el-input v-model="form.provinceName" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="城市编号">-->
<!--          <el-input v-model="form.cityCode" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="城市名称">-->
<!--          <el-input v-model="form.cityName" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="区域编号">-->
<!--          <el-input v-model="form.districtCode" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="区域名称">-->
<!--          <el-input v-model="form.districtName" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="详细地址">-->
<!--          <el-input v-model="form.detailAddress" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="经度">-->
<!--          <el-input v-model="form.longitude" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="纬度">-->
<!--          <el-input v-model="form.latitude" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="">-->
<!--          <el-input v-model="form.geoPoint" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="定位位置">-->
<!--          <el-input v-model="form.location" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="门店状态 1正常">-->
<!--          <el-input v-model="form.status" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="创建人id">-->
<!--          <el-input v-model="form.createUid" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="创建时间">-->
<!--          <el-date-picker v-model="form.createTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="选择日期时间"></el-date-picker>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="最后修改时间">-->
<!--          <el-date-picker v-model="form.updateTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="选择日期时间"></el-date-picker>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="是否启用,启用后可被消费者看到">-->
<!--          <el-input v-model="form.isEnable" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="是否删除">-->
<!--          <el-input v-model="form.isDelete" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->

<!--      </el-form>-->
<!--      <div slot="footer" class="dialog-footer">-->
<!--        <el-button @click="dialogFormVisible = false">取 消</el-button>-->
<!--        <el-button type="primary" @click="save">确 定</el-button>-->
<!--      </div>-->
<!--    </el-dialog>-->

<!--    <el-dialog title="信息" :visible.sync="dialogFormVisible" width="40%" :close-on-click-modal="false">-->
<!--      <el-form label-width="120px" size="small" style="width: 80%; margin: 0 auto">-->
<!--&lt;!&ndash;        <el-form-item label="用户id">&ndash;&gt;-->
<!--&lt;!&ndash;          <el-input v-model="form.id" autocomplete="off" disabled></el-input>&ndash;&gt;-->
<!--&lt;!&ndash;        </el-form-item>&ndash;&gt;-->
<!--&lt;!&ndash;        <el-form-item label="用户实名">&ndash;&gt;-->
<!--&lt;!&ndash;          <el-input v-model="form.realName" autocomplete="off" disabled ></el-input>&ndash;&gt;-->
<!--&lt;!&ndash;        </el-form-item>&ndash;&gt;-->
<!--&lt;!&ndash;        <el-form-item label="用户角色">&ndash;&gt;-->
<!--&lt;!&ndash;          <el-input v-model="form.roleFlag" autocomplete="off" disabled ></el-input>&ndash;&gt;-->
<!--&lt;!&ndash;        </el-form-item>&ndash;&gt;-->
<!--&lt;!&ndash;        <el-form-item label="门店id">&ndash;&gt;-->
<!--&lt;!&ndash;          <el-input v-model="form.storeId" autocomplete="off"></el-input>&ndash;&gt;-->
<!--&lt;!&ndash;        </el-form-item>&ndash;&gt;-->
<!--        <el-form-item label="专卖证号">-->
<!--          <el-input v-model="form.licenseNo" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="门店名称">-->
<!--          <el-input v-model="form.name" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="省份编号">-->
<!--          <el-input v-model="form.provinceCode" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="省份名称">-->
<!--          <el-input v-model="form.provinceName" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="城市编号">-->
<!--          <el-input v-model="form.cityCode" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="城市名称">-->
<!--          <el-input v-model="form.cityName" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="区域编号">-->
<!--          <el-input v-model="form.districtCode" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="区域名称">-->
<!--          <el-input v-model="form.districtName" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="详细地址">-->
<!--          <el-input v-model="form.detailAddress" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->

<!--      </el-form>-->
<!--      <div slot="footer" class="dialog-footer">-->
<!--        <el-button @click="dialogFormVisible = false">取 消</el-button>-->
<!--        <el-button type="primary" @click="save">确 定</el-button>-->
<!--      </div>-->
<!--    </el-dialog>-->


<!--    <el-dialog title="答卷细节" :visible.sync="answerDetaildialogFormVisible" width="40%" :close-on-click-modal="false">-->
<!--      <el-form label-width="150px" size="small" style="width: 80%; margin: 0 auto">-->
<!--          <el-form-item label="零售门店店名">-->
<!--            <el-input v-model="form.name" autocomplete="off" disabled></el-input>-->
<!--          </el-form-item>-->
<!--            <el-form-item label="零售门店答卷数量">-->
<!--              <el-input v-model="form.answerAmountOfStore" autocomplete="off" disabled></el-input>-->
<!--            </el-form-item>-->
<!--            <el-form-item label="消费者答卷数量">-->
<!--              <el-input v-model="form.answerAmountOfUser" autocomplete="off" disabled ></el-input>-->
<!--            </el-form-item>-->
<!--            <el-form-item label="总答卷数量">-->
<!--              <el-input v-model="form.answerTotalAmount" autocomplete="off" disabled ></el-input>-->
<!--            </el-form-item>-->
<!--      </el-form>-->
<!--      <div slot="footer" class="dialog-footer">-->
<!--        <el-button @click="answerDetaildialogFormVisible = false">取 消</el-button>-->
<!--        <el-button type="primary" @click="save">确 定</el-button>-->
<!--      </div>-->
<!--    </el-dialog>-->
  </div>

  </el-main>

</template>

<script>
import {getStoreListApi, getStoreListPageApi, addStoreApi, editStoreApi, deleteStoreApi, deleteBatchStoreApi} from "@/api/storeshop";

import { getStoreListByAdminApi, getStoreListByPartnerApi, getStoreByIdApi, deleteAuthByStoreId } from "@/api/storeshop";
// import { getStoresByUserId} from "@/api/user";


export default {
  name: "StoreDetail",
  data() {
    return {
      storeId: "",
      param: "",

      store: {},
      // user:{},
      storeOwner:{},
      createPartner:{},

      id: "",
      userId: "",
      name: "",
      realName: "",
      createUserName: "",
      mobile: "",

      currentPage: 1,
      pageSize: 10,
      // params: {
      //   userId: 0,
      //   name: "",
      //   realName: "",
      //   currentPage: 1, //从第几页开始
      //   pageSize: 10, //每页查询的条数
      // },
      total: 0,
      tableList: [],
      form: {},

      readOnly:false,

      dialogFormVisible: false,
      answerDetaildialogFormVisible: false,
      multipleSelection: [],

      // user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      // currentUserId: this.user.id,
      // currentUserRealName: this.user.realName,
      // currentUserRoleFlag: this.user.roleFlag,

      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}
    }
  },
  created() {
    this.user = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}

    // console.log("666666 ===StoreDetail=== this.$route", this.$route)
    console.log("666666 ===StoreDetail=== this.$route.params.storeId", this.$route.params.storeId)

    if(this.$route.params.storeId){
      this.storeId = this.$route.params.storeId;
    }
    console.log("666666 ===StoreDetail=== this.storeId", this.storeId)

    this.load()
  },
  // mounted() {
  //   this.$nextTick(() => {
  //     this.params.id = this.user.id
  //   });
  //   console.log("666666 ===stores=== this.user.id ", this.user.id)
  // },

  methods: {
    load() {
      this.getStore()
    },
    //搜索按钮
    // searchBtn() {
    //   this.getStore();
    // },
    async getStore() {
      // this.params = {
      //     // userId: this.user.userId,
      //     // userId: "41187250859409408",
      //     name: this.params.name,
      //     realName: this.params.realName,
      //     currentPage: this.params.currentPage,
      //     pageSize: this.params.pageSize,
      // }
      // console.log("888888 ===stores=== this.user ", this.user)
      // console.log("888888 ===stores=== this.user.roleFlag ", this.user.roleFlag)

      let res = await getStoreByIdApi(this.storeId);

      // let res = null;
      // if (this.user.roleFlag == "ROLE_ADMIN"){
      //   res = await getStoreListByAdminApi(this.params);
      // } else if (this.user.roleFlag == "ROLE_PARTNER") {
      //   res = await getStoreListByPartnerApi(this.params);
      // }

      if (res && res.data && res.code == 200) {
        console.log("888888 ===stores=== res.data ", res.data)

        this.store = res.data
        this.storeOwner = res.data.storeOwner
        this.createPartner = res.data.createPartner

        // console.log("888888 ===stores=== this.store ", this.store)
        // console.log("888888 ===stores=== this.store.storeOwner ", this.store.storeOwner)

      }
    },

    returnStoresPage() {
      // this.$router.push({ name: '零售门店' , params: {userId: this.createPartner.userId} });
      this.$router.push({ name: '零售门店' , query: {userId: this.createPartner.userId} });

    },

    async deleteAuth() {
      //信息确认提示
      let confirm = await this.$myconfirm('确定解除认证关系吗?')
      console.log(confirm)
      if(confirm){
          let res = await deleteAuthByStoreId(this.storeId);
          console.log("888888 ===storeDetail deleteAuth this.storeId ===  " , this.storeId )

          if (res.code === 200 ) {
            this.$message.success("解除认证关系成功!")
            this.readOnly = true
            this.load()
          } else {
            this.$message.error("解除认证关系失败!")
          }
      }
    },

    // save() {
    //   this.request.post("/store", this.form).then(res => {
    //     if (res.code === '200') {
    //       this.$message.success("保存成功")
    //       this.dialogFormVisible = false
    //       this.load()
    //     } else {
    //       this.$message.error("保存失败")
    //     }
    //   })
    // },
    async save() {
      let res = await addStoreApi(this.form);

      if (res.code === 200 ) {
        this.$message.success("保存成功")
        this.dialogFormVisible = false
        this.load()
      } else {
        this.$message.error("保存失败")
      }
    },

    handleAdd() {
      this.dialogFormVisible = true
      this.form = {}
      this.$nextTick(() => {
        if(this.$refs.img) {
           this.$refs.img.clearFiles();
         }
         if(this.$refs.file) {
          this.$refs.file.clearFiles();
         }
      })
    },
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogFormVisible = true
       this.$nextTick(() => {
         if(this.$refs.img) {
           this.$refs.img.clearFiles();
         }
         if(this.$refs.file) {
          this.$refs.file.clearFiles();
         }
       })
    },
    checkAnswerDetail(row) {
      // console.log("000000000 checkAnswerDetail")
      this.form = JSON.parse(JSON.stringify(row))
      this.answerDetaildialogFormVisible = true
      this.$nextTick(() => {
        if(this.$refs.img) {
          this.$refs.img.clearFiles();
        }
        if(this.$refs.file) {
          this.$refs.file.clearFiles();
        }
      })
    },
    del(id) {
      this.request.delete("/store/" + id).then(res => {
        if (res.code === '200') {
          this.$message.success("删除成功")
          this.load()
        } else {
          this.$message.error("删除失败")
        }
      })
    },

    handleSelectionChange(val) {
      console.log(val)
      this.multipleSelection = val
    },
    delBatch() {
      if (!this.multipleSelection.length) {
        this.$message.error("请选择需要删除的数据")
        return
      }
      let ids = this.multipleSelection.map(v => v.id)  // [{}, {}, {}] => [1,2,3]
      this.request.post("/store/del/batch", ids).then(res => {
        if (res.code === '200') {
          this.$message.success("批量删除成功")
          this.load()
        } else {
          this.$message.error("批量删除失败")
        }
      })
    },
    // reset() {
    //   this.params.realName = ""
    //   this.load()
    // },
    handleSizeChange(pageSize) {
      console.log(pageSize)
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(currentPage) {
      console.log(currentPage)
      this.currentPage = currentPage
      this.load()
    },
    handleFileUploadSuccess(res) {
      this.form.file = res
    },
    handleImgUploadSuccess(res) {
      this.form.img = res
    },
    download(url) {
      window.open(url)
    },
    exp() {
    },
    handleExcelImportSuccess() {
      this.$message.success("导入成功")
      this.load()
    }
  }
}
</script>


<style  lang="scss" scoped>
.headerBg {
  background: #eee!important;
}

.headerBg {
  background: #eee!important;
}

a{
  text-decoration: underline;
  color: blue;
}

.el-row{
  border-bottom : 1px solid #5d5a5a;
  border-top : 1px solid #5d5a5a;
  height:50px;
}
.el-col {
  border-left: 1px solid #5d5a5a;
  border-right: 1px solid #5d5a5a;
  height:50px;
}
.header {
  background-color: lightgrey;
  text-align: center;
  height:50px;
  font-weight: bolder;
}
.body {
  font-weight: lighter;
  height:50px;
  text-align: center;
}
.table {
  font-weight: lighter;
  font-size: 1em;
}
.title {
  text-align: center;
  font-size: 1.2em;
  height:50px;
}


//.el-row {
//  margin-bottom: 20px;
//  &:last-child {
//    margin-bottom: 0;
//  }
//}
//.el-col {
//  border-radius: 4px;
//}

.bg-purple-dark {
  background: #99a9bf;
}
.bg-purple {
  background: #d3dce6;
}
.bg-purple-light {
  background: #e5e9f2;
}
.grid-content {
  border-radius: 4px;
  min-height: 36px;
}
.row-bg {
  padding: 10px 0;
  background-color: #f9fafc;
}
</style>
