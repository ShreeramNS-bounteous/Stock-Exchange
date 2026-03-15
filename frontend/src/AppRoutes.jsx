import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom"

import LoginPage from "./pages/LoginPage"
import RegisterPage from "./pages/RegisterPage"
import TradePage from "./pages/TradePage"
import ProtectedRoute from "./ProtectedRoute"

function App() {

 return (

  <BrowserRouter>

   <Routes>

    {/* default route */}
    <Route path="/" element={<Navigate to="/login" />} />

    <Route path="/login" element={<LoginPage />} />

    <Route path="/register" element={<RegisterPage />} />

    <Route
     path="/trade"
     element={
      <ProtectedRoute>
       <TradePage />
      </ProtectedRoute>
     }
    />

   </Routes>

  </BrowserRouter>

 )

}

export default App