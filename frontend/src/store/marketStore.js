import { create } from "zustand"

export const useMarketStore = create((set) => ({

symbol: "TCS",

setSymbol: (symbol) => set({ symbol }),

}))