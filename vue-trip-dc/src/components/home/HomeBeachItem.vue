<script setup>
import { useRouter } from "vue-router";
import Rating from "../attraction/Rating.vue";

const props = defineProps({
  attraction: Object,
});

const router = useRouter();

const select = () => {
  router.push({
    name: "beach-detail",
    params: { contentId: props.attraction.id },
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
        src="https://www.visitbusan.net/uploadImgs/files/cntnts/20191225170315839_oen"
        class="card-img-top loading"
        style="object-fit: cover"
        alt="..."
        height="150px"
        loading="eager"
      />

      <div ref="card" class="card-body">
        <h5 class="card-title" v-html="attraction.name"></h5>
        <p class="card-text">
          {{ attraction.parcelAddr }}
        </p>
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
