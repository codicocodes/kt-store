package server

import com.sun.net.httpserver.HttpExchange

interface IExchangeDataReader {
    fun HttpExchange.readData(): ByteArray
}

class ExchangeDataReader : IExchangeDataReader {
    override fun HttpExchange.readData(): ByteArray {
        return requestBody.readAllBytes()
    }

}

interface IExchangeIdReader {
    fun HttpExchange.readID(): String
}

class ExchangeIdReader : IExchangeIdReader {
    override fun HttpExchange.readID(): String {
        return requestURI.path.split("/")[1]
    }
}
