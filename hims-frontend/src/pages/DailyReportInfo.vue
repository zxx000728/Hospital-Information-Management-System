<template>
  <el-container>
    <el-header><navmenu></navmenu></el-header>
    <el-main>
      <el-row type="flex" justify="center">
        <el-col align="left">
          <el-page-header
            @back="goBack"
            :content="'登记' + this.p_name + '每日信息'"
          >
          </el-page-header>
        </el-col>
      </el-row>
      <el-row type="flex" justify="center">
        <el-col :span="8">
          <el-form
            @submit.native.prevent
            :model="drInfoForm"
            :rules="rules"
            status-icon
            ref="drInfoForm"
            label-width="100px"
            v-loading="loading"
          >
            <el-form-item label="登记日期" prop="testDate">
              <el-date-picker
                v-model="drInfoForm.date"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="选择日期"
                :picker-options="datePickerOptions"
              >
              </el-date-picker
            ></el-form-item>
            <el-form-item label="体温" prop="temperature">
              <el-input v-model="drInfoForm.temperature"></el-input>
            </el-form-item>
            <el-form-item label="存在症状" prop="symptom">
              <el-input v-model="drInfoForm.symptom"></el-input>
            </el-form-item>

            <el-form-item>
              <el-button
                native-type="submit"
                type="primary"
                @click="submitForm('drInfoForm')"
                >提交每日信息登记</el-button
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
  name: "DailyReportInfo",
  components: { navmenu },
  data() {
    return {
      user: [],
      r_id: null,
      p_id: null,
      p_name: null,

      drInfoForm: {
        date: "",
        temperature: "",
        symptom: "",
      },
      datePickerOptions: {
        disabledDate(time) {
          return time.getTime() > Date.now();
        },
      },

      rules: {
        date: {
          required: true,
          message: "请输入日期",
          blur: "change",
        },
        temperature: {
          required: true,
          message: "请输入体温",
          blur: "change",
        },
        symptom: {
          required: true,
          message: "请输入症状",
          blur: "change",
        },
      },
      loading: false,
    };
  },
  created() {
    this.handleUserData();
    this.handleParams();
  },
  methods: {
    handleUserData() {
      if (this.$store.state.user) {
        this.user = this.$store.state.user;
      }
    },
    handleParams() {
      if (this.$route.params.p_id && this.$route.params.p_name) {
        this.p_id = this.$route.params.p_id;
        this.p_name = this.$route.params.p_name;
      }
    },

    goBack() {
      this.$router.push("/drDataPanel/" + this.p_id + "&" + this.p_name);
    },

    submitForm(formName) {
      this.loading = true;
      this.$axios
        .get("/addDailyReport", {
          params: {
            p_id: this.p_id,
            date: this.drInfoForm.date,
            temperature: this.drInfoForm.temperature,
            symptom: this.drInfoForm.symptom,
            w_nurse_id: this.user.id,
          },
        })
        .then((resp) => {
          this.loading = false;
          if (resp.status === 200) {
            this.$router.push("/drDataPanel/" + this.p_id + "&" + this.p_name);
          } else {
            this.$message.error("请求错误，请重试");
          }
        })
        .catch((error) => {
          console.log(error);
          if (error.response.status == 400) {
            this.$message.error("当日信息登记已存在！");
          } else {
            this.$message.error("请求错误，请重试");
          }
        });
    },
  },
};
</script>

<style>
</style>