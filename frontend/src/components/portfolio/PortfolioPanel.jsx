import { useEffect,useState } from "react"
import { getPortfolio } from "../../api/portfolioApi"

export default function PortfolioPanel(){

const [items,setItems] = useState([])

useEffect(()=>{

getPortfolio().then(res=>setItems(res.data))

},[])

return(

<div>

<h3>PORTFOLIO</h3>

<table>

<tbody>

{items.map(p=>(
<tr key={p.symbol}>
<td>{p.symbol}</td>
<td>{p.quantity}</td>
<td>{p.pnl}</td>
</tr>
))}

</tbody>

</table>

</div>

)

}