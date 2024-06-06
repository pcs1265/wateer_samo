import { ref } from "vue";
import { defineStore } from "pinia";
import { kakaoMobilityAxios, localAxios } from "@/util/http-commons";

const local = localAxios();
const kkom = kakaoMobilityAxios();

export const usePlanStore = defineStore("plan", () => {
  const plans = ref([]);
  const detailPlan = ref(null);

  const planRoute = ref(null);

  function getPlanList(param, success, fail) {
    local.get("/plan/planList", { params: param }).then(({ data }) => {
      plans.value = data;
    });
  }

  function makePlan(param, success, fail) {
    local.post("/plan/plan", param).then(({ data }) => {
      console.log(data);
      if (data != "1") {
        alert("계획을 추가하지 못했습니다.");
      } else {
        if (success != null) {
          success();
        }
      }
    });
  }

  function getDetail(contentId, success) {
    detailPlan.value = null;
    local.post("/plan/planDetail", { id: contentId }).then(({ data }) => {
      console.log(data);
      detailPlan.value = data;
      if (success != null) {
        success();
      }
    });
  }

  function addToPlan(data, success) {
    local.post("/plan/planItem", data).then(() => {
      if (success != null) {
        success();
      }
    });
  }

  function getRoute() {
    const data = {
      origin: {
        x: detailPlan.value.items[0].longitude,
        y: detailPlan.value.items[0].latitude,
      },
      destination: {
        x: detailPlan.value.items[detailPlan.value.items.length - 1].longitude,
        y: detailPlan.value.items[detailPlan.value.items.length - 1].latitude,
      },
      waypoints: [],
      priority: "RECOMMEND",
      car_fuel: "GASOLINE",
      car_hipass: false,
      alternatives: false,
      road_details: false,
    };
    for (let i = 1; i < detailPlan.value.items.length - 1; ++i) {
      data.waypoints.push({
        name: detailPlan.value.items[i].name,
        x: detailPlan.value.items[i].longitude,
        y: detailPlan.value.items[i].latitude,
      });
    }
    kkom.post("", data).then(({ data }) => {
      planRoute.value = data;
    });
  }

  function deletePlanItem(data, success) {
    console.log(data);
    local.delete("/plan/planItem", { data }).then(() => {
      if (success != null) {
        success();
      }
    });
  }

  function deletePlan(data, success) {
    local.delete("/plan/plan", { data }).then(() => {
      if (success != null) {
        success();
      }
    });
  }

  function savePlan(data, success) {
    local.put("/plan/plan", data).then(() => {
      if (success != null) {
        success();
      }
    });
  }

  return {
    plans,
    getPlanList,
    makePlan,
    getDetail,
    savePlan,
    detailPlan,
    addToPlan,
    getRoute,
    planRoute,
    deletePlanItem,
    deletePlan,
  };
});
