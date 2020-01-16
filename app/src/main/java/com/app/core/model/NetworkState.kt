package com.app.core.model


import androidx.annotation.IntDef
import com.app.core.common.NetworkStatus.Companion.COMPLETE
import com.app.core.common.NetworkStatus.Companion.FAILED
import com.app.core.common.NetworkStatus.Companion.MAX
import com.app.core.common.NetworkStatus.Companion.RUNNING
import com.app.core.common.NetworkStatus.Companion.SUCCESS

class NetworkState(
    @param:Status @get:Status
    val status: Int, val msg: String
) {

    @IntDef(SUCCESS, FAILED, RUNNING, COMPLETE, MAX)
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class Status

    override fun equals(other: Any?): Boolean {
        other?.let {
            if (other is NetworkState) {
                val other = other as NetworkState?
                val self = this
                return other?.status == self.status
            }
        }
        return false
    }

    override fun hashCode(): Int {
        var result = status
        result = 31 * result + msg.hashCode()
        return result
    }

    companion object {
        val LOADING: NetworkState = NetworkState(RUNNING, "Running")
        val LOADED: NetworkState = NetworkState(SUCCESS, "Success")
        val ERROR: NetworkState = NetworkState(FAILED, "Failed")
        val MAX_PAGE: NetworkState = NetworkState(MAX, "No More page")
        val COMPLETED: NetworkState = NetworkState(COMPLETE, "No More page")
    }

}

