import { useState } from "react"
import { useNavigate } from "react-router-dom"
import { useAuthStore } from "../store/authStore"

export default function RegisterPage(){

 const navigate = useNavigate()

 const register = useAuthStore((s)=>s.register)

 const [email,setEmail] = useState("")
 const [password,setPassword] = useState("")
 const [error,setError] = useState("")

 const submit = async (e)=>{

  e.preventDefault()

  const success = await register({
    email,
    password
  })

  if(success){
    navigate("/trade")
  }else{
    setError("Registration failed")
  }

 }

 return(

  <div className="h-screen flex items-center justify-center bg-[#0b0e11]">

   <form
    onSubmit={submit}
    className="bg-[#111827] p-8 w-96 text-white rounded-lg shadow-lg"
   >

    <h2 className="text-xl font-semibold mb-6">
     Register
    </h2>

    {error && (
     <div className="text-red-400 text-sm mb-4">
      {error}
     </div>
    )}

    <input
     type="email"
     placeholder="Email"
     value={email}
     onChange={(e)=>setEmail(e.target.value)}
     className="w-full mb-3 p-2 bg-gray-800 rounded"
    />

    <input
     type="password"
     placeholder="Password"
     value={password}
     onChange={(e)=>setPassword(e.target.value)}
     className="w-full mb-5 p-2 bg-gray-800 rounded"
    />

    <button
     className="w-full bg-green-600 py-2 rounded font-semibold"
    >
     Register
    </button>

    <div className="text-sm mt-4 text-gray-400">

     Already have an account?{" "}
     <span
      onClick={()=>navigate("/")}
      className="cursor-pointer text-blue-400"
     >
      Login
     </span>

    </div>

   </form>

  </div>

 )

}