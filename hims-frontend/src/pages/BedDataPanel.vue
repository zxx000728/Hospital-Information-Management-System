<template>
  <el-container>
    <el-header><navmenu></navmenu></el-header>
    <el-main>
      <el-row type="flex" justify="center">
        <el-col type="flex" justify="center">
          <el-table :data="tableData" align="center" empty-text="暂无数据">
            <el-table-column prop="id" label="病床号" width="150">
            </el-table-column>
            <el-table-column prop="w_id" label="病房号" width="150">
            </el-table-column>
            <el-table-column prop="state" label="状态" width="150">
            </el-table-column>
            <el-table-column prop="p_id" label="病人ID" width="150">
            </el-table-column>
            <el-table-column prop="p_name" label="病人姓名" width="150">
            </el-table-column>
            <el-table-column align="right">
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
  name: "BedDataPanel",
  components: { navmenu },
  data() {
    return {
      user: [],
      tableData: [],
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
    },
    loadTableData() {
      this.$axios
        .get("/bedDataPanel", {
          params: { id: this.user.id },
        })
        .then((resp) => {
          if (resp.status === 200) {
            var index = -1;
            resp.data.bed.forEach((element) => {
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

    loadRow(index, bed) {
      this.tableData.push({
        id: bed.id,
        w_id: bed.w_id,
        state: this.parseState(bed.state),
        p_id: bed.p_id == 0 ? "" : bed.p_id,
        p_name: bed.p_name == null ? "" : bed.p_name,
      });
    },
    parseState(state) {
      switch (state) {
        case "occupied":
          return "使用中";
        case "free":
          return "空闲";
        default:
          return "未知";
      }
    },
  },
};
</script>

<style>
</style>