<template>
  <el-container>
    <el-header><navmenu></navmenu></el-header>
    <el-main>
      <el-row type="flex" justify="center">
        <el-col type="flex" justify="center">
          <el-table :data="tableData" align="center" empty-text="暂无数据">
            <el-table-column prop="type" label="消息类型" width="150">
            </el-table-column>
            <el-table-column prop="p_id" label="所涉病人ID" width="150">
            </el-table-column>
            <el-table-column prop="t_area_id" label="目标病区" width="150">
            </el-table-column>
            <el-table-column prop="content" label="消息内容" width="600">
            </el-table-column>
            <el-table-column align="right">
              <template slot-scope="scope">
                <el-button
                  size="mini"
                  @click="handleEdit(scope.$index, scope.row)"
                  >查看对应病人</el-button
                >
                <el-button
                  size="mini"
                  @click="handleConfirm(scope.$index, scope.row)"
                  >确认</el-button
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
  name: "Messages",
  components: { navmenu },
  inject: ["reload"],
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
        .get("/getMessage", {
          params: { id: this.user.id },
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

    loadRow(index, message) {
      this.tableData.push({
        type: this.parseType(message),
        p_id: message.id,
        t_area_id: this.parseArea(message),
        content: this.parseContent(message),
      });
    },
    parseType(message) {
      if (message.is_to_be_released) {
        return "出院通知";
      } else if (message.is_to_be_transferred) {
        return "转区通知";
      } else {
        return "未知";
      }
    },
    parseArea(message) {
      if (message.is_to_be_transferred) {
        switch (message.rating) {
          case "mild":
            return "1";
          case "severe":
            return "2";
          case "critical":
            return "3";
        }
      } else {
        return "出院";
      }
    },
    parseContent(message) {
      if (message.is_to_be_released) {
        return (
          "您的病人" +
          message.name +
          "（ID为" +
          message.id +
          "）已经可以出院了。是否确认？"
        );
      } else if (message.is_to_be_transferred) {
        return (
          "您的病人" +
          message.name +
          "（ID为" +
          message.id +
          "）将要转入您的病区。是否确认？"
        );
      } else {
        return "未知";
      }
    },

    handleEdit(index, row) {
      this.$router.push("/patientInfo/" + row.p_id);
    },
    handleConfirm(index, row) {
      switch (row.type) {
        case "出院通知":
          this.$axios
            .get("/permitRelease", {
              params: {
                id: row.p_id.toString(),
              },
            })
            .then((resp) => {
              if (resp.status === 200) {
                this.$message.info("已确认！");
                this.reload();
              }
            })
            .catch((error) => {
              console.log(error);
              this.$message.error("请求错误，请重试");
            });
          break;
        case "转区通知":
          this.$axios
            .get("/transfer", {
              params: {
                id: row.p_id.toString(),
                t_area_id: row.t_area_id.toString(),
              },
            })
            .then((resp) => {
              if (resp.status === 200) {
                if (resp.data == "No free bed, wait.") {
                  this.$message.info("没有空闲床位，请稍后再试！");
                  this.reload();
                } else {
                  this.$message.info("已确认！");
                  this.reload();
                }
              }
            })
            .catch((error) => {
              console.log(error);
              this.$message.error("请求错误，请重试");
            });
          break;
        default:
          break;
      }
    },
  },
};
</script>

<style>
</style>