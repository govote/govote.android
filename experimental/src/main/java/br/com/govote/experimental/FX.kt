package br.com.govote.experimental

object FX {
  fun <T> anyOf(vararg fns: (T) -> Boolean): (T) -> Boolean =
    fun(source: T): Boolean {
      fns.forEach { f ->
        if (f(source)) {
          return true
        }
      }

      return false
    }

  fun <T> allOf(vararg fns: (T) -> Boolean): (T) -> Boolean =
    fun(source: T): Boolean {
      fns.forEach { f ->
        if (!f(source)) {
          return false
        }
      }

      return true
    }

  fun <A1, A2, R> curry(fn: (A1, A2) -> R): (A1) -> (A2) -> R =
    fun(a: A1): (A2) -> R =
      fun(b: A2): R = fn(a, b)
}
