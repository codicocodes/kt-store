package cache

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class Cache(private val ttl: Long = 0, private val ttlIntervalMs: Long = 1000) : Cacher {
    private val cache = mutableMapOf<String, ByteArray>()
    private val times = mutableMapOf<String, LocalDateTime>()
    private val shouldCheckTTL = ttl > 0

    override fun get(key: String): ByteArray? {
        return cache[key]
    }

    override fun put(key: String, value: ByteArray): Unit {
        val now = LocalDateTime.now()
        cache[key] = value
        times[key] = now
    }

    override fun delete(key: String): ByteArray? {
        times.remove(key)
        return cache.remove(key)
    }

    suspend fun checkTTL() {
        while (shouldCheckTTL) {
            delay(ttlIntervalMs)
            for (entry in times.entries.iterator()) {
                if (shouldClearEntry(entry)) {
                    delete(entry.key)
                }
            }
        }
    }

    private fun shouldClearEntry(entry: MutableMap.MutableEntry<String, LocalDateTime>): Boolean {
        val now = LocalDateTime.now()
        val timeInCacheMs = ChronoUnit.MILLIS.between(entry.value, now)
        return timeInCacheMs > ttl
    }
}
