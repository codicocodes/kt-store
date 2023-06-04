package main

import com.sun.net.httpserver.HttpExchange
import java.nio.charset.Charset

import cache.Cache
import cache.Cacher
import server.Server
import server.ServerOptions
import server.NotFound
import server.OK
import server.Response
import server.SimpleRoute


fun main() {
    val cache = Cache()
    Server(ServerOptions(), Handler(cache)).start()
}

class Handler (private val cache: Cacher) : SimpleRoute() {
    override fun get(exchange: HttpExchange): Response {
        val id = exchange.getKey()
        val value = cache.get(id) ?: throw NotFound()
        return OK(value.utf8())
    }

    override fun post(exchange: HttpExchange): Response {
        val id = exchange.getKey()
        val data = exchange.readData()
        cache.put(id, data)
        return OK(data.utf8())
    }

    override fun delete(exchange: HttpExchange): Response {
        val id = exchange.getKey()
        val value = cache.delete(id) ?: throw NotFound()
        return OK(value.utf8())
    }

    private fun ByteArray.utf8(): String {
        return toString(Charset.defaultCharset())
    }

    private fun HttpExchange.readData(): ByteArray {
        return requestBody.readAllBytes()
    }

    private fun HttpExchange.getKey(): String {
        return requestURI.path.split("/")[1]
    }
}