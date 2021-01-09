<template>
  <el-container>
    <el-header><navmenu></navmenu></el-header>
    <el-main>
      <el-row type="flex" justify="center">
        <el-col align="left">
          <el-page-header
            @back="goBack"
            :content="'病人详情：' + this.patient.name"
          >
          </el-page-header>
        </el-col>
      </el-row>
      <el-row type="flex" justify="center"
        ><el-col :span="8">
          <el-form
            v-if="isCreating"
            @submit.native.prevent
            :model="patientInfoForm"
            :rules="rules"
            status-icon
            ref="patientInfoForm"
            label-width="150px"
            v-loading="loading"
          >
            <el-form-item label="导入人ID" prop="e_nurse_id">
              <el-input
                v-model="patientInfoForm.e_nurse_id"
                :disabled="true"
              ></el-input>
            </el-form-item>
            <el-form-item label="姓名" prop="name">
              <el-input v-model="patientInfoForm.name"></el-input>
            </el-form-item>
            <el-form-item label="年龄" prop="age">
              <el-input v-model="patientInfoForm.age"></el-input>
            </el-form-item>
            <el-form-item label="电话" prop="phone">
              <el-input v-model="patientInfoForm.phone"></el-input>
            </el-form-item>
            <el-form-item label="地址" prop="address">
              <el-input v-model="patientInfoForm.address"></el-input>
            </el-form-item>
            <el-form-item label="病情评级" prop="rating">
              <el-select v-model="patientInfoForm.rating" placeholder="请选择">
                <el-option
                  v-for="item in ratingOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                >
                </el-option> </el-select
            ></el-form-item>
            <el-form-item label="核酸检测结果" prop="NATResult">
              <el-select
                v-model="patientInfoForm.NATResult"
                placeholder="请选择"
              >
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
                v-model="patientInfoForm.testDate"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="选择日期"
                :picker-options="datePickerOptions"
              >
              </el-date-picker
            ></el-form-item>
            <el-form-item label="核酸检测时间" prop="testTime">
              <el-time-picker
                v-model="patientInfoForm.testTime"
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
                @click="submitForm('patientInfoForm')"
                >提交新病人</el-button
              >
            </el-form-item>
          </el-form>
        </el-col>
        <!-- <el-form v-if="isReading">
            <el-form-item label="ID">
              {{ this.patient.id }}
            </el-form-item>
            <el-form-item label="姓名">
              {{ this.patient.name }}
            </el-form-item>
            <el-form-item label="年龄">
              {{ this.patient.age }}
            </el-form-item>
            <el-form-item label="Email">
              {{ this.patient.email }}
            </el-form-item>
            <el-form-item label="电话">
              {{ this.patient.phone }}
            </el-form-item>

          </el-form> -->
      </el-row>
    </el-main>
  </el-container>
</template>

<script>
import navmenu from "../components/Nav.vue";

export default {
  name: "PatientInfo",
  components: { navmenu },
  data() {
    return {
      user: [],
      isCreating: true,
      isReading: false,

      patientInfoForm: {
        e_nurse_id: "",
        name: "",
        age: "",
        phone: "",
        address: "",
        rating: "",
        NATResult: "",
        testDate: "",
        testTime: "",
      },

      ratingOptions: [
        {
          value: "mild",
          label: "轻症",
        },
        {
          value: "severe",
          label: "重症",
        },
        {
          value: "critical",
          label: "危重症",
        },
      ],
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
        e_nurse_id: {
          required: true,
          message: "请输入导入人ID",
          blur: "change",
        },
        name: { required: true, message: "请输入姓名", blur: "change" },
        age: { required: true, message: "请输入年龄", blur: "change" },
        phone: { required: true, message: "请输入电话", blur: "change" },
        address: { required: true, message: "请输入地址", blur: "change" },
        rating: { required: true, message: "请输入病情评级", blur: "change" },
        NATResult: {
          required: true,
          message: "请输入核酸检测结果",
          blur: "change",
        },
        testDate: {
          required: true,
          message: "请输入核酸检测日期",
          blur: "change",
        },
        testTime: {
          required: true,
          message: "请输入核酸检测时间",
          blur: "change",
        },
      },
      loading: false,

      patient: {
        id: "",
        e_nurse_id: "",
        name: "新建",
        age: "",
        phone: "",
        address: "",
        rating: "",
        NATResult: "",
        testDate: "",
        testTime: "",
      },
    };
  },
  created() {
    this.handleUserData();
    this.handleParams();
    if (this.isReading) {
      this.loadPatientData();
    }
  },
  methods: {
    handleUserData() {
      if (this.$store.state.user) {
        this.user = this.$store.state.user;
      }
    },
    handleParams() {
      if (this.$route.params.p_id) {
        this.isReading = true;
        this.isCreating = false;
      } else {
        if (this.user.u_type == "e_nurse") {
          this.patientInfoForm.e_nurse_id = this.user.id.toString();
        }
      }
    },
    loadPatientData() {
      // this.$axios
      //   .get("/patientInfo", {
      //     params: { id: this.$route.params.p_id },
      //   })
      //   .then((resp) => {
      //     if (resp.status === 200) {
      //       if (resp.data.patient) {
      //         this.patient.id = resp.data.patient.id;
      //         this.patient.name = resp.data.patient.name;
      //         this.patient.age = resp.data.patient.age;
      //         this.patient.email = resp.data.patient.email;
      //         this.patient.phone = resp.data.patient.phone;
      //         this.patient.isWNurse = resp.data.patient.u_type == "w_nurse";
      //       } else {
      //         this.$message.error("请求错误，请重试");
      //       }
      //     } else {
      //       this.$message.error("请求错误，请重试");
      //     }
      //   })
      //   .catch((error) => {
      //     console.log(error);
      //     this.$message.error("请求错误，请重试");
      //   });
    },

    goBack() {
      this.$router.push("/patientDataPanel");
    },

    submitForm(formName) {
      this.loading = true;
      this.$axios
        .post("/addPatient", {
          request: this.patientInfoForm,
        })
        .then((resp) => {
          if (resp.status === 200) {
            this.loading = false;
            this.$router.push("/patientDataPanel");
          }
        })
        .catch((error) => {
          console.log(error);
        });
    },
  },
};
</script>

<style>
</style>