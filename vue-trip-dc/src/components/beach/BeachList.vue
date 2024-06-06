<script setup>
import { storeToRefs } from "pinia";
import { onMounted } from "vue";
import { useRoute } from "vue-router";
import BeachListItem from "./BeachListItem.vue";
import { useBeachStore } from "@/stores/beach";

const aStore = useBeachStore();

const route = useRoute();

onMounted(() => {
  if (route.params.contentId != undefined) {
    aStore.getDetail(route.params.contentId, () => {
      aStore.attractions.push(aStore.detailAttraction);
    });
  }
});

const { detailAttraction, detailAttractionWeather } = storeToRefs(aStore);

const selected = (contentId) => {
  aStore.getDetail(contentId);
};

const closed = () => {
  detailAttraction.value = null;
  detailAttractionWeather.value = null;
};
</script>

<template>
  <div style="height: 100%">
    <Transition name="list">
      <div class="row list" ref="list" v-show="detailAttraction == null">
        <BeachListItem
          v-for="attraction in aStore.attractions"
          :attraction="attraction"
          :key="attraction.contentId"
          @selected="selected"
        ></BeachListItem>
      </div>
    </Transition>

    <template v-if="detailAttraction != null">
      <RouterView :attraction="detailAttraction" @closed="closed"> </RouterView>
    </template>
    <!-- <AttractionDetail
        v-if="detailAttraction != null"
        :attraction="detailAttraction"
        @closed="closed"
        class="detail"
      ></AttractionDetail> -->
  </div>
</template>

<style scoped>
.list {
  height: 100%;
  overflow-y: scroll;

  display: flex;
  justify-content: left;
  align-items: start;

  scrollbar-width: none; /* Firefox */
}
.list::-webkit-scrollbar {
  display: none; /* Chrome, Safari, Opera*/
}

.list-enter-active,
.list-leave-active {
  transition: height 0.4s ease, opacity 0.4s ease;
}

.list-enter-from,
.list-leave-to {
  opacity: 0;
  height: 0px;
}
</style>
