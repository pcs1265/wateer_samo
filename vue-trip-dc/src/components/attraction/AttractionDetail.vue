<script setup>
import { computed, onMounted, toRefs, ref, watch } from "vue";
import AttractionImage from "./AttractionImage.vue";
import { useRouter } from "vue-router";
import Rating from "./Rating.vue";
import { localAxios } from "@/util/http-commons";
import { useUserStore } from "@/stores/user";
import { useAttractionStore } from "@/stores/attraction";

const local = localAxios();
const props = defineProps({
  attraction: Object,
});

const aStore = useAttractionStore();
const uStore = useUserStore();
const { attraction } = toRefs(props);

const router = useRouter();

const emit = defineEmits(["closed"]);

const heart = ref(false);

onMounted(() => {
  if (uStore.isLogin) {
    const response = local
      .get(
        `attraction/wish/${uStore.userInfo.userId}/${router.currentRoute.value.params.contentId}`
      )
      .then(({ data }) => {
        if (data == 0) {
          heart.value = false;
        } else {
          heart.value = true;
        }
      });
  }
});

const close = () => {
  emit("closed");
  router.replace({ name: "attraction-list" });
};

const firstImage1 = computed(() => {
  if (props.attraction.firstImage == "") {
    return "";
  } else {
    return props.attraction.firstImage;
  }
});

const checkWish = async () => {
  heart.value = !heart.value; // heart의 값을 반전시킵니다.
  if (heart.value == true) {
    const response = await local.post(
      "/attraction/wish",
      JSON.stringify({
        userId: uStore.userInfo.userId,
        itemId: router.currentRoute.value.params.contentId,
      })
    );

    console.log(response);
  } else {
    const response = await local.delete("/attraction/wish", {
      params: {
        userId: uStore.userInfo.userId,
        itemId: router.currentRoute.value.params.contentId,
      },
    });
    console.log(response);
  }
  uStore.getWishList();
};

import { usePlanStore } from "@/stores/plan";
import AddModal from "../common/plan/AddModal.vue";
const pStore = usePlanStore();
const loadPlanList = () => {
  const param = { user_id: uStore.userInfo.userId };
  pStore.getPlanList(param);
};

const addToPlan = (plan) => {
  const data = {
    name: attraction.value.title,
    plan_id: plan.id,
    addr: attraction.value.addr1,
    latitude: attraction.value.latitude,
    longitude: attraction.value.longitude,
    description: "",
    order: 0,
  };
  pStore.addToPlan(data);
};
</script>

<template>
  <div class="h-100">
    <div class="card border-0">
      <div class="overflow-x-scroll pic-list">
        <div
          class="row flex-nowrap pic-list"
          data-bs-toggle="modal"
          :data-bs-target="'#' + attraction.contentId + 'image_modal'"
        >
          <img
            v-if="firstImage1 != ''"
            :src="firstImage1"
            class="col-4 p-0"
            alt="..."
            style="object-fit: cover"
          />
        </div>
      </div>
      <div class="card-body">
        <div class="card-body-header">
          <div class="d-flex justify-content-between">
            <div>
              <div style="display: flex; align-items: center">
                <h5 style="margin-right: 10px">
                  <b>{{ attraction.title }}</b>
                </h5>
                <button
                  type="button"
                  class="btn btn-outline-danger"
                  v-show="!heart"
                  id="heart"
                  @click="checkWish"
                  v-if="uStore.isLogin"
                >
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    width="16"
                    height="16"
                    fill="currentColor"
                    class="bi bi-heart"
                    viewBox="0 0 16 16"
                  >
                    <path
                      d="m8 2.748-.717-.737C5.6.281 2.514.878 1.4 3.053c-.523 1.023-.641 2.5.314 4.385.92 1.815 2.834 3.989 6.286 6.357 3.452-2.368 5.365-4.542 6.286-6.357.955-1.886.838-3.362.314-4.385C13.486.878 10.4.28 8.717 2.01zM8 15C-7.333 4.868 3.279-3.04 7.824 1.143q.09.083.176.171a3 3 0 0 1 .176-.17C12.72-3.042 23.333 4.867 8 15"
                    />
                  </svg>
                </button>
                <button
                  type="button"
                  class="btn btn-outline-danger"
                  v-show="heart"
                  id="fill-heart"
                  @click="checkWish"
                  v-if="uStore.isLogin"
                >
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    width="16"
                    height="16"
                    fill="currentColor"
                    class="bi bi-heart-fill"
                    viewBox="0 0 16 16"
                  >
                    <path
                      fill-rule="evenodd"
                      d="M8 1.314C12.438-3.248 23.534 4.735 8 15-7.534 4.736 3.562-3.248 8 1.314"
                    />
                  </svg>
                </button>
                <button
                  type="button"
                  class="btn btn-primary ms-3"
                  data-bs-toggle="modal"
                  data-bs-target="#addToPlan_attraction"
                  @click="loadPlanList"
                  v-if="uStore.isLogin"
                >
                  계획에 추가
                </button>
              </div>

              <RouterLink :to="{ name: 'attraction-review' }">
                <Rating :score="attraction.reviewRating" :number="attraction.reviewNumber"></Rating>
              </RouterLink>
            </div>
            <button class="btn-close h5 m-0" aria-label="Close" @click="close"></button>
          </div>

          <h6 class="card-title mt-2">{{ attraction.addr1 }}</h6>
        </div>

        <div class="card-text description mb-3">
          {{ attraction.overview }}
          <br />
          <p class="card-text">
            <small class="text-body-secondary">출처 : 한국관광공사</small>
          </p>
        </div>
      </div>
      <!-- <div
        class="modal fade"
        :id="attraction.contentId + 'image_modal'"
        tabindex="-1"
        aria-labelledby="exampleModalLabel"
        aria-hidden="true"
      >
        <AttractionImage></AttractionImage>
      </div> -->
      <!-- Modal -->
      <div
        class="modal fade"
        id="addToPlan_attraction"
        data-bs-backdrop="static"
        data-bs-keyboard="false"
        tabindex="-1"
        aria-labelledby="staticBackdropLabel"
        aria-hidden="true"
      >
        <AddModal @addToPlan="addToPlan" :plans="pStore.plans"></AddModal>
      </div>
    </div>
  </div>
</template>

<style scoped>
.card {
  display: grid;
  grid-template-rows: auto minmax(0, 100%);
  max-height: 90%;
}

.pic-list {
  -ms-overflow-style: none; /* IE and Edge */
  scrollbar-width: none; /* Firefox */
  max-height: 200px;

  grid-row: 1;
}

.card-body {
  grid-row: 2;
  overflow-y: hidden;
  display: grid;
  grid-template-rows: auto auto auto;
}
.card-body-header {
  grid-row: 1;
}
.description {
  overflow-y: auto;
  grid-row: 2;

  /* IE and Edge */
  /* -ms-overflow-style: none;  */
  /* Firefox */
  /* scrollbar-width: none; */
}
</style>
