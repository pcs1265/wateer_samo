<script setup>
import { ref, onMounted } from "vue";
import { localAxios } from "@/util/http-commons";
import { useRouter } from "vue-router";
import { HttpStatusCode } from "axios";
import { useUserStore } from "@/stores/user";

import { storeToRefs } from "pinia";

const store = useUserStore();
const { isLogin } = storeToRefs(store);

const router = useRouter();
const local = localAxios();
const registerForm = ref({
  userId: "",
  password: "",
  nickname: "",
  phone: "",
  email: "",
});

const passwordCheck = ref("");
const passwordValidate = ref(true);
const messageForId = ref("");

const isUseId = ref(false);

const emailConfirm = ref("");
const emailKey = ref("");
const emailValid = ref(false);

onMounted(() => {
  registerForm.value.email = router.currentRoute.value.params.email;
  console.log(router.currentRoute.value.params.email);
});

const checkPassword = () => {
  if (passwordCheck.value != registerForm.value.password) {
    passwordValidate.value = false;
  } else {
    passwordValidate.value = true;
  }
};

const registUser = async () => {
  if (!isUseId.value) {
    alert("아이디를 확인해주세요");
    return;
  }
  try {
    const response = await local.post("/user/join", JSON.stringify(registerForm.value));
    console.log(response);
    alert(response.data.message);
    store.oAuth = "kakao";
    await store.login(registerForm);
    let token = localStorage.getItem("accessToken");
    if (isLogin.value) {
      await store.verifyToken(token);
      router.replace({ name: "home" });
    }
  } catch (error) {
    alert("회원가입중 오류발생");
    await router.replace({ name: "userJoin" });
  }
};

const validateId = async () => {
  let userId = registerForm.value.userId;
  let resultDiv = document.querySelector("#idcheck-result");

  if (userId.length < 4 || userId.length > 16) {
    resultDiv.setAttribute("class", "mb-3 text dark");
    messageForId.value = "아이디는 4자 이상 16자 이하입니다.";
    isUseId.value = false;
  } else {
    const response = await local.get(`user/${userId}`);
    if (response.data.isDuplicated) {
      resultDiv.setAttribute("class", "mb-3 text-danger");
      messageForId.value = userId + "는 사용할 수 없습니다.";
      isUseId.value = false;
    } else {
      resultDiv.setAttribute("class", "mb-3 text-primary");
      messageForId.value = userId + "는 사용할 수 있습니다.";
      isUseId.value = true;
    }
  }
};
</script>

<template>
  <div class="container">
    <div class="form-box">
      <form @submit.prevent="">
        <div class="form-group">
          <label for="id">아이디:</label>
          <input
            type="text"
            class="form-control"
            placeholder="아이디"
            id="userid"
            v-model="registerForm.userId"
            @keyup="validateId"
          />
          <div id="idcheck-result">{{ messageForId }}</div>
        </div>
        <div class="form-group">
          <label for="pwd">비밀번호:</label>
          <input
            type="password"
            class="form-control"
            placeholder="비밀번호"
            id="pwd"
            v-model="registerForm.password"
          />
        </div>
        <div class="form-group">
          <label for="pwd">비밀번호확인:</label>
          <input
            type="password"
            class="form-control"
            placeholder="비밀번호 확인"
            id="pwdCheck"
            v-model="passwordCheck"
            @keyup="checkPassword"
          />
          <div class="mb-3 text-danger" v-show="!passwordValidate">비밀번호를 확인해주세요.</div>
        </div>
        <div class="form-group">
          <label for="nickname">닉네임:</label>
          <input
            type="text"
            class="form-control"
            placeholder="닉네임"
            id="nickname"
            v-model="registerForm.nickname"
          />
        </div>
        <div class="form-group">
          <label for="phone">핸드폰:</label>
          <input
            type="text"
            class="form-control"
            placeholder="숫자만 입력하세요!"
            id="phone"
            v-model="registerForm.phone"
            oninput="this.value = this.value.replace(/[^0-9.]/g, '')"
            maxlength="11"
          />
        </div>
        <div class="form-group">
          <label for="email">이메일:</label>
          <input
            type="email"
            class="form-control"
            id="email"
            v-model="registerForm.email"
            disabled
          />
        </div>

        <button class="btn btn-primary" @click="registUser">Submit</button>
      </form>
    </div>
  </div>
</template>

<style scoped>
.container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh; /* 뷰포트 높이의 100% */
}

.form-box {
  width: 50%; /* 폼의 너비 */
  padding: 20px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); /* 상자 그림자 효과 */
  border-radius: 5px; /* 모서리 둥글게 */
}
</style>
