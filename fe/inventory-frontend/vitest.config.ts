import react from '@vitejs/plugin-react';
import { defineConfig } from 'vitest/config';

export default defineConfig({
    plugins: [react()],
    test: {
        environment: 'jsdom',
        setupFiles: './src/setup-test.ts',
        coverage: {
            provider: 'v8',
            reporter: ['text', 'html', 'lcov'],
            exclude: ['node_modules/', 'src/setup-test.ts']
        },
        reporters: ['default', 'junit'],
        outputFile: 'coverage/test-reporter.xml'
    },
});
