<script setup>
import { useBoardStore } from "@/stores/board";
import { useRoute, useRouter } from "vue-router";
import { onMounted, ref } from "vue";
import TuiEditor from "@/editor/TuiEditor.vue";
const route = useRoute();
const router = useRouter();

const store = useBoardStore();

const { no } = route.params;
onMounted(async () => {
  await store.getArticle(no);
});

const modifyArticle = () => {
  store.modifyArticle(() => {
    //router.push({ name: "board" });
    //수정을 했으면 보통 수정한 글로 이동하는게 맞는거 같아서 일단 바꿈 ㅎㅎ
    router.push({ name: "article-view", param: store.article.no });
  });
};

const cancelModify = () => {
  let realCancel = confirm("정말 취소하시겠습니까? 저장하지 않은 내용이 사라집니다.");
  if (realCancel) router.push({ name: "article-view", param: store.article.no });
};

const updateValue = (promps) => {
  store.article.content = promps;
};
</script>

<template>
  <div class="vh-100 back-image">
    <div class="blur-drop">
      <div class="container vh-100">
        <div class="header text-white noto-sans-kr-header">
          <p>게시글 수정</p>
        </div>
        <div class="content">
          <div class="bg-light p-3 rounded-3">
            <table class="table table-bordered">
              <tbody>
                <tr>
                  <th class="col-1 text-center">제목</th>
                  <td class="col-5">
                    <input
                      type="text"
                      v-model="store.article.title"
                      class="form-control rounded-0"
                    />
                  </td>
                  <th class="col-1 text-center">작성자</th>
                  <td class="col-1 text-center">
                    {{ store.article.nickname }}
                  </td>
                  <th class="col-1 text-center">등록일</th>
                  <td class="col-2 text-center">
                    {{ store.article.writeDate }}
                  </td>
                </tr>
                <tr>
                  <th class="col-1 text-center">내용</th>
                  <td class="col-11" colspan="5">
                    <!-- <textarea
              v-model="store.article.content"
              class="form-control rounded-0"
              rows="15"
            ></textarea> -->
                    <TuiEditor
                      :modelValue="store.article.content"
                      @update-model-value="updateValue"
                    />
                  </td>
                </tr>
              </tbody>
            </table>
            <div class="d-flex justify-content-end">
              <button @click="modifyArticle" class="btn btn-primary ms-2">저장</button>
              <button @click="cancelModify" class="btn btn-danger ms-2">취소</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
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

.inner-content {
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
