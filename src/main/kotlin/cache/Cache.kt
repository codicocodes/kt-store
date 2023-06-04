package cache

import java.time.LocalDateTime

class Cache(val ttl: Int = 0) : Cacher {
    private val cache = mutableMapOf<String, ByteArray>()
    private val times = mutableMapOf<String, LocalDateTime>()

    override fun get(key: String): ByteArray? {
        return cache[key]
    }

    override fun put(key: String, value: ByteArray): Unit {
        val now = LocalDateTime.now()
        cache.put(key, value)
        times.put(key, now)
    }

    override fun delete(key: String): ByteArray? {
        return cache.remove(key)
    }

    fun checkTTL() {}
}
