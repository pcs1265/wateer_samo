<script setup>
import { useRouter } from 'vue-router';

const props = defineProps({
  article: Object,
});

const router = useRouter();

const viewArticle = ()=>{
  router.push({ name: 'article-view', params: { no: props.article.no } });
}
</script>

<template>
  <tr @click="viewArticle" role='button'>
    <td class="text-center">{{ article.no }}</td>
    <td>
        {{ article.title }}
    </td>
    <td class="text-center">
        {{ article.nickname }}
    </td>
    <td class="text-center">{{ article.hits }}</td>
    <td class="text-center">{{ article.writeDate }}</td>
  </tr>
</template>

<style scoped></style>
