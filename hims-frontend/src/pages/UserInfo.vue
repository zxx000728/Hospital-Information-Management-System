<template>
  <el-container>
    <el-header><navmenu></navmenu></el-header>
    <el-main>
      <el-row type="flex" justify="center">
        <el-col align="left">
          <el-page-header @back="goBack" content="个人中心"> </el-page-header>
        </el-col>
      </el-row>
      <el-row type="flex" justify="center">
        <el-col :span="8">
          <el-form
            v-if="isUpdating"
            @submit.native.prevent
            :model="userInfoForm"
            :rules="rules"
            status-icon
            ref="userInfoForm"
            v-loading="loading"
          >
            <el-form-item label="姓名" prop="name">
              <el-input v-model="userInfoForm.name"></el-input>
            </el-form-item>
            <el-form-item label="密码" prop="password">
              <el-input
                type="password"
                v-model="userInfoForm.password"
                autocomplete="off"
              ></el-input>
            </el-form-item>
            <el-form-item label="年龄" prop="age">
              <el-input v-model="userInfoForm.age"></el-input>
            </el-form-item>
            <el-form-item label="Email" prop="email">
              <el-input v-model="userInfoForm.email"></el-input>
            </el-form-item>
            <el-form-item label="电话" prop="phone">
              <el-input v-model="userInfoForm.phone"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button
                native-type="submit"
                type="primary"
                @click="submitForm('userInfoForm')"
                >提交个人信息</el-button
              >
            </el-form-item>
          </el-form>

          <el-form v-if="isReading">
            <el-form-item label="ID">
              {{ this.user.id }}
            </el-form-item>
            <el-form-item label="姓名">
              {{ this.user.name }}
            </el-form-item>
            <el-form-item label="年龄">
              {{ this.user.age }}
            </el-form-item>
            <el-form-item label="Email">
              {{ this.user.email }}
            </el-form-item>
            <el-form-item label="电话">
              {{ this.user.phone }}
            </el-form-item>
            <el-form-item label="身份">
              {{ this.parseUType(this.user.u_type) }}
            </el-form-item>
            <el-form-item>
              <el-button @click="handleUpdate">修改个人信息</el-button>
            </el-form-item>
          </el-form>
        </el-col>
      </el-row>
    </el-main>
  </el-container>
</template>

<script>
import navmenu from "../components/Nav.vue";

export default {
  name: "UserInfo",
  components: { navmenu },
  data() {
    return {
      user: [],
      isUpdating: false,
      isReading: true,

      userInfoForm: {
        name: "",
        password: "",
        age: "",
        email: "",
        phone: "",
      },
      rules: {
        name: { required: true, message: "请输入姓名", blur: "change" },
        password: { required: true, message: "请输入密码", blur: "change" },
        age: { required: true, message: "请输入年龄", blur: "change" },
        email: { required: true, message: "请输入Email", blur: "change" },
        phone: { required: true, message: "请输入电话", blur: "change" },
      },
      loading: false,
    };
  },
  created() {
    this.handleUserData();
    this.loadWorkerData();
  },
  methods: {
    handleUserData() {
      if (this.$store.state.user) {
        this.user = this.$store.state.user;
      }
    },
    loadWorkerData() {
      this.$axios
        .get("/workerInfo", {
          params: { id: this.user.id },
        })
        .then((resp) => {
          if (resp.status === 200) {
            if (resp.data.worker) {
              this.user.id = resp.data.worker.id;
              this.user.name = resp.data.worker.name;
              this.user.password = resp.data.worker.password;
              this.user.age = resp.data.worker.age;
              this.user.email = resp.data.worker.email;
              this.user.phone = resp.data.worker.phone;
              this.user.u_type = resp.data.worker.u_type;

              this.userInfoForm.name = resp.data.worker.name;
              this.userInfoForm.password = resp.data.worker.password;
              this.userInfoForm.age = resp.data.worker.age;
              this.userInfoForm.email = resp.data.worker.email;
              this.userInfoForm.phone = resp.data.worker.phone;
            } else {
              this.$message.error("请求错误，请重试");
            }
          } else {
            this.$message.error("请求错误，请重试");
          }
        })
        .catch((error) => {
          console.log(error);
          this.$message.error("请求错误，请重试");
        });
    },

    goBack() {
      this.$router.push("/workerDataPanel");
    },

    handleUpdate() {
      this.isReading = false;
      this.isUpdating = true;
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

    submitForm(formName) {
      // this.loading = true;
      // this.$axios
      //   .post("/login", {
      //     id: this.loginForm.id,
      //     password: this.loginForm.password,
      //   })
      //   .then((resp) => {
      //     if (resp.status === 200 && resp.data.hasOwnProperty("user")) {
      //       this.$message({
      //         type: "success",
      //         message: "欢迎登陆！",
      //       });
      //       this.$router.push("/patientDataPanel");
      //       this.reload();
      //     }
      //   })
      //   .catch((error) => {
      //     console.log(error);
      //     if (error.message == "Request failed with status code 403") {
      //       this.$message({
      //         type: "error",
      //         message: "用户id或密码错误，请重试。",
      //       });
      //       this.loading = false;
      //     } else {
      //       this.$message({
      //         type: "error",
      //         message: "服务暂时不可用，请稍后再试。",
      //       });
      //       this.loading = false;
      //     }
      //   });
    },
  },
};
</script>

<style>
</style>