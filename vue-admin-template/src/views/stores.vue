<template>
  <el-main>
  <div>

    <el-form :model="parms" ref="searchForm" label-width="100px" :inline="true" size="small">
      <el-form-item label="搜索店名">
        <el-input v-model="parms.name" style="width: 200px"></el-input>
      </el-form-item>
      <el-form-item label="搜索专卖证号">
        <el-input v-model="parms.licenseNo" style="width: 200px"></el-input>
      </el-form-item>
<!--      <el-form-item label="搜索客户经理">-->
<!--      <el-input v-model="parms.realName"></el-input>-->
<!--      </el-form-item>-->
      <el-form-item>
        <el-button icon="el-icon-search" @click="searchBtn">搜索</el-button>
        <el-button style="color: #ff7670" @click="reset" icon="el-icon-close">重置</el-button>
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd"  v-if="user.roleFlag === 'ROLE_ADMIN' || user.roleFlag === 'ROLE_PARTNER'">新增</el-button>

<!--        action="http://localhost:8089/api/store/importStoreList" -->
<!--        action="http://43.140.195.12:8089/api/store/importStoreList" -->
<!--        action="https://adminapi.xiangwenli.com/api/store/importStoreList"-->
        <el-upload
          action="http://localhost:8089/api/store/importStoreList"
          accept=".png,.jpg,.jpeg,.doc,.docx,.txt,.xls,.xlsx"
          :show-file-list="false"
          :on-success="importStoreList"
          :headers=headers
          style="display: inline-block;  margin-left: 50px"
          v-if="user.roleFlag === 'ROLE_DEV' || user.roleFlag === 'ROLE_ADMIN'"
        >
          <el-button type="warning" icon="el-icon-top" size="small">导入门店信息 </el-button>
        </el-upload>
        <el-button icon="el-icon-bottom" type="warning" style="display: inline-block;  margin-left: 10px" @click="exportStoreList" v-if="user.roleFlag === 'ROLE_DEV' || user.roleFlag === 'ROLE_ADMIN'">导出门店查询结果</el-button>
        <el-button icon="el-icon-bottom" type="warning" style="display: inline-block;  margin-left: 10px" @click="exportStoreHeadList" v-if="user.roleFlag === 'ROLE_DEV' || user.roleFlag === 'ROLE_ADMIN'">下载门店信息模板</el-button>
      </el-form-item>
    </el-form>

<!--    <div style="color: #000000; font-size: large; font-weight: 600; margin-bottom:20px">-->
<!--      <span>{{ user.roleFlag }}</span>-->
<!--      <span style="color: #FF7670;">  {{ user.realName }}  </span>-->
<!--      <span>的门店列表：</span>-->
<!--    </div>-->

    <el-table :data="tableList" :height="tableHeight" border stripe :header-cell-class-name="'headerBg'"  @selection-change="handleSelectionChange">
<!--      <el-table-column type="selection" width="55"></el-table-column>-->
<!--      <el-table-column prop="id" label="ID" width="80" sortable></el-table-column>-->
      <el-table-column prop="licenseNo" label="专卖证号"></el-table-column>
<!--      <el-table-column prop="roleFlag" label="角色标识" width=120></el-table-column>-->
<!--            <el-table-column prop="storeId" label="门店id(FT)" width=160 sortable></el-table-column>-->
      <el-table-column prop="name" label="门店名称" width=180></el-table-column>
      <!--      <el-table-column prop="provinceCode" label="省份编号"></el-table-column>-->
      <el-table-column prop="provinceName" label="省份名称"></el-table-column>
      <!--      <el-table-column prop="cityCode" label="城市编号"></el-table-column>-->
<!--      <el-table-column prop="cityName" label="城市名称"></el-table-column>-->
      <!--      <el-table-column prop="districtCode" label="区域编号"></el-table-column>-->
      <el-table-column prop="districtName" label="区域名称"></el-table-column>
      <!--      <el-table-column prop="detailAddress" label="详细地址"></el-table-column>-->
<!--      <el-table-column prop="status" label="小程序认证状态"></el-table-column>-->
      <el-table-column prop="status" label="小程序认证状态">
        <template v-slot="scope">
          <el-tag type="danger" v-if="scope.row.status === 0">未认证</el-tag>
          <el-tag type="success" v-if="scope.row.status === 1">已认证</el-tag>
        </template>
      </el-table-column>
<!--      <el-table-column prop="answerTotalAmount" label="完成问卷数量"></el-table-column>-->
<!--      <el-table-column prop="answerTotalAmount" label="完成问卷数量" @click="$router.push('/login')"></el-table-column>-->
<!--      <el-table-column prop="answerTotalAmount" label="完成问卷数量"  @click="handleEdit(scope.row)" ></el-table-column>-->

      <el-table-column prop="answerTotalAmount" label="完成问卷数量" width=“120”>
        <template slot-scope="scope">
