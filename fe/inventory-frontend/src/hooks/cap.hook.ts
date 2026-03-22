import {useMutation, useQuery, useQueryClient, UseQueryResult} from '@tanstack/react-query';

import {capApi} from '../api/cap.api.ts';
import {Cap, CreateCap, UpdateCap} from '../api/cap.api.types.ts';
import {ListResponse} from '../api/api.types.ts';

export enum CapQueryKeys {
    Caps = 'caps',
    Cap = 'cap',
}

export function useCaps(): UseQueryResult<ListResponse<Cap>> {
    return useQuery({
        queryKey: [CapQueryKeys.Caps],
        queryFn: () => capApi.fetchCaps(),
    });
}

export function useCap(id: string): UseQueryResult<Cap> {
    return useQuery({
        queryKey: [CapQueryKeys.Cap, id],
        queryFn: () => capApi.fetchCap(id),
    });
}

export function useCreateCap() {
    const queryClient = useQueryClient();
    return useMutation({
        mutationFn: (cap: CreateCap) => capApi.createCap(cap),
        onSuccess: () => queryClient.invalidateQueries({queryKey: [CapQueryKeys.Caps]}),
    });
}

export function useUpdateCap() {
    const queryClient = useQueryClient();
    return useMutation({
        mutationFn: ({id, cap}: { id: string; cap: UpdateCap }) => capApi.updateCap(id, cap),
        onSuccess: () => queryClient.invalidateQueries({queryKey: [CapQueryKeys.Caps]}),
    });
}

export function useDeleteCap() {
    const queryClient = useQueryClient();
    return useMutation({
        mutationFn: (id: string) => capApi.deleteCap(id),
        onSuccess: () => queryClient.invalidateQueries({queryKey: [CapQueryKeys.Caps]}),
    });
}
