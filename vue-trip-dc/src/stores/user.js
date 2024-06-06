import { defineStore } from "pinia";
import { localAxios } from "@/util/http-commons";
import { useRouter } from "vue-router";
import { jwtDecode } from "jwt-decode";
import { ref } from "vue";
import { httpStatusCode } from "@/util/http-status";
import axios from "axios";
import { getCurrentInstance } from "vue";
//import vueCookies from "vue3-cookies"

const local = localAxios();
//const { cookies } = vueCookies();

export const useUserStore = defineStore(
  "user",
  () => {
    const router = useRouter();
    const instance = getCurrentInstance();

    const isLogin = ref(false);
    const isLoginError = ref(false);
    const userInfo = ref(null);
    const isValidToken = ref(false);
    const oAuth = ref("");

    const userList = ref([]);
    const currentPage = ref(1);
    const totalPage = ref(0);
    const wishList = ref([]);

    const login = async (params) => {
      try {
        console.log(params);
        const response = await local.post(
          "/user/login",
          JSON.stringify(params.value)
        );
        if (response.status === httpStatusCode.CREATE) {
          console.log("로그인 성공");
          isLogin.value = true;
          isLoginError.value = false;
          isValidToken.value = true;

          if (params.value.isIdCheck) {
            instance.proxy.$cookies.set("userId", params.value.userId, "7d");
          } else {
            instance.proxy.$cookies.remove("userId");
          }

          localStorage.setItem("accessToken", response.data["access-token"]);
          localStorage.setItem("refreshToken", response.data["refresh-token"]);
        }
        console.log(response);
      } catch (error) {
        console.log("로그인 대실패!");
        console.log(error);
        isLogin.value = false;
        isLoginError.value = true;
        isValidToken.value = false;
      }
    };

    const verifyToken = async (token) => {
      try {
        let decodeToken = jwtDecode(token);

        console.log(localStorage.getItem("accessToken"));
        local.defaults.headers["Authorization"] =
          localStorage.getItem("accessToken");

        const response = await local.get(`/user/info/${decodeToken.userId}`);

        if (response.status === httpStatusCode.OK) {
          console.log(response.data.userInfo);
          userInfo.value = response.data.userInfo;
          console.log("userInfo:" + userInfo.value);
        } else {
          console.log("유저정보 없음");
        }
      } catch (error) {
        console.log("토큰 만료됨.");
        isValidToken.value = false;
        await tokenRegenerate();
      }
    };

    const tokenRegenerate = async () => {
      try {
        local.defaults.headers["refreshToken"] =
          localStorage.getItem("refreshToken");

        const response = await local.post(
          "/user/refresh",
          JSON.stringify(userInfo.value)
        );
        if (response.status === httpStatusCode.CREATE) {
          let accessToken = response.data["access-token"];

          localStorage.setItem("accessToken", accessToken);
          isValidToken.value = true;
        }
      } catch (error) {
        try {
          const response = await local.get(`/user/logout/${userInfo.userId}`);
          localStorage.removeItem("accessToken");
          localStorage.removeItem("refreshToken");
          if (response.status === httpStatusCode.OK) {
            console.log("리프레시 토큰 제거");
          } else {
            console.log("리프레시 토큰 제거 실패");
          }
          alert("리프레시 토큰 기간만료. 다시 로그인해주세요");
          isLogin.value = false;
          userInfo.value = null;
          isValidToken.value = false;
          router.push({ name: "login" });
        } catch (error) {
          console.log(error);
          isLogin.value = false;
          userInfo.value = null;
        }
      }
    };

    const logout = async () => {
      //console.log("로그아웃 아이디:" + userInfo.value.userId);
      try {
        const response = local.get(`/user/logout/${userInfo.userId}`);
        if ((await response).status === httpStatusCode.OK) {
          isLogin.value = false;
          userInfo.value = null;
          isValidToken.value = false;

          localStorage.removeItem("accessToken");
          localStorage.removeItem("refreshToken");
        } else {
          console.log("유저정보X");
        }

        if (oAuth.value === "kakao") {
          oAuth.value = "";
          window.location.href = `https://kauth.kakao.com/oauth/logout?client_id=51d5deee2572e76d9b0cb7b8f9c578c5&logout_redirect_uri=http://${window.location.hostname}:5173/oauth2/logout/kakao`;
        }
        alert("사이트에서 로그아웃되었습니다.");
      } catch (error) {
        console.log(error);
      }
    };

    const modify = async (param) => {
      try {
        const response = await local.put(
          `/user/${userInfo.userId}`,
          JSON.stringify(param.value)
        );
        if (response.status === httpStatusCode.OK) {
          alert(response.data.message);
        } else {
          alert(response.data.message);
        }
      } catch (error) {
        console.log(error);
      }
    };

    const findPw = async (param) => {
      console.log(JSON.stringify(param.value));
      try {
        const response = await local.post(
          "/user/findPw",
          JSON.stringify(param.value)
        );
        console.log(response);
        return response.data;
      } catch (error) {
        console.log(error);
      }
    };

    const getMemberList = async (param) => {
      userList.value = [];
      try {
        const response = await local.get("/user", JSON.stringify(param.value));
        console.log(response);
        for (let user of response.data) {
          userList.value.push(user);
        }
      } catch (error) {
        console.log(error);
      }
    };

    const getWishList = async () => {
      console.log(userInfo.value.userId);
      wishList.value = [];
      const response = await local.get(
        `attraction/wish/${userInfo.value.userId}`
      );
      console.log(response);
      const wishes = response.data;
      for (let wish of wishes) {
        wishList.value.push(wish);
      }
    };

    return {
      login,
      verifyToken,
      isLogin,
      isLoginError,
      isValidToken,
      userInfo,
      tokenRegenerate,
      logout,
      modify,
      findPw,
      oAuth,
      userList,
      currentPage,
      totalPage,
      getMemberList,
      getWishList,
      wishList,
    };
  },
  {
    persist: true,
  }
);
