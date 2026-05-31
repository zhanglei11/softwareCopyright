import { defineConfig } from 'vite'
import type { UserConfig, ConfigEnv } from 'vite'
import vue from '@vitejs/plugin-vue'
import { fileURLToPath, URL } from 'node:url'

export default defineConfig(({ mode }: ConfigEnv): UserConfig => {
  return {
    base: './',
    plugins: [vue()],
    server: {
      port: 29913,
      open: true,
      cors: true,
      proxy: {
        '/api': {
          target: 'http://127.0.0.1:19913',
          changeOrigin: true,
        },
      },
    },
    build: {
      outDir: mode === 'production' ? 'dist-prod' : 'dist-dev',
    },
    resolve: {
      alias: {
        '@': fileURLToPath(new URL('./src', import.meta.url)),
      },
    },
    css: {
      preprocessorOptions: {
        scss: { api: 'modern-compiler' },
      },
    },
  }
})
