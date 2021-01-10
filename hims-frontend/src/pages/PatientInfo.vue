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
            <el-form-item label="导入人ID" prop="ENurseId">
              <el-input
                v-model="patientInfoForm.ENurseId"
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
          <el-form v-if="isReading">
            <el-form-item label="ID">
              {{ this.patient.id }}
            </el-form-item>
            <el-form-item label="姓名">
              {{ this.patient.name }}
            </el-form-item>
            <el-form-item label="年龄">
              {{ this.patient.age }}
            </el-form-item>
            <el-form-item label="电话">
              {{ this.patient.phone }}
            </el-form-item>
            <el-form-item label="地址">
              {{ this.patient.address }}
            </el-form-item>

            <el-form-item label="病情评级" v-if="!isModifyingRating">
              {{ this.parseRating(this.patient.rating) }}
              <el-button
                v-if="isDoctor && patient.state == 'hospitalized'"
                type="text"
                @click="handleModifyRating()"
                >修改</el-button
              >
            </el-form-item>
            <el-form-item label="病情评级" v-if="isModifyingRating">
              <el-select v-model="tempRating">
                <el-option
                  v-for="item in ratingOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                >
                </el-option>
              </el-select>
              <el-button type="text" @click="submitModifyRating()"
                >提交</el-button
              >
              <el-button type="text" @click="cancelModifyRating()"
                >取消</el-button
              >
            </el-form-item>

            <el-form-item label="生命状态" v-if="!isModifyingState">
              {{ this.parseState(this.patient.state) }}
              <el-button
                v-if="isDoctor && patient.state == 'hospitalized'"
                type="text"
                @click="handleModifyState()"
                >修改</el-button
              >
            </el-form-item>
            <el-form-item label="生命状态" v-if="isModifyingState">
              <el-select v-model="tempState">
                <el-option
                  v-for="item in stateOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                >
                </el-option>
              </el-select>
              <el-button type="text" @click="submitModifyState()"
                >提交</el-button
              >
              <el-button type="text" @click="cancelModifyState()"
                >取消</el-button
              >
            </el-form-item>
            <el-form-item label="待出院">
              {{ this.parseReleased(this.patient.is_to_be_released) }}
            </el-form-item>
            <el-form-item label="待转院">
              {{ this.parseTransferred(this.patient.is_to_be_transferred) }}
            </el-form-item>
            <el-form-item label="导入人ID">
              {{ this.patient.e_nurse_id }}
            </el-form-item>
            <el-form-item label="对应病房护士ID">
              {{ this.patient.w_nurse_id }}
            </el-form-item>
            <el-form-item label="所在病区">
              {{
                this.patient.t_area_id == 0 ? "隔离区" : this.patient.t_area_id
              }}
            </el-form-item>
            <el-form-item label="病房号">
              {{ this.patient.w_id }}
            </el-form-item>
            <el-form-item label="病床号">
              {{ this.patient.bed_id }}
            </el-form-item>
            <el-form-item>
              <el-button @click="handleNat()">查看核酸检测单</el-button>

              <el-button @click="handleDr()">查看每日信息</el-button>
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
  name: "PatientInfo",
  components: { navmenu },
  inject: ["reload"],
  data() {
    return {
      user: [],
      isCreating: true,
      isReading: false,
      isDoctor: false,

      isModifyingState: false,
      tempState: "hospitalized",
      isModifyingRating: false,
      tempRating: null,

      patientInfoForm: {
        ENurseId: "",
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
      stateOptions: [
        {
          value: "hospitalized",
          label: "在院治疗",
        },
        {
          value: "dead",
          label: "病亡",
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
        ENurseId: {
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
        name: "新建",
        age: "",
        phone: "",
        address: "",
        rating: "",
        e_nurse_id: "",
        w_nurse_id: "",
        t_area_id: "",
        w_id: "",
        bed_id: "",
        state: "",
        is_to_be_released: "",
        is_to_be_transferred: "",
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
        this.isDoctor = this.$store.state.user.u_type == "doctor";
      }
    },
    handleParams() {
      if (this.$route.params.p_id) {
        this.isReading = true;
        this.isCreating = false;
      } else {
        if (this.user.u_type == "e_nurse") {
          this.patientInfoForm.ENurseId = this.user.id.toString();
        }
      }
    },
    loadPatientData() {
      this.$axios
        .get("/getPatientInfo", {
          params: { id: this.$route.params.p_id },
        })
        .then((resp) => {
          if (resp.status === 200) {
            if (resp.data) {
              this.patient.id = resp.data.id;
              this.patient.name = resp.data.name;
              this.patient.age = resp.data.age;
              this.patient.phone = resp.data.phone;
              this.patient.address = resp.data.address;
              this.patient.rating = resp.data.rating;
              this.tempRating = resp.data.rating;
              this.patient.e_nurse_id = resp.data.e_nurse_id;
              this.patient.w_nurse_id = resp.data.w_nurse_id;
              this.patient.t_area_id = resp.data.t_area_id;
              this.patient.w_id = resp.data.w_id;
              this.patient.bed_id = resp.data.bed_id;
              this.patient.state = resp.data.state;
              this.patient.is_to_be_transferred =
                resp.data.is_to_be_transferred;
              this.patient.is_to_be_released = resp.data.is_to_be_released;
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

    parseRating(rating) {
      switch (rating) {
        case "mild":
          return "轻症";
        case "severe":
          return "重症";
        case "critical":
          return "危重症";
      }
    },
    parseState(state) {
      switch (state) {
        case "discharge":
          return "康复出院";
        case "hospitalized":
          return "在院治疗";
        case "dead":
          return "病亡";
        default:
          return "隔离区";
      }
    },
    parseReleased(is_to_be_released) {
      switch (is_to_be_released) {
        case true:
          return "是";
        case false:
          return "否";
      }
    },
    parseTransferred(is_to_be_transferred) {
      switch (is_to_be_transferred) {
        case true:
          return "是";
        case false:
          return "否";
      }
    },

    goBack() {
      this.$router.push("/patientDataPanel");
    },
    handleNat() {
      this.$router.push(
        "/natDataPanel/" + this.patient.id + "&" + this.patient.name
      );
    },
    handleDr() {
      this.$router.push(
        "/drDataPanel/" + this.patient.id + "&" + this.patient.name
      );
    },

    submitForm(formName) {
      this.loading = true;
      this.$axios
        .get("/addPatient", {
          params: {
            ENurseId: this.user.id.toString(),
            name: this.patientInfoForm.name,
            age: this.patientInfoForm.age,
            phone: this.patientInfoForm.phone,
            address: this.patientInfoForm.address,
            NATResult: this.patientInfoForm.NATResult,
            rating: this.patientInfoForm.rating,
            testDate: this.patientInfoForm.testDate,
            testTime: this.patientInfoForm.testTime,
          },
        })
        .then((resp) => {
          this.loading = false;
          if (resp.status === 200) {
            this.$router.push("/patientDataPanel");
          } else {
            this.$message.error("请求错误，请重试");
          }
        })
        .catch((error) => {
          console.log(error);
          this.$message.error("请求错误，请重试");
        });
    },

    handleModifyState() {
      this.isModifyingState = true;
    },
    submitModifyState() {
      this.$axios
        .get("/modifyPatientState", {
          params: {
            id: this.patient.id.toString(),
            state: this.tempState,
          },
        })
        .then((resp) => {
          if (resp.status === 200) {
            this.$message.info("修改成功！");
            this.reload();
          }
        })
        .catch((error) => {
          console.log(error);
          this.$message.error("请求错误，请重试");
        });
    },
    cancelModifyState() {
      this.isModifyingState = false;
      this.tempState = "hospitalized";
    },

    handleModifyRating() {
      this.isModifyingRating = true;
    },
    submitModifyRating() {
      this.$axios
        .get("/modifyPatientRating", {
          params: {
            id: this.patient.id.toString(),
            rating: this.tempRating,
          },
        })
        .then((resp) => {
          if (resp.status === 200) {
            this.$message.info("修改成功！");
            this.reload();
          }
        })
        .catch((error) => {
          console.log(error);
          this.$message.error("请求错误，请重试");
        });
    },
    cancelModifyRating() {
      this.isModifyingRating = false;
      this.tempRating = this.patient.rating;
    },
  },
};
</script>

<style>
</style>