package com.gilesc

import cats.free.Free
import cats.free.Free._

trait ApplicationScript {
  type Script[A] = Free[AppAction, A]
  trait AppAction[A] {
    def lift: Script[A] = liftF(this)
  }
}
