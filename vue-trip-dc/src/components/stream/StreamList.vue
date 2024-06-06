<script setup>
import { useStreamStore } from "@/stores/stream";
import { storeToRefs } from "pinia";
import { computed, onBeforeMount, onMounted, watch } from "vue";
import { useRoute } from "vue-router";
import { useScroll } from "@vueuse/core";
import { ref } from "vue";

import StreamListItem from "./StreamListItem.vue";

const streamStore = useStreamStore();

const route = useRoute();

onMounted(() => {
  if (route.params.objtId != undefined) {
    streamStore.getDetail(route.params.objtId, () => {
      streamStore.detailStream.page = 1;
      streamStore.streams.push(streamStore.detailStream);
    });
  }
});

const list = ref(null);
const { arrivedState } = useScroll(list);

watch(arrivedState, () => {
  if (!arrivedState.top && arrivedState.bottom) {
    streamStore.loadMore();
  }
});

const { page, totalPage, detailStream } = storeToRefs(streamStore);
const isLastPage = computed(() => {
  if (page.value == totalPage.value) {
    return true;
  } else {
    return false;
  }
});

const selected = (objtId) => {
  streamStore.getDetail(objtId);
};
const closed = () => {
  detailStream.value = null;
};
</script>

<template>
  <div style="height: 100%">
    <Transition name="list">
      <div class="row list" ref="list" v-show="detailStream == null">
        <StreamListItem
          v-for="stream in streamStore.streams"
          :stream="stream"
          :key="stream.objtId"
          @selected="selected"
        ></StreamListItem>
        <div v-if="isLastPage" class="">
          <p class="text-center py-3 bg-light rounded-3">마지막 내용입니다.</p>
        </div>
      </div>
    </Transition>
    <template class="detail" v-if="detailStream != null">
      <RouterView
        :stream="detailStream"
        @closed="closed"
        v-slot="{ Component }"
      >
      </RouterView>
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
.detail-enter-active,
.detail-leave-active {
  transition: opacity 0.5s ease;
}

.detail-enter-from,
.detail-leave-to {
  opacity: 0;
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
./StreamDetail.vue./StreamListItem.vue./StreamReview.vue
