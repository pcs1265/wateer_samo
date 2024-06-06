<script setup>
import { ref, onMounted } from "vue";
import { useBoardStore } from "@/stores/board";
import { useUserStore } from "@/stores/user";
import { useDropzone } from "vue3-dropzone";
import axios from "axios";
import router from "@/router";
import TuiEditor from "@/editor/TuiEditor.vue";

const store = useBoardStore();
const userStore = useUserStore();
const article = ref({
  writer: "",
  title: "",
  content: "",
});

const uploadedFiles = ref([]); // 업로드된 파일 목록을 저장할 배열

const updateFiles = (file) => {
  uploadedFiles.value.push(file);
};

const updateValue = (promps) => {
  article.value.content = promps;
};

// 파일 업로드 로직
const onUploadFile = async (files) => {
  if (files.length > 0) {
    for (const file of files) {
      const base64 = await toBase64(file);
      uploadedFiles.value.push({
        name: file.name,
        base64: base64,
      });
      // 로직 구현
      console.log(`Uploaded file:`, file.name);
    }
  }
};

// 파일을 Base64로 변환하는 함수
const toBase64 = (file) =>
  new Promise((resolve, reject) => {
    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = () => resolve(reader.result);
    reader.onerror = (error) => reject(error);
  });

// 드래그 앤 드롭 설정
const { getRootProps, getInputProps, isDragActive } = useDropzone({
  onDrop: (acceptedFiles, rejectReasons) => {
    console.log("acceptedFiles", acceptedFiles);
    if (rejectReasons) {
      console.log("rejectReasons", rejectReasons);
    }
    if (acceptedFiles) {
      onUploadFile(acceptedFiles);
    }
  },
});

// 게시글 작성 함수
const writeArticle = () => {
  //uploadInStorage();
  article.value.writer = userStore.userInfo.userId;
  store.writeArticle(article.value, () => {
    router.push({ name: "board" });
  });
};

onMounted(() => {
  // 필요한 초기화 로직이 있다면 여기에 작성
});

const cancelWrite = () => {
  let realCancel = confirm(
    "정말 취소하시겠습니까? 저장하지 않은 내용이 사라집니다."
  );
  if (realCancel) router.push({ name: "board" });
};

const addImage = (blob, callback) => {
  console.log("addImage");
  const url = "ddd";
  callback(url, "text22");
};

const uploadInStorage = async () => {
  const formData = new FormData();
  uploadedFiles.value.forEach((element) => {
    formData.append("image", element);
  });
  // for (image in updateFiles.value) {
  //   formData.append("image", blob);
  // }

  //upLoadEditorImage 메서드 호출
  const response = await axios.post(
    `http://${window.location.hostname}:8080/tui-editor/image-upload`,
    formData
  );

  const filename = await response.data.fileNames;
  console.log("서버 저장 파일명:", filename);
};
</script>

<template>
  <div class="vh-100 back-image">
    <div class="blur-drop">
      <div class="container vh-100">
        <div class="header text-white noto-sans-kr-header">
          <p>게시글 작성</p>
        </div>
        <div class="content">
          <div class="inner-content bg-light p-3 rounded-3">
            <table class="table table-bordered">
              <tbody>
                <tr>
                  <th class="col-1 text-center">제목</th>
                  <td class="col-5">
                    <input
                      type="text"
                      v-model="article.title"
                      class="form-control rounded-0"
                    />
                  </td>
                </tr>
                <tr>
                  <th class="col-1 text-center">내용</th>
                  <td class="col-11">
                    <!-- <textarea v-model="article.content" class="form-control rounded-0" rows="15"></textarea> -->
                    <TuiEditor @update-model-value="updateValue" />
                  </td>
                </tr>
                <tr>
                  <th>파일첨부</th>
                  <td>
                    <div
                      class="upField cm-ac mt10 mb10"
                      v-bind="getRootProps()"
                      :class="{ isDragActive }"
                    >
                      <input multiple v-bind="getInputProps()" type="file" />
                      <span>파일을 마우스로 끌어 추가해주세요</span>
                    </div>
                    <div>
                      <!-- 업로드된 파일 목록 표시 -->
                      <ul>
                        <li v-for="file in uploadedFiles" :key="file.name">
                          {{ file.name }}
                        </li>
                      </ul>
                    </div>
                  </td>
                </tr>
              </tbody>
            </table>

            <div class="d-flex justify-content-end">
              <button @click="writeArticle" class="btn btn-primary ms-2">
                작성
              </button>
              <button @click="cancelWrite" class="btn btn-danger ms-2">
                취소
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
img {
  height: 70%;
  width: 5%;
}

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
