import SockJS from "sockjs-client"
import Stomp from "stompjs"

let stompClient = null

export const connectSocket = () => {

if(stompClient) return

const socket = new SockJS("http://localhost:8080/ws")

stompClient = Stomp.over(socket)

stompClient.connect({}, () => {

console.log("WebSocket connected")

})

}

export const subscribeTopic = (topic,callback) => {

if(!stompClient) return

return stompClient.subscribe(topic,msg => {

callback(JSON.parse(msg.body))

})

}

export const disconnectSocket = () => {

if(stompClient){

stompClient.disconnect()
stompClient = null

}

}