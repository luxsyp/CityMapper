package john.snow.citymapper.core.common

import john.snow.citymapper.core.common.Status.ERROR
import john.snow.citymapper.core.common.Status.LOADING
import john.snow.citymapper.core.common.Status.SUCCESS

enum class Status {
    LOADING,
    SUCCESS,
    ERROR
}

data class Resource<out T>(val status: Status, val data: T?, val error: Throwable?) {
    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(SUCCESS, data, null)
        }

        fun <T> error(throwable: Throwable, data: T? = null): Resource<T> {
            return Resource(ERROR, data, throwable)
        }

        fun <T> loading(data: T? = null): Resource<T> {
            return Resource(LOADING, data, null)
        }
    }
}