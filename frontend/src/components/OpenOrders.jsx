import { useEffect, useState } from "react"
import { getOrders, cancelOrder } from "../api/orderApi"

export default function OpenOrders(){

const [orders,setOrders] = useState([])

const loadOrders = () => {

getOrders().then(res=>{
setOrders(res.data)
})

}

useEffect(()=>{
loadOrders()
},[])

const cancel = async (id) => {

await cancelOrder(id)

loadOrders()

}

return(

<div style={{display:"flex",flexDirection:"column",height:"100%"}}>

<h3 style={{padding:"10px"}}>Open Orders</h3>

<div className="panel-body">

<table className="table">

<thead>
<tr>
<th>Symbol</th>
<th>Side</th>
<th>Price</th>
<th>Qty</th>
<th>Status</th>
<th></th>
</tr>
</thead>

<tbody>

{orders.length === 0 ? (

<tr>
<td colSpan="6" style={{
textAlign:"center",
padding:"20px",
color:"#9ca3af"
}}>
No open orders yet. Place a trade to see orders here.
</td>
</tr>

) : (

orders.map(o=>(
<tr key={o.id}>
<td>{o.symbol}</td>
<td>{o.side}</td>
<td>{o.price}</td>
<td>{o.quantity}</td>
<td>{o.status}</td>
<td>
<button
onClick={()=>cancel(o.id)}
style={{
background:"#ef4444",
border:"none",
color:"white",
padding:"4px 8px",
borderRadius:"4px"
}}
>
Cancel
</button>
</td>
</tr>
))

)}

</tbody>

</table>

</div>

</div>

)

}