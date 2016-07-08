package com.gilesc

trait Logging extends ApplicationScript {
  sealed trait LogLevel
  case object Debug extends LogLevel
  case object Info extends LogLevel
  case object Warn extends LogLevel
  case object Error extends LogLevel

  case class Logger(message: String, level: LogLevel) extends AppAction[Unit]

  case object log {
    def debug(message: String) = Logger(message, Debug).lift
    def info(message: String) = Logger(message, Info).lift
    def warn(message: String) = Logger(message, Warn).lift
    def error(message: String) = Logger(message, Error).lift
  }

  def log(message: String, level: LogLevel) = Logger(message, level).lift
}


