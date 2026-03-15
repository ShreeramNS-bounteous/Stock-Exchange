export default function TradingLayout({ children }) {

    return (
  
      <div className="h-screen w-screen grid grid-cols-[260px_1fr_360px] grid-rows-[1fr_90px_220px] bg-[#0b0e11]">
  
        {children}
  
      </div>
  
    )
  
  }