import "@/assets/main.css";
import { createApp } from "vue";
import { createPinia } from "pinia";
import { useKakao } from "vue3-kakao-maps/@utils";
import VueCookies from "vue-cookies";
import piniaPluginPersistedstate from "pinia-plugin-persistedstate";

import "bootstrap";
import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap-icons/font/bootstrap-icons.css";

import VueSidebarMenu from "vue-sidebar-menu";
import "vue-sidebar-menu/dist/vue-sidebar-menu.css";

import App from "./App.vue";
import router from "./router";

useKakao("**key**", [
  "clusterer",
  "services",
  "drawing",
]);

const app = createApp(App);

app.use(VueCookies);
app.config.globalProperties.$cookies.config("7d");

const pinia = createPinia();
pinia.use(piniaPluginPersistedstate);
app.use(pinia);
app.use(router);
app.use(VueSidebarMenu);

app.mount("#app");
