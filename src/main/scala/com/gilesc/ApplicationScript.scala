package com.gilesc

import cats.data.Coproduct

trait ApplicationScript {
  type FirstApp[A] = Coproduct[LoggingAction, DateTimeAction, A]
  type SecondApp[A] = Coproduct[RegistrationAction, FirstApp, A]
  type MyApp[A] = Coproduct[PasswordAction, SecondApp, A]
}
