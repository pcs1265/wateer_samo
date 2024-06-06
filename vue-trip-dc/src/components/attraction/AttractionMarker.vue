<script setup>
import { KakaoMapCustomOverlay, KakaoMapMarker } from "vue3-kakao-maps";
import { computed, ref, watch } from "vue";
import { useAttractionStore } from "@/stores/attraction";
import { storeToRefs } from "pinia";
import { useRouter } from "vue-router";

const aStore = useAttractionStore();
const router = useRouter();
const props = defineProps({
  attraction: Object,
});

const content = `<div class="rounded-3 border border-secondary text-center bg-light p-2 mb-3">
    ${props.attraction.title}
  </div>`;

const showOverlay = ref(false);

const emit = defineEmits(["centerMap"]);
const markerClicked = () => {
  aStore.getDetail(props.attraction.contentId);
  centerHere();
  router.replace({
    name: "attraction-detail",
    params: { contentId: props.attraction.contentId },
  });
};

const centerHere = () => {
  emit("centerMap", props.attraction.latitude, props.attraction.longitude);
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
    :key="attraction.contentId"
    :lat="attraction.latitude"
    :lng="attraction.longitude"
    :clickable="true"
    @onClickKakaoMapMarker="markerClicked"
    @mouse-over-kakao-map-marker="markerOver"
    @mouse-out-kakao-map-marker="markerOut"
  ></KakaoMapMarker>
  <KakaoMapCustomOverlay
    v-if="showOverlay"
    :lat="attraction.latitude"
    :lng="attraction.longitude"
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
