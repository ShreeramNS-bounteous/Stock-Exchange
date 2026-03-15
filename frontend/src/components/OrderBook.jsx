import { useEffect, useState } from "react"
import { getOrderBook } from "../api/marketApi"
import { useMarketStore } from "../store/marketStore"
import { connectSocket,subscribeSymbol,disconnectSocket } from "../websocket/socket"

export default function OrderBook(){

const symbol = useMarketStore(s=>s.symbol)

const [book,setBook] = useState({
buyOrders:[],
sellOrders:[]
})

useEffect(()=>{

if(!symbol) return

getOrderBook(symbol).then(res=>setBook(res.data))

connectSocket()

subscribeSymbol(symbol,null,(bookUpdate)=>{

setBook(bookUpdate)

})

return ()=>disconnectSocket()

},[symbol])

return(

<div style={{display:"flex",flexDirection:"column",height:"100%"}}>

<h3 style={{padding:"10px"}}>OrderBook</h3>

<div className="panel-body">

<table className="table">

<tbody>

{book.sellOrders.map((o,i)=>(
<tr key={"s"+i} className="ask">
<td>{o.price}</td>
<td>{o.quantity}</td>
</tr>
))}

{book.buyOrders.map((o,i)=>(
<tr key={"b"+i} className="bid">
<td>{o.price}</td>
<td>{o.quantity}</td>
</tr>
))}

</tbody>

</table>

</div>

</div>

)

}