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
                  v-if="isHeadNurse"
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
                  v-if="isHeadNurse"
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
  inject: ["reload"],
  data() {
    return {
      user: {},
      tableData: [],
      isHeadNurse: false,
      isDoctor: false,
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
        this.isDoctor = this.user.u_type == "doctor";
      }
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

    handleAdd(index, row) {
      this.$router.push("/workerInfo");
    },
    handleEdit(index, row) {
      this.$router.push("/workerInfo/" + row.id);
    },
    handleDelete(index, row) {
      this.$axios
        .get("/deleteWardNurse", {
          params: { u_id: this.user.id, w_nurse_id: row.id },
        })
        .then((resp) => {
          if (resp.status === 200) {
            this.$message.info("成功删除！");
            this.reload();
          } else if (resp.status === 401) {
            this.$message.error("您无删除权限！");
          } else {
            this.$message.error("您所请求的删除服务不可用");
          }
        })
        .catch((error) => {
          console.log(error);
          this.$message.error("请求错误，请重试");
        });
    },
  },
};
</script>

<style>
</style>