package server

import com.sun.net.httpserver.HttpExchange

abstract class HTTPError() : Exception(), Response {
    abstract override val message: String
    abstract val code: Int
    override fun send(exchange: HttpExchange) {
        return TextResponse(message, code).send(exchange)
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