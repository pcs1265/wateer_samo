<script setup>
import { useRouter } from "vue-router";
import { useUserStore } from "@/stores/user";

const props = defineProps({
  member: Object,
});

const router = useRouter();
const modify = () => {
  console.log(props.member.userId);
  router.push({ name: "member-modify", params: { userId: props.member.userId } });
};
</script>

<template>
  <tr @click="" role="button">
    <td class="text-center">{{ member.userId }}</td>
    <td>
      {{ member.password }}
    </td>
    <td class="text-center">
      {{ member.nickname }}
    </td>
    <td class="text-center">{{ member.email }}</td>
    <td class="text-center">{{ member.joinDate }}</td>
    <td class="text-center"><button type="button" class="btn btn-primary" @click="modify">수정</button></td>
    <td class="text-center"><button type="button" class="btn btn-danger">삭제</button></td>
    <td class="text-center"><button type="button" class="btn btn-dark">정지</button></td>
  </tr>
</template>

<style scoped></style>
