package com.app.core.model


/**
 * @param actionId  id for an async call [if multiple network calls in same page]
 * @param actionType it could be error or feedback
 * @param serverException  server exception
 * @param message  feedback message
 * @param navId  navigate id for navigation controller
 */

data class Action(
    val actionId: Int? = null,
    val actionType: Int? = null,
    val serverException: ServerException? = null,
    val message: String? = null,
    val navId: Int? = null
)