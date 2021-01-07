<template>
  <el-container>
    <el-header><navmenu></navmenu></el-header>
    <el-main>
      <el-row type="flex" justify="center">
        <el-col type="flex" justify="center">
          <el-table :data="tableData" align="center" empty-text="暂无数据">
            <el-table-column prop="id" label="病床号" width="150">
            </el-table-column>
            <el-table-column prop="w_id" label="病房号" width="120">
            </el-table-column>
            <el-table-column prop="status" label="状态" width="120">
            </el-table-column>
            <el-table-column align="right">
              <!-- <template slot="header" slot-scope="scope">
                <el-button
                  size="mini"
                  @click="handleAdd(scope.$index, scope.row)"
                  >添加</el-button
                >
              </template> -->
              <template slot-scope="scope">
                <el-button
                  size="mini"
                  @click="handleEdit(scope.$index, scope.row)"
                  >详情</el-button
                >
                <!-- <el-button
                  size="mini"
                  type="danger"
                  @click="handleDelete(scope.$index, scope.row)"
                  >删除</el-button
                > -->
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
  name: "BedDataPanel",
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
      }
    },
    loadTableData() {
      // if (this.user) {
      //   this.tableData[0].id = this.user.id;
      //   this.tableData[0].w_id = this.user.w_id;
      //   switch (this.user.u_type) {
      //     case "doctor":
      //       this.tableData[0].u_type = "主治医生";
      //       break;
      //     case "h_nurse":
      //       this.tableData[0].u_type = "护士长";
      //       break;
      //     case "w_nurse":
      //       this.tableData[0].u_type = "病房护士";
      //       break;
      //     case "e_nurse":
      //       this.tableData[0].u_type = "急诊护士";
      //       break;
      //   }
      // }
    },
  },
};
</script>

<style>
</style>