import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import { fileURLToPath, URL } from 'node:url'

// https://vite.dev/config/
export default defineConfig({
  plugins: [vue()],
  server: {
    host: '0.0.0.0',
    port: 29915,
    open: false,
    cors: true,
    proxy: {
      '/api': {
        target: 'http://127.0.0.1:19915',
        changeOrigin: true,
      },
      '/v3': {
        target: 'http://127.0.0.1:19915',
        changeOrigin: true,
      },
    },
  },
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url)),
    },
  },
})
