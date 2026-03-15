import { useNavigate } from "react-router-dom"

 
export default function Navbar(){
 
 const navigate = useNavigate()
 
 const logout = () => {
 
  localStorage.removeItem("token")
 
  navigate("/login")
 
 }
 
 return(
 
  <div className="navbar">
 
   <div className="nav-left">
 
    <h2
     className="logo"
     onClick={()=>navigate("/trade")}
     style={{cursor:"pointer"}}
    >
     StockExchange
    </h2>
 
    <div className="nav-links">
 
     <span onClick={()=>navigate("/markets")}>
      Markets
     </span>
 
     <span onClick={()=>navigate("/trade")}>
      Trade
     </span>
 
     <span onClick={()=>navigate("/portfolio")}>
      Portfolio
     </span>
 
     <span onClick={()=>navigate("/orders")}>
      Orders
     </span>
 
     <span onClick={()=>navigate("/trades")}>
      Trades
     </span>
 
    </div>
 
   </div>
 
   <div className="nav-right">
 
    <span className="balance">
     Balance: ₹100000
    </span>
 
    <button
     className="logout-btn"
     onClick={logout}
    >
     Logout
    </button>
 
   </div>
 
  </div>
 
 )
 
}
 