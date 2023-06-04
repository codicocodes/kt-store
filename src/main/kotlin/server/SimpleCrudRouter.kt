package server

import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import kotlinx.coroutines.runBlocking

open class SimpleCrudRoute: HttpHandler,
    IExchangeIdReader by ExchangeIdReader(),
    IExchangeDataReader by ExchangeDataReader() {
    override fun handle(exchange: HttpExchange) = runBlocking {
        runHandler(exchange).send(exchange)
    }

    private suspend fun runHandler(exchange: HttpExchange): Response {
        return try {
            this.routeRequest(exchange)
        } catch (error: HTTPError) {
            error
        } catch (error: Exception) {
            InternalServerError(error)
        }
    }

    private suspend fun routeRequest(exchange: HttpExchange): Response {
        return when (exchange.requestMethod) {
            HttpMethod.GET -> routeGet(exchange)
            HttpMethod.POST -> runCreate(exchange)
            HttpMethod.PUT -> runUpdate(exchange)
            HttpMethod.DELETE -> runDelete(exchange)
            else -> throw MethodNotAllowed()
        }
    }

    private suspend fun runCreate(exchange: HttpExchange): Response {
        val data = exchange.readData()
        return create(exchange, data)
    }
    private suspend fun routeGet(exchange: HttpExchange): Response {
        return try {
            val id = exchange.readID()
            detail(exchange, id)
        } catch (e: IndexOutOfBoundsException) {
            list(exchange)
        }
    }

    private suspend fun runUpdate(exchange: HttpExchange): Response {
        val id = exchange.readID()
        val data = exchange.readData()
        return update(exchange, id, data)
    }

    open suspend fun runDelete(exchange: HttpExchange): Response {
        val id = exchange.readID()
        return delete(exchange, id)
    }

    open suspend fun create(exchange: HttpExchange, data: ByteArray): Response {
        throw MethodNotAllowed()
    }


    open suspend fun detail(exchange: HttpExchange, id: String): Response {
        throw MethodNotAllowed()
    }

    open suspend fun list(exchange: HttpExchange): Response {
        throw MethodNotAllowed()
    }

    open suspend fun update(exchange: HttpExchange, id: String, data: ByteArray): Response {
        throw MethodNotAllowed()
    }

    open suspend fun delete(exchange: HttpExchange, id: String): Response {
        throw MethodNotAllowed()
    }
}