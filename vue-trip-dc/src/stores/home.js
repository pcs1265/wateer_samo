import { localAxios } from "@/util/http-commons";
import { defineStore } from "pinia";
import { ref } from "vue";

const local = localAxios();

export const useHomeStore = defineStore("home", () => {
  const topAttr = ref([]);

  const randAttr = ref(null);
  const randBeach = ref(null);
  const randStream = ref(null);

  const topBoard = ref([]);
  const wishList = ref([]);
  const plans = ref([]);

  const getTopAttr = async () => {
    local.get("/attraction/listPopular").then(({ data }) => {
      topAttr.value = data;
    });
  };

  const getRandData = async () => {
    local.get("/attraction/random").then(({ data }) => {
      randAttr.value = data;
    });
    local.get("/ocean/random").then(({ data }) => {
      randBeach.value = data;
    });
    local.get("/water/random").then(({ data }) => {
      randStream.value = data;
    });
  };

  const getTopArticle = async () => {
    local.get("/board/listTopArticle").then(({ data }) => {
      topBoard.value = data;
    });
  };

  const getWishList = async (userId) => {
    wishList.value = [];
    local.get(`attraction/wish/${userId}`).then(({ data }) => {
      wishList.value = data;
    });
  };

  const getPlanList = async (param, success, fail) => {
    local.get("/plan/planList", { params: param }).then(({ data }) => {
      plans.value = data;
    });
  };

  return {
    topAttr,
    randAttr,
    randBeach,
    randStream,
    topBoard,
    wishList,
    plans,
    getTopAttr,
    getRandData,
    getTopArticle,
    getWishList,
    getPlanList,
  };
});
