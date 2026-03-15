import { useEffect, useState } from "react"
import { subscribeTopic } from "../websocket/socket"
import { useMarketStore } from "../store/marketStore"

export default function TradesFeed(){

 const [trades,setTrades] = useState([])

 const symbol = useMarketStore(s=>s.symbol)

 useEffect(()=>{

  if(!symbol) return

  const subscription = subscribeTopic(
   `/topic/trades.${symbol}`,
   (trade)=>{

    setTrades(prev => [trade,...prev.slice(0,20)])

   }
  )

  return ()=>subscription?.unsubscribe()

 },[symbol])

 return(
  <div className="trades">

   <table>

    <thead>
     <tr>
      <th>Price</th>
      <th>Qty</th>
      <th>Time</th>
     </tr>
    </thead>

    <tbody>

     {trades.map((t,i)=>(
      <tr key={i}>
       <td>{t.price}</td>
       <td>{t.quantity}</td>
       <td>{t.time}</td>
      </tr>
     ))}

    </tbody>

   </table>

  </div>
 )

}