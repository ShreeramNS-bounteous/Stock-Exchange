import { create } from "zustand"
import { login as loginApi, register as registerApi } from "../api/authApi"

export const useAuthStore = create((set) => ({

  token: localStorage.getItem("token") || null,
  email: localStorage.getItem("email") || null,
  role: localStorage.getItem("role") || null,
  loading: false,

  login: async (credentials) => {

    set({ loading: true })

    try {

      const res = await loginApi(credentials)

      const { token, email, role } = res.data

      localStorage.setItem("token", token)
      localStorage.setItem("email", email)
      localStorage.setItem("role", role)

      set({
        token,
        email,
        role,
        loading: false
      })

      return true

    } catch (err) {

      set({ loading: false })
      return false

    }

  },

  register: async (data) => {

    set({ loading: true })

    try {

      const res = await registerApi(data)

      const { token, email, role } = res.data

      localStorage.setItem("token", token)
      localStorage.setItem("email", email)
      localStorage.setItem("role", role)

      set({
        token,
        email,
        role,
        loading: false
      })

      return true

    } catch (err) {

      set({ loading: false })
      return false

    }

  },

  logout: () => {

    localStorage.removeItem("token")
    localStorage.removeItem("email")
    localStorage.removeItem("role")

    set({
      token: null,
      email: null,
      role: null
    })

  }

}))