import axiosClient from "./axios"

export const placeOrder = (order) =>
axiosClient.post("/orders", order)

export const cancelOrder = (id) =>
axiosClient.delete(`/orders/${id}`)

export const getOrders = () =>
axiosClient.get("/orders/my")

export const getOrderHistory = () =>
axiosClient.get("/orders/history")