import { ref } from "vue";
import { defineStore } from "pinia";
import { localAxios } from "@/util/http-commons";

const local = localAxios();

export const useAttractionStore = defineStore("attraction", () => {
  const sidoList = ref([]);
  const gugunList = ref([]);
  const attractions = ref([]);
  const page = ref(1);
  const perPage = 10;
  const totalPage = ref(0);
  const detailAttraction = ref(null);
  const attractionReviews = ref(null);
  const reviewPage = ref(null);
  const reviewTotalPage = ref(null);

  let lastParam = {};

  local.get("/attraction/sidoList").then(({ data }) => {
    sidoList.value = data;
  });

  function getGugunList(sido) {
    local.get("/attraction/gugunList", { params: { sidoCode: sido } }).then(({ data }) => {
      gugunList.value = data;
    });
  }

  function getAttractionListByRegion(param, success, fail) {
    lastParam = param;
    lastParam.searchType = "regionSearch";
    page.value = 1;
    param.page = page.value;
    param.per_page = perPage;
    local.get("/attraction/regionSearch", { params: param }).then(({ data }) => {
      attractions.value = [];
      for (let newAtt of data.attractions) {
        newAtt.page = page.value;
        attractions.value.push(newAtt);
      }
      totalPage.value = data.totalPageCount;
      if (success != null) success();
    });
    detailAttraction.value = null;
  }

  function getAttractionListByKeyword(param, success, fail) {
    lastParam = param;
    lastParam.searchType = "keywordSearch";
    page.value = 1;
    param.page = page.value;
    param.per_page = perPage;
    local.get("/attraction/keywordSearch", { params: param }).then(({ data }) => {
      attractions.value = [];
      for (let newAtt of data.attractions) {
        newAtt.page = page.value;
        attractions.value.push(newAtt);
      }
      //attractions.value = data.attractions;
      totalPage.value = data.totalPageCount;
      if (success != null) success();
    });
    detailAttraction.value = null;
  }

  function loadMore() {
    if (lastParam.page == totalPage.value) return;
    page.value = page.value + 1;
    lastParam.page = page.value;
    lastParam.per_page = perPage;
    local.get("/attraction/" + lastParam.searchType, { params: lastParam }).then(({ data }) => {
      if (attractions.value.length >= 5 * perPage) {
        attractions.value.splice(0, perPage);
      }
      data.attractions.forEach((element) => {
        element.page = page.value;
        attractions.value.push(element);
        //attractions.value.push(element);
      });
    });
  }

  function getDetail(contentId, success) {
    detailAttraction.value = null;
    local.get("/attraction/detail", { params: { contentId } }).then(({ data }) => {
      detailAttraction.value = data;
      if (success != null) {
        success();
      }
    });
  }

  function getReviewList(contentId, success) {
    local
      .get("/attraction/reviewList", {
        params: { content_id: contentId, perPage: perPage.value },
      })
      .then(({ data }) => {
        attractionReviews.value = data.reviews;
        reviewPage.value = data.currentPage;
        reviewTotalPage.value = data.totalPageCount;
        if (success != null) {
          success();
        }
      });
  }

  function writeReview(data, success) {
    local
      .post("/attraction/review", data, {
        headers: {
          "Content-Type": "multipart/form-data",
        },
        transformRequest: [
          function () {
            return data;
          },
        ],
      })
      .then(() => {
        if (success != null) {
          success();
        }
      });
  }

  function init() {
    attractions.value = [];
    page.value = 1;
    totalPage.value = 0;
    detailAttraction.value = null;
  }

  return {
    sidoList,
    gugunList,
    attractions,
    getGugunList,
    getAttractionListByRegion,
    getAttractionListByKeyword,
    loadMore,
    getDetail,
    getReviewList,
    writeReview,
    attractionReviews,
    page,
    totalPage,
    detailAttraction,
    init,
  };
});
