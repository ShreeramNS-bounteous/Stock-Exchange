import axiosClient from "./axios"

export const login = (data) =>
  axiosClient.post("/auth/login", data)

export const register = (data) =>
  axiosClient.post("/auth/register", data)

export const getUser = () =>
  axiosClient.get("/auth/me")

export const getBalance = () =>
  axiosClient.get("/auth/balance")