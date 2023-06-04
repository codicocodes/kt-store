package main

import com.sun.net.httpserver.HttpExchange
import java.nio.charset.Charset
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

import cache.Cache
import cache.Cacher
import server.*


fun main() = runBlocking {
    val cache = Cache(10000)
    launch { cache.checkTTL() }
    Server(ServerOptions(), Handler(cache)).start()
}

class Handler(private val cache: Cacher) : SimpleCrudRoute() {
    override fun detail(exchange: HttpExchange, id: String): Response {
        val value = cache.get(id) ?: throw NotFound()
        return OK(value.toUtf8String())
    }

    override fun update(exchange: HttpExchange, id: String, data: ByteArray): Response {
        cache.put(id, data)
        return OK(data.toUtf8String())
    }

    override fun delete(exchange: HttpExchange, id: String): Response {
        val value = cache.delete(id) ?: throw NotFound()
        return OK(value.toUtf8String())
    }

    private fun ByteArray.toUtf8String(): String {
        return toString(Charset.defaultCharset())
    }
}