<script setup>
import Viewer from "@toast-ui/editor/dist/toastui-editor-viewer";
import "@toast-ui/editor/dist/toastui-editor-viewer.css";
import { ref, onMounted, defineProps, defineEmits,watch } from "vue";

const editorRef = ref(null);
const props = defineProps({
  tuiContent: String,
});
const emits = defineEmits();

let viewer;
onMounted(() => {
  if (editorRef.value) {
    viewer = new Viewer({
      el: editorRef.value,
      initialValue: props.tuiContent,
    });
  }
});

watch(()=>props.tuiContent,(newContent)=>{
  if(viewer){
    viewer.setMarkdown(newContent);
  }
})
</script>

<template>
  <div ref="editorRef"></div>
</template>

<style scoped></style>
