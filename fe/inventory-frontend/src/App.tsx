import './App.css'
import {HomePage} from "./pages/home.tsx";
import {QueryClient, QueryClientProvider} from "@tanstack/react-query";
import {FC, useMemo} from "react";

export const App: FC = () => {
    const queryClient = useNewQueryClient();


    return (
        <>
            <QueryClientProvider client={queryClient}><HomePage/></QueryClientProvider>
        </>
    )
}

function useNewQueryClient(): QueryClient {
    return useMemo(
        () => new QueryClient({defaultOptions: {queries: {staleTime: Infinity}}}),
        [],
    );
}

export default App;
