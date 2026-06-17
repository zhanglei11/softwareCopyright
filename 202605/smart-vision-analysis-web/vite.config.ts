import { defineConfig } from 'vite'
import type { UserConfig, ConfigEnv } from 'vite'
import vue from '@vitejs/plugin-vue'
import { fileURLToPath, URL } from 'node:url'

export default defineConfig(({ mode }: ConfigEnv): UserConfig => ({
  base: './',
  plugins: [vue()],
  server: {
    port: 29911,
    open: true,
    cors: true,
    proxy: {
      '/api': { target: 'http://127.0.0.1:19911', changeOrigin: true },
      '/uploads': { target: 'http://127.0.0.1:19911', changeOrigin: true },
    },
  },
  build: { outDir: mode === 'production' ? 'dist-prod' : 'dist-dev' },
  resolve: { alias: { '@': fileURLToPath(new URL('./src', import.meta.url)) } },
}))
