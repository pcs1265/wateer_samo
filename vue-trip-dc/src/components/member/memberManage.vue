<script setup>
import { useUserStore } from "@/stores/user";
import PageNavigation from "./common/PageNavigation.vue";

import { ref, onMounted } from "vue";
import MemberListItem from "./MemberListItem.vue";

const uStore = useUserStore();

const param = ref({
  pgno: uStore.currentPage,
  perPage: 12,
});

onMounted(async () => {
  await uStore.getMemberList(param.value);
  console.log(uStore.userList);
});

const onPageChange = (val) => {
  console.log(val + "번 페이지로 이동 준비 끝!!!");
  console.log(uStore.currentPage);
  uStore.currentPage = val;
  uStore.getMemberList(param.value);
};
</script>

<template>
  <div class="vh-100 back-image">
    <div class="blur-drop">
      <div class="container vh-100">
        <div class="header text-white noto-sans-kr-header">
          <p>회원 목록</p>
        </div>
        <div class="content">
          <div class="inner-content">
            <div class="bg-light p-3 my-3 rounded-3">
              <table class="table table-bordered table-striped table-hover mt-2">
                <thead>
                  <tr>
                    <th class="col-2 text-center">아이디</th>
                    <th class="col-2 text-center">비밀번호</th>
                    <th class="col-1 text-center">닉네임</th>
                    <th class="col-2 text-center">이메일</th>
                    <th class="col-2 text-center">가입일</th>
                    <th class="col-1 text-center">수정</th>
                    <th class="col-1 text-center">삭제</th>
                    <th class="col-1 text-center">정지</th>
                  </tr>
                </thead>
                <tbody>
                  <MemberListItem
                    v-for="member of uStore.userList"
                    :key="member.userId"
                    :member="member"
                  />
                </tbody>
              </table>
              <PageNavigation
                :current-page="uStore.currentPage"
                :total-page="uStore.totalPage"
                @pageChange="onPageChange"
              ></PageNavigation>
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

.back-image {
  background-image: url(https://oiwww.s3.us-east-2.amazonaws.com/wp-content/uploads/2020/10/31064653/springboard-qlandscape.jpeg);
  background-size: cover;
}
.blur-drop {
  backdrop-filter: blur(5px);

  background-color: rgba(0, 0, 0, 0.5);
}
</style>
