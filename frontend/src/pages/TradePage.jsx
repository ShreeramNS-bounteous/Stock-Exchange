import { useEffect } from "react"
import { connectSocket, disconnectSocket } from "../websocket/socket"
import Navbar from "../components/Navbar"
import TradingLayout from "../layout/TradingLayout"

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