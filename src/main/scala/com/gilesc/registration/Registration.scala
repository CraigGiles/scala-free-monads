package com.gilesc.registration

import java.time.ZonedDateTime

import cats.{~>, Id}
import cats.free.Free
import cats.free.Free.liftF
import com.gilesc.ApplicationScript

case class User(id: Long, username: String, email: String, password: String, createdAt: ZonedDateTime)

trait RegistrationScript {
  type RegistrationScript[A] = Free[RegistrationAction, A]
  sealed trait RegistrationAction[A] {
    def lift: RegistrationScript[A] = liftF(this)
  }
}

trait Registration extends ApplicationScript {
  case class RegistrationContext(username: String, email: String, password: String, confirmation: String)

  case class Register(cxt: RegistrationContext) extends AppAction[Option[User]]
  def register(cxt: RegistrationContext): Script[Option[User]] = Register(cxt).lift
}


