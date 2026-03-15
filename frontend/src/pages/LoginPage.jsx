import { useState } from "react"
import { login } from "../api/authApi"
import { useNavigate } from "react-router-dom"
import "../styles/auth.css"

export default function LoginPage(){

 const navigate = useNavigate()

 const [email,setEmail] = useState("")
 const [password,setPassword] = useState("")

 const handleLogin = async () => {

  try{

   const res = await login({
    email,
    password
   })

   localStorage.setItem("token",res.data.token)

   navigate("/trade")

  }catch(e){

   alert("Login failed")

  }

 }

 return(

  <div className="auth-container">

   <div className="auth-card">

    <h2>Login</h2>

    <input
     className="auth-input"
     placeholder="Email"
     value={email}
     onChange={(e)=>setEmail(e.target.value)}
    />

    <input
     className="auth-input"
     type="password"
     placeholder="Password"
     value={password}
     onChange={(e)=>setPassword(e.target.value)}
    />

    <button
     className="auth-btn"
     onClick={handleLogin}
    >
     Login
    </button>

    <div className="auth-footer">

     Don't have an account?{" "}
     <span
      className="auth-link"
      onClick={()=>navigate("/register")}
     >
      Register
     </span>

    </div>

   </div>

  </div>

 )

}