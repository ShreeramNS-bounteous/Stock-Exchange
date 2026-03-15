import { create } from "zustand"

export const useMarketStore = create((set) => ({

  symbol: null,

  setSymbol: (symbol) => set({ symbol })

}))