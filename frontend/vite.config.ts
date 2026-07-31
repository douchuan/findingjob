import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';
import { VitePWA } from 'vite-plugin-pwa';

export default defineConfig({
  plugins: [
    react(),
    VitePWA({
      registerType: 'autoUpdate',
      manifest: {
        name: 'FindingJob — 技能求职平台',
        short_name: 'FindingJob',
        description: '以技能为核心的求职平台',
        theme_color: '#1677ff',
        icons: [
          { src: '/pwa-192x192.png', sizes: '192x192', type: 'image/png' },
          { src: '/pwa-512x512.png', sizes: '512x512', type: 'image/png' },
        ],
      },
    }),
  ],
  server: {
    port: 5173,
    proxy: {
      '/api/auth': {
        target: 'http://localhost:8001',
        changeOrigin: true,
      },
      '/api/profile': {
        target: 'http://localhost:8002',
        changeOrigin: true,
      },
      '/api/company': {
        target: 'http://localhost:8003',
        changeOrigin: true,
      },
      '/api/rating': {
        target: 'http://localhost:8004',
        changeOrigin: true,
      },
      '/api/resume': {
        target: 'http://localhost:8005',
        changeOrigin: true,
      },
      '/api/storage': {
        target: 'http://localhost:8006',
        changeOrigin: true,
      },
      '/api/notification': {
        target: 'http://localhost:8007',
        changeOrigin: true,
      },
    },
  },
});
