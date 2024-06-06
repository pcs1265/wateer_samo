<script setup>
import AttractionList from "@/components/attraction/AttractionList.vue";
import AttractionMap from "@/components/attraction/AttractionMap.vue";
import AttractionSearchInput from "@/components/attraction/AttractionSearchInput.vue";
import BeachList from "@/components/beach/BeachList.vue";
import BeachMap from "@/components/beach/BeachMap.vue";
import BeachSearchInput from "@/components/beach/BeachSearchInput.vue";
import { useBeachStore } from "@/stores/beach";
import { onMounted } from "vue";

const aStore = useBeachStore();

onMounted(() => {
  aStore.init();
});
</script>

<template>
  <div class="vh-100 back-image">
    <div class="blur-drop">
      <div class="container vh-100">
        <div class="header text-white noto-sans-kr-header">
          <p>해수욕장 가요</p>
        </div>
        <div class="row content">
          <div class="col-lg-6 h-100">
            <BeachMap class="h-100" :attraction-list="aStore.attractions" />
          </div>
          <div class="col-lg-6 vertical-area">
            <div class="search-bar">
              <BeachSearchInput></BeachSearchInput>
            </div>
            <div class="search-result">
              <BeachList></BeachList>
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
  background-image: url(https://hips.hearstapps.com/hmg-prod/images/beach-quotes-1559667853.jpg);
  background-size: auto;
}
.blur-drop {
  backdrop-filter: blur(5px);

  background-color: rgba(0, 0, 0, 0.8);
}
</style>
