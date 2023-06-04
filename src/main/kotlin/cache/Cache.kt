package cache

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.concurrent.locks.Lock

class Cache(private val ttl: Long = 0, private val ttlIntervalMs: Long = 1000) : Cacher {
    private val cache = mutableMapOf<String, ByteArray>()
    private val times = mutableMapOf<String, LocalDateTime>()
    private val shouldCheckTTL = ttl > 0
    private val mutex = Mutex()

    override suspend fun get(key: String): ByteArray? {
        mutex.withLock {
            return cache[key]
        }
    }

    override suspend fun put(key: String, value: ByteArray): Unit {
        mutex.withLock {
            val now = LocalDateTime.now()
            cache[key] = value
            times[key] = now
        }
    }

    override suspend fun delete(key: String): ByteArray? {
        mutex.withLock {
            times.remove(key)
            return cache.remove(key)
        }
    }

    suspend fun checkTTL() {
        while (shouldCheckTTL) {
            delay(ttlIntervalMs)
            mutex.withLock {
                for (entry in times.entries.iterator()) {
                    if (shouldClearEntry(entry)) {
                        times.remove(entry.key)
                        cache.remove(entry.key)
                    }
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
