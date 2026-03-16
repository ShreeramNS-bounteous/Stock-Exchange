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

{items.length === 0 ? (

<tr>
<td colSpan="3" style={{
textAlign:"center",
padding:"20px",
color:"#9ca3af"
}}>
No holdings yet. Start trading to build your portfolio.
</td>
</tr>

) : (

items.map((p,i)=>(
<tr key={i}>
<td>{p.symbol}</td>
<td>{p.quantity}</td>
<td>{p.pnl}</td>
</tr>
))

)}

</tbody>

</table>

</div>

</div>

)

}