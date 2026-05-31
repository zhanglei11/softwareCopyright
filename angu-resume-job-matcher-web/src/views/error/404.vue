<template>
  <div class="not-found">
    <div class="not-found-card glass-card">
      <span>404</span>
      <h1>页面不存在或当前账号无访问权限</h1>
      <p>请从左侧菜单重新进入，或返回首页重新加载路由权限。</p>
      <router-link to="/stats/dashboard"><a-button type="primary" size="large">返回首页</a-button></router-link>
    </div>
  </div>
</template>

<style scoped>
.not-found { display: grid; place-items: center; min-height: 100vh; padding: 24px; }
.not-found-card { max-width: 620px; padding: 36px; text-align: center; }
.not-found-card span { display: inline-block; margin-bottom: 10px; color: var(--teal); font-size: 64px; font-weight: 800; }
.not-found-card h1 { margin: 0 0 12px; }
.not-found-card p { margin: 0 0 24px; color: var(--ink-soft); }
</style>