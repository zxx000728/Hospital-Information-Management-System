<template>
  <el-container>
    <el-header><navmenu></navmenu></el-header>
    <el-main>
      <el-row type="flex" justify="center">
        <el-col justify="left" :span="8">
          <el-form
            v-if="isCreating"
            @submit.native.prevent
            :model="workerInfoForm"
            :rules="rules"
            status-icon
            ref="workerInfoForm"
            v-loading="loading"
          >
            <el-form-item label="姓名" prop="name">
              <el-input v-model="workerInfoForm.name"></el-input>
            </el-form-item>
            <el-form-item label="年龄" prop="age">
              <el-input v-model="workerInfoForm.age"></el-input>
            </el-form-item>
            <el-form-item label="Email" prop="email">
              <el-input v-model="workerInfoForm.email"></el-input>
            </el-form-item>
            <el-form-item label="电话" prop="phone">
              <el-input v-model="workerInfoForm.phone"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button
                native-type="submit"
                type="primary"
                @click="submitForm('workerInfoForm')"
                >提交新用户</el-button
              >
            </el-form-item>
          </el-form>

          <el-form
            v-if="isReading"
            :model="workerInfoView"
            ref="workerInfoView"
          >
            <el-form-item label="ID">
              {{ this.worker.id }}
            </el-form-item>
            <el-form-item label="姓名">
              {{ this.worker.name }}
            </el-form-item>
            <el-form-item label="年龄">
              {{ this.worker.age }}
            </el-form-item>
            <el-form-item label="Email">
              {{ this.worker.email }}
            </el-form-item>
            <el-form-item label="电话">
              {{ this.worker.phone }}
            </el-form-item>
            <el-form-item>
              <el-button v-if="worker.isWNurse"
                >查看{{ this.worker.name }}所负责的病人</el-button
              >
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
  name: "WorkerInfo",
  components: { navmenu },
  data() {
    return {
      user: [],
      isCreating: true,
      isReading: false,

      workerInfoForm: {
        name: "",
        age: "",
        email: "",
        phone: "",
      },
      rules: {
        name: { required: true, message: "请输入姓名", blur: "change" },
        age: { required: true, message: "请输入年龄", blur: "change" },
        email: { required: true, message: "请输入Email", blur: "change" },
        phone: { required: true, message: "请输入电话", blur: "change" },
      },
      loading: false,

      worker: {
        id: "",
        name: "",
        age: "",
        email: "",
        phone: "",
        isWNurse: false,
      },
    };
  },
  created() {
    this.handleUserData();
    this.handleParams();
    if (this.isReading) {
      this.loadWorkerData();
    }
  },
  methods: {
    handleUserData() {
      if (this.$store.state.user) {
        this.user = this.$store.state.user;
      }
    },
    handleParams() {
      if (this.$route.params.w_id) {
        this.isReading = true;
        this.isCreating = false;
      }
    },
    loadWorkerData() {
      this.$axios
        .get("/workerInfo", {
          params: { id: this.$route.params.w_id },
        })
        .then((resp) => {
          if (resp.status === 200) {
            if (resp.data.worker) {
              this.worker.id = resp.data.worker.id;
              this.worker.name = resp.data.worker.name;
              this.worker.age = resp.data.worker.age;
              this.worker.email = resp.data.worker.email;
              this.worker.phone = resp.data.worker.phone;
              this.worker.isWNurse = resp.data.worker.u_type == "w_nurse";
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