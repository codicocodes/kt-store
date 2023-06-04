package server

import com.sun.net.httpserver.HttpExchange

abstract class HTTPError() : Exception(), Response {
    abstract override val message: String
    abstract val code: Int
    override fun send(exchange: HttpExchange) {
        exchange.sendResponseHeaders(this.code, message.length.toLong())
        val os = exchange.responseBody
        os.write(message.toByteArray())
        os.close()
    }
}

class NotFound : HTTPError() {
    override val message = "Not found"
    override val code = 404
}

class MethodNotAllowed : HTTPError() {
    override val message = "Method not allowed"
    override val code = 405
}

class InternalServerError(val error: Exception) : HTTPError() {
    override val message = "Internal server error"
    override val code = 500
}