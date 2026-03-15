import { useEffect,useState } from "react"
import { getOrders } from "../../api/orderApi"

export default function OpenOrders(){

const [orders,setOrders] = useState([])

useEffect(()=>{

getOrders().then(res=>setOrders(res.data))

},[])

return(

<div>

<h3>OPEN ORDERS</h3>

<table>

<thead>

<tr>
<th>Symbol</th>
<th>Type</th>
<th>Side</th>
<th>Price</th>
<th>Qty</th>
<th>Status</th>
</tr>

</thead>

<tbody>

{orders.map(o=>(
<tr key={o.id}>
<td>{o.symbol}</td>
<td>{o.orderType}</td>
<td>{o.side}</td>
<td>{o.price}</td>
<td>{o.quantity}</td>
<td>{o.status}</td>
</tr>
))}

</tbody>

</table>

</div>

)

}