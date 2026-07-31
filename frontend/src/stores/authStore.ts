import { create } from 'zustand';
import type { AuthState, User } from '../types';

export const useAuthStore = create<AuthState>((set) => ({
  token: localStorage.getItem('jwt_token'),
  user: null,

  setToken: (token: string) => {
    localStorage.setItem('jwt_token', token);
    set({ token });
  },

  setUser: (user: User) => set({ user }),

  logout: () => {
    localStorage.removeItem('jwt_token');
    set({ token: null, user: null });
  },
}));
