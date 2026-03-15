import SockJS from "sockjs-client"
import { Client } from "@stomp/stompjs"

let client = null

export const connectSocket = (symbol, onTrade, onOrderBook) => {

  // disconnect previous socket
  if (client) {
    client.deactivate()
    client = null
  }

  const socket = new SockJS("http://localhost:8080/ws")

  client = new Client({
    webSocketFactory: () => socket,
    reconnectDelay: 5000,

    onConnect: () => {

      console.log("WebSocket connected")

      if (onTrade) {
        client.subscribe(`/topic/trades/${symbol}`, (msg) => {
          onTrade(JSON.parse(msg.body))
        })
      }

      if (onOrderBook) {
        client.subscribe(`/topic/orderbook/${symbol}`, (msg) => {
          onOrderBook(JSON.parse(msg.body))
        })
      }

    },

    onStompError: (frame) => {
      console.error("WebSocket error", frame)
    }

  })

  client.activate()
}

export const disconnectSocket = () => {

  if (client) {
    client.deactivate()
    client = null
    console.log("WebSocket disconnected")
  }

}