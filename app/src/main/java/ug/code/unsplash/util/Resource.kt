package ug.code.unsplash.util

import ug.code.unsplash.util.Status.*

data class Resource<out T>(val status: Status, val data: T?, val message: String?) {
    companion object {
        fun <T> success(data: T?, msg: String = ""): Resource<T> {
            return Resource(SUCCESS, data, msg)
        }

        fun <T> error(msg: String = ""): Resource<T> {
            return Resource(ERROR, null, msg)
        }

        fun <T> loading(): Resource<T> {
            return Resource(LOADING, null, null)
        }
    }
}

enum class Status {
    LOADING, SUCCESS, ERROR
}