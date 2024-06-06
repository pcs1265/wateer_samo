import { ref } from "vue";
import { defineStore } from "pinia";
import { localAxios } from "@/util/http-commons";

const local = localAxios();

export const useStreamStore = defineStore("stream", () => {
  const sidoList = ref([
    "서울특별시",
    "부산광역시",
    "광주광역시",
    "대전광역시",
    "울산광역시",
    "세종특별자치시",
    "경기도",
    "강원도",
    "충청북도",
    "충청남도",
    "전라북도",
    "전라남도",
    "경상북도",
    "경상남도",
    "제주특별자치도",
  ]);
  const gugunList = ref([]);
  const streams = ref([]);
  const page = ref(1);
  const perPage = 10;
  const totalPage = ref(0);
  const detailStream = ref(null);
  const streamReviews = ref(null);
  const reviewPage = ref(null);
  const reviewTotalPage = ref(null);
  let lastParam = {};

  //   local.get("/attraction/sidoList").then(({ data }) => {
  //     sidoList.value = data;
  //   });

  function getGugunList() {
    local.get("/water/gugunList").then(({ data }) => {
      gugunList.value = data;
    });
  }

  function getStreamListByRegion(param, success, fail) {
    lastParam = param;
    lastParam.searchType = "regionSearch";
    page.value = 1;
    param.page = page.value;
    param.per_page = perPage;
    local.get("/water/regionSearch", { params: param }).then(({ data }) => {
      streams.value = [];
      for (let newAtt of data.waters) {
        newAtt.page = page.value;
        streams.value.push(newAtt);
      }
      totalPage.value = data.totalPageCount;
      if (success != null) success();
    });
    detailStream.value = null;
  }

  function getStreamListByKeyword(param, success, fail) {
    lastParam = param;
    lastParam.searchType = "keywordSearch";
    page.value = 1;
    param.page = page.value;
    param.per_page = perPage;
    local.get("/water/keywordSearch", { params: param }).then(({ data }) => {
      streams.value = [];
      console.log(data);
      for (let newAtt of data.waters) {
        newAtt.page = page.value;
        streams.value.push(newAtt);
      }
      //attractions.value = data.attractions;
      totalPage.value = data.totalPageCount;
      if (success != null) success();
    });
    detailStream.value = null;
  }

  function loadMore() {
    if (lastParam.page == totalPage.value) return;
    page.value = page.value + 1;
    lastParam.page = page.value;
    lastParam.per_page = perPage;
    local.get("/water/" + lastParam.searchType, { params: lastParam }).then(({ data }) => {
      data.waters.forEach((element) => {
        element.page = page.value;
        streams.value.push(element);
        //attractions.value.push(element);
      });
    });
  }

  function getDetail(objtId, success) {
    detailStream.value = null;
    local.get("/water/detail", { params: { objt_id: objtId } }).then(({ data }) => {
      detailStream.value = data;
      if (success != null) {
        success();
      }
    });
  }

  function getReviewList(objtId, success) {
    local
      .get("/water/reviewList", {
        params: { objt_id: objtId, perPage: perPage.value },
      })
      .then(({ data }) => {
        streamReviews.value = data.reviews;
        reviewPage.value = data.currentPage;
        reviewTotalPage.value = data.totalPageCount;
        if (success != null) {
          success();
        }
      });
  }

  function writeReview(data, success) {
    local
      .post("/water/review", data, {
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
    streams.value = [];
    page.value = 1;
    totalPage.value = 0;
    detailStream.value = null;
  }

  return {
    sidoList,
    gugunList,
    streams,
    getGugunList,
    getStreamListByRegion,
    getStreamListByKeyword,
    loadMore,
    getDetail,
    getReviewList,
    writeReview,
    streamReviews,
    page,
    totalPage,
    detailStream,
    init,
  };
});
