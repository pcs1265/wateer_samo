<script setup>
import { useBeachStore } from "@/stores/beach";
import { onMounted, ref, watch } from "vue";
import { useRouter } from "vue-router";

const aStore = useBeachStore();
const router = useRouter();

const sido = ref();
const gugun = ref(0);
const keyword = ref("");
const contentType = ref(0);
const searchType = ref("region");

const setSearchType = (type) => {
  searchType.value = type;
};

onMounted(() => {
  aStore.getSidoList(() => {
    sido.value = aStore.sidoList[0];
  });
});

const searchByRegion = () => {
  const param = {
    SIDO_NM: sido.value,
  };
  aStore.getAttractionListByRegion(param);
  router.replace({ name: "beach-list" });
};
</script>

<template>
  <div class="row mb-3">
    <div></div>
    <!-- <div class="col-md-6 col-lg-4 mt-3 mt-lg-0">
      <div class="input-group shadow rounded-3">
        <button
          :class="[
            'btn form-control border-secondary',
            { 'bg-secondary text-light': searchType == 'keyword' },
          ]"
          @click="setSearchType('keyword')"
        >
          키워드
        </button>
        <button
          :class="[
            'btn form-control border-secondary',

            { 'bg-secondary text-light': searchType == 'region' },
          ]"
          @click="setSearchType('region')"
        >
          지역별
        </button>
      </div>
    </div> -->
    <div class="col-lg-12 mt-3 mt-lg-0">
      <div v-if="searchType == 'region'" class="input-group shadow rounded-3">
        <select class="form-select" v-model="sido">
          <option v-for="sido in aStore.sidoList" :value="sido" :key="sido">
            {{ sido }}
          </option>
        </select>
        <button class="btn btn-secondary" @click="searchByRegion">검색</button>
      </div>

      <div v-if="searchType == 'keyword'" class="input-group shadow rounded-3">
        <input
          type="text"
          class="form-control"
          placeholder="키워드..."
          v-model="keyword"
          @keydown.enter="searchByKeyword"
        />
        <button class="btn btn-secondary" @click="searchByKeyword">검색</button>
      </div>
    </div>
  </div>
</template>

<style scoped></style>
