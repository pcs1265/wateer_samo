<script setup>
import AttractionList from "@/components/attraction/AttractionList.vue";
import AttractionMap from "@/components/attraction/AttractionMap.vue";
import AttractionSearchInput from "@/components/attraction/AttractionSearchInput.vue";
import AttractionWish from "@/components/attraction/AttractionWish.vue";
import { useAttractionStore } from "@/stores/attraction";
import { useUserStore } from "@/stores/user";
import { storeToRefs } from "pinia";
import { onMounted, watch, ref } from "vue";

const aStore = useAttractionStore();
const uStore = useUserStore();
onMounted(() => {
  aStore.init();
});

const { wishList } = storeToRefs(uStore);
const componentKey = ref(0);

watch(wishList, () => {
  componentKey.value++;
});
</script>

<template>
  <div class="vh-100 back-image">
    <div class="blur-drop">
      <div class="container vh-100">
        <div class="header text-white noto-sans-kr-header">
          <p>여행가요</p>
        </div>
        <div class="row content">
          <div class="col-lg-6 vertical-area">
            <AttractionMap :attraction-list="aStore.attractions" />
            <AttractionWish :key="componentKey"></AttractionWish>
          </div>
          <div class="col-lg-6 vertical-area">
            <div class="search-bar">
              <AttractionSearchInput></AttractionSearchInput>
            </div>
            <div class="search-result">
              <AttractionList></AttractionList>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
@import url("https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@100..900&display=swap");

.noto-sans-kr-header {
  font-family: "Noto Sans KR", sans-serif;
  font-optical-sizing: auto;
  font-weight: 700;
  font-style: normal;
  font-size: 3rem;
}

.header {
  height: 15%;
  display: flex;
  align-items: center;
}
.content {
  height: 85%;
}
.vertical-area {
  height: 100%;
  display: grid;
  grid-template-rows: auto minmax(0, 100%);
}
.search-bar {
  grid-row: 1;
}
.search-result {
  grid-row: 2;
  overflow: hidden;
}

.back-image {
  background-image: url(https://blog.kakaocdn.net/dn/cTPtM5/btrbENJy3Zp/L4XXvH6a4yWS8PJFQkWX6K/img.png);
  background-size: cover;
}
.blur-drop {
  backdrop-filter: blur(5px);

  background-color: rgba(0, 0, 0, 0.7);
}
</style>
