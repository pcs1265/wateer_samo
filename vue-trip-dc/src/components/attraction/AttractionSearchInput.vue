<script setup>
import { useAttractionStore } from "@/stores/attraction";
import { onMounted, ref, watch } from "vue";
import { useRouter } from "vue-router";

const aStore = useAttractionStore();
const router = useRouter();

const sido = ref(1);
const gugun = ref(0);
const keyword = ref("");
const contentType = ref(0);
const searchType = ref("keyword");

watch(sido, () => {
  aStore.getGugunList(sido.value);
});

onMounted(() => {
  aStore.getGugunList(sido.value);
});

const setSearchType = (type) => {
  searchType.value = type;
};

const searchByRegion = () => {
  const param = {
    sido_code: sido.value,
    gugun_code: gugun.value,
    content_type_id: contentType.value,
  };
  aStore.getAttractionListByRegion(param);
  router.replace({ name: "attraction-list" });
};

const searchByKeyword = () => {
  if (keyword.value.length <= 1) {
    alert("키워드의 최소 길이는 2글자입니다.");
    return;
  }

  const param = {
    keyword: keyword.value,
    content_type_id: contentType.value,
  };
  aStore.getAttractionListByKeyword(param);
  router.replace({ name: "attraction-list" });
};
</script>

<template>
  <div class="row mb-3">
    <div></div>
    <div class="col-md-6 col-lg-4 mt-3 mt-lg-0">
      <div class="input-group shadow rounded-3">
        <button
          :class="[
            'btn form-control border-secondary',
            {
              'bg-secondary text-light': searchType == 'keyword',
              'bg-light text-dark': searchType != 'keyword',
            },
          ]"
          @click="setSearchType('keyword')"
        >
          키워드
        </button>
        <button
          :class="[
            'btn form-control border-secondary',

            {
              'bg-secondary text-light': searchType == 'region',
              'bg-light text-dark': searchType != 'region',
            },
          ]"
          @click="setSearchType('region')"
        >
          지역별
        </button>
      </div>
    </div>
    <div class="col-md-6 col-lg-3 mt-3 mt-lg-0">
      <select class="form-select shadow" v-model="contentType">
        <option value="0">- 전체 -</option>
        <option value="12">관광지</option>
        <option value="14">문화시설</option>
        <option value="15">축제공연행사</option>
        <option value="25">여행코스</option>
        <option value="28">레포츠</option>
        <option value="32">숙박</option>
        <option value="38">쇼핑</option>
        <option value="39">음식점</option>
      </select>
    </div>
    <div class="col-lg-5 mt-3 mt-lg-0">
      <div v-if="searchType == 'region'" class="input-group shadow rounded-3">
        <select class="form-select" v-model="sido">
          <option
            v-for="sido in aStore.sidoList"
            :value="sido.sidoCode"
            :key="sido.sidoCode"
          >
            {{ sido.sidoName }}
          </option>
        </select>
        <select class="form-select" v-model="gugun">
          <option value="0">- 전체 -</option>
          <option
            v-for="gugun in aStore.gugunList"
            :value="gugun.gugunCode"
            :key="gugun.gugunCode"
          >
            {{ gugun.gugunName }}
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
