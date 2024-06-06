<script setup>
import { usePlanStore } from "@/stores/plan";
import { storeToRefs } from "pinia";
import { computed } from "vue";

const pStore = usePlanStore();

const { detailPlan, planRoute } = storeToRefs(pStore);
const getRoute = () => {
  pStore.getRoute();
};

const savePlan = () => {
  detailPlan.value.items.forEach((val, idx) => {
    val.order = idx + 1;
  });
  pStore.savePlan(detailPlan.value, () => {
    alert("저장 했습니다.");
  });
};

const distance = (val) => {
  if (val > 1000) {
    return val / 1000 + "km";
  } else {
    return val + "m";
  }
};

const duration = (val) => {
  // sec
  var hours = Math.floor(val / 3600);
  var mins = Math.floor((val - hours * 3600) / 60);
  var secs = val - hours * 3600 - mins * 60;
  let res = "";
  if (hours != 0) {
    res += hours + "시간";
  }
  if (mins != 0) {
    res += hours + "분";
  }
  if (secs != 0) {
    res += hours + "초";
  }
  return res;
};
</script>

<template>
  <div class="bg-light rounded-3 h-100 my-3 overflow-hidden">
    <div class="row h-100">
      <div class="col-10 overflow-scroll">
        <template v-if="planRoute != null">
          <div class="p-3">
            <table class="table text-center align-middle table-bordered">
              <tbody>
                <tr>
                  <th>총 이동거리</th>
                  <td colspan="2">
                    {{ distance(planRoute.routes[0].summary.distance) }}
                  </td>
                </tr>
                <tr>
                  <th>총 소요시간</th>
                  <td colspan="2">
                    {{ duration(planRoute.routes[0].summary.duration) }}
                  </td>
                </tr>
                <tr>
                  <th rowspan="2" class="col-3">교통비</th>
                  <th class="col-3">택시 요금</th>
                  <td class="col-6">
                    {{ planRoute.routes[0].summary.fare.taxi }} 원
                  </td>
                </tr>
                <tr>
                  <th>톨게이트 요금</th>
                  <td>{{ planRoute.routes[0].summary.fare.toll }} 원</td>
                </tr>
                <tr>
                  <th>이동 경로</th>
                  
                </tr>
                <tr></tr>
              </tbody>
            </table>
          </div>
        </template>
      </div>
      <div class="col-2 d-flex flex-column h-100 ps-0">
        <button class="btn btn-primary rounded-0 w-100 h-50" @click="getRoute">
          경로<br />계산
        </button>
        <button class="btn btn-success rounded-0 w-100 h-50" @click="savePlan">
          저장
        </button>
      </div>
    </div>
  </div>
</template>

<style scoped></style>
