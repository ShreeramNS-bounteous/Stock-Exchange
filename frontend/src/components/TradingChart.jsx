import { useEffect, useRef } from "react"
import { createChart } from "lightweight-charts"
import { getCandles } from "../api/marketApi"
import { useMarketStore } from "../store/marketStore"
import { connectSocket, subscribeSymbol, disconnectSocket } from "../websocket/socket"

export default function TradingChart(){

const containerRef = useRef(null)
const chartRef = useRef(null)
const candleSeriesRef = useRef(null)

const symbol = useMarketStore(s => s.symbol)

useEffect(()=>{

if(!symbol || !containerRef.current) return

// remove previous chart
if(chartRef.current){
chartRef.current.remove()
}

// create chart
const chart = createChart(containerRef.current,{
width: containerRef.current.clientWidth,
height: 400,

layout:{
background:{ color:"#0b0f1a" },
textColor:"#9ca3af"
},

grid:{
vertLines:{ color:"#1f2937" },
horzLines:{ color:"#1f2937" }
},

rightPriceScale:{
scaleMargins:{
top:0.2,
bottom:0.2
}
}

})

chartRef.current = chart

// create candle series
const candleSeries = chart.addCandlestickSeries()
candleSeriesRef.current = candleSeries

// load initial candles
getCandles(symbol)
.then(res=>{
candleSeries.setData(res.data)
})
.catch(err=>{
console.error("Failed to load candles",err)
})

// websocket connection
connectSocket()

subscribeSymbol(symbol,(trade)=>{

const price = trade.price
const time = Math.floor(Date.now()/1000)

candleSeries.update({
time,
close:price
})

})

// resize handling
const handleResize = () => {
chart.applyOptions({
width: containerRef.current.clientWidth
})
}

window.addEventListener("resize",handleResize)

// cleanup
return () => {

disconnectSocket()

window.removeEventListener("resize",handleResize)

if(chartRef.current){
chartRef.current.remove()
}

}

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