<script setup>
import { usePlanStore } from "@/stores/plan";
import { computed } from "vue";
import { useRouter } from "vue-router";

const props = defineProps({
  spot: Object,
});
const pStore = usePlanStore();
const deleteItem = () => {
  let input = confirm(`${props.spot.name}을 삭제할까요?`);
  if (input) {
    pStore.deletePlanItem(props.spot, () => {
      pStore.getDetail(pStore.detailPlan.id);
    });
  }
};
</script>

<template>
  <div
    class="mb-4 col-12 d-flex justify-content-center align-items-center card-div"
  >
    <div class="card w-100 h-100 shadow rounded-3 hover">
      <div ref="card" class="card-body row">
        <div class="col-10">
          <h5 class="card-title" v-html="spot.name"></h5>
          <p class="card-text">
            {{ spot.addr }}
          </p>
        </div>

        <div class="col-2 d-flex justify-content-center align-items-center">
          <i class="bi bi-trash h3 text-danger" @click="deleteItem"></i>
        </div>
        <!-- <Rating
          v-if="attraction.reviewNumber != 0"
          :score="attraction.reviewRating"
          :number="attraction.reviewNumber"
        ></Rating> -->
        <!-- <a href="#" class="btn btn-primary">Go somewhere</a> -->
      </div>
    </div>
  </div>
</template>

<style scoped>
.card {
  transition: background-color 0.3s ease;
}
.card:hover {
  background-color: antiquewhite;
}
</style>
