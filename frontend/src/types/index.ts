export type UserRole = 'JOBSEEKER' | 'HR' | 'ADMIN';

export interface User {
  id: number;
  phone: string | null;
  name: string | null;
  avatar: string | null;
  role: UserRole;
  status: string;
  createdAt: string;
}

export interface AuthState {
  token: string | null;
  user: User | null;
  setToken: (token: string) => void;
  setUser: (user: User) => void;
  logout: () => void;
}

export interface ApiResponse<T> {
  code: number;
  message: string;
  data: T;
}
