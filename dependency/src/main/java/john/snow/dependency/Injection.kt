package john.snow.dependency

import kotlin.reflect.KClass

object Injection {
    private val dependencies: MutableMap<KClass<out Any>, Any> = mutableMapOf()

    @Suppress("UNCHECKED_CAST")
    fun <D : Any> get(kClass: KClass<D>): D {
        return try {
            dependencies[kClass] as D
        } catch (e: IllegalStateException) {
            throw DependencyNotInjectedException("Missing dependency: $kClass")
        }
    }

    fun <D : Any> set(kClass: KClass<D>, dependency: D) {
        dependencies[kClass] = dependency
    }

    class DependencyNotInjectedException(message: String) : Throwable(message)
}