<!-- 链接跳转         <router-link v-bind:to="'/problem/'+scope.row.id">{{scope.row.id}}</router-link>-->
<!--          <router-link @click="checkAnswerDetail(scope.row)"></router-link>-->
          <el-button type="text" @click="checkAnswerDetail(scope.row)" >{{ scope.row.answerTotalAmount }}</el-button>
        </template>
      </el-table-column>

      <!--          <router-link to="'/partnerusers/' +  scope.row.id" >{{ scope.row.createUserName }}</router-link>-->
      <!--          <router-link v-bind:to="'/problem/'+scope.row.id">{{scope.row.id}}</router-link>-->
      <!--          <router-link @click="checkAnswerDetail(scope.row)"></router-link>-->
      <!--          <el-link href="partnerusers" >默认链接</el-link>-->
      <!--          <el-button type="text" @click="checkAnswerDetail(scope.row)" >{{ scope.row.answerTotalAmount }}</el-button>-->
      <el-table-column prop="createUserName" label="归属客户经理" v-if="user.roleFlag === 'ROLE_ADMIN'">
        <template slot-scope="scope" >
          <!--  /partnerusers?realName=xxx  -->
          <router-link :to="{path:'/partnerusers',query: { realName: scope.row.createUserName }}">{{scope.row.createUserName}}</router-link>
        </template>
      </el-table-column>
      <el-table-column prop="createUserName" label="归属客户经理" v-if="user.roleFlag === 'ROLE_DEV' || user.roleFlag === 'ROLE_PARTNER'"></el-table-column>

<!--      <el-table-column prop="createUid" label="创建者(FT)" width="100px"></el-table-column>-->

      <!--      <el-table-column prop="id" label="ID" width="80" sortable></el-table-column>-->
      <!--      <el-table-column prop="storeId" label="门店id"></el-table-column>-->
      <!--      <el-table-column prop="userId" label="用户id"></el-table-column>-->
      <!--      <el-table-column prop="name" label="门店名称"></el-table-column>-->
<!--      <el-table-column prop="logo" label="门店logo"></el-table-column>-->
<!--      <el-table-column prop="telephone" label="电话号码"></el-table-column>-->
<!--      <el-table-column prop="businessHours" label="营业时间"></el-table-column>-->
<!--      <el-table-column prop="level" label="门店等级"></el-table-column>-->
<!--      <el-table-column prop="tradeLevel" label="行业等级"></el-table-column>-->
<!--      <el-table-column prop="starLevel" label="门店星级0-10，共5个满星"></el-table-column>-->
<!--      <el-table-column prop="provinceCode" label="省份编号"></el-table-column>-->
<!--      <el-table-column prop="provinceName" label="省份名称"></el-table-column>-->
<!--      <el-table-column prop="cityCode" label="城市编号"></el-table-column>-->
<!--      <el-table-column prop="cityName" label="城市名称"></el-table-column>-->
<!--      <el-table-column prop="districtCode" label="区域编号"></el-table-column>-->
<!--      <el-table-column prop="districtName" label="区域名称"></el-table-column>-->
<!--      <el-table-column prop="detailAddress" label="详细地址"></el-table-column>-->
<!--      <el-table-column prop="longitude" label="经度"></el-table-column>-->
<!--      <el-table-column prop="latitude" label="纬度"></el-table-column>-->
<!--      <el-table-column prop="geoPoint" label=""></el-table-column>-->
<!--      <el-table-column prop="location" label="定位位置"></el-table-column>-->
<!--      <el-table-column prop="status" label="门店状态 1正常"></el-table-column>-->
<!--      <el-table-column prop="createUid" label="创建人id"></el-table-column>-->
<!--      <el-table-column prop="createTime" label="创建时间"></el-table-column>-->
<!--      <el-table-column prop="updateTime" label="最后修改时间"></el-table-column>-->
<!--      <el-table-column prop="isEnable" label="是否启用,启用后可被消费者看到"></el-table-column>-->
<!--      <el-table-column prop="isDelete" label="是否删除"></el-table-column>-->

<!--      <el-table-column label="操作"  width="180" align="center">-->
<!--        <template slot-scope="scope">-->
<!--          <el-button type="success" @click="handleEdit(scope.row)">编辑 <i class="el-icon-edit"></i></el-button>-->
<!--          <el-popconfirm-->
<!--              class="ml-5"-->
<!--              confirm-button-text='确定'-->
<!--              cancel-button-text='我再想想'-->
<!--              icon="el-icon-info"-->
<!--              icon-color="red"-->
<!--              title="您确定删除吗？"-->
<!--              @confirm="del(scope.row.id)"-->
<!--          >-->
<!--            <el-button type="danger" slot="reference">删除 <i class="el-icon-remove-outline"></i></el-button>-->
<!--          </el-popconfirm>-->
<!--        </template>-->
<!--      </el-table-column>-->

      <el-table-column label="操作"  align="left" width="350px">
        <template slot-scope="scope">

          <el-button type="primary" size="small" @click="checkStoreDetail(scope.row)" >详细信息 <i class="el-icon-edit"></i></el-button>
<!--          <router-link :to="{path:'/partnerusers',query: { realName: scope.row.createUserName }}">{{scope.row.createUserName}}</router-link>-->

          <el-button type="success" size="small" @click="handleEdit(scope.row)">编辑 <i class="el-icon-edit"></i></el-button>
<!--          <el-button type="danger" size="small" @click="del(scope.row)" v-if="user.roleFlag === 'ROLE_ADMIN' || user.roleFlag === 'ROLE_PARTNER'">删除门店 <i class="el-icon-delete"></i></el-button>-->
        </template>
      </el-table-column>

