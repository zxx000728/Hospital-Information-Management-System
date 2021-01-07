<template>
  <el-container>
    <el-header><navmenu></navmenu></el-header>
    <el-main>
      <el-row type="flex" justify="center">
        <el-col type="flex" justify="center">
          <el-table :data="tableData" align="center" empty-text="暂无数据">
            <el-table-column prop="id" label="ID" width="150">
            </el-table-column>
            <el-table-column prop="name" label="姓名" width="120">
            </el-table-column>
            <el-table-column prop="age" label="年龄" width="120">
            </el-table-column>
            <el-table-column prop="phone" label="电话" width="180">
            </el-table-column>
            <el-table-column prop="address" label="地址" width="300">
            </el-table-column>
            <el-table-column prop="rating" label="病情评级" width="150">
            </el-table-column>
            <el-table-column
              prop="state"
              label="生命状态"
              width="150"
              :filters="[
                { text: '康复出院', value: '康复出院' },
                { text: '在院治疗', value: '在院治疗' },
                { text: '病亡', value: '病亡' },
              ]"
              :filter-method="filterState"
              filter-placement="bottom-end"
            >
            </el-table-column>
            <el-table-column align="right">
              <template slot="header" slot-scope="scope">
                <el-button
                  size="mini"
                  @click="handleAdd(scope.$index, scope.row)"
                  >添加</el-button
                >
              </template>
              <template slot-scope="scope">
                <el-button
                  size="mini"
                  @click="handleEdit(scope.$index, scope.row)"
                  >详情</el-button
                >
                <el-button
                  size="mini"
                  type="danger"
                  @click="handleDelete(scope.$index, scope.row)"
                  >删除</el-button
                >
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-row>
    </el-main>
  </el-container>
</template>

<script>
import navmenu from "../components/Nav.vue";

export default {
  name: "PatientDataPanel",
  components: { navmenu },
  data() {
    return {
      user: [],
      tableData: [],
      loading: false,
    };
  },
  created() {
    this.handleUserData();
    this.loadTableData();
  },
  methods: {
    handleUserData() {
      if (this.$store.state.user) {
        this.user = this.$store.state.user;
        this.isHeadNurse = this.user.u_type == "h_nurse";
      }
    },
    loadTableData() {},
    filterState(value, row) {
      return row.state === value;
    },
  },
};
</script>

<style>
</style>