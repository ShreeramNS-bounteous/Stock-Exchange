import { useState } from "react"
import { placeOrder } from "../api/orderApi"
import { useMarketStore } from "../store/marketStore"

export default function OrderPanel(){

const symbol = useMarketStore(s => s.symbol)

const [side,setSide] = useState("BUY")
const [orderType,setOrderType] = useState("LIMIT")
const [price,setPrice] = useState("")
const [quantity,setQuantity] = useState("")

const submitOrder = async () => {

if(!quantity) return

const order = {
symbol,
side,
orderType,
price: orderType === "MARKET" ? null : Number(price),
quantity:Number(quantity)
}

try{

await placeOrder(order)

alert("Order placed")

setPrice("")
setQuantity("")

}catch(e){

alert("Order failed")

}

}

return(

<div style={{display:"flex",flexDirection:"column",height:"100%"}}>

<h3 style={{padding:"10px"}}>Trade</h3>

<div className="panel-body" style={{padding:"10px",display:"flex",flexDirection:"column",gap:"10px"}}>

<div>Symbol: <b>{symbol}</b></div>

{/* BUY SELL */}

<div style={{display:"flex",gap:"10px"}}>

<button
className="buy-btn"
onClick={()=>setSide("BUY")}
>
BUY
</button>

<button
className="sell-btn"
onClick={()=>setSide("SELL")}
>
SELL
</button>

</div>

{/* ORDER TYPE */}

<select
value={orderType}
onChange={e=>setOrderType(e.target.value)}
>

<option value="LIMIT">LIMIT</option>
<option value="MARKET">MARKET</option>

</select>

{/* PRICE */}

{orderType==="LIMIT" && (

<input
type="number"
placeholder="Price"
value={price}
onChange={e=>setPrice(e.target.value)}
/>

)}

{/* QUANTITY */}

<input
type="number"
placeholder="Quantity"
value={quantity}
onChange={e=>setQuantity(e.target.value)}
/>

<button
onClick={submitOrder}
style={{
background: side==="BUY" ? "#10b981" : "#ef4444",
color:"white",
padding:"10px",
border:"none",
borderRadius:"6px"
}}
>

Place {side}

</button>

</div>

</div>

)

}