<!--      <el-table-column prop="answerTotalAmount" label="完成问卷数量" width=“120”>-->
<!--        <template slot-scope="scope">-->
<!--          &lt;!&ndash; 链接跳转         <router-link v-bind:to="'/problem/'+scope.row.id">{{scope.row.id}}</router-link>&ndash;&gt;-->
<!--          &lt;!&ndash;          <router-link @click="checkAnswerDetail(scope.row)"></router-link>&ndash;&gt;-->
<!--          <el-button type="text" @click="checkAnswerDetail(scope.row)" >{{ scope.row.answerTotalAmount }}</el-button>-->
<!--        </template>-->
<!--      </el-table-column>-->

    </el-table>
    <div style="padding: 10px 0">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page.sync="parms.pageNum"
          :page-sizes="[2, 5, 10, 20]"
          :page-size.sync="parms.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="parms.total"
          background>
      </el-pagination>
    </div>

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

    <el-dialog title="信息" :visible.sync="dialogFormVisible" width="40%" :close-on-click-modal="false">
      <el-form
          :model="addModel"
          ref="addForm"
          :rules="rules"
          label-width="130px"
          :inline="true"
          size="small"
          style="width: 80%; margin: 0 auto"
      >
<!--        <el-form-item label="用户id">-->
<!--          <el-input v-model="form.id" autocomplete="off" disabled></el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="用户实名">-->
<!--          <el-input v-model="form.realName" autocomplete="off" disabled ></el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="用户角色">-->
<!--          <el-input v-model="form.roleFlag" autocomplete="off" disabled ></el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="门店id">-->
<!--          <el-input v-model="form.storeId" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->
        <el-form-item prop="licenseNo" label="专卖证号">
          <el-input v-model="addModel.licenseNo" autocomplete="off" :disabled="isReadOnly"></el-input>
        </el-form-item>
        <el-form-item prop="name" label="门店名称">
          <el-input v-model="addModel.name" autocomplete="off"></el-input>
        </el-form-item>
<!--        <el-form-item label="门店等级">-->
<!--          <el-input v-model="form.level" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->

<!--        <el-form-item label="门店id">-->
<!--          <el-input v-model="form.logo" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->

        <el-form-item prop="selectedAreaOptions" label="所在地区" > <!--:props="props" 可多选-->
          <el-cascader
            filterable
            disabled
            size="large"
            :options="regionDataOptions"
            v-model="addModel.selectedAreaOptions"
            @change="addressChange">
          </el-cascader>
          <!--            <el-cascader-->
          <!--              :props="props"-->
          <!--              clearable-->
          <!--              filterable-->
          <!--              size="large"-->
          <!--              :options="pcaTextArrOptions"-->
          <!--              v-model="form.selectedOptions"-->
          <!--              @change="addressChange">-->
          <!--            </el-cascader>-->
        </el-form-item>

        <el-form-item prop="detailAddress"  label="门店地址">
          <el-input v-model="addModel.detailAddress" autocomplete="off"></el-input>
        </el-form-item>

<!--        <el-form-item label="店主姓名">-->
<!--          <el-input v-model="" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->
        <el-form-item prop="telephone" label="门店电话">
          <el-input v-model="addModel.telephone" autocomplete="off"></el-input>
        </el-form-item>
<!--        <el-form-item label="营业时间">-->
<!--          <el-input v-model="form.business_hours" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->
        <el-form-item prop="tradeLevel" label="门店档位">
          <el-input v-model="addModel.tradeLevel" autocomplete="off"></el-input>
        </el-form-item>
<!--        <el-form-item label="行业等级">-->
<!--          <el-input v-model="form.trade_level" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="门店星级">-->
<!--          <el-input v-model="form.star_level" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="门店电话">-->
<!--          <el-input v-model="form.status" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="门店电话">-->
<!--          <el-input v-model="form.business_hours" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->


<!--        <el-form-item prop="createUid" label="归属客户经理" v-if="this.user.roleFlag === 'ROLE_ADMIN'">-->
<!--&lt;!&ndash;          <el-select v-model="form.selectedPartner" filterable placeholder="请选择">&ndash;&gt;-->
<!--          <el-select v-model="addModel.createUid" filterable placeholder="请选择">-->
<!--            <el-option-->
<!--              v-for="item in allPartnerList"-->
<!--              :key="item.userId"-->
<!--              :label=" item.realName + '-' + item.provinceName + item.districtName "-->
<!--              :value="item.userId">-->
<!--            </el-option>-->
<!--          </el-select>-->
<!--        </el-form-item>-->
<!--        <el-form-item prop="realName" label="归属客户经理" v-if="this.user.roleFlag === 'ROLE_PARTNER'">-->
<!--          <el-input v-model="user.realName" :disabled="true" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->

<!--        <el-form-item prop="createUid" label="归属客户经理" v-if="this.user.roleFlag === 'ROLE_ADMIN'">-->
<!--          &lt;!&ndash;          <el-select v-model="form.selectedPartner" filterable placeholder="请选择">&ndash;&gt;-->
<!--&lt;!&ndash;          <el-select v-model="addModel.createUid" filterable placeholder="请选择">&ndash;&gt;-->
<!--          <el-select v-model="addModel.createUserName" filterable placeholder="请选择">-->
<!--            <el-option-->
<!--              v-for="item in allPartnerList"-->
<!--              :key="item.userId"-->
<!--              :label=" item.realName + '-' + item.provinceName + item.districtName "-->
<!--              :value="item.userId">-->
<!--            </el-option>-->
<!--          </el-select>-->
<!--        </el-form-item>-->
<!--        <el-form-item prop="realName" label="归属客户经理" v-if="this.user.roleFlag === 'ROLE_PARTNER'">-->
<!--          <el-input v-model="user.realName" :disabled="true" autocomplete="off"></el-input>-->
<!--        </el-form-item>        -->

        <el-form-item prop="createUserName" label="归属客户经理" v-if="this.user.roleFlag === 'ROLE_ADMIN'">
          <el-select v-model="addModel.createUserName" filterable placeholder="请选择">
            <el-option
              v-for="item in allPartnerListByArea"
              :key="item.userId"
              :label=" item.realName + '-' + item.provinceName + item.districtName "
              :value="item.userId">
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item prop="realName" label="归属客户经理" v-if="this.user.roleFlag === 'ROLE_PARTNER'">
          <el-input v-model="user.realName" :disabled="true" autocomplete="off"></el-input>
        </el-form-item>


