export interface ListResponse<T> {
    total: number;
    items: T[];
}

export interface ProblemDetail {
    title: string;
    status: number;
    detail?: string;
    instance?: string;
    errors?: Record<string, string>;
}

export class ProblemDetailError extends Error {
    constructor(public readonly problemDetail: ProblemDetail) {
        super(problemDetail.detail ?? problemDetail.title);
    }
}
