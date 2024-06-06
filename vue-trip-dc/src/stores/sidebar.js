import { computed, ref } from "vue";
import { defineStore } from "pinia";

export const useSidebarStore = defineStore("sidebar", () => {
  //모바일인지 여부
  const isOnMobile = ref(false);
  const isNarrow = ref(false);

  //사이드바가 접혔는지 여부
  const isActualCollapsed = ref(false);
  const inTransition = ref(false);

  const isCollapsed = computed(() => {
    if (isNarrow.value) {
      return true;
    } else {
      return isActualCollapsed.value;
    }
  });

  window.addEventListener("resize", onResize);
  function onResize() {
    if (window.innerWidth <= 1600) {
      isNarrow.value = true;
    } else {
      isNarrow.value = true;
    }
    if (window.innerWidth <= 767) {
      isOnMobile.value = true;
    } else {
      isOnMobile.value = false;
    }
  }
  onResize();
  isActualCollapsed.value = isNarrow.value;
  return { isCollapsed, isOnMobile, isNarrow, isActualCollapsed, inTransition };
});
