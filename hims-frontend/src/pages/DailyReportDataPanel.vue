<template>
  <el-container>
    <el-header><navmenu></navmenu></el-header>
    <el-main>
      <el-row type="flex" justify="center">
        <el-col align="left">
          <el-page-header
            @back="goBack"
            :content="this.p_name + '的每日信息登记'"
          >
          </el-page-header>
        </el-col>
      </el-row>
      <el-row type="flex" justify="center">
        <el-col type="flex" justify="center">
          <el-table :data="tableData" align="center" empty-text="暂无数据">
            <el-table-column prop="id" label="每日信息ID" width="120">
            </el-table-column>
            <el-table-column prop="date" label="日期" width="150">
            </el-table-column>
            <el-table-column prop="temperature" label="体温" width="150">
            </el-table-column>
            <el-table-column prop="symptom" label="症状" width="150">
            </el-table-column>
            <el-table-column prop="state" label="生命状态" width="150">
            </el-table-column>
            <el-table-column prop="w_nurse_id" label="登记人ID" width="150">
            </el-table-column>
            <el-table-column align="right">
              <template slot="header" slot-scope="scopeAdd">
                <el-button
                  size="mini"
                  @click="handleAdd(scopeAdd.$index, scopeAdd.row)"
                  >填写新的每日信息</el-button
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
  name: "DailyReportDataPanel",
  components: { navmenu },
  inject: ["reload"],
  data() {
    return {
      user: [],
      tableData: [],

      p_id: null,
      p_name: null,

      loading: false,
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
      }

      if (this.$route.params.p_id && this.$route.params.p_name) {
        this.p_id = this.$route.params.p_id;
        this.p_name = this.$route.params.p_name;
      } else {
        this.$message.error("您所查看的病人不存在！");
      }
    },
    loadTableData() {
      this.$axios
        .get("/getDailyReport", {
          params: { p_id: this.p_id },
        })
        .then((resp) => {
          if (resp.status === 200) {
            var index = -1;
            resp.data.forEach((element) => {
              index++;
              this.loadRow(index, element);
            });
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

    loadRow(index, report) {
      this.tableData.push({
        id: report.id,
        date: report.date,
        temperature: report.temperature,
        symptom: report.symptom,
        state: this.parseState(report.state),
        w_nurse_id: report.w_nurse_id,
      });
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

    goBack() {
      this.$router.push("/patientInfo/" + this.p_id);
    },
    handleAdd(index, row) {
      this.$router.push("/drInfo/" + this.p_id + "&" + this.p_name);
    },
  },
};
</script>

<style>
</style>