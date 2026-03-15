import { useEffect, useState } from "react"
import { getPortfolio } from "../api/portfolioApi"

export default function PortfolioPanel(){

const [items,setItems] = useState([])

useEffect(()=>{

getPortfolio().then(res=>{
setItems(res.data)
})

},[])

return(

<div style={{display:"flex",flexDirection:"column",height:"100%"}}>

<h3 style={{padding:"10px"}}>Portfolio</h3>

<div className="panel-body">

<table className="table">

<thead>
<tr>
<th>Symbol</th>
<th>Qty</th>
<th>PnL</th>
</tr>
</thead>

<tbody>

{items.map((p,i)=>(
<tr key={i}>
<td>{p.symbol}</td>
<td>{p.quantity}</td>
<td>{p.pnl}</td>
</tr>
))}

</tbody>

</table>

</div>

</div>

)

}