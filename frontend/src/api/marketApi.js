import axiosClient from "./axios"

export const getStocks = () =>
axiosClient.get("/stocks")

export const getOrderBook = (symbol) =>
axiosClient.get(`/orderbook?symbol=${symbol}`)

export const getTrades = (symbol) =>
axiosClient.get(`/trades/${symbol}`)

export const getCandles = (symbol) =>
axiosClient.get(`/market/candles/${symbol}`)