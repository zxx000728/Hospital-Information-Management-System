<template>
  <el-container>
    <el-header><navmenu></navmenu></el-header>
    <el-main>
      <el-row type="flex" justify="center">
        <el-col type="flex" justify="center">
          <el-table :data="tableData" align="center">
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
      user:[],
      tableData: [
        {
          id: "",
          name: "",
          age: "",
          email: "",
          phone: "",
          u_type: "",
        },
      ],
      loading: false,
    };
  },
  created() {
    this.handleUserData(),
    this.loadTableData()
  },
  methods: {
    handleUserData() {
      if (JSON.parse(localStorage.getItem("user")).user) {
        this.user = JSON.parse(localStorage.getItem("user")).user;
      }
    },
    loadTableData(){
      if (this.user) {
        this.tableData[0].id = this.user.id;
        this.tableData[0].name = this.user.name;
        this.tableData[0].age = this.user.age;
        this.tableData[0].email = this.user.email;
        this.tableData[0].phone = this.user.phone;
        switch (this.user.u_type) {
          case "doctor":
            this.tableData[0].u_type = "doctor";
            break;
          case "h_nurse":
            this.tableData[0].u_type = "head nurse";
            break;
          case "w_nurse":
            this.tableData[0].u_type = "ward nurse";
            break;
          case "e_nurse":
            this.tableData[0].u_type = "emergency nurse";
            break;
        }
      }
    }
  },
};
</script>

<style>
</style>