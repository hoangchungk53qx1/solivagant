package com.freeletics.khonshu.navigation.internal

import kotlinx.coroutines.channels.ChannelResult
import kotlinx.coroutines.channels.SendChannel

internal fun <T> SendChannel<T>.trySendBlocking(element: T): ChannelResult<Unit> {
  TODO()
  return trySend(element)
}
