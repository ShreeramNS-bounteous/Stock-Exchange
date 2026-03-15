import { BrowserRouter, Routes, Route } from "react-router-dom"

import LoginPage from "../pages/LoginPage"
import RegisterPage from "../pages/Register"
import TradingTerminal from "../pages/TradingTerminal"
import ProtectedRoute from "./ProtectedRoute"

export default function AppRoutes(){

 return(

  <BrowserRouter>

   <Routes>

    <Route path="/" element={<LoginPage/>} />

    <Route path="/register" element={<RegisterPage/>} />

    <Route
      path="/trade"
      element={
        <ProtectedRoute>
          <TradingTerminal/>
        </ProtectedRoute>
      }
    />

   </Routes>

  </BrowserRouter>

 )

}