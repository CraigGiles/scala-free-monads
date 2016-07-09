package com.gilesc

trait Testing extends ApplicationScript {
  def timingProgram(msg: String)(implicit L: LoggingActions[MyApp], D: DateTimeActions[MyApp]) = {
    import D._
    import L._

    for {
      _ <- log.debug("We are starting to do something")
      _ <- log.debug(msg)
      time <- getCurrentTime
      _ <- log.debug(s"Current Time: $time")
    } yield time
  }

  def registerNewUser(username: String, email: String, password: String)
                     (implicit L: LoggingActions[MyApp], D: DateTimeActions[MyApp], R: RegistrationActions[MyApp]) = {

    import D._, L._, R._

    for {
      time <- getCurrentTime
      _ <- log.debug(s"Registering new user $username at time $time")
      user <- register(RegistrationContext(username, email, password, password))
    } yield user
  }

}


