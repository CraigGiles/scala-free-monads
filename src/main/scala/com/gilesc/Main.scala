package com.gilesc

import cats.{Id, ~>}

object Main extends App with Testing {
  val composedInterpreter: FirstApp ~> Id = ConsoleLoggingInterpreter or ZonedDateTimeInterpreter
  val interpreter: MyApp ~> Id = TestRegistrationInterpreter or composedInterpreter

  val prg = timingProgram("my message")
  val reg = registerNewUser("myusername", "myemail", "mypassword")
  prg.foldMap(interpreter)
  reg.foldMap(interpreter)
}
