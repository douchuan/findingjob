import axios from 'axios';
import type { ApiResponse } from '../types';

const api = axios.create({
  baseURL: '/api',
  timeout: 10000,
});

// Request interceptor: attach JWT token
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('jwt_token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Response interceptor: handle 401
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('jwt_token');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  },
);

export default api;

interface RequestOptions {
  headers?: Record<string, string>;
}

export async function get<T>(url: string): Promise<ApiResponse<T>> {
  const { data } = await api.get<ApiResponse<T>>(url);
  return data;
}

export async function post<T>(url: string, payload?: unknown, options?: RequestOptions): Promise<ApiResponse<T>> {
  const { data } = await api.post<ApiResponse<T>>(url, payload, {
    headers: options?.headers,
  });
  return data;
}

export async function put<T>(url: string, payload?: unknown): Promise<ApiResponse<T>> {
  const { data } = await api.put<ApiResponse<T>>(url, payload);
  return data;
}

export async function del<T>(url: string): Promise<ApiResponse<T>> {
  const { data } = await api.delete<ApiResponse<T>>(url);
  return data;
}
