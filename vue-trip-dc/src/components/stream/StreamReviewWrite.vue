<script setup>
import { ref } from "vue";
import RatingInput from "./RatingInput.vue";
import { useUserStore } from "@/stores/user";
import { useStreamStore } from "@/stores/stream";

const props = defineProps({
  streamId: Number,
});

const uStore = useUserStore();
const sStore = useStreamStore();

const data = ref({
  streamId: props.streamId,
  content: "",
  writer: uStore.userInfo.userId,
  rating: 3,
  file: null,
});

const ratingInput = (score) => {
  data.value.rating = score;
};

const fileSlc = (e) => {
  var files = e.target.files || e.dataTransfer.files;
  if (files.length != 0) {
    data.value.file = files[0];
  }
};

const submit = () => {
  const formData = new FormData();
  formData.append("waterId", data.value.streamId);
  formData.append("content", data.value.content);
  formData.append("writer", data.value.writer);
  formData.append("rating", data.value.rating);
  formData.append("image", data.value.file);
  sStore.writeReview(formData, () => {
    sStore.getReviewList(props.streamId);
    data.value.content = "";
  });
};
</script>

<template>
  <div class="p-3">
    <label>리뷰 내용</label>
    <textarea
      class="form-control"
      placeholder="리뷰 내용"
      rows="5"
      v-model="data.content"
    ></textarea>
    <div class="my-2 row">
      <div class="col-5 align-middle text-center row">
        <div class="col-3 text-end">별점</div>
        <div class="col-9">
          <RatingInput @rating="ratingInput"></RatingInput>
        </div>
      </div>
      <div class="col-5">
        <input type="file" class="form-control" v-on:change="fileSlc" />
      </div>
      <div class="col-2">
        <button type="button" class="btn btn-primary" @click="submit">
          Submit
        </button>
      </div>
    </div>
  </div>
</template>

<style scoped></style>
