<script setup>
import { onMounted, ref } from "vue";
import { useRoute, useRouter } from "vue-router";
import axios from "axios";
import { localAxios } from "@/util/http-commons";
import { useUserStore } from "@/stores/user";

const local = localAxios();
const route = useRoute();
const router = useRouter();
const accessToken = ref("");
const userStore = useUserStore();
onMounted(async () => {
  // URL에서 인증 코드를 추출하는 로직
  console.log("gg");
  await handleRedirect();
});

function handleRedirect() {
  // URL에서 code 파라미터의 값을 추출
  const code = route.query.code;
  console.log(code);
  if (code) {
    // 서버에 액세스 토큰 요청
    requestAccessToken(code);
  }
}

const requestAccessToken = async (code) => {
  try {
    // 서버에 POST 요청을 보내 액세스 토큰 요청
    const response = await local.post("/api/auth/kakao", { authorizationCode: code });
    console.log(response);

    // 성공적으로 토큰을 받았다면, 사용자 정보 요청 또는 페이지 이동
    if (response.data.userId != null) {
      userStore.isLogin = true;
      userStore.isLoginError = false;
      userStore.isValidToken = true;
      userStore.oAuth = "kakao";

      localStorage.setItem("accessToken", response.data["access-token"]);
      localStorage.setItem("refreshToken", response.data["refresh-token"]);
      // 로컬 스토리지에서 액세스 토큰 가져오기
      let token = await localStorage.getItem("accessToken");
      await userStore.verifyToken(token);
      // 로그인 상태이면 토큰 검증
      if (userStore.isLogin) {
        // await userStore.verifyToken(token);
        callParent("board", {});
        router.replace({ name: "board" });
      } else {
        callParent("join", response.data.email);
        console.log("로그인 상태가 아닙니다.");
        router.replace({ name: "oauth2-join", params: { email: response.data.email } });
      }
    } else {
      callParent("join", response.data.email);
      console.log("사용자 ID가 없습니다.");
      router.replace({ name: "oauth2-join", params: { email: response.data.email } });
    }
  } catch (error) {
    console.error("액세스 토큰 요청 실패:", error);
  }
};

function callParent(msg, params) {
  console.log("부모창에 있는 함수 call 호출");

  opener.call(msg, params);
  //window.opener.location.reload();
  self.close();
}

// function getUserInfo(token) {
//   // 액세스 토큰을 사용하여 사용자 정보 요청
//   axios.get('/api/user', { headers: { 'Authorization': `Bearer ${token}` } })
//     .then(response => {
//       // 사용자 정보를 받아와 처리
//       console.log('사용자 정보:', response.data);
//       // 원하는 페이지로 이동 또는 상태 업데이트
//     })
//     .catch(error => {
//       console.error('사용자 정보 요청 실패:', error);
//     });
// }
</script>

<template>
  <div>
    <p>안녕하세요</p>
  </div>
</template>
<style scoped></style>
