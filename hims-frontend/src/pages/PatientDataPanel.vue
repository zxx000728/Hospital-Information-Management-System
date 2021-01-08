<template>
  <el-container>
    <el-header><navmenu></navmenu></el-header>
    <el-main>
      <el-row type="flex" justify="center">
        <el-col type="flex" justify="center">
          <el-table :data="tableData" align="center" empty-text="暂无数据">
            <el-table-column prop="id" label="ID" width="120">
            </el-table-column>
            <el-table-column prop="name" label="姓名" width="150">
            </el-table-column>
            <el-table-column prop="age" label="年龄" width="120">
            </el-table-column>
            <el-table-column prop="rating" label="病情评级" width="150">
            </el-table-column>
            <el-table-column
              prop="state"
              label="生命状态"
              width="150"
              :filters="[
                { text: '康复出院', value: '康复出院' },
                { text: '在院治疗', value: '在院治疗' },
                { text: '病亡', value: '病亡' },
              ]"
              :filter-method="filterState"
              filter-placement="bottom-end"
            >
            </el-table-column>
            <el-table-column
              prop="is_to_be_released"
              label="待出院"
              width="150"
              :filters="[
                { text: '是', value: '是' },
                { text: '否', value: '否' },
              ]"
              :filter-method="filterReleased"
              filter-placement="bottom-end"
            >
            </el-table-column>
            <el-table-column
              prop="is_to_be_transferred"
              label="待转区"
              width="150"
              :filters="[
                { text: '是', value: '是' },
                { text: '否', value: '否' },
              ]"
              :filter-method="filterTransferred"
              filter-placement="bottom-end"
            >
            </el-table-column>
            <el-table-column align="right">
              <template slot="header" slot-scope="scope">
                <el-button
                  size="mini"
                  @click="handleAdd(scope.$index, scope.row)"
                  v-if="isENurse"
                  >添加</el-button
                >
              </template>
              <template slot-scope="scope">
                <el-button
                  size="mini"
                  @click="handleEdit(scope.$index, scope.row)"
                  >详情</el-button
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
  name: "PatientDataPanel",
  components: { navmenu },
  data() {
    return {
      user: [],
      tableData: [],
      isENurse: false,
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
        this.isENurse = this.user.u_type == "e_nurse";
      }
    },
    loadTableData() {
      this.$axios
        .get("/patientDataPanel", {
          params: { id: this.user.id, type: this.user.u_type },
        })
        .then((resp) => {
          if (resp.status === 200) {
            var index = -1;
            resp.data.patient.forEach((element) => {
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

    loadRow(index, patient) {
      this.tableData.push({
        id: patient.id,
        name: patient.name,
        age: patient.age,
        rating: this.parseRating(patient.rating),
        state: this.parseState(patient.state),
        is_to_be_released: this.parseReleased(patient.is_to_be_released),
        is_to_be_transferred: this.parseTransferred(
          patient.is_to_be_transferred
        ),
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

    filterState(value, row) {
      return row.state === value;
    },
    filterReleased(value, row) {
      return row.is_to_be_released === value;
    },
    filterTransferred(value, row) {
      return row.is_to_be_transferred === value;
    },
  },
};
</script>

<style>
</style>