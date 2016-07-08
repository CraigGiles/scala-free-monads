name := "free-registration"
version := "1.0"
scalaVersion := "2.11.8"

// Category Theory
libraryDependencies += "org.typelevel" %% "cats" % "0.6.0"

// Encryption
libraryDependencies += "org.mindrot" % "jbcrypt" % "0.3m"

// Slick
libraryDependencies += "com.typesafe.slick" %% "slick" % "3.1.1"
libraryDependencies += "com.typesafe.slick" %% "slick-hikaricp" % "3.1.1"
libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.36"

libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.6" % "test"

