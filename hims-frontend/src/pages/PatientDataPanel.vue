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
            <el-table-column prop="phone" label="电话" width="180">
            </el-table-column>
            <el-table-column prop="address" label="地址" width="300">
            </el-table-column>
            <el-table-column prop="rating" label="病情评级" width="150">
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
      tableData: [
        {
          id: "",
          name: "",
          age: "",
          phone: "",
          address: "",
          rating: "",
        },
      ],
      loading: false,
    };
  },
  created() {
    this.handleUserData(), this.loadTableData();
  },
  methods: {
    handleUserData() {
      if (JSON.parse(localStorage.getItem("user")).user) {
        this.user = JSON.parse(localStorage.getItem("user")).user;
      }
    },
    loadTableData() {
      if (this.user) {
        this.tableData[0].id = this.user.id;
        this.tableData[0].name = this.user.name;
        this.tableData[0].age = this.user.age;
        this.tableData[0].phone = this.user.phone;
        this.tableData[0].address = this.user.address;
        switch (this.user.rating) {
          case "mild":
            this.tableData[0].rating = "轻症";
            break;
          case "severe":
            this.tableData[0].rating = "重症";
            break;
          case "critical":
            this.tableData[0].rating = "危重症";
            break;
        }
      }
    },
  },
};
</script>

<style>
</style>