import { useEffect,useState } from "react"
import { getOrderBook } from "../../api/marketApi"
import { useMarketStore } from "../../store/marketStore"

export default function OrderBook(){

const symbol = useMarketStore(s=>s.symbol)

const [book,setBook] = useState({buyOrders:[],sellOrders:[]})

useEffect(()=>{

if(!symbol) return

getOrderBook(symbol).then(res=>setBook(res.data))

},[symbol])

return(

<div>

<h3>ORDER BOOK</h3>

<table>

<tbody>

{book.sellOrders.map((o,i)=>(
<tr key={i} className="ask">
<td>{o.price}</td>
<td>{o.quantity}</td>
</tr>
))}

{book.buyOrders.map((o,i)=>(
<tr key={i} className="bid">
<td>{o.price}</td>
<td>{o.quantity}</td>
</tr>
))}

</tbody>

</table>

</div>

)

}