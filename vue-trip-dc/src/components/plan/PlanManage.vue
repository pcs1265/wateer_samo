<script setup>
import { useBeachStore } from "@/stores/beach";
import { onMounted } from "vue";
import PlanItemList from "./PlanItemList.vue";
import PlanMap from "./PlanMap.vue";
import { usePlanStore } from "@/stores/plan";
import { useRoute } from "vue-router";

const pStore = usePlanStore();
const route = useRoute();

onMounted(() => {
  pStore.getDetail(route.params.contentId);
  pStore.planRoute = null;
});
</script>

<template>
  <div class="row content h-100">
    <div class="col-lg-6 h-100">
      <PlanMap class="h-100" />
    </div>
    <div class="col-lg-6 h-100">
      <PlanItemList></PlanItemList>
    </div>
  </div>
</template>

<style scoped></style>
