import { useEffect, useRef } from "react"
import { createChart } from "lightweight-charts"
import { getCandles } from "../../api/marketApi"
import { useMarketStore } from "../../store/marketStore"

export default function TradingChart() {

  const ref = useRef(null)
  const chartRef = useRef(null)

  const symbol = useMarketStore((s) => s.symbol)

  useEffect(() => {

    if (!symbol || !ref.current) return

    // destroy old chart
    if (chartRef.current) {
      chartRef.current.remove()
      chartRef.current = null
    }

    const chart = createChart(ref.current, {
      width: ref.current.clientWidth,
      height: 400,
      layout: {
        background: { color: "#111827" },
        textColor: "#DDD"
      },
      grid: {
        vertLines: { color: "#1f2937" },
        horzLines: { color: "#1f2937" }
      }
    })

    chartRef.current = chart

    const candleSeries = chart.addCandlestickSeries()

    getCandles(symbol).then((res) => {
      candleSeries.setData(res.data)
    })

    return () => {
      if (chartRef.current) {
        chartRef.current.remove()
        chartRef.current = null
      }
    }

  }, [symbol])

  return <div ref={ref}></div>

}