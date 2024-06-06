<script setup>
import { useRoute, useRouter } from "vue-router";
import Rating from "./Rating.vue";
import { useAttractionStore } from "@/stores/attraction";
import { storeToRefs } from "pinia";
import AttractionReviewItem from "./AttractionReviewItem.vue";
import { useUserStore } from "@/stores/user";
import AttractionReviewWrite from "./AttractionReviewWrite.vue";
import { onBeforeMount } from "vue";

const route = useRoute();
const router = useRouter();

const uStore = useUserStore();

console.log(uStore.userInfo);

const aStore = useAttractionStore();

const { detailAttraction: attraction, attractionReviews } = storeToRefs(aStore);

onBeforeMount(() => {
  if (attraction.value == null) {
    aStore.getDetail(route.params.contentId);
  }
  aStore.getReviewList(route.params.contentId);
});

const close = () => {
  router.replace({
    name: "attraction-detail",
    params: { contentId: route.params.contentId },
  });
};
</script>

<template>
  <div class="h-100 review-section" v-if="attraction != null">
    <div class="card">
      <div class="card-body">
        <div class="card-body-header">
          <div class="d-flex justify-content-between">
            <div>
              <h5>
                <b>{{ attraction.title }}</b>
              </h5>
              <Rating
                :score="attraction.reviewRating"
                :number="attraction.reviewNumber"
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
          <AttractionReviewItem
            v-for="review in attractionReviews"
            :key="review.id"
            :review="review"
          />
        </div>
      </div>
    </div>
    <div
      v-if="uStore.isLogin"
      class="my-3 border border-secondary rounded-3 review-write bg-light"
    >
      <AttractionReviewWrite
        :attraction-id="attraction.contentId"
      ></AttractionReviewWrite>
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
