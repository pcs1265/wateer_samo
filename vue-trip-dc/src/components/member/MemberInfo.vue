<script setup>
import { ref, onMounted } from "vue";
import { useRoute, useRouter } from "vue-router";
import { localAxios } from "@/util/http-commons";
const local = localAxios();
const route = useRoute();

const userId = route.params.userId;

onMounted(async () => {
  const response = await local.get(`user/one/${userId}`);
  user.value = response.data;
  console.log(response);
  console.log(user.value);
});

const user = ref({
  userId: "",
  nickname: "",
  password: "",
  phone: "",
  email: "",
});
</script>

<template>
  <div class="container mt-5">
    <h2 class="mb-4">회원정보</h2>
    <form @submit.prevent="">
      <div class="mb-3">
        <label for="nickname" class="form-label">아이디</label>
        <input type="text" class="form-control" id="userId" v-model="user.userId" disabled />
      </div>
      <div class="mb-3">
        <label for="nickname" class="form-label">닉네임</label>
        <input type="text" class="form-control" id="nickname" v-model="user.nickname" disabled />
      </div>
      <div class="mb-3">
        <label for="oldPassword" class="form-label">비밀번호</label>
        <input
          type="password"
          class="form-control"
          id="oldPasswrod"
          v-model="user.password"
          disabled
        />
      </div>
      <div class="mb-3">
        <label for="phone" class="form-label">전화번호</label>
        <input type="phone" class="form-control" id="phone" v-model="user.phone" disabled />
      </div>
      <div class="mb-3">
        <label for="email" class="form-label">이메일</label>
        <input type="email" class="form-control" id="email" v-model="user.email" disabled />
      </div>

      <!-- <button type="submit" class="btn btn-primary" @click="modifyInform">닫기</button> -->
    </form>
    <div class="user-container">
      <RouterLink :to="{ name: 'member-modify', params: { userId: userId } }" class="user-link">
        회원정보수정
      </RouterLink>
      <br />
      <RouterLink :to="{ name: 'member-manage' }" class="user-link" v-if="userId == 'admin'">
        회원 관리
      </RouterLink>
      <br />
    </div>
  </div>
</template>

<style scoped>
.preview img {
  border: 1px;
  max-width: 20%;
  height: auto;
}
.container {
  display: flex;
  justify-content: center;
  align-items: center;
  justify-content: center; /* Center children vertically */
  flex-direction: column;
  border: 1px solid #ccc; /* 테두리 색상과 두께 설정 */
  border-radius: 5px; /* 모서리 둥글게 설정 */
  padding: 20px; /* Adjust padding as needed */
  width: 20%;
}

.user-link {
  display: inline-block;
  padding: 10px 15px;
  background-color: #4caf50;
  color: white;
  border-radius: 5px;
  text-decoration: none;
  transition: background-color 0.3s ease;
  margin-bottom: 10px;
}

.user-modify-link:hover {
  background-color: #45a049;
}

.user-container {
  display: flex;
  flex-direction: column; /* Stack children vertically */
  align-items: center; /* Center children horizontally */
  justify-content: center; /* Center children vertically */
}
</style>
