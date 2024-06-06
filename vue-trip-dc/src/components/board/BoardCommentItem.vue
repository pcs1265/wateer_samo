<script setup>
const props = defineProps({
  comment: Object,
});
</script>

<template>
  <tr>
    <td class="">{{ comment.content }}</td>
    <td class="text-center">{{ comment.nickname }}</td>
    <td class="text-center">{{ comment.writeDate }}</td>
  </tr>
</template>

<style scoped></style>
