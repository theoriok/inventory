import './App.css'
import {HomePage} from "./pages/home.tsx";
import {QueryClient, QueryClientProvider} from "@tanstack/react-query";
import {App} from "antd";
import {FC, useMemo} from "react";

export const InventoryApp: FC = () => {
    const queryClient = useNewQueryClient();

    return (
        <QueryClientProvider client={queryClient}>
            <App>
                <HomePage/>
            </App>
        </QueryClientProvider>
    );
};

function useNewQueryClient(): QueryClient {
    return useMemo(
        () => new QueryClient({defaultOptions: {queries: {staleTime: Infinity}}}),
        [],
    );
}
