<template>
  <el-container>
    <el-header><navmenu></navmenu></el-header>
    <el-main>
      <el-row type="flex" justify="center">
        <el-col type="flex" justify="center"> </el-col>
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
    };
  },
  created() {},
  methods: {},
};
</script>

<style>
</style>