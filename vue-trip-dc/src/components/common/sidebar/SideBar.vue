<script setup>
import { useSidebarStore } from "@/stores/sidebar";
import { onMounted, ref } from "vue";
import { SidebarMenu } from "vue-sidebar-menu";
import UserFooter from "./UserFooter.vue";
import { useUserStore } from "@/stores/user";

const sStore = useSidebarStore();
const uStore = useUserStore();

const menu = ref([
  // {
  //   header: "물사모",
  //   hiddenOnCollapse: true,
  // },
  {
    href: { name: "home" },
    title: "Home",
    icon: "bi bi-house-door-fill h5",
  },
  {
    href: { name: "attraction-search" },
    title: "여행가요",
    icon: "bi bi-backpack-fill h5",
  },
  {
    icon: "bi bi-headset-vr h5",
    title: "물놀이해요",
    child: [
      {
        href: { name: "beach" },
        title: "해수욕장 가요",
        icon: "bi bi-umbrella",
      },
      {
        href: { name: "stream" },
        title: "계곡, 하천 가요",
        icon: "bi bi-water h5",
      },
    ],
  },
  {
    href: { name: "plan" },
    title: "계획해요",
    icon: "bi bi-compass-fill h5",
  },

  {
    href: { name: "board" },
    title: "게시판",
    icon: "bi bi-chat-dots-fill h5",
  },
  {
    href: { name: "mypage" },
    title: "마이페이지",
    icon: "bi bi-person-fill h5",
  },
]);

const selectedTheme = "white-theme";

// @update:collapsed="onToggleCollapse"
// @item-click="onItemClick"

onMounted(() => {
  let res = document.querySelectorAll(".v-sidebar-menu");
  res[0].addEventListener("transitionend", (event) => {
    event.stopPropagation();
    if (event.target.classList.contains("v-sidebar-menu")) {
      sStore.inTransition = false;
    }
  });
  res[0].addEventListener("transitionstart", (event) => {
    event.stopPropagation();
    if (event.target.classList.contains("v-sidebar-menu")) {
      sStore.inTransition = true;
    }
  });
});
</script>

<template>
  <SidebarMenu
    v-model:collapsed="sStore.isActualCollapsed"
    :menu="menu"
    :show-one-child="true"
    :disable-hover="false"
    ref="sidebarElement"
  >
    <template v-slot:header>
      <Transition name="title">
        <img src="@/assets/logo.svg" class="logo" />
      </Transition>
    </template>
    <template v-slot:footer>
      <UserFooter></UserFooter>
    </template>
  </SidebarMenu>
</template>

<style>
.logo {
  width: 100%;
  backdrop-filter: blur(3px);
  background-color: rgba(255, 255, 255, 0.3);
}

.title-enter-active,
.title-leave-active {
  width: 290px;
  overflow: hidden;
  transition: width 0.3s ease, height 0.3 ease, font-size 0.3 ease;
}

.title-enter-from,
.title-leave-to {
  width: 0px;
  height: 0px;
  font-size: 0px;
}

.v-sidebar-menu {
  transition: all 0.3s ease;
  box-shadow: rgba(0, 0, 0, 0.8) 0 0 30px 5px;
}

.v-sidebar-menu.vsm_expanded {
  background-image: url("https://images.unsplash.com/photo-1500835556837-99ac94a94552?q=80&w=1000&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTl8fHRyYXZlbGxpbmd8ZW58MHx8MHx8fDA%3D");
  background-size: cover;
}
.v-sidebar-menu.vsm_collapsed {
  background-image: url("https://images.unsplash.com/photo-1500835556837-99ac94a94552?q=80&w=1000&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTl8fHRyYXZlbGxpbmd8ZW58MHx8MHx8fDA%3D");
  background-size: cover;
}

.v-sidebar-menu .vsm--item {
  transition: all 0.3s ease;
}
.v-sidebar-menu.vsm_expanded .vsm--item {
  background-color: rgba(0, 0, 0, 0.1);
  backdrop-filter: blur(20px);
}
.v-sidebar-menu.vsm_collapsed .vsm--item {
  background-color: rgba(0, 0, 0, 0.3);
}

.v-sidebar-menu .vsm--child {
  -ms-overflow-style: none; /* IE and Edge */
  scrollbar-width: none; /* Firefox */
}
</style>
