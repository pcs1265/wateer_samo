<script setup>
import { ref } from "vue";

const score = ref(3);

const emit = defineEmits("rating");

const setScore = (num) => {
  emit("rating", num);
  score.value = num;
};
</script>

<template>
  <div class="h4">
    <i
      v-if="score >= 1"
      class="bi bi-star-fill"
      style="color: red"
      @click="setScore(1)"
    ></i>
    <i
      v-if="score >= 2"
      class="bi bi-star-fill"
      style="color: red"
      @click="setScore(2)"
    ></i>
    <i
      v-if="score >= 3"
      class="bi bi-star-fill"
      style="color: red"
      @click="setScore(3)"
    ></i>
    <i
      v-if="score >= 4"
      class="bi bi-star-fill"
      style="color: red"
      @click="setScore(4)"
    ></i>
    <i
      v-if="score >= 5"
      class="bi bi-star-fill"
      style="color: red"
      @click="setScore(5)"
    ></i>

    <i
      v-if="5 - score >= 4"
      class="bi bi-star"
      style="color: red"
      @click="setScore(2)"
    ></i>
    <i
      v-if="5 - score >= 3"
      class="bi bi-star"
      style="color: red"
      @click="setScore(3)"
    ></i>
    <i
      v-if="5 - score >= 2"
      class="bi bi-star"
      style="color: red"
      @click="setScore(4)"
    ></i>
    <i
      v-if="5 - score >= 1"
      class="bi bi-star"
      style="color: red"
      @click="setScore(5)"
    ></i>
    <b>&nbsp;{{ score }}</b>
  </div>
</template>

<style scoped></style>
