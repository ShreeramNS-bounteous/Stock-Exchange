import { useEffect, useRef } from "react"
import { createChart } from "lightweight-charts"
import { getCandles } from "../../api/marketApi"
import { useMarketStore } from "../../store/marketStore"

export default function TradingChart() {

  const ref = useRef()

  const symbol = useMarketStore((s) => s.symbol)

  useEffect(() => {

    if (!symbol) return

    const chart = createChart(ref.current, {
      width: ref.current.clientWidth,
      height: 400,
      layout: {
        background: { color: "#111827" },
        textColor: "#DDD"
      }
    })

    const candleSeries = chart.addCandlestickSeries()

    getCandles(symbol).then((res) => {
      candleSeries.setData(res.data)
    })

  }, [symbol])

  return <div ref={ref}></div>

}