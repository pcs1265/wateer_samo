<script setup>
const props = defineProps({
  images: Array,
});
</script>

<template>
  <div class="modal-dialog modal-dialog-centered">
    <div class="modal-content">
      <img :src="image" data-bs-dismiss="modal" v-for="image in images" />
    </div>
  </div>
</template>

<style scoped></style>
