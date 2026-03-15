import { useEffect, useRef } from "react"
import { createChart, CandlestickSeries } from "lightweight-charts"
import { getCandles } from "../../api/marketApi"
import { useMarketStore } from "../../store/marketStore"

export default function TradingChart() {

  const ref = useRef(null)
  const chartRef = useRef(null)

  const symbol = useMarketStore((s) => s.symbol)

  useEffect(() => {

    if (!symbol || !ref.current) return

    if (chartRef.current) {
      chartRef.current.remove()
      chartRef.current = null
    }

    const chart = createChart(ref.current,{
        width:ref.current.clientWidth,
        height:380
        })

    chartRef.current = chart

    const candleSeries = chart.addSeries(CandlestickSeries)

    getCandles(symbol).then(res => {
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