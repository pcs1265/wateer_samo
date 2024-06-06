<script setup>
import { usePlanStore } from "@/stores/plan";
import { useUserStore } from "@/stores/user";
import { onMounted, ref } from "vue";
import { RouterLink } from "vue-router";

const pStore = usePlanStore();
const uStore = useUserStore();

onMounted(() => {
  const param = { user_id: uStore.userInfo.userId };
  pStore.getPlanList(param);
});

const planName = ref("새 계획 이름");

const submitPlan = () => {
  const param = {
    user_id: uStore.userInfo.userId,
    name: planName.value,
  };
  planName.value = "새 계획 이름";
  pStore.makePlan(param, () => {
    const param = { user_id: uStore.userInfo.userId };
    pStore.getPlanList(param);
  });
};

const deletePlan = (plan) => {
  let input = confirm(`"${plan.name}" 계획을 삭제할까요?`);
  if (input) {
    pStore.deletePlan(plan, () => {
      const param = { user_id: uStore.userInfo.userId };
      pStore.getPlanList(param);
    });
  }
};
</script>

<template>
  <div class="h-100 overflow-y-scroll list">
    <!-- Button trigger modal -->

    <div class="row mt-3">
      <div class="col-3 d-flex">
        <button
          type="button"
          class="btn btn-primary w-100 m-3"
          data-bs-toggle="modal"
          data-bs-target="#staticBackdrop"
        >
          새 계획 만들기
        </button>
      </div>
      <div class="col-3" v-for="plan in pStore.plans" :key="plan.id">
        <RouterLink
          class="bg-light rounded-3 d-flex flex-column justify-content-top align-items-center text-decoration-none text-dark p-0 m-3"
          :to="{ name: 'plan-detail', params: { contentId: plan.id } }"
        >
          <div
            class="border border-secondary w-100 text-center p-3 rounded-3 bg-secondary text-light shadow row"
          >
            <div class="m-0 fw-bold col-10">{{ plan.name }}</div>
            <button
              class="btn col-1 border-0"
              @click.prevent="deletePlan(plan)"
            >
              <i class="bi bi-trash h4"></i>
            </button>
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

          <p class="mt-3" v-if="plan.items.length == 0">계획이 비어있습니다.</p>
        </RouterLink>
      </div>
    </div>
  </div>

  <!-- Modal -->
  <div
    class="modal fade"
    id="staticBackdrop"
    data-bs-backdrop="static"
    data-bs-keyboard="false"
    tabindex="-1"
    aria-labelledby="staticBackdropLabel"
    aria-hidden="true"
  >
    <div class="modal-dialog modal-dialog-centered">
      <div class="modal-content">
        <div class="modal-header">
          <h1 class="modal-title fs-5" id="staticBackdropLabel">계획 만들기</h1>
          <button
            type="button"
            class="btn-close"
            data-bs-dismiss="modal"
            aria-label="Close"
          ></button>
        </div>
        <div class="modal-body">
          <div class="form-floating mb-3">
            <input
              type="text"
              class="form-control"
              id="floatingInput"
              v-model="planName"
            />
            <label for="floatingInput">계획 이름</label>
          </div>
        </div>
        <div class="modal-footer">
          <button
            type="button"
            class="btn btn-secondary"
            data-bs-dismiss="modal"
          >
            취소
          </button>
          <button
            type="button"
            data-bs-dismiss="modal"
            class="btn btn-primary"
            @click="submitPlan"
          >
            추가
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<style>
.modal-backdrop {
  display: none;
}

.modal {
  background: rgba(0, 0, 0, 0.5);
}
</style>

<style scoped>
.list {
  scrollbar-width: none; /* Firefox */
}
.list::-webkit-scrollbar {
  display: none; /* Chrome, Safari, Opera*/
}
</style>
