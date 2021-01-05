<template>
  <el-menu mode="horizontal" router>
    <el-menu-item index="0"><strong>医院信息管理系统</strong></el-menu-item>
    <el-menu-item index="/workerDataPanel">医护面板</el-menu-item>
    <el-menu-item index="2">病人面板</el-menu-item>
    <el-menu-item index="3">床位面板</el-menu-item>
    <el-menu-item index="4">消息</el-menu-item>
    <el-menu-item index="5" style="float: right">登出</el-menu-item>
    <el-menu-item index="6" style="float: right">我的</el-menu-item>
  </el-menu>
</template>

<script>
export default {
  name: "navmenu",
};
</script>

<style>
</style>