<script setup>
import { useAttractionStore } from "@/stores/attraction";
import AttractionListItem from "./AttractionListItem.vue";
import { storeToRefs } from "pinia";
import { computed, onBeforeMount, onMounted, watch } from "vue";
import AttractionDetail from "./AttractionDetail.vue";
import { useRoute } from "vue-router";
import { useScroll } from "@vueuse/core";
import { ref } from "vue";
import AttractionReview from "./AttractionReview.vue";

const aStore = useAttractionStore();

const route = useRoute();

onMounted(() => {
  if (route.params.contentId != undefined) {
    aStore.getDetail(route.params.contentId, () => {
      aStore.detailAttraction.page = 1;
      aStore.attractions.push(aStore.detailAttraction);
    });
  }
});

const list = ref(null);
const { arrivedState } = useScroll(list);

watch(arrivedState, () => {
  if (!arrivedState.top && arrivedState.bottom) {
    aStore.loadMore();
  }
});

const { page, totalPage, detailAttraction } = storeToRefs(aStore);
const isLastPage = computed(() => {
  if (page.value == totalPage.value) {
    return true;
  } else {
    return false;
  }
});

const selected = (contentId) => {
  aStore.getDetail(contentId);
};
const closed = () => {
  detailAttraction.value = null;
};
</script>

<template>
  <div style="height: 100%">
    <Transition name="list">
      <div class="row list" ref="list" v-show="detailAttraction == null">
        <AttractionListItem
          v-for="attraction in aStore.attractions"
          :attraction="attraction"
          :key="attraction.contentId"
          @selected="selected"
        ></AttractionListItem>
        <div v-if="isLastPage">
          <hr />
          <p class="text-center">마지막 내용입니다.</p>
          <hr />
        </div>
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
