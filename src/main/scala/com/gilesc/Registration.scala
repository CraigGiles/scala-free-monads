package com.gilesc

import java.time.ZonedDateTime

import cats.free.{Inject, Free}
import cats.free.Free.liftF
import cats.{Id, ~>}

case class User(id: Long, username: String, email: String, password: String, createdAt: ZonedDateTime)

sealed trait RegistrationAction[A]

case class RegistrationContext(username: String, email: String, password: String, confirmation: String)
case class Register(cxt: RegistrationContext) extends RegistrationAction[Option[User]]

class RegistrationActions[F[_]](implicit I: Inject[RegistrationAction, F]) {
  def register(cxt: RegistrationContext) = Free.inject[RegistrationAction, F](Register(cxt))
}

object RegistrationActions {
  implicit def actions[F[_]](implicit I: Inject[RegistrationAction, F]): RegistrationActions[F] = new RegistrationActions[F]
}

object TestRegistrationInterpreter extends (RegistrationAction ~> Id) {
  val users = List.empty[User]
  override def apply[A](fa: RegistrationAction[A]): Id[A] = fa match {
    case Register(cxt) =>
      val time = ZonedDateTimeInterpreter.apply(GetCurrentTime)
      Some(User(users.size, cxt.username, cxt.email, cxt.password, time))
  }
}
