<script setup></script>

<template>
  <div class="vh-100 back-image">
    <div class="blur-drop">
      <div class="container vh-100 overflow-hidden">
        <div class="header text-white noto-sans-kr-header">
          <p>계획해요</p>
        </div>
        <div class="content">
          <RouterView></RouterView>
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
.vertical-area {
  height: 100%;
  display: grid;
  grid-template-rows: auto minmax(0, 100%);
}
.search-bar {
  grid-row: 1;
}
.search-result {
  grid-row: 2;
  overflow: hidden;
}

.back-image {
  background-image: url(https://practicalwanderlust.com/wp-content/uploads/2017/02/How-to-Plan-a-Trip-Travel-Planning-Tips.jpg);
  background-size: auto;
}
.blur-drop {
  backdrop-filter: blur(5px);

  background-color: rgba(0, 0, 0, 0.5);
}
</style>
