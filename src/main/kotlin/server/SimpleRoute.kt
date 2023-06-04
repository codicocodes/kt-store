package server

import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler

open class SimpleRoute: HttpHandler {
    override fun handle(exchange: HttpExchange) {
        this.runHandler(exchange).send(exchange)
    }

    private fun runHandler(exchange: HttpExchange): Response {
        return try {
            this.routeRequest(exchange)
        } catch (error: HTTPError) {
            error
        } catch (error: Exception) {
            InternalServerError(error)
        }
    }

    private fun routeRequest(exchange: HttpExchange): Response {
        return when (exchange.requestMethod) {
            HttpMethod.GET -> this.get(exchange)
            HttpMethod.POST -> this.post(exchange)
            HttpMethod.PUT -> this.put(exchange)
            HttpMethod.DELETE -> this.delete(exchange)
            else -> throw MethodNotAllowed()
        }
    }

    open fun get(exchange: HttpExchange): Response {
        throw MethodNotAllowed()
    }

    open fun post(exchange: HttpExchange): Response {
        throw MethodNotAllowed()
    }

    open fun put(exchange: HttpExchange): Response {
        throw MethodNotAllowed()
    }

    open fun delete(exchange: HttpExchange): Response {
        throw MethodNotAllowed()
    }
}
