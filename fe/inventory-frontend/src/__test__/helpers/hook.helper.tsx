import {QueryClient, QueryClientProvider} from '@tanstack/react-query';
import {ReactNode} from 'react';

export const createWrapper = () => {
    const queryClient = new QueryClient({
        defaultOptions: {
            queries: {retry: false},
        },
    });

    const wrapper = function Wrapper({children}: { children: ReactNode }) {
        return <QueryClientProvider client={queryClient}>{children}</QueryClientProvider>;
    };

    return {wrapper, queryClient};
};
