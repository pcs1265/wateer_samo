<script setup>
import { useBoardStore } from "@/stores/board";
import { useUserStore } from "@/stores/user";
import { ref } from "vue";
import { useRoute } from "vue-router";

const comment = ref({
  writer:"",
  content: "",
});
const route = useRoute();
const {no} = route.params;
const store = useBoardStore();
const userStore=useUserStore();
const writeComment = () => {
  comment.value.writer=userStore.userInfo.userId;
  store.writeComment(no, comment.value);
  comment.value.content = "";
};
</script>

<template>
  <div class="row mt-3">
    <div class="col-10">
      <textarea
        v-model="comment.content"
        class="form-control rounded-0"
        rows="3"
        placeholder="댓글 작성하기..."
      ></textarea>
    </div>

    <div class="col-2 ps-0">
      <button @click="writeComment" class="btn btn-outline-primary h-100 w-100">
        댓글 등록
      </button>
    </div>
  </div>
</template>

<style scoped></style>
