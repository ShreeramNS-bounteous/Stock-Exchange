import { useEffect, useState } from "react"
import { getOrderBook } from "../api/marketApi"
import { useMarketStore } from "../store/marketStore"
import { subscribeTopic } from "../websocket/socket"

export default function OrderBook(){

 const symbol = useMarketStore(s=>s.symbol)

 const [book,setBook] = useState({
  buyOrders:[],
  sellOrders:[]
 })

 useEffect(()=>{

  if(!symbol) return

  getOrderBook(symbol).then(res=>{
   setBook(res.data)
  })

  const subscription = subscribeTopic(
   `/topic/orderbook.${symbol}`,
   (bookUpdate)=>{
    setBook(bookUpdate)
   }
  )

  return ()=>{
   subscription?.unsubscribe()
  }

 },[symbol])

 if(book.buyOrders.length === 0 && book.sellOrders.length === 0){

  return(
   <div className="orderbook">
    <div style={{
     padding:"20px",
     textAlign:"center",
     color:"#9ca3af"
    }}>
     No orders in book yet.
    </div>
   </div>
  )

 }

 return(

  <div className="orderbook">

   <table>

    <thead>
     <tr>
      <th className="bid">Bid</th>
      <th>Qty</th>
      <th className="ask">Ask</th>
      <th>Qty</th>
     </tr>
    </thead>

    <tbody>

     {book.buyOrders.map((b,i)=>(
      <tr key={i}>
       <td className="bid">{b.price}</td>
       <td>{b.quantity}</td>
       <td></td>
       <td></td>
      </tr>
     ))}

     {book.sellOrders.map((s,i)=>(
      <tr key={i}>
       <td></td>
       <td></td>
       <td className="ask">{s.price}</td>
       <td>{s.quantity}</td>
      </tr>
     ))}

    </tbody>

   </table>

  </div>

 )

}