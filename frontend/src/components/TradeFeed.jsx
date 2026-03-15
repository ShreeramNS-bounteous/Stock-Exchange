import { useEffect, useState } from "react"
import { getTrades } from "../api/marketApi"
import { useMarketStore } from "../store/marketStore"
import { subscribeTopic } from "../websocket/socket"

export default function TradeFeed(){

const symbol = useMarketStore(s=>s.symbol)

const [trades,setTrades] = useState([])

useEffect(()=>{

if(!symbol) return

getTrades(symbol).then(res=>{
setTrades(res.data)
})

const sub = subscribeTopic(`/topic/trades/${symbol}`,trade=>{

setTrades(prev=>[trade,...prev.slice(0,40)])

})

return ()=>sub?.unsubscribe()

},[symbol])

return(

<div style={{display:"flex",flexDirection:"column",height:"100%"}}>

<h3 style={{padding:"10px"}}>Trades</h3>

<div className="panel-body">

<table className="table">

<tbody>

{trades.map((t,i)=>(
<tr key={i}>
<td>{t.price}</td>
<td>{t.quantity}</td>
<td>{new Date(t.timestamp).toLocaleTimeString()}</td>
</tr>
))}

</tbody>

</table>

</div>

</div>

)

}