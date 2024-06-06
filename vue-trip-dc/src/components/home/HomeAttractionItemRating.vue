<script setup>
import {
  Teleport,
  Transition,
  computed,
  onBeforeUpdate,
  ref,
  watch,
} from "vue";
import { useRouter } from "vue-router";
import Rating from "../attraction/Rating.vue";

const props = defineProps({
  attraction: Object,
});

const router = useRouter();

const image = computed(() => {
  if (props.attraction.firstImage == "") {
    return "https://placehold.co/300x200?text=No+Image";
  } else {
    return props.attraction.firstImage;
  }
});

const emit = defineEmits(["selected", "visible", "hidden"]);

const select = () => {
  emit("selected", props.attraction.contentId);
  router.push({
    name: "attraction-detail",
    params: { contentId: props.attraction.contentId },
  });
};
</script>

<template>
  <div
    class="mb-4 col-4 d-flex justify-content-center align-items-center card-div"
    ref="target"
  >
    <div
      class="card w-100 h-100 shadow rounded-3 hover border-0"
      @click="select"
    >
      <img
        :src="image"
        class="card-img-top loading"
        style="object-fit: cover"
        alt="..."
        height="150px"
        loading="eager"
      />

      <div ref="card" class="card-body">
        <h5 class="card-title" v-html="attraction.title"></h5>
        <p class="card-text" v-html="attraction.addr1"></p>
        <Rating
          v-if="attraction.reviewNumber != 0"
          :score="attraction.reviewRating"
          :number="attraction.reviewNumber"
        ></Rating>
        <!-- <a href="#" class="btn btn-primary">Go somewhere</a> -->
      </div>
    </div>
  </div>
</template>

<style scoped>
.loading {
  background: transparent url("https://placehold.co/600x400?text=...") center
    center no-repeat;
}
.card-div {
  cursor: pointer;
}

.card {
  transition: background-color 0.3s ease;
}
.card:hover {
  background-color: antiquewhite;
}
</style>
