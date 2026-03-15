import MarketWatch from "../components/market/MarketWatch"
import TradingChart from "../components/chart/TradingChart"
import OrderPanel from "../components/trading/OrderPanel"
import OrderBook from "../components/orderbook/OrderBook"
import TradeFeed from "../components/trades/TradeFeed"
import PortfolioPanel from "../components/portfolio/PortfolioPanel"
import OpenOrders from "../components/trading/OpenOrders"

import "./terminal.css"

export default function TradingTerminal(){

return(

<div className="terminal">

<div className="markets">
<MarketWatch/>
</div>

<div className="chart">
<TradingChart/>
</div>

<div className="orderpanel">
<OrderPanel/>
</div>

<div className="orderbook">
<OrderBook/>
</div>

<div className="trades">
<TradeFeed/>
</div>

<div className="portfolio">
<PortfolioPanel/>
</div>

<div className="orders">
<OpenOrders/>
</div>

</div>

)

}