import "../styles/terminal.css"

import MarketWatch from "../components/MarketWatch"
import TradingChart from "../components/TradingChart"
import OrderBook from "../components/OrderBook"
import TradeFeed from "../components/TradeFeed"
import OrderPanel from "../components/OrderPanel"
import PortfolioPanel from "../components/PortfolioPanel"
import OpenOrders from "../components/OpenOrders"

export default function TradingLayout(){

return(

<div className="terminal">

<div className="panel markets">
<MarketWatch/>
</div>

<div className="panel chart">
<TradingChart/>
</div>

<div className="panel orderpanel">
<OrderPanel/>
</div>

<div className="panel orderbook">
<OrderBook/>
</div>

<div className="panel trades">
<TradeFeed/>
</div>

<div className="panel orders">
<OpenOrders/>
</div>

<div className="panel portfolio">
<PortfolioPanel/>
</div>

</div>

)

}