<script setup>
import Editor from "@toast-ui/editor";
import "@toast-ui/editor/dist/toastui-editor.css"; // Editor's Style
import { ref, onMounted, defineProps, defineEmits, watch } from "vue";
import { localAxios } from "@/util/http-commons";
import { useRouter } from "vue-router";
import axios from "axios";

const router = useRouter();
const local = localAxios();
const editorRef = ref(null);
const props = defineProps({
  modelValue: String,
});
const emit = defineEmits(["updateModelValue", "addImage", "updateFilesValue"]);

const fileArray = ref([]);
// const add = (blob, callback) => {
//   console.log("add");
//   console.log(blob);
//   emit("addImage", blob, callback);
// };

onMounted(() => {
  if (editorRef.value) {
    const editor = new Editor({
      el: editorRef.value,
      height: "500px",
      initialEditType: "wysiwyg",
      previewStyle: "vertical",
      initialValue: props.modelValue,
      events: {
        change: () => {
          emit("updateModelValue", editor.getMarkdown());
        },
      },
      hooks: {
        async addImageBlobHook(blob, callback) {
          try {
            //fileArray.value.push(blob);
            //emit("updateFilesValue", blob);
            const formData = new FormData();
            formData.append("image", blob);

            //upLoadEditorImage 메서드 호출
            const response = await axios.post(
              `http://${window.location.hostname}:8080/tui-editor/image-upload`,
              formData
            );

            const filename = await response.data.fileName;
            // console.log("서버 저장 파일명:", filename);

            // const imageUrl = `src/assets/images?filename=${filename}`;

            const imageUrl = `http://${window.location.hostname}:8080/tui-editor/image-print?filename=${filename}`;
            callback(imageUrl, "image alt attribute");
          } catch (error) {
            console.error("업로드실패:" + error);
            alert(error.data);

            router.push({ name: "board" });
          }
        },
      },
    });
    editor.getMarkdown();
  }
});
</script>

<template>
  <div ref="editorRef"></div>
</template>

<style scoped></style>
