<script setup>
import { usePlanStore } from "@/stores/plan";

const props = defineProps({
  plans: Array,
});
const emit = defineEmits(["addToPlan"]);

const addToPlan = (plan) => {
  let input = confirm(`${plan.name} 계획에 추가할까요?`);
  if (input) {
    emit("addToPlan", plan);
  }
};
</script>

<template>
  <div class="modal-dialog modal-dialog-centered modal-lg">
    <div class="modal-content">
      <div class="modal-header">
        <h1 class="modal-title fs-5" id="staticBackdropLabel">
          어떤 계획에 추가하시겠어요?
        </h1>
        <button
          type="button"
          class="btn-close"
          data-bs-dismiss="modal"
          aria-label="Close"
        ></button>
      </div>
      <div class="modal-body">
        <div class="p-3">
          <div class="row">
            <div
              v-for="plan in plans"
              :key="plan.id"
              class="col-3 d-flex justify-content-center align-items-center text-decoration-none p-3"
            >
              <button
                class="btn btn-outline-secondary rounded-3 w-100"
                data-bs-dismiss="modal"
                @click="addToPlan(plan)"
              >
                <p class="m-0">{{ plan.name }}</p>
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped></style>
