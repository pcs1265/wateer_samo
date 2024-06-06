<script setup>
import { computed, ref, toRefs, watch } from "vue";
import { KakaoMap, KakaoMapCustomOverlay } from "vue3-kakao-maps";

import StreamMarker from "./StreamMarker.vue";
import { useAttractionStore } from "@/stores/attraction";
import { storeToRefs } from "pinia";
import { useStreamStore } from "@/stores/stream";

const props = defineProps({
  streamList: Array,
});

const { streamList } = toRefs(props);

const streamStore = useStreamStore();
const { detailStream } = storeToRefs(streamStore);

const map = ref();
// 지도를 재설정할 범위정보를 가지고 있을 LatLngBounds 객체를 생성합니다
let bounds;

const onLoadKakaoMap = (mapRef) => {
  map.value = mapRef;
  if (detailStream.value != null) {
    centerMap(detailStream.value.latitude, detailStream.value.longitude);
  }
};

watch(streamList, () => {
  setBounds();
});

watch(detailStream, (n) => {
  if (n != null && map.value != null) {
    centerMap(n.latitude, n.longitude);
  }
});
const content = computed(() => {
  return `<div class="rounded-3 border border-secondary text-center bg-light p-2 mb-3">
    ${detailStream.value.wtrplayPlcNm}
  </div>`;
});

const setBounds = () => {
  if (streamList.value.length == 0) {
    return;
  }
  bounds = new kakao.maps.LatLngBounds();

  for (let stream of streamList.value) {
    let point = new kakao.maps.LatLng(stream.latitude, stream.longitude);
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
      <StreamMarker
        v-for="stream in streamList"
        :key="stream.objtId"
        :stream="stream"
        @centerMap="centerMap"
      ></StreamMarker>
      <KakaoMapCustomOverlay
        v-if="detailStream != null"
        :lat="detailStream.latitude"
        :lng="detailStream.longitude"
        :yAnchor="1.5"
        :content="content"
      />
    </KakaoMap>
  </div>
</template>

<style scoped></style>
./StreamMarker.vue
