import { defineConfig, loadEnv } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig(({ mode }) => {
    const env = loadEnv(mode, process.cwd());
    return {
        base: "/",
        plugins: [react()],
        preview: {
            port: 8080,
            strictPort: true,
        },
        server: {
            port: 8080,
            strictPort: true,
            host: true,
            watch: {
                usePolling: true,
            },
            hmr: {
                host: 'localhost',
                protocol: 'ws',
            },
            proxy: {
                '/api': {
                    target: env.VITE_BACKEND_URL,
                    changeOrigin: true,
                    secure: false,      
                    ws: true,
                    configure: (proxy, _options) => {
                        proxy.on('error', (err, _req, _res) => {
                            console.log('proxy error', err);
                        });
                        proxy.on('proxyReq', (proxyReq, req, _res) => {
                            console.log('Sending Request to the Target:', req.method, req.url);
                        });
                        proxy.on('proxyRes', (proxyRes, req, _res) => {
                            console.log('Received Response from the Target:', proxyRes.statusCode, req.url);
                        });
                    },
                }
            }
        },
    };
});