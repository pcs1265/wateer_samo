import { ref } from "vue";
import { defineStore } from "pinia";
import { localAxios } from "@/util/http-commons";

const local = localAxios();

export const useBeachStore = defineStore("beach", () => {
  const sidoList = ref([]);
  const attractions = ref([]);
  const page = ref(1);
  const perPage = 10;
  const totalPage = ref(0);
  const detailAttraction = ref(null);
  const detailAttractionWeather = ref(null);

  const attractionReviews = ref(null);
  const reviewPage = ref(null);
  const reviewTotalPage = ref(null);
  let lastParam = {};

  function getSidoList(success) {
    local
      .get("/ocean/beachSidoList")
      .then(({ data }) => {
        sidoList.value = data;
      })
      .then(() => {
        if (success != null) {
          success();
        }
      });
  }

  function getAttractionListByRegion(param, success, fail) {
    local.get("/ocean/beachList", { params: param }).then(({ data }) => {
      attractions.value = data;
      console.log(data);
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
    local
      .get("/attraction/keywordSearch", { params: param })
      .then(({ data }) => {
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
    local
      .get("/attraction/" + lastParam.searchType, { params: lastParam })
      .then(({ data }) => {
        data.attractions.forEach((element) => {
          element.page = page.value;
          attractions.value.push(element);
          //attractions.value.push(element);
        });
      });
  }

  function getRandomList(param, success, fail) {
    local.get("/ocean/randomBeachList").then(({ data }) => {
      attractions.value = data;
      console.log(data);
      if (success != null) success();
    });
    detailAttraction.value = null;
  }

  function getDetail(id, success) {
    if (detailAttraction.value != null) {
      detailAttraction.value.isLoading = true;
    }
    local.get("/ocean/beachDetail", { params: { id } }).then(({ data }) => {
      detailAttraction.value = data;
      if (success != null) {
        success();
      }
      console.log('??');
      getWeather(id);
    });
  }

  function getWeather(id, success) {
    //detailAttraction.value = null;
    detailAttractionWeather.value = null;
    local.get("/ocean/beachWeather", { params: { id } }).then(({ data }) => {
      detailAttractionWeather.value = data;
      let arr = {};
      for (let i of data.ultraShortWeather.item) {
        if (arr[i.category] === undefined) {
          arr[i.category] = {};
        }
        arr[i.category][i.fcstTime] = i.fcstValue;
      }
      detailAttractionWeather.value.usw = arr;
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

    detailAttraction.value = null;
    detailAttractionWeather.value = null;
  }
  return {
    sidoList,
    attractions,
    getSidoList,
    getAttractionListByRegion,
    getAttractionListByKeyword,
    getRandomList,
    loadMore,
    getDetail,
    getWeather,
    getReviewList,
    writeReview,
    attractionReviews,
    page,
    totalPage,
    detailAttraction,
    detailAttractionWeather,
    init,
  };
});
