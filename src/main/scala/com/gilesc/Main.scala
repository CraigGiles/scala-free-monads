package com.gilesc

object Main extends App with Testing {
  TestInterpreter.run(doSomething("Testing"))
  val usr = TestInterpreter.run(registerNewUser("myusername", "my@email.com", "mypassword"))
  println(usr)
}
