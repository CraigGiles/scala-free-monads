package com.gilesc

import cats.{Id, ~>}

trait Testing extends ApplicationScript with Logging with DateTime with Registration {
  def registerNewUser(username: String, email: String, password: String) = for {
    _ <- log.debug(s"Registering new user $username")
    something <- register(RegistrationContext(username, email, password, password))
  } yield something

  def doSomething(msg: String) = for {
    _ <- log.debug("We are starting to do something")
    _ <- log.debug(msg)
    time <- getCurrentTime
    _ <- log.debug(s"Current Time: $time")
  } yield time

  object TestInterpreter extends (AppAction ~> Id) {
    var users = List.empty[User]

    override def apply[A](fa: AppAction[A]): Id[A] = fa match {
      case Logger(msg, _) => println(msg)
      case GetCurrentTime => java.time.ZonedDateTime.now()
      case Register(cxt) =>
        val usr = User(users.size, cxt.username, cxt.email, cxt.password, java.time.ZonedDateTime.now())
        users = users :+ usr
        Option(usr)
    }

    def run[A](script: Script[A]): A = script.foldMap(this)
  }
}


