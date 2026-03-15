import { useEffect, useRef } from "react"
import { getCandles } from "../api/marketApi"
import { useMarketStore } from "../store/marketStore"
import { subscribeTopic } from "../websocket/socket"
import {createChart, CandlestickSeries} from "lightweight-charts"

export default function TradingChart(){

 const containerRef = useRef(null)
 const chartRef = useRef(null)
 const candleSeriesRef = useRef(null)

    const symbol = useMarketStore(s => s.symbol)
    
    let currentCandle = null

 useEffect(()=>{

  if(!symbol || !containerRef.current) return

  if(chartRef.current){
   chartRef.current.remove()
  }

  const chart = createChart(containerRef.current,{
   width: containerRef.current.clientWidth,
   height: 400,
   layout:{
    background:{color:"#0b0f1a"},
    textColor:"#9ca3af"
   }
  })

  chartRef.current = chart

  const candleSeries = chart.addSeries(CandlestickSeries)

  candleSeriesRef.current = candleSeries

  getCandles(symbol).then(res=>{
   candleSeries.setData(res.data)
  })

  const subscription = subscribeTopic(
    `/topic/trades.${symbol}`,
    (trade)=>{
   
     const price = trade.price
   
     const bucket = Math.floor(Date.now()/60000)*60
   
     if(!currentCandle || currentCandle.time !== bucket){
   
       currentCandle = {
         time: bucket,
         open: price,
         high: price,
         low: price,
         close: price
       }
   
       candleSeries.update(currentCandle)
   
     } else {
   
       currentCandle.high = Math.max(currentCandle.high, price)
       currentCandle.low = Math.min(currentCandle.low, price)
       currentCandle.close = price
   
       candleSeries.update(currentCandle)
     }
   
    })

  return () => {
   if(subscription) subscription.unsubscribe()
  }

 },[symbol])

 return(
  <div
   ref={containerRef}
   style={{width:"100%",height:"100%"}}
  />
 )

}