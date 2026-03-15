export default function Navbar(){

    return(
    
    <div className="navbar">
    
    <div className="nav-left">
    
    <h2 className="logo">StockExchange</h2>
    
    <div className="nav-links">
    
    <span>Markets</span>
    <span>Trade</span>
    <span>Portfolio</span>
    <span>Orders</span>
    <span>Trades</span>
    
    </div>
    
    </div>
    
    <div className="nav-right">
    
    <span className="balance">Balance: ₹100000</span>
    
    <button className="logout-btn">
    Logout
    </button>
    
    </div>
    
    </div>
    
    )
    
    }