package server

import com.sun.net.httpserver.HttpExchange

interface Response {
    fun send(exchange: HttpExchange)
}

abstract class TextResponse (private val message: String) : Response {
    abstract val code: Int
    override fun send(exchange: HttpExchange) {
        exchange.sendResponseHeaders(code, message.length.toLong())
        val os = exchange.responseBody
        os.write(this.message.toByteArray())
        os.close()
    }
}

class OK(message: String) : TextResponse(message) {
    override val code = 200
}