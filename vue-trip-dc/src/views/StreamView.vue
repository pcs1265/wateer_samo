<script setup>
import AttractionList from "@/components/attraction/AttractionList.vue";
import AttractionMap from "@/components/attraction/AttractionMap.vue";
import AttractionSearchInput from "@/components/attraction/AttractionSearchInput.vue";
import BeachList from "@/components/beach/BeachList.vue";
import BeachMap from "@/components/beach/BeachMap.vue";
import BeachSearchInput from "@/components/beach/BeachSearchInput.vue";
import StreamList from "@/components/stream/StreamList.vue";
import StreamMap from "@/components/stream/StreamMap.vue";
import StreamSearchInput from "@/components/stream/StreamSearchInput.vue";
import { useBeachStore } from "@/stores/beach";
import { useStreamStore } from "@/stores/stream";
import { onMounted } from "vue";

const sStore = useStreamStore();

onMounted(() => {
  sStore.init();
});
</script>

<template>
  <div class="vh-100 back-image">
    <div class="blur-drop">
      <div class="container vh-100">
        <div class="header text-white noto-sans-kr-header">
          <p>물놀이 가요</p>
        </div>
        <div class="row content">
          <div class="col-lg-6 vertical-area">
            <StreamMap :stream-list="sStore.streams" />
            <!-- <div class="border border-secondary rounded-3 my-3 text-center">
              찜 목록이 들어갈 자리입니다.
            </div> -->
          </div>
          <div class="col-lg-6 vertical-area">
            <div class="search-bar">
              <StreamSearchInput></StreamSearchInput>
            </div>
            <div class="search-result">
              <StreamList></StreamList>
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
.header {
  height: 15%;
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
  background-image: url(https://visitkochijapan.com/image/rendering/attraction_image/1254/trim.900/3/2?v=139ce038e5becfda2ae8565ae1d6987a82a1628f);
  background-size: cover;
}
.blur-drop {
  backdrop-filter: blur(5px);

  background-color: rgba(0, 0, 0, 0.8);
}
</style>
