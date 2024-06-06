<script setup>
import { useUserStore } from "@/stores/user";

import { storeToRefs } from "pinia";
import { ref, onMounted } from "vue";
import { useRoute, useRouter } from "vue-router";
import { localAxios } from "@/util/http-commons";
const local = localAxios();
const route = useRoute();
const router = useRouter();
const { params } = useRouter();
const userId = route.params.userId;

const userStore = useUserStore();
const { userInfo } = storeToRefs(userStore);

onMounted(async () => {
  const response = await local.get(`user/one/${userId}`);
  user.value = response.data;
  console.log(response);
  console.log(user.value);
});

const user = ref({});

const modifiedInfo = ref({
  userId: userId,
  nickname: "",
  password: "",
  phone: "",
  email: user.value.email,
});

const oldPassword = ref("");
const paramForPw = ref({
  userId: userId,
  email: user.value.email,
});

const modifyInform = async () => {
  const orgPw = user.value.password;
  console.log(orgPw);
  console.log(oldPassword.value);
  if (orgPw != oldPassword.value) {
    alert("비밀번호를 확인해주세요!");
    router.replace({ name: "member-modify" });
    return;
  }
  await userStore.modify(modifiedInfo);
  router.push({ name: "mypage" });
};

const profileImage = ref(""); // 프로필 이미지를 위한 ref 생성

// 이미지를 선택할 때 호출될 함수
const onImageChange = (event) => {
  const files = event.target.files;
  if (files.length !== 0) {
    const fileReader = new FileReader();
    fileReader.onload = (e) => {
      profileImage.value = e.target.result; // 이미지 데이터를 base64로 변환하여 저장
    };
    fileReader.readAsDataURL(files[0]);
  }
};
</script>

<template>
  <div class="container mt-5">
    <h2 class="mb-4">회원정보 수정</h2>
    <form @submit.prevent="">
      <div class="mb-3">
        <label for="userId" class="form-label">ID</label>
        <input
          type="text"
          class="form-control"
          id="userId"
          v-model="modifiedInfo.userId"
          disabled
        />
      </div>

      <div class="mb-3">
        <label for="nickname" class="form-label">닉네임</label>
        <input type="text" class="form-control" id="nickname" v-model="modifiedInfo.nickname" />
      </div>
      <div class="mb-3">
        <label for="oldPassword" class="form-label">기존 비밀번호</label>
        <input type="password" class="form-control" id="oldPasswrod" v-model="oldPassword" />
      </div>
      <div class="mb-3">
        <label for="newPassword" class="form-label">신규 비밀번호</label>
        <input
          type="password"
          class="form-control"
          id="newPassword"
          v-model="modifiedInfo.password"
        />
      </div>
      <div class="mb-3">
        <label for="phone" class="form-label">전화번호</label>
        <input
          type="phone"
          class="form-control"
          id="phone"
          placeholder="숫자만 입력하세요!"
          oninput="this.value = this.value.replace(/[^0-9.]/g, '')"
          maxlength="11"
          v-model="modifiedInfo.phone"
        />
      </div>
      <div class="mb-3">
        <label for="email" class="form-label">이메일</label>
        <input type="email" class="form-control" id="email" v-model="user.email" disabled />
      </div>

      <button type="submit" class="btn btn-primary" @click="modifyInform">수정하기</button>
    </form>
  </div>
</template>

<style scoped>
.preview img {
  border: 1px;
  max-width: 20%;
  height: auto;
}
</style>
