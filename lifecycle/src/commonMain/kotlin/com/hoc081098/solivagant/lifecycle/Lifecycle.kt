package com.hoc081098.solivagant.lifecycle

import kotlin.jvm.JvmStatic
import kotlinx.coroutines.flow.StateFlow

/**
 * A holder of [Lifecycle.State] that can be observed for changes.
 *
 * Possible transitions:
 *
 * ```
 * [INITIALIZED] ──┐
 *                 ↓
 *         ┌── [CREATED] ──┐
 *         ↓       ↑       ↓
 *    [DESTROYED]  └── [STARTED] ──┐
 *                         ↑       ↓
 *                         └── [RESUMED]
 * ```
 */
public interface Lifecycle {
  public val currentStateFlow: StateFlow<State>

  public fun subscribe(observer: Observer): Cancellable

  public fun interface Observer {
    public fun onStateChanged(event: Event)
  }

  public fun interface Cancellable {
    public fun cancel()
  }

  /**
   * Defines the possible states of the [Lifecycle].
   */
  public enum class State {
    DESTROYED,
    INITIALIZED,
    CREATED,
    STARTED,
    RESUMED,
  }

  public enum class Event {
    ON_CREATE,
    ON_START,
    ON_RESUME,
    ON_PAUSE,
    ON_STOP,
    ON_DESTROY,
    ;

    public companion object {
      /**
       * Returns the [Lifecycle.Event] that will be reported by a [Lifecycle]
       * leaving the specified [Lifecycle.State] to a lower state, or `null`
       * if there is no valid event that can move down from the given state.
       *
       * @param state the higher state that the returned event will transition down from
       * @return the event moving down the lifecycle phases from state
       */
      @JvmStatic
      public fun downFrom(state: State): Event? {
        return when (state) {
          State.CREATED -> ON_DESTROY
          State.STARTED -> ON_STOP
          State.RESUMED -> ON_PAUSE
          else -> null
        }
      }

      /**
       * Returns the [Lifecycle.Event] that will be reported by a [Lifecycle]
       * entering the specified [Lifecycle.State] from a higher state, or `null`
       * if there is no valid event that can move down to the given state.
       *
       * @param state the lower state that the returned event will transition down to
       * @return the event moving down the lifecycle phases to state
       */
      @JvmStatic
      public fun downTo(state: State): Event? {
        return when (state) {
          State.DESTROYED -> ON_DESTROY
          State.CREATED -> ON_STOP
          State.STARTED -> ON_PAUSE
          else -> null
        }
      }

      /**
       * Returns the [Lifecycle.Event] that will be reported by a [Lifecycle]
       * leaving the specified [Lifecycle.State] to a higher state, or `null`
       * if there is no valid event that can move up from the given state.
       *
       * @param state the lower state that the returned event will transition up from
       * @return the event moving up the lifecycle phases from state
       */
      @JvmStatic
      public fun upFrom(state: State): Event? {
        return when (state) {
          State.INITIALIZED -> ON_CREATE
          State.CREATED -> ON_START
          State.STARTED -> ON_RESUME
          else -> null
        }
      }

      /**
       * Returns the [Lifecycle.Event] that will be reported by a [Lifecycle]
       * entering the specified [Lifecycle.State] from a lower state, or `null`
       * if there is no valid event that can move up to the given state.
       *
       * @param state the higher state that the returned event will transition up to
       * @return the event moving up the lifecycle phases to state
       */
      @JvmStatic
      public fun upTo(state: State): Event? {
        return when (state) {
          State.CREATED -> ON_CREATE
          State.STARTED -> ON_START
          State.RESUMED -> ON_RESUME
          else -> null
        }
      }
    }
  }

  public companion object {
    public val Lifecycle.currentState: State
      get() = currentStateFlow.value
  }
}
