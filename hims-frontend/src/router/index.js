import Vue from "vue";
import Router from "vue-router";

import Login from "@/pages/Login";
import WorkerDataPanel from "@/pages/WorkerDataPanel";
import PatientDataPanel from "@/pages/PatientDataPanel";
import BedDataPanel from "@/pages/BedDataPanel";
import WorkerInfo from "@/pages/WorkerInfo";

Vue.use(Router);

export default new Router({
  mode: "history",
  base: process.env.BASE_URL,
  routes: [
    {
      path: "/",
      name: "Main",
      component: PatientDataPanel
    },
    {
      path: "/login",
      name: "Login",
      component: Login
    },
    {
      path: "/workerDataPanel",
      name: "WorkerDataPanel",
      component: WorkerDataPanel
    },
    {
      path: "/patientDataPanel",
      name: "PatientDataPanel",
      component: PatientDataPanel
    },
    {
      path: "/bedDataPanel",
      name: "BedDataPanel",
      component: BedDataPanel
    },
    {
      path: "/workerInfo",
      name: "WorkerInfo",
      component: WorkerInfo
    },
    {
      path: "/workerInfo/:w_id",
      name: "WorkerInfo",
      component: WorkerInfo
    },
    {
      path: "*",
      redirect: "/"
    }
  ]
});
