package server

import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler

open class SimpleCrudRoute: HttpHandler,
    IExchangeIdReader by ExchangeIdReader(),
    IExchangeDataReader by ExchangeDataReader() {
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
            HttpMethod.GET -> this.routeGet(exchange)
            HttpMethod.POST -> this.runCreate(exchange)
            HttpMethod.PUT -> this.runUpdate(exchange)
            HttpMethod.DELETE -> this.runDelete(exchange)
            else -> throw MethodNotAllowed()
        }
    }

    private fun runCreate(exchange: HttpExchange): Response {
        val data = exchange.readData()
        return create(exchange, data)
    }
    private fun routeGet(exchange: HttpExchange): Response {
        return try {
            val id = exchange.readID()
            detail(exchange, id)
        } catch (e: IndexOutOfBoundsException) {
            list(exchange)
        }
    }

    private fun runUpdate(exchange: HttpExchange): Response {
        val id = exchange.readID()
        val data = exchange.readData()
        return update(exchange, id, data)
    }

    open fun runDelete(exchange: HttpExchange): Response {
        val id = exchange.readID()
        return delete(exchange, id)
    }

    open fun create(exchange: HttpExchange, data: ByteArray): Response {
        throw MethodNotAllowed()
    }


    open fun detail(exchange: HttpExchange, id: String): Response {
        throw MethodNotAllowed()
    }

    open fun list(exchange: HttpExchange): Response {
        throw MethodNotAllowed()
    }

    open fun update(exchange: HttpExchange, id: String, data: ByteArray): Response {
        throw MethodNotAllowed()
    }

    open fun delete(exchange: HttpExchange, id: String): Response {
        throw MethodNotAllowed()
    }
}