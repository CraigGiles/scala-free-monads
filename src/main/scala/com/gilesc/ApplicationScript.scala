package com.gilesc

import cats.data.Coproduct

trait ApplicationScript {
  type FirstApp[A] = Coproduct[LoggingAction, DateTimeAction, A]
  type MyApp[A] = Coproduct[RegistrationAction, FirstApp, A]
}
