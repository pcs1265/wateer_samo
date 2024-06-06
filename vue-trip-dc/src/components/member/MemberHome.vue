<script setup>
import { RouterLink, useRouter } from "vue-router";
import { useUserStore } from "@/stores/user";
import { onMounted } from "vue";


const userStore = useUserStore();
const router=useRouter();
const userId = userStore.userInfo.userId;

onMounted(async()=>{
  await router.push({name:'memberInfo', params:{userId:userId}});
})
</script>

<template>
  <div class="user-container">
    <RouterLink :to="{ name: 'member-modify', params: { userId: userId } }" class="user-link">
      회원정보수정/탈퇴
    </RouterLink>
    <br />
    <RouterLink :to="{ name: 'member-manage' }" class="user-link" v-if="userId == 'admin'">
      회원 관리
    </RouterLink>
    <br />
    <RouterLink :to="{ name: 'memberInfo', params: { userId: userId } }" class="user-link">
      회원 정보
    </RouterLink>
  </div>
</template>

<style scoped>
.user-container {
  display: flex;
  flex-direction: column; /* Stack children vertically */
  align-items: center; /* Center children horizontally */
  justify-content: center; /* Center children vertically */
  height: 100vh; /* Full height of the viewport */
  margin-top: 0; /* Remove the margin-top as it's no longer needed */

  border: 1px solid #ccc; /* 테두리 색상과 두께 설정 */
  padding: 10px; /* 내부 여백 설정 */
  margin: 10px 0; /* 상자 주변 여백 설정 */
  border-radius: 5px; /* 모서리 둥글게 설정 */
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
</style>
