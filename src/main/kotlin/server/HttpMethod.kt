package server

annotation class HttpMethod(val value: String) {
    companion object {
        const val GET = "GET"
        const val POST = "POST"
        const val PUT = "PUT"
        const val DELETE = "DELETE"
    }
}

