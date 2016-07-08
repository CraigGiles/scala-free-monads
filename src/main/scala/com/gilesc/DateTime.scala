package com.gilesc

import java.time.ZonedDateTime

trait DateTime extends ApplicationScript {
  case object GetCurrentTime extends AppAction[ZonedDateTime]
  def getCurrentTime = GetCurrentTime.lift
}
