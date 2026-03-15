import { useState } from "react"
import { placeOrder } from "../../api/orderApi"
import { useMarketStore } from "../../store/marketStore"

export default function OrderPanel(){

 const symbol = useMarketStore(s=>s.symbol)

 const [price,setPrice] = useState("")
 const [qty,setQty] = useState("")

 const submit=(type)=>{

  placeOrder({
   stockSymbol:symbol,
   type,
   orderMode:"LIMIT",
   quantity:Number(qty),
   price:Number(price)
  })

 }

 return(

  <div className="bg-[#0b0e11] border-t border-gray-800 flex items-center gap-4 px-6">

   <div className="text-white text-sm">
    {symbol}
   </div>

   <input
    placeholder="Price"
    className="bg-gray-800 text-white px-3 py-2 text-sm"
    value={price}
    onChange={e=>setPrice(e.target.value)}
   />

   <input
    placeholder="Qty"
    className="bg-gray-800 text-white px-3 py-2 text-sm"
    value={qty}
    onChange={e=>setQty(e.target.value)}
   />

   <button
    onClick={()=>submit("BUY")}
    className="bg-green-600 px-5 py-2 text-sm font-semibold"
   >
    BUY
   </button>

   <button
    onClick={()=>submit("SELL")}
    className="bg-red-600 px-5 py-2 text-sm font-semibold"
   >
    SELL
   </button>

  </div>

 )

}