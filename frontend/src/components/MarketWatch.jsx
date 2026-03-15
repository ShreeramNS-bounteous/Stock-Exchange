import { useEffect, useState } from "react"
import { getStocks } from "../api/marketApi"
import { useMarketStore } from "../store/marketStore"

export default function MarketWatch(){

const [stocks,setStocks] = useState([])

const setSymbol = useMarketStore(s=>s.setSymbol)

useEffect(()=>{

getStocks().then(res=>{

setStocks(res.data)

})

},[])

return(

<div className="panel-body">

<h3 style={{padding:"10px"}}>Markets</h3>

{stocks.map(stock=>(

<div
key={stock.symbol}
className="market-row"
onClick={()=>setSymbol(stock.symbol)}
>

<span>{stock.symbol}</span>

<span>₹{stock.price?.toFixed(2)}</span>

</div>

))}

</div>

)

}