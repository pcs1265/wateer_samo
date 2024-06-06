<script setup>
import { computed, onMounted, ref, toRefs, watch } from "vue";
import { onBeforeRouteUpdate, useRouter } from "vue-router";
import Rating from "./Rating.vue";
import { useBeachStore } from "@/stores/beach";
import { storeToRefs } from "pinia";
import { Bar, Line } from "vue-chartjs";
import Chart, { scales } from "chart.js/auto";

const props = defineProps({
  attraction: Object,
});

const { attraction } = toRefs(props);

const router = useRouter();
const aStore = useBeachStore();
const { detailAttractionWeather } = storeToRefs(aStore);

const xData = ref([]);
const yData = ref([]);

onMounted(() => {});

watch(detailAttractionWeather, (n) => {
  if (n != null) {
    const xArr = [];
    const yArr = [];
    for (let idx in detailAttractionWeather.value.usw["T1H"]) {
      const obj = detailAttractionWeather.value.usw["T1H"][idx];
      xArr.push("");
      yArr.push(obj);
    }
    xData.value = xArr;
    yData.value = yArr;
  }
});

const data = computed(function () {
  return {
    labels: xData.value,
    datasets: [
      {
        label: "기온",
        borderWidth: 1,
        data: yData.value,

        fill: true,
        tension: 0.3,
      },
    ],
  };
});

const option = {
  scales: {
    y: {
      type: "linear",
      grace: "3",
    },
  },
  plugins: {
    legend: {
      display: false,
    },
  },
};

const emit = defineEmits(["closed"]);

const getKoreanLabel = (str) => {
  switch (str) {
    case "LGT":
      return "낙뢰";
    case "T1H":
      return "기온";
    case "RN1":
      return "1시간 강수량";
    case "SKY":
      return "하늘상태";
    case "UUU":
      return "동서바람성분";
    case "VVV":
      return "남북바람성분";
    case "REH":
      return "습도";
    case "PTY":
      return "강수형태";
    case "VEC":
      return "풍향";
    case "WSD":
      return "풍속";
  }
};

const getSkyKorean = (val) => {
  if (val <= 5) {
    return "맑음";
  } else if (val <= 8) {
    return "구름";
  } else {
    return "흐림";
  }
};

const close = () => {
  emit("closed");
  router.replace({ name: "beach-list" });
};

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
    name: attraction.value.name,
    plan_id: plan.id,
    addr: attraction.value.parcelAddr,
    latitude: attraction.value.lat,
    longitude: attraction.value.lon,
    description: "",
    order: 0,
  };
  pStore.addToPlan(data);
};
</script>

<template>
  <div class="h-100">
    <div class="card">
      <div class="card-body">
        <div class="card-body-header">
          <div class="d-flex justify-content-between">
            <div class="d-flex align-items-center">
              <b class="h4 lh-base fw-bold m-0">{{ attraction.name }}</b>
              <button
                type="button"
                class="btn btn-primary ms-3"
                data-bs-toggle="modal"
                data-bs-target="#addToPlan_beach"
                @click="loadPlanList"
                v-if="uStore.isLogin"
              >
                계획에 추가
              </button>
              <!-- <RouterLink :to="{ name: 'beach-review' }">
                <Rating
                  :score="attraction.reviewRating"
                  :number="attraction.reviewNumber"
                ></Rating>
              </RouterLink> -->
            </div>
            <button
              class="btn-close h5 m-0"
              aria-label="Close"
              @click="close"
            ></button>
          </div>
          <h6 class="card-title mt-2">{{ attraction.addr1 }}</h6>
        </div>
        <div v-if="detailAttractionWeather == null">
          <h3>로드 중</h3>
        </div>
        <div v-else>
          <table class="table border-secondary border">
            <tr>
              <th>수온</th>
              <td>{{ detailAttractionWeather.temp.item[0].tw }} °C</td>
              <th>파고</th>
              <td>{{ detailAttractionWeather.waveHeight.item[0].wh }} m</td>
            </tr>
          </table>
          <div>
            <table class="table table-bordered text-center">
              <thead>
                <tr>
                  <th></th>
                  <th
                    v-for="(item, index) in detailAttractionWeather.usw['LGT']"
                    :key="index"
                  >
                    {{ index.substr(0, 2) }}시
                  </th>
                </tr>
              </thead>
              <tbody>
                <tr>
                  <th>{{ getKoreanLabel("T1H") }}</th>
                  <td colspan="6" style="position: relative; height: 100px">
                    <Line :data="data" :options="option" />
                  </td>
                </tr>
                <tr>
                  <th>{{ getKoreanLabel("PTY") }}</th>
                  <td
                    v-for="(item, index) in detailAttractionWeather.usw['PTY']"
                    :key="index + 'PTY'"
                    class="p-0"
                  >
                    <img
                      v-if="item == 0"
                      src="@/assets/image/weather/day.svg"
                      width="100%"
                    />
                    <img
                      v-if="item == 1 || item == 2 || item == 5 || item == 6"
                      src="@/assets/image/weather/rainy-1.svg"
                      width="100%"
                    />
                    <img
                      v-if="item == 3 || item == 7"
                      src="@/assets/image/weather/snowy-1.svg"
                      width="100%"
                    />
                  </td>
                </tr>
                <tr>
                  <th>{{ getKoreanLabel("SKY") }}</th>
                  <td
                    v-for="(item, index) in detailAttractionWeather.usw['SKY']"
                    :key="index + 'SKY'"
                  >
                    {{ getSkyKorean(item) }}
                  </td>
                </tr>
                <tr>
                  <th>{{ getKoreanLabel("REH") }}</th>
                  <td
                    v-for="(item, index) in detailAttractionWeather.usw['REH']"
                    :key="index + 'REH'"
                  >
                    {{ item }}%
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>

        <div class="card-text description mb-3">
          <p class="card-text">
            <small class="text-body-secondary">출처 : 기상청</small>
          </p>
        </div>
      </div>
    </div>
  </div>

  <div
    class="modal fade"
    id="addToPlan_beach"
    data-bs-backdrop="static"
    data-bs-keyboard="false"
    tabindex="-1"
    aria-labelledby="staticBackdropLabel"
    aria-hidden="true"
    v-if="uStore.isLogin"
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
