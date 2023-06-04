package cache

interface Cacher {
    fun get(key: String): ByteArray?
    fun put(key: String, value: ByteArray)
    fun delete(key: String): ByteArray?
}
