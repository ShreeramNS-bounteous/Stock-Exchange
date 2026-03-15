import { useEffect, useState } from "react"
import { getStocks } from "../../api/marketApi"
import { useMarketStore } from "../../store/marketStore"

export default function MarketWatch() {

  const [stocks,setStocks] = useState([])
  const setSymbol = useMarketStore(s=>s.setSymbol)

  useEffect(()=>{

    getStocks().then(res=>{

      setStocks(res.data)

      if(res.data.length>0){
        setSymbol(res.data[0].symbol)
      }

    })

  },[])

  return(

    <div className="h-full overflow-y-auto text-white border-r border-gray-800">

      <div className="p-3 font-semibold text-sm border-b border-gray-800">
        MARKETS
      </div>

      {stocks.map(stock=>(
        <div
          key={stock.symbol}
          onClick={()=>setSymbol(stock.symbol)}
          className="flex justify-between p-3 text-sm hover:bg-gray-800 cursor-pointer"
        >

          <span>{stock.symbol}</span>

          <span className="text-gray-300">
            ₹{stock.price?.toFixed(2)}
          </span>

        </div>
      ))}

    </div>

  )

}