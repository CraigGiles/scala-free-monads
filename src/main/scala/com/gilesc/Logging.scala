package com.gilesc

import cats.free.{Free, Inject}
import cats.{Id, ~>}

trait LoggingAction[A]

sealed trait LogLevel
case object Debug extends LogLevel { override def toString = "[debug] - "}
case object Info extends LogLevel { override def toString = "[info] - "}
case object Warn extends LogLevel { override def toString = "[warn] - "}
case object Error extends LogLevel { override def toString = "[error] - "}

case class Logger(message: String, level: LogLevel) extends LoggingAction[Unit] {
  override def toString = level.toString + message
}

class LoggingActions[F[_]](implicit I: Inject[LoggingAction, F]) {
  def log(message: String, level: LogLevel) = Free.inject[LoggingAction,F](Logger(message, level))

  case object log {
    def debug(message: String) = Free.inject[LoggingAction,F](Logger(message, Debug))
    def info(message: String) = Free.inject[LoggingAction,F](Logger(message, Info))
    def warn(message: String) = Free.inject[LoggingAction,F](Logger(message, Warn))
    def error(message: String) = Free.inject[LoggingAction,F](Logger(message, Error))
  }
}

object LoggingActions {
  implicit def actions[F[_]](implicit I: Inject[LoggingAction, F]): LoggingActions[F] = new LoggingActions[F]
}

object ConsoleLoggingInterpreter extends (LoggingAction ~> Id) {
  def apply[A](fa: LoggingAction[A]): Id[A] = fa match {
    case Logger(a,b) => println(b+a)
  }
}

