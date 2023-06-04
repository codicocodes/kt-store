package main

import com.sun.net.httpserver.HttpExchange
import java.nio.charset.Charset
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

import cache.Cache
import cache.Cacher
import server.Server
import server.ServerOptions
import server.NotFound
import server.OK
import server.Response
import server.SimpleRoute


fun main() = runBlocking {
    val cache = Cache(10000)
    launch { cache.checkTTL() }
    Server(ServerOptions(), Handler(cache)).start()
}

class Handler (private val cache: Cacher) : SimpleRoute() {
    override fun get(exchange: HttpExchange): Response {
        val id = exchange.getKey()
        val value = cache.get(id) ?: throw NotFound()
        return OK(value.toUtf8String())
    }

    override fun post(exchange: HttpExchange): Response {
        val id = exchange.getKey()
        val data = exchange.readData()
        cache.put(id, data)
        return OK(data.toUtf8String())
    }

    override fun delete(exchange: HttpExchange): Response {
        val id = exchange.getKey()
        val value = cache.delete(id) ?: throw NotFound()
        return OK(value.toUtf8String())
    }

    private fun ByteArray.toUtf8String(): String {
        return toString(Charset.defaultCharset())
    }

    private fun HttpExchange.readData(): ByteArray {
        return requestBody.readAllBytes()
    }

    private fun HttpExchange.getKey(): String {
        return requestURI.path.split("/")[1]
    }
}