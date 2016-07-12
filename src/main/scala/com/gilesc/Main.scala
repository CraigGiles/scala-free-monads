package com.gilesc

import cats.{Id, ~>}

object Main extends App with Testing {
  val interpreter01: FirstApp ~> Id = ConsoleLoggingInterpreter or ZonedDateTimeInterpreter
  val interpreter02: SecondApp ~> Id = TestRegistrationInterpreter or interpreter01
  val interpreter: MyApp ~> Id = BCryptPasswordInterpreter or interpreter02


  val prg = timingProgram("my message")
  val reg = registerNewUser("myusername", "myemail", "mypassword")
  prg.foldMap(interpreter)
  reg.foldMap(interpreter)

  val pass = "mypassword"
  val conf = "mypassword"
  val pw = hashThisPassword(pass, conf)
  val something = pw.foldMap(interpreter)
  println("Hashed Password: " + something)
}
