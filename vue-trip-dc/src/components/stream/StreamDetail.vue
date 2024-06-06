<script setup>
import { computed, onMounted, toRefs } from "vue";

import { useRouter } from "vue-router";
import Rating from "./Rating.vue";

const props = defineProps({
  stream: Object,
});

const { stream } = toRefs(props);

const router = useRouter();

const emit = defineEmits(["closed"]);

const close = () => {
  emit("closed");
  router.replace({ name: "stream-list" });
};

// const firstImage1 = computed(() => {
//   if (props.attraction.firstImage == "") {
//     return "";
//   } else {
//     return props.attraction.firstImage;
//   }
// });

import { usePlanStore } from "@/stores/plan";
import { useUserStore } from "@/stores/user";
import AddModal from "../common/plan/AddModal.vue";
const pStore = usePlanStore();
const uStore = useUserStore();
const loadPlanList = () => {
  const param = { user_id: uStore.userInfo.userId };
  pStore.getPlanList(param);
};

const addToPlan = (plan) => {
  const data = {
    name: stream.value.wtrplayPlcNm,
    plan_id: plan.id,
    addr: stream.value.adres,
    latitude: stream.value.latitude,
    longitude: stream.value.longitude,
    description: "",
    order: 0,
  };
  pStore.addToPlan(data);
};
</script>

<template>
  <div class="h-100">
    <div class="card">
      <div class="overflow-x-scroll pic-list">
        <div
          class="row flex-nowrap pic-list"
          data-bs-toggle="modal"
          :data-bs-target="'#' + stream.objtId + 'image_modal'"
        >
          <!-- <img
            v-if="firstImage1 != ''"
            :src="firstImage1"
            class="col-4 p-0"
            alt="..."
            style="object-fit: cover"
          /> -->
        </div>
      </div>
      <div class="card-body">
        <div class="card-body-header">
          <div class="d-flex justify-content-between">
            <div>
              <h5>
                <b>{{ stream.wtrplayPlcNm }}</b>
              </h5>
              <RouterLink :to="{ name: 'stream-review' }">
                <Rating
                  :score="stream.reviewRating"
                  :number="stream.reviewNumber"
                ></Rating>
              </RouterLink>
            </div>
            <button
              type="button"
              class="btn btn-primary ms-3"
              data-bs-toggle="modal"
              data-bs-target="#addToPlan_stream"
              @click="loadPlanList"
              v-if="uStore.isLogin"
            >
              계획에 추가
            </button>
            <button
              class="btn-close h5 m-0"
              aria-label="Close"
              @click="close"
            ></button>
          </div>

          <h6 class="card-title mt-2">{{ stream.adres }}</h6>
        </div>

        <div class="card-text description mb-3">
          {{ stream.detailPlcnm }}
          <br />
          <!-- <p class="card-text">
            <small class="text-body-secondary">출처 : 한국관광공사</small>
          </p> -->
        </div>
        <div>최대 수심: {{ stream.wtrplayDeep }}m</div>
        <div>평균 수심: {{ stream.wtrplayDeepAvg }}m</div>
        <div>관리 타입: {{ stream.managementType }}</div>
      </div>
      <div
        class="modal fade"
        :id="stream.objtId + 'image_modal'"
        tabindex="-1"
        aria-labelledby="exampleModalLabel"
        aria-hidden="true"
      ></div>
    </div>
  </div>

  <!-- Modal -->
  <div
    class="modal fade"
    id="addToPlan_stream"
    data-bs-backdrop="static"
    data-bs-keyboard="false"
    tabindex="-1"
    aria-labelledby="staticBackdropLabel"
    aria-hidden="true"
  >
    <AddModal @addToPlan="addToPlan" :plans="pStore.plans"></AddModal>
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
./StreamImage.vue
