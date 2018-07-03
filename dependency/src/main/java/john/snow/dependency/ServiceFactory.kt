package john.snow.dependency
import kotlin.reflect.KClass

interface ServiceFactory {
    fun <S : Any> get(kClass: KClass<S>): S
}