import { useState } from "react"
import { register } from "../api/authApi"
import { useNavigate } from "react-router-dom"
import "../styles/auth.css"

export default function RegisterPage(){

 const navigate = useNavigate()

 const [name,setName] = useState("")
 const [email,setEmail] = useState("")
 const [password,setPassword] = useState("")

 const handleRegister = async () => {

  try{

   await register({
    email,
    password
   })

   alert("Registration successful")

   navigate("/trade")

  }catch(e){

   alert("Registration failed")

  }

 }

 return(

  <div className="auth-container">

   <div className="auth-card">

    <h2>Register</h2>
    <input
     className="auth-input"
     placeholder="Email"
     value={email}
     onChange={e=>setEmail(e.target.value)}
    />

    <input
     className="auth-input"
     type="password"
     placeholder="Password"
     value={password}
     onChange={e=>setPassword(e.target.value)}
    />

    <button
     className="auth-btn"
     onClick={handleRegister}
    >
     Register
    </button>

    <p className="auth-footer">

     Already have an account?{" "}

     <span
      className="auth-link"
      onClick={()=>navigate("/login")}
     >
      Login
     </span>

    </p>

   </div>

  </div>

 )

}