<script setup>
import { useBoardStore } from "@/stores/board";
import { useUserStore } from "@/stores/user";
import { RouterLink, useRoute, useRouter } from "vue-router";
import { onMounted, computed } from "vue";
import TuiViewer from "@/editor/TuiViewer.vue";
import axios from "axios";
import BoardCommentList from "./BoardCommentList.vue";
import BoardCommentWrite from "./BoardCommentWrite.vue";

const route = useRoute();
const router = useRouter();
const store = useBoardStore();
const userStore = useUserStore();

const { no } = route.params;
onMounted(async () => {
  await store.getArticle(no);
});

const modifyArticle = () => {
  router.push({ name: "article-modify", params: no });
};

const moveList = () => {
  router.push({ name: "board" });
};

const deleteArticle = () => {
  console.log(no);
  let realCancel = confirm("정말 삭제하시겠습니까?");
  if (realCancel) store.deleteArticle(no, moveList);
};

const modifyable = computed(() => {
  if (userStore.userInfo == null) {
    return false;
  } else {
    return store.article.writer == userStore.userInfo.userId;
  }
});
</script>

<template>
  <div class="vh-100 back-image">
    <div class="blur-drop">
      <div class="container vh-100">
        <div class="header text-white noto-sans-kr-header">
          <p>게시판 조회</p>
        </div>
        <div class="content overflow-y-scroll">
          <div class="bg-light p-3 rounded-3 mb-5">
            <table class="table table-bordered">
              <tbody>
                <tr>
                  <th class="col-1 text-center">제목</th>
                  <td class="col-5">{{ store.article.title }}</td>
                  <th class="col-1 text-center">작성자</th>
                  <td class="col-1 text-center">
                    {{ store.article.nickname }}
                  </td>
                  <th class="col-1 text-center">등록일</th>
                  <td class="col-2 text-center">
                    {{ store.article.writeDate }}
                  </td>
                </tr>
                <tr>
                  <th class="col-1 text-center">내용</th>
                  <!-- <td class="col-11" colspan="5">{{ store.article.content }}</td> -->

                  <TuiViewer :tuiContent="store.article.content" />
                </tr>

                <tr>
                  <th class="col-1 text-center">댓글</th>
                  <td class="col-11" colspan="6">
                    <BoardCommentList></BoardCommentList>
                    <BoardCommentWrite v-if="userStore.isLogin"></BoardCommentWrite>
                  </td>
                </tr>
              </tbody>
            </table>
            <div class="row">
              <div class="col-3">
                <RouterLink class="btn btn-primary" :to="{ name: 'board' }"> 글목록 </RouterLink>
              </div>
              <div class="d-flex justify-content-end col-9">
                <button
                  @click="modifyArticle"
                  class="btn btn-outline-primary ms-2"
                  v-if="modifyable"
                >
                  수정
                </button>
                <button
                  @click="deleteArticle"
                  class="btn btn-outline-danger ms-2"
                  v-if="modifyable"
                >
                  삭제
                </button>
              </div>
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
  scrollbar-width: none;
}

.inner-content {
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
