import { useEffect,useState } from "react"
import { getStocks } from "../../api/marketApi"
import { useMarketStore } from "../../store/marketStore"

export default function MarketWatch(){

const [stocks,setStocks] = useState([])

const setSymbol = useMarketStore(s=>s.setSymbol)

useEffect(()=>{

getStocks().then(res=>{

setStocks(res.data)

if(res.data.length>0)
setSymbol(res.data[0].symbol)

})

},[])

return(

<div>

<h3 style={{padding:"10px"}}>MARKETS</h3>

{stocks.map(s=>(
<div
key={s.symbol}
className="market-row"
onClick={()=>setSymbol(s.symbol)}
>

<span>{s.symbol}</span>
<span>₹{s.price?.toFixed(2)}</span>

</div>
))}

</div>

)

}