<!--        <el-form-item prop="createUid" label="归属客户经理id" v-show="false">-->
<!--          <el-input v-model="addModel.createUid" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>


    <el-dialog title="答卷细节" :visible.sync="answerDetaildialogFormVisible" width="40%" :close-on-click-modal="false">
      <el-form label-width="150px" size="small" style="width: 80%; margin: 0 auto">
          <el-form-item label="零售门店店名">
            <el-input v-model="form.name" autocomplete="off" disabled></el-input>
          </el-form-item>
            <el-form-item label="零售门店答卷数量">
              <el-input v-model="form.answerAmountOfStore" autocomplete="off" disabled></el-input>
            </el-form-item>
            <el-form-item label="消费者答卷数量">
              <el-input v-model="form.answerAmountOfUser" autocomplete="off" disabled ></el-input>
            </el-form-item>
            <el-form-item label="总答卷数量">
              <el-input v-model="form.answerTotalAmount" autocomplete="off" disabled ></el-input>
            </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="answerDetaildialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>
  </div>

  </el-main>

</template>

<!--<script src="https://unpkg.com/element-china-area-data@6.0.2/dist/element-china-area-data.iife.js"></script>-->
<script>
import {
  getStoreListApi,
  getStoreListPageApi,
  addStoreApi,
  editStoreApi,
  deleteStoreApi,
  deleteBatchStoreApi,
  getStoreListByAdminApi,
  getStoreListByPartnerApi,
  getStoreListByUserIdApi,
  insertOrUpdateStoreApi,
  getStoreByIdApi
} from "@/api/storeshop";
import {getAllPartnerListApi, getAllPartnerListByAreaApi} from "@/api/user";
import {getProvinceNameByProvinceCodeApi, getCityNameByCityCodeApi, getDistrictNameByDistrictCodeApi} from "@/api/chinaRegion";
import {
  provinceAndCityData,  //省市二级联动数据,汉字+code
  pcTextArr,            //省市区三级联动数据
  regionData,           //省市联动数据，纯汉字
  pcaTextArr,           //省市区联动数据，纯汉字
  codeToText,           //是个大对象，属性是区域码，属性值是汉字 用法例如：codeToText['110000']输出北京市
} from "element-china-area-data";
import {deleteUserApi} from "@/api/user";
import {addPaperApi, editPaperApi} from "@/api/paper";


