import { defineConfig, loadEnv } from "vite";
import react from "@vitejs/plugin-react";

export default defineConfig(({ mode }) => {
    // eslint-disable-next-line no-undef
    const env = loadEnv(mode, process.cwd() + "/..");
    return {
        base: "/",
        plugins: [react()],
        preview: {
            port: env.FRONTEND_PORT,
            strictPort: true,
        },
        server: {
            port: env.FRONTEND_PORT,
            strictPort: true,
            host: true,
            watch: {
                usePolling: true,
            },
            hmr: {
                host: "localhost",
                protocol: "ws",
            },
            proxy: {
                "/api": {
                    target: env.VITE_FRONTEND_PROXY,
                    changeOrigin: true,
                    secure: false,
                    ws: true,
                    configure: (proxy) => {
                        proxy.on("error", (err) => {
                            console.log("proxy error", err);
                        });
                        proxy.on("proxyReq", (_, req) => {
                            console.log(
                                "Sending Request to the Target:",
                                req.method,
                                req.url,
                            );
                        });
                        proxy.on("proxyRes", (proxyRes, req) => {
                            console.log(
                                "Received Response from the Target:",
                                proxyRes.statusCode,
                                req.url,
                            );
                        });
                    },
                },
            },
        },
    };
});
