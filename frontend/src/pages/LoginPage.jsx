import { useState } from "react"
import { useNavigate } from "react-router-dom"
import { useAuthStore } from "../store/authStore"

export default function LoginPage(){

 const navigate = useNavigate()

 const login = useAuthStore((s)=>s.login)

 const [email,setEmail] = useState("")
 const [password,setPassword] = useState("")
 const [error,setError] = useState("")

 const submit = async (e)=>{

  e.preventDefault()

  const success = await login({
    email,
    password
  })

  if(success){
    navigate("/trade")
  }else{
    setError("Invalid credentials")
  }

 }

 return(

  <div className="h-screen flex items-center justify-center bg-[#0b0e11]">

   <form
    onSubmit={submit}
    className="bg-[#111827] p-8 w-96 text-white rounded-lg shadow-lg"
   >

    <h2 className="text-xl font-semibold mb-6">
     Login
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
     className="w-full bg-blue-600 py-2 rounded font-semibold"
    >
     Login
    </button>

    <div className="text-sm mt-4 text-gray-400">

     No account?{" "}
     <span
      onClick={()=>navigate("/register")}
      className="cursor-pointer text-blue-400"
     >
      Register
     </span>

    </div>

   </form>

  </div>

 )

}