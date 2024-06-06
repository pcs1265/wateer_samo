<script setup>
import { ref } from "vue";
import { localAxios } from "@/util/http-commons";
import { useRouter } from "vue-router";
import { HttpStatusCode } from "axios";

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
const isEmailCertify = ref("");
const isUseId = ref(false);

const emailConfirm = ref("");
const emailKey = ref("");
const emailValid = ref(false);
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
  if (emailValid.value == false) {
    alert("이메일 인증을 완료해야 합니다.");
    return;
  }
  try {
    const response = await local.post("/user/join", JSON.stringify(registerForm.value));
    console.log(response);
    alert(response.data.message);
    await router.replace({ name: "login" });
  } catch (error) {
    alert(error.data.message);
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

const sendEmail = async () => {
  if (registerForm.value.email == "") {
    alert("이메일을 입력하세요.");
    return;
  }
  isEmailCertify.value = true;

  const form = new FormData();
  form.append("email", registerForm.value.email);
  const response = await local.post("/user/email", registerForm.value.email);
  if (response.data.exist) {
    alert(response.data.exist);
  } else if (response.status == HttpStatusCode.Ok) {
    alert("이메일이 발송되었습니다.");
    takeTarget();
    emailKey.value = response.data.key;
  } else {
    alert("잘못된 이메일입니다.");
  }
};

const checkEmail = () => {
  console.log(isEmailCertify.value);
  if (isEmailCertify.value == false) {
    alert("인증시간을 초과하였습니다. 다시 인증해주세요.");
    return;
  }
  if (emailConfirm.value == emailKey.value) {
    alert("인증완료");
    emailValid.value = true;
    document.querySelector(".target__time").hidden = true;
    document.querySelector("#email").disabled = true;
    document.querySelector("#emailConfirm").disabled = true;
    document.querySelector("#email-certifyBtn").disabled = true;
    document.querySelector("#email-completeBtn").disabled = true;
  } else {
    alert("인증번호가 일치하지 않습니다.");
  }
};

const takeTarget = () => {
  let time = 180;

  const timer = (document.querySelector(".target__time").hidden = false);
  const remainingMin = document.getElementById("remaining__min");
  const remainingSec = document.getElementById("remaining__sec");
  const completeBtn = document.getElementById("email-completeBtn");

  remainingMin.style.color = "black";
  remainingSec.style.color = "black";
  let timerFunc = setInterval(function () {
    if (time > 0) {
      // >= 0 으로하면 -1까지 출력된다.
      time = time - 1; // 여기서 빼줘야 3분에서 3분 또 출력되지 않고, 바로 2분 59초로 넘어간다.
      let min = Math.floor(time / 60);
      let sec = String(time % 60).padStart(2, "0");
      remainingMin.innerText = min;
      remainingSec.innerText = sec;
      // time = time - 1
      if (time < 5) {
        remainingMin.style.color = "red";
        remainingSec.style.color = "red";
      }
    } else {
      // completeBtn.disabled = true;
      isEmailCertify.value = false;
      clearInterval(timerFunc);
    }
  }, 1000);
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
            placeholder="Enter email"
            id="email"
            v-model="registerForm.email"
          />
          <button type="button" class="btn btn-light" @click="sendEmail" id="email-certifyBtn">
            이메일인증
          </button>
        </div>
        <div class="form-group">
          <label for="emailConfirm">인증코드:</label>
          <input
            type="text"
            class="form-control"
            placeholder="이메일인증코드"
            id="emailConfirm"
            v-model="emailConfirm"
          />
          <span class="target__time" hidden>
            <span id="remaining__min">3</span> :
            <span id="remaining__sec">00</span>
          </span>
          <button type="button" class="btn btn-light" @click="checkEmail" id="email-completeBtn">
            확인
          </button>
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
