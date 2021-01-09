<template>
  <el-container>
    <el-header><navmenu></navmenu></el-header>
    <el-main>
      <el-row type="flex" justify="center">
        <el-col align="left">
          <el-page-header
            @back="goBack"
            :content="'编辑' + this.p_name + '核酸检测单详情'"
          >
          </el-page-header>
        </el-col>
      </el-row>
      <el-row type="flex" justify="center">
        <el-col :span="8">
          <el-form
            @submit.native.prevent
            :model="natInfoForm"
            :rules="rules"
            status-icon
            ref="natInfoForm"
            label-width="100px"
            v-loading="loading"
          >
            <el-form-item label="核酸检测结果" prop="NATResult">
              <el-select v-model="natInfoForm.result" placeholder="请选择">
                <el-option
                  v-for="item in NATResultOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                >
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="核酸检测日期" prop="testDate">
              <el-date-picker
                v-model="natInfoForm.date"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="选择日期"
                :picker-options="datePickerOptions"
              >
              </el-date-picker
            ></el-form-item>
            <el-form-item label="核酸检测时间" prop="testTime">
              <el-time-picker
                v-model="natInfoForm.time"
                value-format="HH:mm:ss"
                :picker-options="{
                  selectableRange: '00:00:00 - 23:59:59',
                }"
                placeholder="选择时间"
              >
              </el-time-picker
            ></el-form-item>
            <el-form-item>
              <el-button
                native-type="submit"
                type="primary"
                @click="submitForm('natInfoForm')"
                >提交核酸检测单</el-button
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
  name: "NatInfo",
  components: { navmenu },
  data() {
    return {
      user: [],
      r_id: null,
      p_id: null,
      p_name: null,

      natInfoForm: {
        result: "",
        date: "",
        time: "",
      },
      NATResultOptions: [
        {
          value: "positive",
          label: "阳性",
        },
        {
          value: "negative",
          label: "阴性",
        },
        {
          value: null,
          label: "未知",
        },
      ],
      datePickerOptions: {
        disabledDate(time) {
          return time.getTime() > Date.now();
        },
      },

      rules: {
        result: {
          required: true,
          message: "请输入核酸检测结果",
          blur: "change",
        },
        date: {
          required: true,
          message: "请输入核酸检测日期",
          blur: "change",
        },
        time: {
          required: true,
          message: "请输入核酸检测时间",
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
      if (
        this.$route.params.r_id &&
        this.$route.params.p_id &&
        this.$route.params.p_name
      ) {
        this.r_id = this.$route.params.r_id;
        this.p_id = this.$route.params.p_id;
        this.p_name = this.$route.params.p_name;
      }
    },

    goBack() {
      this.$router.push("/natDataPanel/" + this.p_id + "&" + this.p_name);
    },

    submitForm(formName) {
      this.loading = true;
      this.$axios
        .get("/fillNATReport", {
          params: {
            id: this.r_id,
            result: this.natInfoForm.result,
            date: this.natInfoForm.date,
            time: this.natInfoForm.time,
          },
        })
        .then((resp) => {
          this.loading = false;
          if (resp.status === 200) {
            this.$router.push("/natDataPanel/" + this.p_id + "&" + this.p_name);
          } else {
            this.$message.error("请求错误，请重试");
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