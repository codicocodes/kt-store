package server

import com.sun.net.httpserver.HttpExchange

interface Response {
    fun send(exchange: HttpExchange)
}

open class TextResponse (private val message: String, private val code: Int) : Response {
    override fun send(exchange: HttpExchange) {
        exchange.sendResponseHeaders(code, message.length.toLong())
        val os = exchange.responseBody
        os.write(this.message.toByteArray())
        os.close()
    }
}

class OK(message: String) : TextResponse(message, 200)
