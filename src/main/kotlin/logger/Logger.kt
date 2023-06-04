package logger

interface Logger {
    fun log(message: String?)
}

class StandardOutLogger : Logger {
    override fun log(message: String?) {
        println(message)
    }
}