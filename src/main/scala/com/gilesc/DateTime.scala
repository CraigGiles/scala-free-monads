package com.gilesc

import java.time.ZonedDateTime

import cats.free.{Free, Inject}
import cats.{Id, ~>}

trait DateTimeAction[A]

case object GetCurrentTime extends DateTimeAction[ZonedDateTime]

class DateTimeActions[F[_]](implicit I: Inject[DateTimeAction, F]) {
  def getCurrentTime = Free.inject[DateTimeAction, F](GetCurrentTime)
}

object DateTimeActions {
  implicit def actions[F[_]](implicit I: Inject[DateTimeAction, F]): DateTimeActions[F] = new DateTimeActions[F]
}

object ZonedDateTimeInterpreter extends (DateTimeAction ~> Id) {
  override def apply[A](fa: DateTimeAction[A]): Id[A] = fa match {
    case GetCurrentTime => java.time.ZonedDateTime.now()
  }
}
