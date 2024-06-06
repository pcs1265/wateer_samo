<script setup>
import { onMounted } from "vue";
import { usePlanStore } from "@/stores/plan";
import { useRoute } from "vue-router";
import PlanMapShare from "./PlanMapShare.vue";
import PlanItemListShare from "./PlanItemListShare.vue";

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
      <PlanMapShare class="h-100" />
    </div>
    <div class="col-lg-6 h-100">
      <PlanItemListShare></PlanItemListShare>
    </div>
  </div>
</template>

<style scoped></style>
