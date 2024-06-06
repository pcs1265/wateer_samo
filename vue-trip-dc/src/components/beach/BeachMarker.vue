<script setup>
import { KakaoMapCustomOverlay, KakaoMapMarker } from "vue3-kakao-maps";
import { computed, ref, watch } from "vue";
import { storeToRefs } from "pinia";
import { useRouter } from "vue-router";
import { useBeachStore } from "@/stores/beach";

const aStore = useBeachStore();
const router = useRouter();
const props = defineProps({
  attraction: Object,
});

const content = `<div class="rounded-3 border border-secondary text-center bg-light p-2 mb-3">
    ${props.attraction.name}
  </div>`;

const showOverlay = ref(false);

const emit = defineEmits(["centerMap"]);
const markerClicked = () => {
  aStore.getDetail(props.attraction.id);
  centerHere();
  router.replace({
    name: "beach-detail",
    params: { contentId: props.attraction.id },
  });
};

const centerHere = () => {
  emit("centerMap", props.attraction.lat, props.attraction.lon);
};

const markerOver = () => {
  showOverlay.value = true;
};
const markerOut = () => {
  showOverlay.value = false;
};
</script>

<template>
  <KakaoMapMarker
    :key="attraction.id"
    :lat="attraction.lat"
    :lng="attraction.lon"
    :clickable="true"
    @onClickKakaoMapMarker="markerClicked"
    @mouse-over-kakao-map-marker="markerOver"
    @mouse-out-kakao-map-marker="markerOut"
  ></KakaoMapMarker>
  <KakaoMapCustomOverlay
    v-if="showOverlay"
    :lat="attraction.lat"
    :lng="attraction.lon"
    :yAnchor="1.5"
    :content="content"
  />
</template>

<style scoped>
.bg-blur {
  backdrop-filter: blur(10px);
}
.bg-glass {
  background-color: rgba(128, 128, 128, 0.5);
}
</style>
