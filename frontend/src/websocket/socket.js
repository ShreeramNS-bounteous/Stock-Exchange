import SockJS from "sockjs-client"
import Stomp from "stompjs"

let stompClient = null
let connected = false
let waitingSubscriptions = []

export const connectSocket = () => {

 if (connected) return

 const socket = new SockJS("http://localhost:8080/ws")

 stompClient = Stomp.over(socket)

 stompClient.connect({}, () => {

  console.log("WebSocket connected")

  connected = true

  // execute queued subscriptions
  waitingSubscriptions.forEach(sub => sub())
  waitingSubscriptions = []

 })

}

export const subscribeTopic = (topic, callback) => {

 const subscribeAction = () => {

  return stompClient.subscribe(topic, msg => {
   callback(JSON.parse(msg.body))
  })

 }

 if (!connected) {

  waitingSubscriptions.push(subscribeAction)
  return

 }

 return subscribeAction()

}

export const disconnectSocket = () => {

 if (stompClient && connected) {

  stompClient.disconnect()

  connected = false
  stompClient = null

 }

}