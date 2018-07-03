package john.snow.dependency

import kotlin.reflect.KClass

interface RepositoryFactory {
    fun <S : Any> get(kClass: KClass<S>): S
}