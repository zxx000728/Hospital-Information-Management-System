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
  name: "WorkerDataPanel",
  components: { navmenu },
  data() {
    return {
      user: {},
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
      if (this.$store.state.user) {
        this.user = this.$store.state.user;
        this.isHeadNurse = this.user.u_type == "h_nurse";
      }
    },
    parseUType(u_type) {
      switch (u_type) {
        case "doctor":
          return "主治医生";
        case "h_nurse":
          return "护士长";
        case "w_nurse":
          return "病房护士";
        case "e_nurse":
          return "急诊护士";
      }
    },
    loadRow(index, worker) {
      this.tableData.push({
        id: worker.id,
        name: worker.name,
        age: worker.age,
        email: worker.email,
        phone: worker.phone,
        u_type: this.parseUType(worker.u_type),
      });
    },
    loadTableData() {
      this.$axios
        .get("/workerDataPanel", {
          params: { id: this.user.id, type: this.user.u_type },
        })
        .then((resp) => {
          if (resp.status === 200) {
            var index = -1;
            if (resp.data.headNurse) {
              index++;
              this.loadRow(index, resp.data.headNurse);
            }
            resp.data.wardNurse.forEach((element) => {
              index++;
              this.loadRow(index, element);
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
    handleAdd(index, row) {
      this.$router.push("/workerInfo");
    },
    handleEdit(index, row) {},
    handleDelete(index, row) {},
  },
};
</script>

<style>
</style>