<script setup>
import { computed, ref, toRefs, watch } from "vue";
import { KakaoMap, KakaoMapCustomOverlay } from "vue3-kakao-maps";
import AttractionMarker from "./AttractionMarker.vue";
import { useAttractionStore } from "@/stores/attraction";
import { storeToRefs } from "pinia";

const props = defineProps({
  attractionList: Array,
});

const { attractionList } = toRefs(props);

const aStore = useAttractionStore();
const { detailAttraction } = storeToRefs(aStore);

const map = ref();
// 지도를 재설정할 범위정보를 가지고 있을 LatLngBounds 객체를 생성합니다
let bounds;

const onLoadKakaoMap = (mapRef) => {
  map.value = mapRef;
  if (detailAttraction.value != null) {
    centerMap(
      detailAttraction.value.latitude,
      detailAttraction.value.longitude
    );
  }
};

watch(attractionList, () => {
  setBounds();
});

watch(detailAttraction, (n) => {
  if (n != null && map.value != null) {
    centerMap(n.latitude, n.longitude);
  }
});
const content = computed(()=>{
  return `<div class="rounded-3 border border-secondary text-center bg-light p-2 mb-3">
    ${detailAttraction.value.title}
  </div>`});

const setBounds = () => {
  if (attractionList.value.length == 0) {
    return;
  }
  bounds = new kakao.maps.LatLngBounds();

  for (let attraction of attractionList.value) {
    let point = new kakao.maps.LatLng(
      attraction.latitude,
      attraction.longitude
    );
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

const centerMap = (lat, lng) => {
  // center.value.lat = lat;
  // center.value.lng = lng;
  map.value.panTo(new kakao.maps.LatLng(lat, lng));
};
</script>

<template>
  <div>
    <KakaoMap
      :lat="center.lat"
      :lng="center.lng"
      class="rounded-3 shadow border border-secondary"
      width="100%"
      @onLoadKakaoMap="onLoadKakaoMap"
    >
      <AttractionMarker
        v-for="attraction in attractionList"
        :key="attraction.contentId"
        :attraction="attraction"
        @centerMap="centerMap"
      ></AttractionMarker>
      <KakaoMapCustomOverlay
        v-if="detailAttraction != null"
        :lat="detailAttraction.latitude"
        :lng="detailAttraction.longitude"
        :yAnchor="1.5"
        :content="content"
      />
    </KakaoMap>
  </div>
</template>

<style scoped></style>
