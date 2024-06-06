<script setup>
import HomeAttractionItem from "@/components/home/HomeAttractionItem.vue";
import HomeAttractionItemRating from "@/components/home/HomeAttractionItemRating.vue";
import HomeBeachItem from "@/components/home/HomeBeachItem.vue";
import HomeStreamItem from "@/components/home/HomeStreamItem.vue";
import { useHomeStore } from "@/stores/home";
import { useUserStore } from "@/stores/user";
import { localAxios } from "@/util/http-commons";
import { storeToRefs } from "pinia";
import { onMounted, ref } from "vue";

const bg = ref();
const onScroll = (e) => {
  var distanceScrolled = e.target.scrollTop;
  bg.value.style.filter = `blur(${Math.min(
    (distanceScrolled * 10) / document.documentElement.clientHeight,
    10
  )}px)`;
};

const uStore = useUserStore();
const hStore = useHomeStore();

const { topAttr, randAttr, randBeach, randStream, topBoard, wishList, plans } =
  storeToRefs(hStore);

onMounted(() => {
  hStore.getTopAttr();
  hStore.getRandData();
  hStore.getTopArticle();

  if (uStore.isLogin) {
    hStore.getWishList(uStore.userInfo.userId);
    const param = { user_id: uStore.userInfo.userId };
    hStore.getPlanList(param);
  }
});
</script>

<template>
  <div class="bg-video">
    <video class="bg-video__content" ref="bg" autoplay muted loop>
      <source src="@/assets/home_bg.mp4" type="video/mp4" />
      Your browser is not supported!
    </video>
  </div>
  <div class="vh-100 overflow-y-scroll" @scroll="onScroll">
    <div class="vh-100" style="background-color: rgba(0, 0, 0, 0.3)">
      <div
        class="d-flex flex-column justify-content-center align-items-center title-area"
        style="height: 90%"
      >
        <div
          class="noto-sans-kr text-light"
          style="font-size: 3rem; font-weight: 500"
        >
          물을 사랑하는 사람들
        </div>
        <div class="noto-sans-kr-header text-light">WATEER SAMO</div>
      </div>
      <div
        style="height: 10%"
        class="d-flex justify-content-center align-items-center"
      >
        <i class="bi bi-chevron-double-down text-light h1"></i>
      </div>
    </div>
    <div class="content-area">
      <div class="container">
        <div class="d-flex flex-column">
          <div
            class="noto-sans-kr text-light mt-5"
            style="font-size: 3rem; font-weight: 700"
          >
            인기 많은 관광지
          </div>
          <div class="row">
            <HomeAttractionItemRating
              v-for="attr in topAttr"
              :key="attr.contentId"
              :attraction="attr"
            ></HomeAttractionItemRating>
          </div>

          <div
            class="noto-sans-kr text-light mt-5"
            style="font-size: 3rem; font-weight: 700"
          >
            여기 어때요?
          </div>
          <div class="row">
            <HomeAttractionItem
              v-if="randAttr != null"
              :attraction="randAttr"
            ></HomeAttractionItem>
            <HomeBeachItem
              v-if="randBeach != null"
              :attraction="randBeach"
            ></HomeBeachItem>
            <HomeStreamItem
              v-if="randStream != null"
              :attraction="randStream"
            ></HomeStreamItem>
          </div>

          <div v-if="uStore.isLogin">
            <div
              class="noto-sans-kr text-light mt-5"
              style="font-size: 3rem; font-weight: 700"
            >
              내가 찜한 관광지
            </div>
            <div class="row">
              <HomeAttractionItem
                v-for="attr in wishList.slice(0, 9)"
                :key="attr.contentId"
                :attraction="attr"
              ></HomeAttractionItem>
            </div>
            <div
              class="noto-sans-kr text-light mt-5"
              style="font-size: 3rem; font-weight: 700"
            >
              내 여행 계획
            </div>
            <div class="row">
              <div
                class="col-3"
                v-for="plan in plans.slice(0, 9)"
                :key="plan.id"
              >
                <RouterLink
                  class="bg-light rounded-3 d-flex flex-column justify-content-top align-items-center text-decoration-none text-dark p-0 m-3"
                  :to="{ name: 'plan-detail', params: { contentId: plan.id } }"
                >
                  <div
                    class="border border-secondary w-100 text-center p-3 rounded-3 bg-secondary text-light shadow row"
                  >
                    <div class="m-0 fw-bold col-12">{{ plan.name }}</div>
                  </div>
                  <div
                    class="my-2"
                    v-for="spot in plan.items.slice(0, 3)"
                    :key="spot.id"
                  >
                    {{ spot.name }}
                  </div>

                  <p class="my-1" v-if="plan.items.length > 3">
                    ...등 {{ plan.items.length }} 개 장소
                  </p>

                  <p class="mt-3" v-if="plan.items.length == 0">
                    계획이 비어있습니다.
                  </p>
                </RouterLink>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="container">
        <div class="d-flex flex-column">
          <div
            class="noto-sans-kr text-light mt-5"
            style="font-size: 3rem; font-weight: 700"
          >
            게시판 인기글
          </div>
          <div class="row">
            <div class="col-12 py-2 px-3">
              <div class="bg-light rounded-3 p-3">
                <div class="row">
                  <div class="col-8 text-center fw-bold">제목</div>
                  <div class="col-1 text-center fw-bold">작성자</div>
                  <div class="col-1 text-center fw-bold">조회수</div>
                  <div class="col-2 text-center fw-bold">작성일</div>
                </div>
              </div>
            </div>
            <RouterLink
              class="col-12 py-2 px-3 text-decoration-none text-black"
              v-for="board in topBoard"
              :key="board.no"
              :to="{ name: 'article-view', params: { no: board.no } }"
            >
              <div class="bg-light rounded-3 p-3">
                <div class="row">
                  <div class="col-8 px-5">{{ board.title }}</div>
                  <div class="col-1 text-center">{{ board.nickname }}</div>
                  <div class="col-1 text-center">{{ board.hits }}</div>
                  <div class="col-2 text-center">{{ board.writeDate }}</div>
                </div>
              </div>
            </RouterLink>
          </div>
        </div>
      </div>

      <div class="p-5"></div>

      <div class="bg-dark text-light">
        <div class="container p-5">개발자 : 강도원, 박찬수</div>
      </div>
    </div>
  </div>
</template>

<style scoped>
@import url("https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@100..900&display=swap");

.noto-sans-kr-header {
  font-family: "Noto Sans KR", sans-serif;
  font-optical-sizing: auto;
  font-weight: 700;
  font-style: normal;
  font-size: 7rem;
  line-height: 5rem;
}

.noto-sans-kr {
  font-family: "Noto Sans KR", sans-serif;
  font-optical-sizing: auto;
}

.bg-video {
  position: fixed;
  top: 0;
  left: 0;
  height: 100%;
  width: 100%;
  z-index: -1;
}

.bg-video__content {
  height: 100%;
  width: 100%;
  object-fit: cover;
  transform: scale(1.05);
}

.title-area {
}

.content-area {
  background-color: rgba(0, 0, 0, 0.8);
  backdrop-filter: blur(10px);
  box-shadow: 0px 0px 100px 20px rgba(0, 0, 0, 0.5);
}
</style>
