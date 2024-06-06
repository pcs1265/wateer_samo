<script setup>
import {
  Teleport,
  Transition,
  computed,
  onBeforeUpdate,
  ref,
  watch,
} from "vue";
import Rating from "./Rating.vue";
import { useRouter } from "vue-router";

const props = defineProps({
  stream: Object,
});

const router = useRouter();

// const image = computed(() => {
//   if (props.attraction.firstImage == "") {
//     return "https://placehold.co/300x200?text=No+Image";
//   } else {
//     return props.attraction.firstImage2;
//   }
// });

const emit = defineEmits(["selected", "visible", "hidden"]);

const select = () => {
  emit("selected", props.stream.objtId);
  router.replace({
    name: "stream-detail",
    params: { objtId: props.stream.objtId },
  });
};
</script>

<template>
  <div
    class="mb-4 col-sm-6 col-md-4 col-lg-6 d-flex justify-content-center align-items-center card-div"
    ref="target"
  >
    <div
      class="card w-100 h-100 shadow rounded-3 hover border-0"
      @click="select"
    >
      <div ref="card" class="card-body">
        <h5 class="card-title" v-html="stream.wtrplayPlcNm"></h5>
        <p class="card-text" v-html="stream.adres"></p>
        <Rating
          v-if="stream.reviewNumber != 0"
          :score="stream.reviewRating"
          :number="stream.reviewNumber"
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
