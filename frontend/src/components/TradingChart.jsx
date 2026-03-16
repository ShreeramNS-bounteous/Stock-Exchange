import { useEffect, useRef } from "react"
import { createChart, CandlestickSeries } from "lightweight-charts"
import { getCandles } from "../api/marketApi"
import { useMarketStore } from "../store/marketStore"
import { connectSocket, subscribeTopic } from "../websocket/socket"

export default function TradingChart(){

const chartRef = useRef(null)
const containerRef = useRef(null)
const candleSeriesRef = useRef(null)

const symbol = useMarketStore(s=>s.symbol)

useEffect(()=>{

if(!symbol || !containerRef.current) return

if(chartRef.current){
 chartRef.current.remove()
}

const chart = createChart(containerRef.current,{
 width: containerRef.current.clientWidth,
 height: containerRef.current.clientHeight,

 layout:{
  background:{color:"#0b0f1a"},
  textColor:"#9ca3af"
 },

 grid:{
  vertLines:{color:"#1f2937"},
  horzLines:{color:"#1f2937"}
 }
})

chartRef.current = chart

const candleSeries = chart.addSeries(CandlestickSeries)

candleSeriesRef.current = candleSeries

// load initial candles
getCandles(symbol).then(res=>{
 candleSeries.setData(res.data)
})

// websocket connection
connectSocket()

subscribeTopic(`/topic/trades/${symbol}`, trade => {

 const price = trade.price
 const time = Math.floor(Date.now() / 1000)

 candleSeries.update({
  time,
  open: price,
  high: price,
  low: price,
  close: price
 })

})

},[symbol])

return(

<div
ref={containerRef}
style={{
 width:"100%",
 height:"100%"
}}
/>

)

}