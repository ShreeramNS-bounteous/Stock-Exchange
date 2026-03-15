import SockJS from "sockjs-client"
import { Client } from "@stomp/stompjs"

let client = null

export const connectSocket = (symbol, onTrade, onOrderBook) => {

  const socket = new SockJS("http://localhost:8080/ws")

  client = new Client({

    webSocketFactory: () => socket,

    reconnectDelay: 5000,

    onConnect: () => {

      client.subscribe(`/topic/trades/${symbol}`, (msg) => {
        onTrade(JSON.parse(msg.body))
      })

      client.subscribe(`/topic/orderbook/${symbol}`, (msg) => {
        onOrderBook(JSON.parse(msg.body))
      })

    }

  })

  client.activate()

}

export const disconnectSocket = () => {
  if (client) client.deactivate()
}