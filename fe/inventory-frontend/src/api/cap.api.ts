import {Cap, CapApi, CreateCap, UpdateCap} from "./cap.api.types.ts";
import {ListResponse} from "./api.types.ts";
import {baseApi} from './base.api.ts';
import {AxiosResponse} from "axios";

export const capApi: CapApi = {
    async fetchCaps(): Promise<ListResponse<Cap>> {
        const {data: caps}: AxiosResponse<Cap[]> = await baseApi.get('/caps');
        return {items: caps, total: caps.length};
    },

    async fetchCap(id: string): Promise<Cap> {
        const {data: cap}: AxiosResponse<Cap> = await baseApi.get(`/caps/${id}`);
        return cap;
    },

    async createCap(cap: CreateCap): Promise<Cap> {
        const {data: createdCap}: AxiosResponse<Cap> = await baseApi.post('/caps', cap);
        return createdCap;
    },

    async updateCap(id: string, cap: UpdateCap): Promise<void> {
        await baseApi.put(`/caps/${id}`, cap);
    },

    async deleteCap(id: string): Promise<void> {
        await baseApi.delete(`/caps/${id}`);
    }
}
