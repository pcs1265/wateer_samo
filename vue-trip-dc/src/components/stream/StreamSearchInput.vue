<script setup>
import { useStreamStore } from "@/stores/stream";
import { onMounted, ref, watch } from "vue";
import { useRouter } from "vue-router";

const sStore = useStreamStore();
const router = useRouter();

const sido = ref(1);
const gugun = ref(0);
const keyword = ref("");
const contentType = ref(0);
const searchType = ref("region");

watch(sido, () => {
  sStore.getGugunList();
});

onMounted(() => {
  sStore.getGugunList();
});

const setSearchType = (type) => {
  searchType.value = type;
};

const searchByRegion = () => {
  const param = {
    sido: sido.value,
    gugun: gugun.value,
    wtrplay_plc_type: contentType.value,
  };
  sStore.getStreamListByRegion(param);
  router.replace({ name: "stream-list" });
};

const searchByKeyword = () => {
  if (keyword.value.length <= 1) {
    alert("키워드의 최소 길이는 2글자입니다.");
    return;
  }

  const param = {
    keyword: keyword.value,
    wtrplay_plc_type: contentType.value,
  };
  sStore.getStreamListByKeyword(param);
  router.replace({ name: "stream-list" });
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
    <div class="col-md-6 col-lg-3 mt-3 mt-lg-0">
      <select class="form-select shadow" v-model="contentType">
        <option value="0">- 전체 -</option>
        <option value="계곡">계곡</option>
        <option value="하천">하천</option>
      </select>
    </div>
    <div class="col-lg-9 mt-6 mt-lg-0">
      <div v-if="searchType == 'region'" class="input-group shadow rounded-3">
        <select class="form-select" v-model="sido">
          <option v-for="sido in sStore.sidoList" :value="sido" :key="sido">
            {{ sido }}
          </option>
        </select>
        <!-- <select class="form-select" v-model="gugun">
          <option value="0">- 전체 -</option>
          <option v-for="gugun in sStore.gugunList" :value="gugun.gugunName" :key="gugun.gugunName">
            {{ gugun.gugunName }}
          </option>
        </select> -->
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
