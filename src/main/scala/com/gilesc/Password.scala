package com.gilesc

import cats.data.{Writer, Xor}
import cats.free.{Free, Inject}
import cats.free.Free._
import cats.implicits._
import cats.{Id, ~>}
import org.mindrot.jbcrypt.BCrypt

// Domain Objects
class ValidatedPassword private (val value: String)
object ValidatedPassword {
  def apply(pw: String) = new ValidatedPassword(pw)
}

class HashedPassword private (val value: String, val salt: Salt) {
  override val toString = s"HashedPassword($value, $salt)"
}
object HashedPassword {
  def apply(pw: String, salt: Salt) = new HashedPassword(pw, salt)
}

case class Salt(value: String)

// Actions
sealed trait PasswordAction[A]

case class ValidatePassword(password: String, confirmation: String) extends PasswordAction[Option[ValidatedPassword]]
case class GenerateSalt(logRounds: Int) extends PasswordAction[Salt]
case class HashPassword(password: ValidatedPassword, salt: Salt) extends PasswordAction[HashedPassword]
case class CheckPassword(password: String, hashed: HashedPassword) extends PasswordAction[Boolean]

class PasswordActions[F[_]](implicit I: Inject[PasswordAction, F]) {
  def validatePassword(password: String, confirmation: String) = Free.inject(ValidatePassword(password, confirmation))
  def generateSalt(logRounds: Int) = Free.inject(GenerateSalt(logRounds))
  def hashPassword(validatedPassword: ValidatedPassword, salt: Salt) = Free.inject(HashPassword(validatedPassword, salt))
  def checkPassword(password: String, hashed: HashedPassword) = Free.inject(CheckPassword(password, hashed))
}

object PasswordActions {
  implicit def actions[F[_]](implicit I: Inject[PasswordAction, F]): PasswordActions[F] = new PasswordActions[F]
}

object BCryptPasswordInterpreter extends (PasswordAction ~> Id) with StringValidation {
  override def apply[A](fa: PasswordAction[A]): Id[A] = fa match {
    case ValidatePassword(pass: String, conf: String) => val result = for {
        equals <- validateIsEqualTo(pass, conf)
        length <- validateIsGreaterThan(pass, 7)
      } yield (equals && length)

      if (!result.value) None else Some(ValidatedPassword(pass))

    case GenerateSalt(logRounds: Int) =>
      Salt(BCrypt.gensalt(logRounds))

    case HashPassword(password: ValidatedPassword, salt: Salt) =>
      HashedPassword(BCrypt.hashpw(password.value, salt.value), salt)

    case CheckPassword(password: String, hashed: HashedPassword) =>
     BCrypt.checkpw(password, hashed.value)
  }
}

case class ValidationError(message: String)

trait StringValidation {
  val isEmpty = (str: String) => {
    if (str.isEmpty) Writer(List(ValidationError("Value can not be empty")), false)
    else Writer(List.empty[ValidationError], true)
  }

  val validateIsGreaterThan = (str: String, length: Int) => {
    if (str.length > length) Writer(List.empty[ValidationError], true)
    else Writer(List(ValidationError(s"Value must be at least $length characters")), false)
  }

  val validateIsEqualTo = (s1: String, s2: String) => {
    if (s1.equals(s2)) Writer(List.empty[ValidationError], true)
    else Writer(List(ValidationError("Values do not match")), false)
  }

}
