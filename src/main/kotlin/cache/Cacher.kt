package cache

interface Cacher {
    suspend fun get(key: String): ByteArray?
    suspend fun put(key: String, value: ByteArray)
    suspend fun delete(key: String): ByteArray?
}
