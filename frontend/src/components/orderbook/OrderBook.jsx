import { useEffect,useState } from "react"
import { getOrderBook } from "../../api/marketApi"
import { useMarketStore } from "../../store/marketStore"
import { connectSocket } from "../../websocket/socket"

export default function OrderBook(){

 const symbol = useMarketStore(s=>s.symbol)

 const [book,setBook] = useState({buyOrders:[],sellOrders:[]})

 useEffect(()=>{

  if(!symbol) return

  getOrderBook(symbol).then(res=>setBook(res.data))

  connectSocket(symbol,()=>{},(data)=>setBook(data))

 },[symbol])

 return(

  <div className="text-white h-full flex flex-col">

   <div className="p-2 border-b border-gray-800 text-sm font-semibold">
     ORDER BOOK
   </div>

   <div className="flex-1 overflow-auto text-sm">

     {book.sellOrders.slice(0,10).map((o,i)=>(
      <div key={i} className="flex justify-between text-red-400 px-2 py-1">
       <span>{o.price.toFixed(2)}</span>
       <span>{o.quantity}</span>
      </div>
     ))}

     {book.buyOrders.slice(0,10).map((o,i)=>(
      <div key={i} className="flex justify-between text-green-400 px-2 py-1">
       <span>{o.price.toFixed(2)}</span>
       <span>{o.quantity}</span>
      </div>
     ))}

   </div>

  </div>

 )

}