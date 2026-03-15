import axiosClient from "./axios"

export const getPortfolio = () =>
axiosClient.get("/portfolio")