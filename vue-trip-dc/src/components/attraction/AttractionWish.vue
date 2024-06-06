<script setup>
import { useUserStore } from "@/stores/user";
import { localAxios } from "@/util/http-commons";
import { onMounted, ref } from "vue";
import AttractionWishItem from "./AttractionWishItem.vue";
const local = localAxios();
const uStore = useUserStore();

onMounted(async () => {
  if (uStore.isLogin) {
    const userId = uStore.userInfo.userId;
    const response = await local.get(`attraction/wish/${userId}`);
    const wishes = response.data;
    for (let wish of wishes) {
      wishList.value.push(wish);
    }
    console.log("ddd" + wishList.value);
  }
});

const wishList = ref([]);




</script>

<template>
  <!-- <div class="overflow-x-scroll">
    <AttractionWishItem v-for="wish in wishList" :key="wish.contentId" :wish="wish" />
  </div> -->
  <div class="rounded-3 my-4 text-center wish">
    <h4 class="title d-inline-block">찜목록</h4>
    <i class="bi bi-box2-heart"></i>

    <div class="px-3">
      <div class="row pic-list flex-nowrap overflow-x-scroll scroll">
        <AttractionWishItem v-for="wish in wishList" :key="wish.contentId" :wish="wish" />
      </div>
    </div>
  </div>
</template>

<style scoped>
.title {
  margin-top: 10px;
  margin-bottom: 0px; /* 타이틀과 목록 사이의 간격을 조절하세요 */
  margin-right: 10px;
}

.wish {
  background-color: rgba(255, 255, 255, 0.8);
}
.scroll {
  -ms-overflow-style: none; /* IE and Edge */
  scrollbar-width: none; /* Firefox */
}
</style>
