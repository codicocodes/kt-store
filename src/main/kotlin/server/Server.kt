package server

import com.sun.net.httpserver.HttpHandler
import com.sun.net.httpserver.HttpServer
import java.net.InetSocketAddress

class ServerOptions {
    val port get(): Int {
        val rawPort: String = System.getenv("port") ?: "8000"
        return rawPort.toInt()
    }
}

class Server (private val options: ServerOptions, private val handler: HttpHandler) {
    fun start() {
        val server = HttpServer.create(InetSocketAddress(this.options.port), 0)
        server.createContext("/", this.handler)
        server.executor = null
        server.start()
    }
}

