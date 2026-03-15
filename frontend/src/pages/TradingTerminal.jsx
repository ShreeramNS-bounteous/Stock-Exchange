import TradingLayout from "../layout/TradingLayot"

import MarketWatch from "../components/market/MarketWatch"
import TradingChart from "../components/chart/TradingChart"
import OrderBook from "../components/orderbook/OrderBook"
import TradeFeed from "../components/trades/TradeFeed"
import OrderPanel from "../components/trading/OrderPanel"
import PortfolioPanel from "../components/portfolio/PortfolioPanel"

export default function TradingTerminal() {

  return (

    <TradingLayout>

      <div className="row-span-2 bg-gray-900">
        <MarketWatch />
      </div>

      <div className="bg-gray-900">
        <TradingChart />
      </div>

      <div className="grid grid-rows-2 bg-gray-900">
        <OrderBook />
        <TradeFeed />
      </div>

      <div className="col-span-2 bg-gray-900">
        <PortfolioPanel />
      </div>

      <div className="bg-gray-900">
        <OrderPanel />
      </div>

    </TradingLayout>

  )
}