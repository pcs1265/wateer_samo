<script setup>
import { KakaoMapCustomOverlay, KakaoMapMarker } from "vue3-kakao-maps";
const props = defineProps({
  spot: Object,
});

const content = `<div class="rounded-3 border border-secondary text-center bg-light p-2 mb-3">
    ${props.spot.name}
  </div>`;
</script>

<template>
  <KakaoMapMarker
    :key="spot.id"
    :lat="spot.latitude"
    :lng="spot.longitude"
  ></KakaoMapMarker>
  <KakaoMapCustomOverlay
    :lat="spot.latitude"
    :lng="spot.longitude"
    :yAnchor="1.5"
    :content="content"
  />
</template>

<style scoped></style>
