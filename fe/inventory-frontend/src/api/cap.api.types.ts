import {ListResponse} from './api.types.ts';

export interface CapApi {
    fetchCaps(): Promise<ListResponse<Cap>>;

    fetchCap(id: string): Promise<Cap>;

    createCap(cap: CreateCap): Promise<Cap>;

    updateCap(id: string, cap: UpdateCap): Promise<void>;

    deleteCap(id: string): Promise<void>;
}

export interface CreateCap {
    name: string;
    description: string;
    amount: number;
    country: string;
}

export interface UpdateCap {
    name: string;
    description: string;
    amount: number;
    country: string;
}

export interface Cap {
    id: string;
    name: string;
    description: string;
    amount: number;
    country: Country;
}

export interface Country {
    name: string;
    code: string;
}
