<template>
  <a-layout style="min-height:100vh">
    <Sidebar />
    <a-layout>
      <AppHeader />
      <a-layout-content style="margin:16px;overflow:auto">
        <router-view />
      </a-layout-content>
    </a-layout>
  </a-layout>
</template>

<script setup lang="ts">
import Sidebar from './components/Sidebar.vue'
import AppHeader from './components/Header.vue'
</script>
