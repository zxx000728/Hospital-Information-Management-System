<template>
  <el-container>
    <el-header>Header</el-header>
    <el-main>
      <el-row type="flex" justify="center">
        <el-col :span="6">
          <el-form
            :model="loginForm"
            status-icon
            ref="loginForm"
            v-loading="loading"
          >
            <el-form-item label="用户id" prop="id">
              <el-input v-model="loginForm.id"></el-input>
            </el-form-item>
            <el-form-item label="密码" prop="password">
              <el-input
                type="password"
                v-model="loginForm.password"
                autocomplete="off"
              ></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="submitForm('loginForm')"
                >登录</el-button
              >
            </el-form-item>
          </el-form>
        </el-col>
      </el-row>
    </el-main>
    <el-footer>Footer</el-footer>
  </el-container>
</template>

<script>
export default {
  name: "Login",
  data() {
    return {
      loginForm: {
        id: "",
        password: "",
      },
      loading: false,
    };
  },
  methods: {
    submitForm(formName) {
      this.loading = true;
      this.$axios
        .post("/login", {
          id: this.loginForm.id,
          password: this.loginForm.password,
        })
        .then((resp) => {
          if (resp.status === 200 && resp.data.hasOwnProperty("token")) {
            // Save token
            // this.$store.commit("login", resp.data);
            this.$message({
              type: "success",
              message: "欢迎登陆！",
              center: true,
            });
            this.$router.replace({ path: "/" });
          } else {
            this.$message({
              type: "error",
              message: "用户id或密码错误，请重试。",
              center: true,
            });
            this.loading = false;
          }
        })
        .catch((error) => {
          console.log(error);
          this.$message({
            type: "error",
            message: "服务暂时不可用，请稍后再试。",
            center: true,
          });
          this.loading = false;
        });
    },
  },
};
</script>

<style>
</style>