<script setup>
import { useRoute, useRouter } from "vue-router";
import Rating from "./Rating.vue";

import { storeToRefs } from "pinia";

import { useUserStore } from "@/stores/user";

import { onBeforeMount } from "vue";
import { useStreamStore } from "@/stores/stream";
import StreamReviewItem from "./StreamReviewItem.vue";
import StreamReviewWrite from "./StreamReviewWrite.vue";

const route = useRoute();
const router = useRouter();

const uStore = useUserStore();

const streamStore = useStreamStore();
const { detailStream: stream, streamReviews } = storeToRefs(streamStore);

onBeforeMount(() => {
  if (stream.value == null) {
    streamStore.getDetail(route.params.objtId);
  }
  streamStore.getReviewList(route.params.objtId);
});

const close = () => {
  router.replace({
    name: "stream-detail",
    params: { objtId: route.params.objtId },
  });
};
</script>

<template>
  <div class="h-100 review-section" v-if="stream != null">
    <div class="card border-0">
      <div class="card-body">
        <div class="card-body-header">
          <div class="d-flex justify-content-between">
            <div>
              <h5>
                <b>{{ stream.wtrplayPlcNm }}</b>
              </h5>
              <Rating
                :score="stream.reviewRating"
                :number="stream.reviewNumber"
              ></Rating>
            </div>
            <button
              class="btn-close h5 m-0"
              aria-label="Close"
              @click="close"
            ></button>
          </div>
        </div>

        <div class="card-text description mb-3 row">
          <StreamReviewItem
            v-for="review in streamReviews"
            :key="review.id"
            :review="review"
          />
        </div>
      </div>
    </div>
    <div
      v-if="uStore.isLogin"
      class="my-3 border-0 rounded-3 review-write bg-white"
    >
      <StreamReviewWrite :streamId="stream.objtId"></StreamReviewWrite>
    </div>
  </div>
</template>

<style scoped>
.card {
  display: grid;
  grid-template-rows: auto minmax(0, 100%);
  grid-row: 1;
}

.card-body {
  grid-row: 2;
  overflow-y: hidden;
  display: grid;
  grid-template-rows: auto auto auto;
}
.card-body-header {
  grid-row: 1;
}
.description {
  overflow-y: auto;
  grid-row: 2;

  /* IE and Edge */
  /* -ms-overflow-style: none;  */
  /* Firefox */
  /* scrollbar-width: none; */
}

.review-section {
  display: grid;
  grid-template-rows: 65% 35%;
}

.review-write {
  grid-row: 2;
}
</style>
./StreamReviewItem.vue./StreamReviewWrite.vue
