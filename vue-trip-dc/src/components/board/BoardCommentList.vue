<script setup>
import { useBoardStore } from "@/stores/board";
import BoardCommentItem from "./BoardCommentItem.vue";
import { onMounted, ref } from "vue";
import { useRoute } from "vue-router";
import CommentPageNavigation from "./common/CommentPageNavigation.vue";

const store = useBoardStore();
const route = useRoute();

const { no } = route.params;
const param = ref({
  pgno: null,
  perPage: 20,
});

onMounted(() => {
  store.getArticleComments(no, param.value);
});

const onPageChange = (val) => {
  console.log("댓글" + val + "번 페이지로 이동 준비 끝!!!");
  store.articleComment.currentPage = val;
  param.value.pgno = val;
  store.getArticleComments(no, param.value);
};
</script>

<template>
  <table class="table">
    <thead>
      <tr>
        <th class="col-8 text-center">내용</th>
        <th class="col-2 text-center">작성자</th>
        <th class="col-2 text-center">등록일</th>
      </tr>
    </thead>
    <tbody>
      <BoardCommentItem
        v-for="comment in store.articleComment.comments"
        :key="comment.id"
        :comment="comment"
      />
    </tbody>
  </table>
  <CommentPageNavigation
    :current-page="store.articleComment.currentPage"
    :total-page="store.articleComment.totalPageCount"
    @pageChange="onPageChange"
  ></CommentPageNavigation>
</template>

<style scoped></style>
