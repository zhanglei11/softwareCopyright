<template>
  <a-layout style="min-height:100vh">
    <Sider />
    <a-layout>
      <Header />
      <a-layout-content style="margin:16px;overflow:auto">
        <router-view />
      </a-layout-content>
    </a-layout>
  </a-layout>
</template>

<script setup lang="ts">
import Sider from './Sidebar/Sider.vue'
import Header from './Header/Header.vue'
</script>
