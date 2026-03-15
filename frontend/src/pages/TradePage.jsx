import { useEffect } from "react"
import { connectSocket } from "../websocket/socket"

export default function TradePage(){

useEffect(()=>{
connectSocket()
},[])

return(
<>
<Navbar/>
<TradingLayout/>
</>
)

}