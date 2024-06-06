<script setup>
import { RouterView } from "vue-router";
import SideBar from "./components/common/sidebar/SideBar.vue";
import { useSidebarStore } from "./stores/sidebar";

const sidebarStore = useSidebarStore();
</script>

<template>
  <SideBar></SideBar>
  <div
    id="main"
    :class="[{ collapsed: sidebarStore.isCollapsed }, { onmobile: sidebarStore.isOnMobile }]"
  >
    <RouterView />
  </div>
</template>

<style scoped>
#main {
  margin-left: 290px;
  transition: margin-left 0.3s ease;
}
#main.collapsed {
  margin-left: 65px;
}
#main.onmobile {
  margin-left: 65px;
}
</style>
