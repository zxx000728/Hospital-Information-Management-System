<template>
  <el-container>
    <el-header><navmenu></navmenu></el-header>
    <el-main>
      <el-row type="flex" justify="center">
        <el-col align="left">
          <el-page-header
            @back="goBack"
            :content="this.p_name + '的核酸检测单'"
          >
          </el-page-header>
        </el-col>
      </el-row>
      <el-row type="flex" justify="center">
        <el-col type="flex" justify="center">
          <el-table :data="tableData" align="center" empty-text="暂无数据">
            <el-table-column prop="id" label="检测单ID" width="120">
            </el-table-column>
            <el-table-column prop="result" label="检测结果" width="150">
            </el-table-column>
            <el-table-column prop="date" label="检测日期" width="150">
            </el-table-column>
            <el-table-column prop="time" label="检测时间" width="150">
            </el-table-column>
            <el-table-column prop="rating" label="病情评级" width="150">
            </el-table-column>
            <el-table-column align="right">
              <template slot="header" slot-scope="scopeAdd">
                <el-button
                  size="mini"
                  @click="handleAdd(scopeAdd.$index, scopeAdd.row)"
                  >添加空白检测单</el-button
                >
              </template>
              <template slot-scope="scopeEdit">
                <el-button
                  v-if="scopeEdit.row.result == ''"
                  size="mini"
                  @click="handleEdit(scopeEdit.$index, scopeEdit.row)"
                  >编辑检测单详情</el-button
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
        .get("/getNATReport", {
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
          this.$message.error("请求错误，请重试");
        });
    },

    loadRow(index, report) {
      this.tableData.push({
        id: report.id,
        result: report.result ? this.parseResult(report.result) : "",
        date: report.date ? report.date : "",
        time: report.time ? report.time : "",
        rating: report.rating ? this.parseRating(report.rating) : "",
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
    parseResult(result) {
      switch (result) {
        case "positive":
          return "阳性";
        case "negative":
          return "阴性";
      }
    },

    goBack() {
      this.$router.push("/patientInfo/" + this.p_id);
    },
    handleAdd(index, row) {
      this.$axios
        .get("/addNATReport", {
          params: {
            p_id: this.p_id.toString(),
          },
        })
        .then((resp) => {
          if (resp.status === 200) {
            this.$message.info("添加成功！");
            this.reload();
          }
        })
        .catch((error) => {
          console.log(error);
          if (error.response.status === 400) {
            this.$message.error("已有空白检测单，不能再新建！");
          } else {
            this.$message.error("请求错误，请重试");
          }
        });
    },
    handleEdit(index, row) {
      this.$router.push(
        "/natInfo/" + this.p_id + "&" + this.p_name + "/" + row.id
      );
    },
  },
};
</script>

<style>
</style>