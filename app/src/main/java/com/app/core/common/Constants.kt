package com.app.core.common


const val APP_TAG: String = "x-app"

interface NetworkStatus {
    companion object {
        const val SUCCESS = 0
        const val FAILED = 1
        const val RUNNING = 2
        const val MAX = 3
        const val COMPLETE = 4
    }

}



