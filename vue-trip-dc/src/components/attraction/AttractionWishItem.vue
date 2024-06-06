<script setup>
import { useAttractionStore } from "@/stores/attraction";
import { useRouter } from "vue-router";

const props = defineProps({
  wish: Object,
});

const aStore = useAttractionStore();
const router = useRouter();
const emit = defineEmits(["selected", "visible", "hidden"]);

const select = () => {
  aStore.getDetail(props.wish.contentId);
  router.replace({
    name: "attraction-detail",
    params: { contentId: props.wish.contentId },
  });
};
</script>

<template>
  <div class="col-4">
    <div class="card-div" @click="select">
      <div class="card w-100 h-70 shadow rounded-3 hover border-0">
        <div class="card-header">{{ wish.title }}</div>
        <div class="card-body">
          <img :src="wish.firstImage2" alt="My Image" width="100%" />
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.card {
  border: 1px solid #eaeaea;
  border-radius: 10px;
  margin-top: 30px;
  margin-bottom: 10px;
}
.card-header {
  font-weight: bold;
}
.card-body {
  margin-top: 10px;
}

.card-div {
  cursor: pointer;
}
</style>
