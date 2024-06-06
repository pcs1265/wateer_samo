<script setup>
import { computed, ref, watch } from "vue";
import { KakaoMap, KakaoMapPolyline } from "vue3-kakao-maps";
import { storeToRefs } from "pinia";
import PlanMarker from "./PlanMarker.vue";
import { usePlanStore } from "@/stores/plan";
import RouteInfo from "./RouteInfo.vue";

const map = ref();
// 지도를 재설정할 범위정보를 가지고 있을 LatLngBounds 객체를 생성합니다
let bounds;

const pStore = usePlanStore();
const { detailPlan, planRoute } = storeToRefs(pStore);

const onLoadKakaoMap = (mapRef) => {
  map.value = mapRef;
  setBounds();
};

watch(detailPlan, () => {
  if (map.value == null) {
    setTimeout(this, 100);
    return;
  }
  setBounds();
});

const setBounds = () => {
  if (detailPlan.value == null || detailPlan.value.items.length == 0) {
    return;
  }
  bounds = new kakao.maps.LatLngBounds();

  for (let spot of detailPlan.value.items) {
    let point = new kakao.maps.LatLng(spot.latitude, spot.longitude);
    bounds.extend(point);
  }
  // LatLngBounds 객체에 추가된 좌표들을 기준으로 지도의 범위를 재설정합니다
  // 이때 지도의 중심좌표와 레벨이 변경될 수 있습니다
  if (map.value !== undefined) {
    map.value.setBounds(bounds);
  }
};
// const markers = ref({});
const center = ref({
  lat: 33.450701,
  lng: 126.570667,
});

const spots = computed(() => {
  if (detailPlan.value == null) {
    return [];
  }
  let arr = [];
  for (let spot of detailPlan.value.items) {
    arr.push(spot);
  }
  return arr;
});

const routes = computed(() => {
  if (planRoute.value == null) {
    return [];
  } else {
    let arr = [];
    for (let section of planRoute.value.routes[0].sections) {
      for (let road of section.roads) {
        let p = {};
        road.vertexes.forEach((vert, idx) => {
          if (idx % 2 == 0) {
            p.lng = vert;
          } else {
            p.lat = vert;
            arr.push({ lat: p.lat, lng: p.lng });
          }
        });
      }
    }
    console.log(arr);
    return arr;
  }
});
</script>

<template>
  <div class="h-100">
    <div style="height: 65%">
      <KakaoMap
        :lat="center.lat"
        :lng="center.lng"
        class="rounded-3 shadow border border-secondary"
        width="100%"
        height="100%"
        @onLoadKakaoMap="onLoadKakaoMap"
      >
        <PlanMarker
          v-for="spot in spots"
          :key="spot.id"
          :spot="spot"
        ></PlanMarker>
        <KakaoMapPolyline :latLngList="routes" :strokeWeight="5" />
      </KakaoMap>
    </div>

    <div style="height: 30%">
      <RouteInfo></RouteInfo>
    </div>
  </div>
</template>

<style scoped></style>
./PlanMarker.vue
