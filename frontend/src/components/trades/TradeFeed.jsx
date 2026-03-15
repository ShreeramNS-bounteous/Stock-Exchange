import { useEffect,useState } from "react"
import { getTrades } from "../../api/marketApi"
import { useMarketStore } from "../../store/marketStore"

export default function TradeFeed(){

const symbol = useMarketStore(s=>s.symbol)

const [trades,setTrades] = useState([])

useEffect(()=>{

    if(!symbol) return
    
    getTrades(symbol).then(res=>setTrades(res.data))
    
    disconnectSocket()
    
    connectSocket(symbol,(trade)=>{
    
    setTrades(prev=>[trade,...prev.slice(0,40)])
    
    })
    
    return ()=>disconnectSocket()
    
    },[symbol])

return(

<div>

<h3>TRADES</h3>

<table>

<tbody>

{trades.map(t=>(
<tr key={t.tradeId}>
<td>{t.price}</td>
<td>{t.quantity}</td>
</tr>
))}

</tbody>

</table>

</div>

)

}