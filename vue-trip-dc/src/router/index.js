import { createRouter, createWebHistory } from "vue-router";
import HomeView from "../views/HomeView.vue";
import BoardView from "@/views/BoardView.vue";
import BoardDetail from "@/components/board/BoardDetail.vue";
import BoardList from "@/components/board/BoardList.vue";
import BoardModify from "@/components/board/BoardModify.vue";
import BoardWrite from "@/components/board/BoardWrite.vue";
import LoginView from "@/views/LoginView.vue";
import RegisterView from "@/views/RegisterView.vue";
import MyPageView from "@/views/MyPageView.vue";

import MemberHome from "@/components/member/MemberHome.vue";
import MemberModify from "@/components/member/MemberModify.vue";
import memberInfo from "@/components/member/MemberInfo.vue";

import Redirect from "@/views/Redirect.vue";
import OAuthLogoutView from "@/views/OAuthLogoutView.vue";
import RegisterOAuthView from "@/views/RegisterOAuthView.vue";

import { useUserStore } from "@/stores/user";
import { storeToRefs } from "pinia";
import AttractionView from "@/views/AttractionView.vue";
import AttractionList from "@/components/attraction/AttractionList.vue";
import AttractionReview from "@/components/attraction/AttractionReview.vue";
import AttractionDetail from "@/components/attraction/AttractionDetail.vue";
import AttractionWish from "@/components/attraction/AttractionWish.vue";

import BeachView from "@/views/BeachView.vue";
import BeachReview from "@/components/beach/BeachReview.vue";
import BeachDetail from "@/components/beach/BeachDetail.vue";
import BeachList from "@/components/beach/BeachList.vue";

import StreamView from "@/views/StreamView.vue";
import StreamList from "@/components/stream/StreamList.vue";
import StreamDetail from "@/components/stream/StreamDetail.vue";
import StreamReview from "@/components/stream/StreamReview.vue";
import PlanView from "@/views/PlanView.vue";
import PlanList from "@/components/plan/PlanList.vue";
import PlanManage from "@/components/plan/PlanManage.vue";
import memberManage from "@/components/member/memberManage.vue";
import PlanShare from "@/components/plan/share/PlanShare.vue";

const onlyAuthUser = async (to, from, next) => {
  const userStore = useUserStore();
  const { userInfo, isValidToken } = storeToRefs(userStore);
  const { verifyToken } = userStore;

  let token = localStorage.getItem("accessToken");

  if (userInfo.value != null && token) {
    await verifyToken(token);
  }
  if (!isValidToken.value || userInfo.value === null) {
    next({ name: "login" });
  } else {
    next();
  }
};

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      name: "home",
      component: HomeView,
    },
    {
      path: "/board",
      name: "board",
      component: BoardView,
      redirect: { name: "article-list" },
      children: [
        {
          path: "list",
          name: "article-list",
          component: BoardList,
        },
        {
          path: "view/:no",
          name: "article-view",
          //beforeEnter: onlyAuthUser,
          component: BoardDetail,
        },
        {
          path: "write",
          name: "article-write",
          beforeEnter: onlyAuthUser,
          component: BoardWrite,
        },
        {
          path: "modify/:no",
          name: "article-modify",
          component: BoardModify,
        },
      ],
    },
    {
      path: "/login",
      name: "login",
      component: LoginView,
    },
    {
      path: "/join",
      name: "userJoin",
      component: RegisterView,
    },
    {
      path: "/mypage",
      name: "mypage",
      component: MyPageView,
      beforeEnter: onlyAuthUser,
      redirect: { name: "mypageHome" },
      children: [
        {
          path: "/mypageHome",
          name: "mypageHome",
          component: MemberHome,
        },
        {
          path: "member/modify/:userId",
          name: "member-modify",
          component: MemberModify,
        },
        {
          path: "member/manage",
          name: "member-manage",
          component: memberManage,
        },
        {
          path: "member/:userId",
          name: "memberInfo",
          component: memberInfo,
        },
      ],
    },

    {
      path: "/attractionSearch",
      name: "attraction-search",
      component: AttractionView,
      children: [
        {
          path: "list",
          name: "attraction-list",
          component: AttractionList,
        },
        {
          path: "detail/:contentId",
          name: "attraction-detail",
          component: AttractionDetail,
        },
        {
          path: "review/:contentId",
          name: "attraction-review",
          component: AttractionReview,
        },
        {
          path: "wish/:userId",
          name: "attraction-wish",
          component: AttractionWish,
        },
      ],
    },
    {
      path: "/beach",
      name: "beach",
      component: BeachView,
      children: [
        {
          path: "list",
          name: "beach-list",
          component: BeachList,
        },
        {
          path: "detail/:contentId",
          name: "beach-detail",
          component: BeachDetail,
        },
        {
          path: "review/:contentId",
          name: "beach-review",
          component: BeachReview,
        },
      ],
    },
    {
      path: "/plan",
      name: "plan",
      component: PlanView,
      redirect: { name: "plan-list" },
      children: [
        {
          path: "list",
          name: "plan-list",
          component: PlanList,
          beforeEnter: onlyAuthUser,
        },
        {
          path: "detail/:contentId",
          name: "plan-detail",
          component: PlanManage,
          beforeEnter: onlyAuthUser,
        },
        {
          path: "share/:contentId",
          name: "plan-share",
          component: PlanShare,
        },
      ],
    },
    {
      path: "/stream",
      name: "stream",
      component: StreamView,
      children: [
        {
          path: "list",
          name: "stream-list",
          component: StreamList,
        },
        {
          path: "detail/:objtId",
          name: "stream-detail",
          component: StreamDetail,
        },
        {
          path: "review/:objtId",
          name: "stream-review",
          component: StreamReview,
        },
      ],
    },
    {
      path: "/oauth2/callback/kakao",
      component: Redirect,
    },
    {
      path: "/oauth2/logout/kakao",
      component: OAuthLogoutView,
    },
    {
      path: "/oauth2/join/:email",
      name: "oauth2-join",
      component: RegisterOAuthView,
    },

    // {
    //   path: '/about',
    //   name: 'about',
    //   // route level code-splitting
    //   // this generates a separate chunk (About.[hash].js) for this route
    //   // which is lazy-loaded when the route is visited.
    //   component: () => import('../views/AboutView.vue')
    // }
  ],
});

export default router;
