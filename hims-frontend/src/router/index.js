import Vue from "vue";
import Router from "vue-router";

import Login from "@/pages/Login";
import WorkerDataPanel from "@/pages/WorkerDataPanel";
import PatientDataPanel from "@/pages/PatientDataPanel";
import BedDataPanel from "@/pages/BedDataPanel";

Vue.use(Router);

export default new Router({
  mode: "history",
  routes: [
    {
      path: "/",
      name: "Login",
      component: Login
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
      path: "*",
      redirect: "/"
    }
  ]
});
