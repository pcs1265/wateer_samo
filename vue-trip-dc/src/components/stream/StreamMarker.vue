<script setup>
import { KakaoMapCustomOverlay, KakaoMapMarker } from "vue3-kakao-maps";
import { computed, ref, watch } from "vue";
import { useAttractionStore } from "@/stores/attraction";
import { storeToRefs } from "pinia";
import { useRouter } from "vue-router";
import { useStreamStore } from "@/stores/stream";

const streamStore = useStreamStore();
const router = useRouter();
const props = defineProps({
  stream: Object,
});

const content = `<div class="rounded-3 border border-secondary text-center bg-light p-2 mb-3">
    ${props.stream.wtrplayPlcNm}
  </div>`;

const showOverlay = ref(false);

const emit = defineEmits(["centerMap"]);
const markerClicked = () => {
  aStore.getDetail(props.stream.objtId);
  centerHere();
  router.replace({
    name: "stream-detail",
    params: { objtId: props.stream.objtId },
  });
};

const centerHere = () => {
  emit("centerMap", props.stream.latitude, props.stream.longitude);
};

const markerOver = () => {
  showOverlay.value = true;
};
const markerOut = () => {
  showOverlay.value = false;
};
</script>

<template>
  <template v-if="stream.page >= streamStore.page - 1 && stream.page <= streamStore.page + 1">
    <KakaoMapMarker
      :key="stream.objtId"
      :lat="stream.latitude"
      :lng="stream.longitude"
      :clickable="true"
      @onClickKakaoMapMarker="markerClicked"
      @mouse-over-kakao-map-marker="markerOver"
      @mouse-out-kakao-map-marker="markerOut"
    ></KakaoMapMarker>
    <KakaoMapCustomOverlay
      v-if="showOverlay"
      :lat="stream.latitude"
      :lng="stream.longitude"
      :yAnchor="1.5"
      :content="content"
    />
  </template>
</template>

<style scoped>
.bg-blur {
  backdrop-filter: blur(10px);
}
.bg-glass {
  background-color: rgba(128, 128, 128, 0.5);
}
</style>
