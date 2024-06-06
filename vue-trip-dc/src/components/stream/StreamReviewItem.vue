<script setup>
import { computed } from "vue";
import Rating from "./Rating.vue";

const props = defineProps({
  review: Object,
});

const { VITE_VUE_API_URL } = import.meta.env;

const image = computed(() => {
  if (props.review.firstImage == null) {
    return null;
  } else {
    return `${VITE_VUE_API_URL}attraction/review-image?filename=${props.review.firstImage}`;
  }
});
</script>

<template>
  <div
    class="my-2 col-sm-6 col-md-4 col-lg-6 d-flex justify-content-center align-items-center card-div"
  >
    <div class="card w-100 h-100 shadow rounded-3" @click="select">
      <img
        v-if="image != null"
        :src="image"
        class="card-img-top loading"
        style="object-fit: cover"
        alt="..."
        height="150px"
        loading="eager"
      />
      <div class="card-body">
        <p class="card-text" v-html="review.content"></p>
        <Rating :score="review.rating" :number="-1" :dix="0"></Rating>
        <!-- <a href="#" class="btn btn-primary">Go somewhere</a> -->
        <p class="card-text">
          <small class="text-body-secondary">작성자 : {{ review.nickname }} </small>
        </p>
      </div>
    </div>
  </div>
</template>

<style scoped>
.loading {
  background: transparent url("https://placehold.co/600x400?text=...") center center no-repeat;
}
</style>
