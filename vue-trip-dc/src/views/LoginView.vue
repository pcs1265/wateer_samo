<script setup>
import { ref, onMounted } from "vue";
import { useUserStore } from "@/stores/user";
import { RouterLink, useRoute, useRouter } from "vue-router";
import { getCurrentInstance } from "vue";

import { storeToRefs } from "pinia";
import axios from "axios";

const instance = getCurrentInstance();
const store = useUserStore();
const { isLogin } = storeToRefs(store);
const { login, verifyToken } = store;
const loginInput = ref({
  userId: "",
  password: "",
  isIdCheck: false,
});

const route = useRoute();
const router = useRouter();
const accessToken = ref("");

const loginGo = async () => {
  if (loginInput.value.userId === "") {
    alert("아이디를 입력하세요.");
    return;
  }
  if (loginInput.value.userPw === "") {
    alert("비밀번호를 입력하세요.");
    return;
  }
  await login(loginInput);
  let token = localStorage.getItem("accessToken");
  if (isLogin.value) {
    await verifyToken(token);
    router.replace({ name: "home" });
  } else {
    alert("아이디나 비밀번호를 다시 확인해주세요.");
    return;
  }
  //let token = JSON.parse(localStorage.getItem("user"))["access-token"];
  //console.log("LoginView:" + token);
};

onMounted(() => {
  if (instance.proxy.$cookies.isKey("userId")) {
    loginInput.value.userId = instance.proxy.$cookies.get("userId");
    loginInput.value.isIdCheck = true;
  }
});

function loginWithKakao() {
  // 팝업 창의 URL (카카오 로그인 페이지)
  const popupUrl = `https://kauth.kakao.com/oauth/authorize?client_id=51d5deee2572e76d9b0cb7b8f9c578c5&redirect_uri=http://${window.location.hostname}:5173/oauth2/callback/kakao&response_type=code`;

  // 팝업 창 옵션
  const popupOptions =
    "width=500, height=600, resizable=no, scrollbars=no, status=no;";

  // 팝업 창 열기
  const popup = window.open(popupUrl, "카카오 로그인", popupOptions);

  // 팝업 창에서 로그인 성공 시 호출될 함수
  // window.kakaoLoginSuccess = function (authObj) {
  //   // 로그인 성공 시 로직
  //   // 예: 사용자 정보 요청, 메인 화면으로 리다이렉트 등
  //   console.log(authObj);
  // };
}

window.call = function (data, param) {
  if (data == "board") {
    window.location.href = "/";
  } else {
    router.replace({ name: "oauth2-join", params: { email: param } });
  }
};
</script>

<template>
  <body>
    <div id="container">
      <!-- login Box -->
      <div class="login-box">
        <div id="loginBoxTitle">Wateer SAMO Login</div>
        <div class="form-group">
          <label>고객 아이디</label>
          <input
            type="text"
            name="uid"
            id="uid"
            class="form-control"
            v-model="loginInput.userId"
          />
        </div>
        <div class="form-group">
          <label>비밀번호</label>
          <input
            type="password"
            name="upw"
            id="upw"
            class="form-control"
            autocomplete="off"
            v-model="loginInput.password"
            @keyup.enter="loginGo"
          />
        </div>
        <div id="login-btn-box">
          <div style="">
            <span> 아이디 저장</span>
            <input
              type="checkbox"
              id="workSite"
              name="worksite"
              style="margin-bottom: 5px"
              v-model="loginInput.isIdCheck"
            />
          </div>
          <div style="">
            <input
              type="button"
              id="btnLogin"
              value="로그인"
              class="btn btn-danger"
              @click="loginGo"
            />
          </div>
        </div>
        <div class="kakao-div">
          <img
            src="@/assets/kakao_login_medium_narrow.png"
            @click="loginWithKakao"
          />
        </div>
      </div>
      <!-- login Box //-->
    </div>

    <!-- Bootstrap Bundle with Popper -->
  </body>
</template>

<style scoped>
body {
  background-image: url("./loginbg.png");
  background-size: 100%;
  font-size: 0.75rem;
}
#loginBoxTitle {
  color: #000000;
  font-weight: bold;
  font-size: 1.9rem;
  text-transform: uppercase;
  padding: 5px;
  margin-bottom: 20px;
  background: linear-gradient(to right, #270a09, #8ca6ce);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

input[type="button"] {
  font-size: 0.75rem;
  padding: 5px 10px;
}
.login-box {
  margin: 150px auto;
  background-color: rgba(255, 255, 255, 1);
  border-radius: 10px;
  padding: 40px 30px;
  border: 5px solid #0e0e0e;
  width: 350px;
  filter: drop-shadow(0px 0px 10px rgba(0, 0, 0, 0.5));
}

.form-group label {
  font-size: 0.75rem;
  margin: 5px 0;
}
#login-btn-box {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-top: 10px;
}

.kakao-div {
  cursor: pointer;
}
</style>
