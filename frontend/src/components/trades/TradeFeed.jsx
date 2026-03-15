import { useEffect,useState } from "react"
import { getTrades } from "../../api/marketApi"
import { useMarketStore } from "../../store/marketStore"
import { connectSocket, disconnectSocket } from "../../websocket/socket"

export default function TradeFeed(){

 const symbol = useMarketStore(s=>s.symbol)
 const [trades,setTrades] = useState([])

 useEffect(()=>{

  if(!symbol) return

  getTrades(symbol).then(res=>setTrades(res.data))

  disconnectSocket()

  connectSocket(symbol,(trade)=>{

   setTrades(prev=>[trade,...prev.slice(0,40)])

  })

  return ()=>disconnectSocket()

 },[symbol])

 return(

  <div className="text-white h-full flex flex-col">

   <div className="p-2 border-b border-gray-800 text-sm font-semibold">
     TRADES
   </div>

   <div className="flex-1 overflow-auto text-sm">

    {trades.map(t=>(
     <div key={t.tradeId} className="flex justify-between px-2 py-1">

       <span>{t.price.toFixed(2)}</span>
       <span>{t.quantity}</span>

     </div>
    ))}

   </div>

  </div>

 )

}