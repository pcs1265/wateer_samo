import { localAxios } from "@/util/http-commons";
import { defineStore } from "pinia";
import { ref } from "vue";

const local = localAxios();

export const useBoardStore = defineStore("board", () => {
  const article = ref({});
  const articleComment = ref({
    comments: [],
    currentPage: 1,
    totalPage: 0,
  });
  const articleList = ref([]);
  const currentPage = ref(1);
  const totalPage = ref(0);

  function getArticleList(param) {
    local
      .get(`/board`, { params: param })
      .then(({ data }) => {
        console.log(data);

        articleList.value = data.articles;
        currentPage.value = data.currentPage;
        totalPage.value = data.totalPageCount;
      })
      .catch((error) => {
        console.log(error);
      });
  }
  function getArticle(no, success, fail) {
    local
      .get(`/board/${no}`)
      .then(({ data }) => {
        console.log(data);
        article.value = data;
        if (success != null) success();
      })
      .catch((error) => {
        console.log(error);
        if (fail != null) fail();
      });
  }
  function modifyArticle(success, fail) {
    local
      .put(`/board/${article.value.no}`, article.value)
      .then(({ data }) => {
        console.log(data);
        if (success != null) success();
      })
      .catch((error) => {
        console.log(error);
        if (fail != null) fail();
      });
  }
  function deleteArticle(no, success, fail) {
    local
      .delete(`/board/${no}`)
      .then((response) => {
        console.log(response);
        if (response.status == 200) {
          if (success != null) success();
        }
      })

      .catch((error) => {
        console.log(error);
        if (fail != null) fail();
      });
  }
  function writeArticle(article, success, fail) {
    local
      .post(`/board`, JSON.stringify(article))
      .then((response) => {
        console.log(response);
        console.log("작성되나요");
        if (response.status == 201) {
          getArticleList({ pgno: 1 });
        }
        if (success != null) success();
      })
      .catch((error) => {
        console.log(error);
        if (fail != null) fail();
      });
  }

  function getArticleComments(no, param) {
    local
      .get(`/board/${no}/comment`, { params: param })
      .then(({ data }) => {
        console.log(data);
        articleComment.value.comments = data.comments;
        articleComment.value.currentPage = data.currentPage;
        articleComment.value.totalPageCount = data.totalPageCount;
      })
      .catch((error) => {
        console.log(error);
      });
  }

  function writeComment(no, comment, success, fail) {
    local
      .post(`/board/${no}/comment`, JSON.stringify(comment))
      .then((response) => {
        console.log(response);
        
        if (response.status == 200) {
          getArticleComments(no, { pgno: 1 });
        }
        if (success != null) success();
      })
      .catch((error) => {
        console.log(error);
        if (fail != null) fail();
      });
  }

  const a = {
    id: 0,
    title: "",
    content: "",
    writer: "",
    writeDate: "",
  };

  const c = {
    id: 0,
    content: "",
    writer: "",
    writeDate: "",
  };
  return {
    article,
    articleComment,
    articleList,
    getArticleList,
    getArticle,
    getArticleComments,
    writeComment,
    modifyArticle,
    deleteArticle,
    writeArticle,
    currentPage,
    totalPage,
  };
});
