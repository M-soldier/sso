import { fileURLToPath, URL } from 'node:url';

import { defineConfig, loadEnv } from 'vite';
import vue from '@vitejs/plugin-vue';
import vueDevTools from 'vite-plugin-vue-devtools';

import type { ConfigEnv } from 'vite';

export default defineConfig(({ mode }: ConfigEnv) => {
  const env = loadEnv(mode, process.cwd());

  return {
    define: {
      __APP_ENV__: JSON.stringify(env.VITE_ENV)
    },
    server: {
      host: env.VITE_HOST,
      port: parseInt(env.VITE_PORT) || 5173,
      allowedHosts: env.VITE_APP_ALLOWED_HOSTS?.split(',') || [],
      proxy: {
        '/authServer': {
          target: env.VITE_API_URL,
          changeOrigin: true,
          rewrite: path => path.replace(/^\/authServer/, '')
        }
      }
    },
    plugins: [
      vue(),
      vueDevTools(),
    ],
    resolve: {
      alias: {
        '@': fileURLToPath(new URL('./src', import.meta.url))
      },
    },
  }
})

// https://vite.dev/config/
// export default defineConfig({
//   server: {
//     host: '0.0.0.0',
//     port: 8080,
//   },
//   plugins: [
//     vue(),
//     vueDevTools(),
//   ],
//   resolve: {
//     alias: {
//       '@': fileURLToPath(new URL('./src', import.meta.url))
//     },
//   },
// })
