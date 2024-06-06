<script setup>
import { useSidebarStore } from "@/stores/sidebar";
import { useUserStore } from "@/stores/user";
import { storeToRefs } from "pinia";
import { watch, watchEffect } from "vue";
import { useRouter } from "vue-router";

const router = useRouter();
const userStore = useUserStore();
const { isLogin } = storeToRefs(userStore);
const sStore = useSidebarStore();

watch(isLogin, () => {
  console.log(isLogin.value);
  
});

// watchEffect(() => {
//   console.log(isLogin.value);
//   console.log("dfafasfadfsafd");
//   // This will force the component to update when the isLogin state changes
// });

const moveLoginForm = () => {
  router.push({ name: "login" });
};

const moveRegisterForm = () => {
  router.push({ name: "userJoin" });
};

const moveMypage=()=>{
  router.push({name:"mypage"});
}
</script>

<template>
  <Transition name="loginMenu">
    <div v-if="!sStore.isActualCollapsed" class="login-menu">
      <div class="login-menu text-center">
        <div v-if="isLogin !== true">
          <button type="button" class="btn btn-primary m-2" @click="moveLoginForm">로그인</button>
          <button type="button" class="btn btn-secondary m-2" @click="moveRegisterForm">
            회원가입
          </button>
        </div>
        <div v-if="isLogin !== false">
          <button type="button" class="btn btn-primary m-2" @click="userStore.logout">
            로그아웃
          </button>
          <button type="button" class="btn btn-secondary m-2" @click="moveMypage">마이페이지</button>
        </div>
      </div>
    </div>
  </Transition>
</template>

<style scoped>
.login-menu {
  height: 60px;
  width: 290px;
  overflow: hidden;
}
.loginMenu-enter-active,
.loginMenu-leave-active {
  transition: width 0.3s;
}

.loginMenu-enter-from,
.loginMenu-leave-to {
  width: 0px;
}
</style>
