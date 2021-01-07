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
            <el-table-column prop="email" label="Email" width="300">
            </el-table-column>
            <el-table-column prop="phone" label="电话" width="180">
            </el-table-column>
            <el-table-column prop="u_type" label="身份" width="150">
            </el-table-column>
            <el-table-column align="right">
              <template slot="header" slot-scope="scope">
                <el-button
                  v-if="this.isHeadNurse"
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
                  v-if="this.isHeadNurse"
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
  name: "WorkerDataPanel",
  components: { navmenu },
  data() {
    return {
      user: [],
      tableData: [],
      isHeadNurse: false,
      loading: false,
    };
  },
  created() {
    this.handleUserData();
    this.loadTableData();
  },
  methods: {
    handleUserData() {
      if (JSON.parse(localStorage.getItem("user")).user) {
        this.user = JSON.parse(localStorage.getItem("user")).user;
        this.isHeadNurse = this.user.u_type == "h_nurse";
      }
    },
    loadTableData() {
      this.$axios
        .get("/workerDataPanel", { id: this.user.id, type: this.user.u_type })
        .then((resp) => {
          if (resp.status === 200) {
            var index = -1;
            if (resp.data.headNurse) {
              index++;
              loadRow(index, resp.data.headNurse);
            }
            resp.data.wardNurse.forEach((element) => {
              index++;
              loadRow(index, element);
            });
          } else {
            this.$message.error("请求错误，请重试");
          }
        })
        .catch((error) => {
          console.log(error);
          this.$message.error("请求错误，请重试");
        });
    },
    loadRow(index, worker) {
      this.tableData[i].id = worker.id;
      this.tableData[i].name = worker.name;
      this.tableData[i].age = worker.age;
      this.tableData[i].email = worker.email;
      this.tableData[i].phone = worker.phone;
      switch (worker.u_type) {
        case "doctor":
          this.tableData[i].u_type = "主治医生";
          break;
        case "h_nurse":
          this.tableData[i].u_type = "护士长";
          break;
        case "w_nurse":
          this.tableData[i].u_type = "病房护士";
          break;
        case "e_nurse":
          this.tableData[i].u_type = "急诊护士";
          break;
      }
    },
    handleAdd() {
      this.$router.push("/workerInfo");
    },
    handleEdit() {},
    handleDelete() {},
  },
};
</script>

<style>
</style>