export default {
  methods: {
    load() {
      this.getStores()
      // this.getAllPartnerList()
      // this.getAllPartnerListByArea()
    },
    //搜索按钮
    searchBtn() {
      this.parms.pageNum = 1;
      this.getStores();
    },

    async getStores() {

      // console.log("888888 ===stores=== this.user ", this.user)
      // console.log("888888 ===stores=== this.user.roleFlag ", this.user.roleFlag)
      // console.log("888888 ===stores=== this.user.roleFlag==ROLE_ADMIN ", this.user.roleFlag == "ROLE_ADMIN")
      // console.log("888888 ===stores=== this.user.roleFlag==ROLE_PARTNER ", this.user.roleFlag == "ROLE_PARTNER")

      // const rLoading = this.openLoading();

      let res = null;
      if (this.user.roleFlag == "ROLE_DEV"){

        // console.log("888888 ===stores=== this.$route.query.userId ", this.$route.query.userId)
        // console.log("888888 ===stores=== this.parms ", this.parms)


        // if(this.$route.query.userId){
        //   res = await getStoreListPageApi(this.parms);
        // }

        console.log("888888 ===stores=== this.parms ", this.parms)
        // res = await getStoreListPageApi(this.parms);

        // res = await getStoreListPageApi(
        //    {
        //      userId: "",
        //     name: this.parms.name,     //搜索店名
        //     licenseNo: this.parms.licenseNo,            //专卖证号
        //     pageNum: 1,               //从第几页开始
        //     pageSize: 10,             //每页查询的条数
        //   }
        // )

        res = await getStoreListPageApi(this.parms);



        // this.stores = res

      } else if (this.user.roleFlag == "ROLE_ADMIN"){

        // console.log("123123 ===stores=== this.$route.query.userId ", this.$route.query.userId )
        // console.log("678678 ===stores=== this.$route.params.userId ", this.$route.params.userId )

        if(this.$route.query.userId){
          // console.log("161616 ===stores=== this.parms ", this.parms)
          res = await getStoreListByUserIdApi(this.parms);
          // console.log("171717 ===stores=== res ", res)
          // this.stores = res
        }
          // else if (this.$route.params.userId) {
          //   // console.log("161616 ===stores=== this.parms ", this.parms)
          //   res = await getStoreListByUserIdApi(this.parms);
          //   // console.log("171717 ===stores=== res ", res)
          //   // this.stores = res
        // }
        else {
          res = await getStoreListByAdminApi(this.parms);
          // this.stores = res
        }
      } else if (this.user.roleFlag == "ROLE_PARTNER") {
        // console.log("161616 ===stores=== this.parms ", this.parms)

        res = await getStoreListByPartnerApi(this.parms);

        // console.log("181818 ===stores ROLE_PARTNER === res ", res)
        // this.stores = res
      }

      // console.log("000000 ===stores=== res ", res)

      // if(storeIdList == null || storeIdList.size()==0 )
      if (res && res.code == 200 && res.data==null) {
        //请求结束关闭loading
        // rLoading.close();

        this.$message.warning("数据库无记录")
      }
      if (res && res.data && res.code == 200) {
        //请求结束关闭loading
        // rLoading.close();

        // console.log("888888 ===stores=== res ", res)

        //给表格数据赋值
        // this.tableList = res.data[0].storeList;
        this.tableList = res.data.records;
        //总条数
        this.parms.total = res.data.total;
        // this.parms.pageNum = res.data.current;  //当前第几页
        // this.parms.pageSize = res.data.size;    //每页几条记录
      }

      // this.stores = res
    },

    async getAllPartnerList(){
      let res = null;
      if (this.user.roleFlag == "ROLE_ADMIN"){
        res = await getAllPartnerListApi();
      }
      if (res && res.data && res.code == 200) {
        //给表格数据赋值
        this.allPartnerList = res.data;
        // console.log("888888000 ===stores=== this.allPartnerList ", this.allPartnerList)

      }
    },

    async getAllPartnerListByArea(districtCode){
      let res = null;
      if (this.user.roleFlag == "ROLE_ADMIN"){
        res = await getAllPartnerListByAreaApi(districtCode);
        // console.log("888888000 ===stores=== districtCode ", districtCode)
        console.log("888888000 ===stores getAllPartnerListByArea === res ", res)
      }
      if (res && res.data && res.code == 200) {
        //给表格数据赋值
        this.allPartnerListByArea = res.data;
        // this.addModel.createUserName = res.data.realName;
        console.log("888888000 ===stores=== res.data ", res.data)
        // console.log("888888000 ===stores=== res.data.createUserName ", res.data.createUserName)
        // console.log("888888000 ===stores=== res.data.realName ", res.data.realName)
        console.log("888888000 ===stores=== this.allPartnerListByArea ", this.allPartnerListByArea)

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
      //表单验证
      this.$refs.addForm.validate(async (valid) => {
        if (valid) {
          // console.log("888===this.addModel===", this.addModel)

          // let provinceCode = row.provinceCode.substr(0, 2)
          // console.log("provinceCode",provinceCode)
          // let cityCode = row.cityCode.substr(0, 4)
          // console.log("cityCode",cityCode)
          // let districtCode = row.districtCode
          // console.log("districtCode",districtCode)
          // // this.selectedAreaOptions = [provinceCode, cityCode, districtCode]
          // this.form.selectedAreaOptions = [provinceCode, cityCode, districtCode]
          // console.log("this.form.selectedAreaOptions",this.form.selectedAreaOptions)

          // let res = await insertOrUpdateStoreApi(this.addModel);

          let res = null;
          if (this.addModel.editType == "0") {

            this.addModel.provinceName = this.provinceName
            this.addModel.provinceCode = this.provinceCode
            this.addModel.cityCode = this.cityCode
            this.addModel.cityName = this.cityName
            this.addModel.districtCode = this.districtCode
            this.addModel.districtName = this.districtName
            console.log("===this.addModel===", this.addModel);

            //新增
            res = await addStoreApi(this.addModel);

          } else {  //编辑
            console.log("===this.addModel===", this.addModel);
            res = await editStoreApi(this.addModel);
          }

          if (res.code === 200 ) {
            this.$message.success("保存成功")
            this.dialogFormVisible = false
            this.load()
          } else {
            this.$message.error("保存失败")
          }

          // this.load
        }
      });
    },




    handleAdd() {
      this.isReadOnly = false

      // this.createUid = this.user.userId
      // console.log("===this.createUid===", this.createUid)

      this.$resetForm('addForm',this.addModel)
      this.allPartnerListByArea = ""


      //20240113 Start
      this.addModel.selectedAreaOptions = [this.user.districtCode.substr(0, 2), this.user.districtCode.substr(0, 4), this.user.districtCode]
      this.getAllPartnerListByArea(this.user.districtCode);
      setTimeout(() => {
        this.getAreaNameByAreaCode(this.user.districtCode);
      }, 5000)
      //20240113 End


      this.addModel.editType = "0";

      // if (this.user.roleFlag == "ROLE_ADMIN"){
      if (this.user.roleFlag == "ROLE_PARTNER"){
        this.addModel.createUid = this.user.userId
      }

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

    async handleEdit(row) {
      console.log("===handleEdit row===", row);
      //表单的清空
      this.$resetForm('addForm',this.addModel)

      this.addModel.editType = "1";
      this.isReadOnly = true

      // console.log('000===row===', row)
      // this.addModel = JSON.parse(JSON.stringify(row))
      this.$objCoppy(row, this.addModel)

      let provinceCode = row.provinceCode.substr(0, 2)
      // console.log("provinceCode",provinceCode)
      let cityCode = row.cityCode.substr(0, 4)
      // console.log("cityCode",cityCode)
      let districtCode = row.districtCode
      // console.log("districtCode",districtCode)
      this.addModel.selectedAreaOptions = [provinceCode, cityCode, districtCode]
      console.log("this.addModel.selectedAreaOptions",this.addModel.selectedAreaOptions)

      // if (this.user.roleFlag == "ROLE_ADMIN"){
      // if (this.user.roleFlag == "ROLE_PARTNER"){
      //   this.addModel.createUid = this.user.userId
      // }
      this.addModel.createUserName = row.createUserName
      // this.addModel.realName = row.createUserName
      // console.log("===999999 row.realName===", row.realName)
      // console.log("===999999 row.createUserName===", row.createUserName)
      // console.log("===999999 this.addModel===", this.addModel)

      // this.addModel.districtCode = districtCode
      this.getAllPartnerListByArea(districtCode);

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
      console.log("000000000 checkAnswerDetail")
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

// <el-button type="success" @click="checkStoreDetail(scope.row)" >详细信息 <i class="el-icon-edit"></i></el-button>
// <router-link :to="{path:'/partnerusers',query: { realName: scope.row.createUserName }}">{{scope.row.createUserName}}</router-link>

    checkStoreDetail(row) {
      console.log("000000000 checkStoreDetail row.storeId", row.storeId)

      this.$router.push({ path:  "/storeDetail/" + row.storeId });

      // @click="$router.push('/login')"
      //
      // this.form = JSON.parse(JSON.stringify(row))
      // this.answerDetaildialogFormVisible = true
      // this.$nextTick(() => {
      //   if(this.$refs.img) {
      //     this.$refs.img.clearFiles();
      //   }
      //   if(this.$refs.file) {
      //     this.$refs.file.clearFiles();
      //   }
      // })
    },

    // del(row) {
    //   this.request.delete("/store/" + row.id).then(res => {
    //     if (res.code === '200') {
    //       this.$message.success("删除成功")
    //       this.load()
    //     } else {
    //       this.$message.error("删除失败")
    //     }
    //   })
    // },
    //删除按钮
    async del(row) {
      console.log(row);
      //信息确认提示
      let confirm = await this.$myconfirm('确定删除该数据吗?')
      console.log(confirm)
      if(confirm){
        let res = await deleteStoreApi({id:row.id})
        if(res && res.code == 200){
          //信息提示
          this.$message.success(res.msg)
          //刷新表格
          this.load();
        }
      }
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
    reset() {
      this.parms.name = ""
      this.parms.licenseNo = ""
      this.load()
    },
    setSelectNull() {
      // this.parms.name = ""
      this.addModel.createUserName = ""
    },
    handleSizeChange(pageSize) {
      console.log("pageSize:",pageSize)
      this.parms.pageSize = pageSize
      this.parms.pageNum = 1;
      this.load()
    },
    handleCurrentChange(pageNum) {
      console.log("pageNum:",pageNum)
      this.parms.pageNum = pageNum
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
      // this.headers = {"X-Token": this.token}
      this.$message.success("导入成功")
      this.load()
    },

    exportStoreList() {
      console.log("181818 ===stores=== this.parms ", this.parms)

      // this.headers = {"X-Token": this.token}
      // console.log("===000000 this.stores===", this.stores)
      // console.log("===000000 exportStoreList===")
      // console.log("===111111 this.user.userId===", this.user.userId)
      // const userId = this.user.userId
      // console.log("===222222 stores exportStoreList : userId===", userId)


      window.open("http://localhost:8089/api/store/export/exportStoreList" + "?userId=" + this.user.userId + "&name=" + this.parms.name + "&licenseNo=" + this.parms.licenseNo)
      // window.open("http://43.140.195.12:8089/api/store/export/exportStoreList/" + "?userId=" + this.user.userId + "&name=" + this.parms.name + "&licenseNo=" + this.parms.licenseNo)
      // window.open("https://adminapi.xiangwenli.com/api/store/export/exportStoreList/" + "?userId=" + this.user.userId + "&name=" + this.parms.name + "&licenseNo=" + this.parms.licenseNo)


    },

    exportStoreHeadList() {
      // this.headers = {"X-Token": this.token}
      // console.log("===000000 this.stores===", this.stores)
      // console.log("===000000 exportStoreList===")
      // console.log("===111111 this.user.userId===", this.user.userId)
      const userId = this.user.userId
      // console.log("===222222 userId===", userId)

      window.open("http://localhost:8089/api/store/export/exportStoreHeadList/" + userId )
      // window.open("http://43.140.195.12:8089/api/store/export/exportStoreHeadList/" + userId )
      // window.open("https://adminapi.xiangwenli.com/api/store/export/exportStoreHeadList/" + userId )

    },

    importStoreList(res) {
      // console.log("=== 000000 importStoreList ===")
      // console.log("=== 000000 importStoreList res ===" , res)

      if (res && res.code == 606) {
        console.log("=== 000000 importStoreList 606 ===")
        this.$message.error("导入失败," + res.msg)
      }
      if (res && res.code == 200) {
        console.log("=== 000000 importStoreList 200 ===")
        this.$message.success("导入成功")
      }
      this.load()
    },

    //省市区三级联动下拉框
    addressChange(arr) {
      this.addModel.createUserName = ""

      //arr[0]:11 arr[1]:1101 arr[2]:110101
      // CodeToText是个大对象，属性是区域码，属性值是汉字 用法例如：codeToText['110000']输出北京市
      const provinceCode = arr[0]+'0000';
      const cityCode = arr[1]+'00';
      const districtCode = arr[2];
      const provinceName = codeToText[arr[0]];
      const cityName = codeToText[arr[1]];
      const districtName = codeToText[arr[2]];

      console.log("provinceCode:", provinceCode);
      console.log("cityCode:", cityCode);
      console.log("districtCode:", districtCode);
      console.log("provinceName:", provinceName);
      console.log("cityName:", cityName);
      console.log("districtName:", districtName);

      this.addModel.provinceCode = provinceCode
      this.addModel.provinceName = provinceName
      this.addModel.cityCode = cityCode
      this.addModel.cityName = cityName
      this.addModel.districtCode = districtCode
      this.addModel.districtName = districtName
      this.addModel.selectedAreaOptions = arr

      console.log("this.addModel",  this.addModel);

      this.getAllPartnerListByArea(districtCode);
      console.log("99999900 districtCode",  districtCode);

    },


    // async getAreaNameByAreaCode(areaCode) {
    //   console.log("===0.0 areaCode",  areaCode);
    //   if (areaCode.substr(2, 6)=='0000'){                         //省级
    //     let provinceCode = areaCode.substr(0, 2)+'0000'           //110000
    //     console.log("===1.1 省级 provinceCode",  provinceCode);
    //     this.getProvinceNameByProvinceCode(provinceCode)
    //     console.log("===1.2 省级 this.provinceName",  this.provinceName);
    //   } else if (areaCode.substr(4, 6)=='00'){                       //市级
    //     let cityCode = areaCode.substr(0, 4)                        //110100
    //     console.log("===2.1 市级 cityCode",  cityCode);
    //     this.getCityNameByCityCode(cityCode)
    //     console.log("===2.1 市级 this.cityName",  this.cityName);
    //   } else {                                                                  //区级
    //     this.getDistrictNameByDistrictCode(areaCode)
    //     console.log("===3.1 区级 areaCode",  areaCode);
    //     console.log("===3.2 区级 this.districtName", this.districtName)
    //   }
    //
    //   console.log("=== this.provinceName",  this.provinceName);
    //   console.log("=== this.cityName",  this.cityName);
    //   console.log("=== this.districtName",  this.districtName);
    //
    // },

    async getAreaNameByAreaCode(areaCode) {
      console.log("======areaCode======",  areaCode);                       //areaCode:110101

      let provinceCode = areaCode.substr(0, 2)+'0000'           //provinceCode:110000
      this.getProvinceNameByProvinceCode(provinceCode)
      // console.log("===this.provinceName",  this.provinceName);

      let cityCode = areaCode.substr(0, 4)+'00'                 //cityCode:110100
      this.getCityNameByCityCode(cityCode)
      // console.log("==this.cityName",  this.cityName);

      // let districtCode = areaCode.substr(0, 6)                           //districtCode:110101
      this.getDistrictNameByDistrictCode(areaCode)
      // console.log("===this.districtName", this.districtName)


      // if (areaCode.substr(2, 6)=='0000'){                         //省级
      //   let provinceCode = areaCode.substr(0, 2)+'0000'           //110000
      //   console.log("===1.1 省级 provinceCode",  provinceCode);
      //   this.getProvinceNameByProvinceCode(provinceCode)
      //   console.log("===1.2 省级 this.provinceName",  this.provinceName);
      // } else if (areaCode.substr(4, 6)=='00'){                       //市级
      //   let cityCode = areaCode.substr(0, 4)                        //110100
      //   console.log("===2.1 市级 cityCode",  cityCode);
      //   this.getCityNameByCityCode(cityCode)
      //   console.log("===2.1 市级 this.cityName",  this.cityName);
      // } else {                                                                  //区级
      //   this.getDistrictNameByDistrictCode(areaCode)
      //   console.log("===3.1 区级 areaCode",  areaCode);
      //   console.log("===3.2 区级 this.districtName", this.districtName)
      // }

    },

    async getProvinceNameByProvinceCode(provinceCode) {
      let res = await getProvinceNameByProvinceCodeApi(provinceCode);
      if (res && res.data && res.code == 200) {
        this.provinceName = res.data.areaName
        this.provinceCode = provinceCode
        console.log("1.1 this.provinceName:", this.provinceName)
        console.log("1.2 this.provinceCode:", this.provinceCode)
      }
    },
    async getCityNameByCityCode(cityCode) {
      let res = await getCityNameByCityCodeApi(cityCode);
      if (res && res.data && res.code == 200) {
        this.cityName = res.data.areaName
        this.cityCode = cityCode
        console.log("2.1 this.cityName:", this.cityName)
        console.log("2.2 this.cityCode:", this.cityCode)
      }
    },
    async getDistrictNameByDistrictCode(districtCode) {
      let res = await getDistrictNameByDistrictCodeApi(districtCode);
      if (res && res.data && res.code == 200) {
        this.districtName = res.data.areaName
        this.districtCode = districtCode
        console.log("3.1 this.districtName:", this.districtName)
        console.log("3.2 this.districtCode:", this.districtCode)
      }
    },

  },
  name: "Store",
  data() {
    const blurText = async(rule, value, callback) => {
      // const reg = /^\-\d\.?\d*$/
      // const boolean = reg.test(value)
      const boolean = new RegExp('^[1-9][0-9]*$').test(value)
      // console.log(boolean)
      if (!boolean) {
        this.$message.warning('请输入正整数')
        this.addModel.tradeLevel = ''
      }
    }
    return {
      id: "",
      userId: "",

      isReadOnly:false,

      headers: {
      //   'Content-Type': 'multipart/form-data;'
      //   "X-Token": this.token
      },

      // name: "",
      // realName: "",
      // currentPage: 1,
      // pageSize: 10,
      // total: 0,
      parms: {
        userId: 0,
        name: "",     //搜索店名
        licenseNo: "",     //专卖证号
        realName: "",   //搜索人名
        // realName: "",   //搜索人名
        // realName: "",   //搜索人名
        pageNum: 1, //从第几页开始
        pageSize: 10, //每页查询的条数
        total: 0, //总条数
      },
      // parms2: {     //跳转到此页面 传入的参数
      //   userId: 0,
      //   // roleFlag: "",
      //   // realName: "",   //人名
      // },
      tableList: [],
      tableHeight: 0,

      allPartnerList: [],
      allPartnerListByArea: [],

      form: {},
      dialogFormVisible: false,
      answerDetaildialogFormVisible: false,
      multipleSelection: [],

      // currentUserId: this.user.id,
      // currentUserRealName: this.user.realName,
      // currentUserRoleFlag: this.user.roleFlag,

      regionDataOptions: regionData,
      pcaTextArrOptions: pcaTextArr,
      selectedAreaOptions: [],
      props: { multiple: true },

      //新增表单绑定的数据域
      addModel: {
        editType: "", //0：新增 1：编辑

        // id: "",
        // userId: this.userId,

        id: "",
        storeId: "",

        licenseNo: "",
        name: "",

        provinceCode: "",
        provinceName: "",
        cityCode: "",
        cityName: "",
        districtCode: "",
        districtName: "",
        selectedAreaOptions: [],

        detailAddress: "",
        telephone: "",
        tradeLevel: "",
        createUid: "",
        createUserName: "",
        realName: "",

      },

      rules: {
        licenseNo: [
          {
            trigger: "change",
            message: "请填写专卖证号",
            required: true,
          },
        ],
        name: [
          {
            trigger: "change",
            message: "请填写门店名称",
            required: true,
          },
        ],
        selectedAreaOptions: [
          {
            trigger: "change",
            message: "请选择所在地",
            required: true,
          },
        ],
        detailAddress: [
          {
            trigger: "change",
            message: "请填写门店地址",
            required: true,
          },
        ],
        telephone: [
          {
            trigger: "change",
            message: "请填写门店电话",
            required: true,
          },
        ],
        tradeLevel: [
          {
            trigger: "blur",
            message: "请填写门店档位",
            required: true,
          },
          { validator: blurText, trigger: 'blur' }  //表单验证的时候会调用的方法
        ],
        level: [
          {
            trigger: "change",
            message: "请填写门店等级",
            required: true,
          },
        ],
        createUid: [
          {
            trigger: "change",
            message: "请选择归属客户经理",
            required: true,
          },
        ],

      },

      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},

      stores: {},

      provinceName: "",
      provinceCode: "",
      cityName: "",
      cityCode: "",
      districtName: "",
      districtCode: "",

      // codeToText: elementChinaAreaData.codeToText,

    }
  },
  mounted() {
    this.$nextTick(() => {
      this.tableHeight = window.innerHeight - 250;
    });
  },
  // mounted() {
  //   this.$nextTick(() => {
  //     this.parms.id = this.user.id
  //   });
  //   console.log("666666 ===stores=== this.user.id ", this.user.id)
  // },

  created() {
    this.user = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}
    this.token = this.user.token
    this.headers = {"X-Token": this.token}

    // this.parms.userId = this.user.userId

    // console.log("666666 ===stores=== this.user.roleFlag" , this.user.roleFlag)
    // console.log("666666 ===stores=== this.user.roleFlag === 'ROLE_ADMIN'" , this.user.roleFlag === 'ROLE_ADMIN')
    // console.log("666666 ===stores=== this.user" , this.user)

    // console.log("666666 ===Stores=== this.$route.query.realName", this.$route.query.realName)
    if(this.$route.query.userId){
      console.log("666666 ===Stores=== this.$route.query.userId", this.$route.query.userId)
      this.parms.userId = this.$route.query.userId;
    }
    if(this.$route.params.userId){
      console.log("666666 ===Stores=== this.$route.params.userId", this.$route.params.userId)
      this.parms.userId = this.$route.params.userId;
    }
    // if(this.$route.query.roleFlag){
    //   this.parms2.roleFlag = this.$route.query.roleFlag;
    // }
    // if(this.$route.query.realName){
    //   // this.parms.realName = this.$route.query.realName;
    //   this.parms2.realName = this.$route.query.realName;
    // }

    // console.log("666666 ===stores=== this.$route.query.name", this.$route.query.name)
    // if(this.$route.query.name){
    //   this.parms.name = this.$route.query.name;
    //   console.log("666666 ===stores=== this.parms.name", this.parms.name)
    // }

    this.load()
  }
}
</script>


<style  lang="scss" scoped>
.headerBg {
  background: #eee!important;
}

a{
  text-decoration: underline;
  color: blue;
}

</style>
