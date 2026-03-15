import { useEffect,useState } from "react"
import axiosClient from "../../api/axios"

export default function PortfolioPanel(){

 const [portfolio,setPortfolio] = useState([])

 useEffect(()=>{

  axiosClient.get("/portfolio")
   .then(res=>setPortfolio(res.data))

 },[])

 return(

  <div className="text-white h-full border-t border-gray-800">

   <div className="p-3 text-sm font-semibold border-b border-gray-800">
     PORTFOLIO
   </div>

   <div className="p-2 text-sm">

    {portfolio.map(p=>(
      <div key={p.stockSymbol} className="flex justify-between py-1">

        <span>{p.stockSymbol}</span>

        <span>{p.quantity}</span>

      </div>
    ))}

   </div>

  </div>

 )

}