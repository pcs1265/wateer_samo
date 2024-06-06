<script setup>
import { computed } from "vue";

const props = defineProps({
  score: Number,
  number: Number,
  dix: Number,
});

const filled = computed(() => {
  return Math.trunc(props.score / 1) % 6;
});
const half = computed(() => {
  return Math.trunc((props.score - filled.value) / 0.5) % 6;
});
const empty = computed(() => {
  return (5 - filled.value - half.value) % 6;
});
</script>

<template>
  <i v-for="i in filled" class="bi bi-star-fill" style="color: red"></i>
  <i v-for="i in half" class="bi bi-star-half" style="color: red"></i>
  <i v-for="i in empty" class="bi bi-star" style="color: red"></i>
  <b>
    <template v-if="dix == null">&nbsp;{{ score.toFixed(2) }}</template>
    <template v-if="dix != null">&nbsp;{{ score.toFixed(0) }}</template>

    <template v-if="number != -1">&nbsp;({{ number }})</template></b
  >
</template>

<style scoped></style>
