<script setup>
import { onBeforeRouteUpdate } from "vue-router";
import BoardListItem from "./BoardListItem.vue";
import BoardSearchInput from "./BoardSearchInput.vue";
import PageNavigation from "./common/PageNavigation.vue";

import { useBoardStore } from "@/stores/board";
import { ref, onMounted, onBeforeUpdate } from "vue";
import axios from "axios";

const boardStore = useBoardStore();

//const currentPage = ref(1);
//const totalPage = ref(0);

const param = ref({
  keyword: "",
  keywordType: "",
  pgno: boardStore.currentPage.value,
  perPage: 12,
});

onMounted(() => {
  boardStore.getArticleList(param.value);
});

const onPageChange = (val) => {
  console.log(val + "번 페이지로 이동 준비 끝!!!");
  console.log(boardStore.currentPage);
  boardStore.currentPage = val;
  console.log(param.value);
  param.value.pgno = val;
  boardStore.getArticleList(param.value);
};

const testFunc = () => {
  axios.get(`http://${window.location.hostname}:8080/user/test`).then(() => {
    console.log("come back");
  });
};

testFunc();
</script>

<template>
  <div class="vh-100 back-image">
    <div class="blur-drop">
      <div class="container vh-100">
        <div class="header text-white noto-sans-kr-header">
          <p>게시판 목록</p>
        </div>
        <div class="content">
          <div class="inner-content">
            <div class="d-flex justify-content-end">
              <RouterLink
                class="btn btn-primary my-1"
                :to="{ name: 'article-write' }"
              >
                게시글 작성
              </RouterLink>
            </div>
            <div class="bg-light p-3 my-3 rounded-3">
              <table
                class="table table-bordered table-striped table-hover mt-2"
              >
                <thead>
                  <tr>
                    <th class="col-1 text-center">번호</th>
                    <th class="col-7 text-center">제목</th>
                    <th class="col-1 text-center">작성자</th>
                    <th class="col-1 text-center">조회수</th>
                    <th class="col-2 text-center">등록일</th>
                  </tr>
                </thead>
                <tbody>
                  <BoardListItem
                    v-for="article in boardStore.articleList"
                    :key="article.no"
                    :article="article"
                  />
                </tbody>
              </table>
              <PageNavigation
                :current-page="boardStore.currentPage"
                :total-page="boardStore.totalPage"
                @pageChange="onPageChange"
              ></PageNavigation>
            </div>
            <BoardSearchInput />
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

.back-image {
  background-image: url(https://oiwww.s3.us-east-2.amazonaws.com/wp-content/uploads/2020/10/31064653/springboard-qlandscape.jpeg);
  background-size: cover;
}
.blur-drop {
  backdrop-filter: blur(5px);

  background-color: rgba(0, 0, 0, 0.5);
}
</style>
