<script setup>
import { storeToRefs } from "pinia";
import { usePlanStore } from "@/stores/plan";
import PlanItem from "./PlanItem.vue";
import draggable from "vuedraggable";
import { useRoute } from "vue-router";

const pStore = usePlanStore();

const route = useRoute();

const { detailPlan } = storeToRefs(pStore);

const share = () => {
  navigator.clipboard
    .writeText(
      `http://${window.location.host}/plan/share/${route.params.contentId}`
    )
    .then(() => {
      alert("계획 공유 링크가 복사되었습니다.");
    });
};
</script>

<template>
  <div style="height: 100%" class="list-section">
    <template v-if="detailPlan != null && detailPlan.items.length != 0">
      <div class="col-12">
        <div class="d-flex justify-content-between align-items-center mb-2">
          <div class="noto-sans-kr-title text-light col-11">
            {{ detailPlan.name }}
          </div>
          <button class="btn btn-light" @click="share">
            <i class="bi bi-share-fill text-dark h3"></i>
          </button>
        </div>
      </div>
      <div class="row list">
        <div class="col-12">
          <div class="alert alert-primary text-center h3" role="alert">
            <i class="bi bi-arrow-down"></i>출발<i class="bi bi-arrow-down"></i>
          </div>
        </div>

        <draggable v-model="detailPlan.items" item-key="id" class="col-12">
          <template #item="{ element }">
            <PlanItem :spot="element" :key="element.id"></PlanItem>
          </template>
        </draggable>
        <div class="col-12">
          <div class="alert alert-danger text-center h3" role="alert">
            <i class="bi bi-flag-fill"></i>도착<i class="bi bi-flag-fill"></i>
          </div>
        </div>
      </div>
    </template>
    <div
      v-if="detailPlan != null && detailPlan.items.length == 0"
      class="d-flex flex-column align-items-center justify-content-center h-100"
    >
      <h1 class="text-center noto-sans-kr text-light">계획 목록이 비었어요.</h1>
      <br />
      <h1 class="text-center noto-sans-kr text-light">
        관광지를 추가해보세요.
      </h1>
    </div>
  </div>
</template>

<style scoped>
.list {
  height: 100%;
  overflow-y: scroll;
  scrollbar-width: none; /* Firefox */

  display: flex;
  flex-direction: column;
  flex-wrap: nowrap;
}
.list::-webkit-scrollbar {
  display: none; /* Chrome, Safari, Opera*/
}

@import url("https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@100..900&display=swap");

.noto-sans-kr {
  font-family: "Noto Sans KR", sans-serif;
  font-optical-sizing: auto;
  font-weight: 700;
  font-style: normal;
  font-size: 3rem;
}

.noto-sans-kr-title {
  font-family: "Noto Sans KR", sans-serif;
  font-optical-sizing: auto;
  font-weight: 600;
  font-style: normal;
  font-size: 2.5rem;
}
</style>
./PlanListItem.vue
