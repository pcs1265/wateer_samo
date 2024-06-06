<template>
  <div class="col-lg-6 mx-auto mb-5">
    <div class="input-group">
      <select v-model="param.keywordType" class="form-select border-0">
        <option value="title">제목</option>
        <option value="nickname">닉네임</option>
      </select>
      <input
        type="text"
        v-model="param.keyword"
        @keydown.enter="searchPost"
        placeholder="키워드"
        class="form-control w-50"
      />

      <button
        @click="searchPost"
        class="form-control bg-secondary text-white border-0"
      >
        검색
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { useBoardStore } from "@/stores/board";

const store = useBoardStore();

const pgno = ref(1);
const param = ref({
  keyword: "",
  keywordType: "title",
  pgno: pgno.value,
  perPage: 20,
});

// 검색 실행 함수
const searchPost = () => {
  console.log(param.value);
  if (param.value.keyword == "") {
    alert("키워드는 필수값입니다.");
    return;
  }
  store.getArticleList(param.value);
};
</script>

<style scoped></style>
