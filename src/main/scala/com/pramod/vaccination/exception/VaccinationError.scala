package com.pramod.vaccination.exception

import sttp.tapir.Schema
import zio.json.{DeriveJsonCodec, JsonCodec}

sealed trait VaccinationError

object VaccinationError {
  implicit lazy val codec: JsonCodec[VaccinationError] = DeriveJsonCodec.gen

  case class InvalidInput(error: String) extends VaccinationError

  object InvalidInput {
    implicit lazy val codec: JsonCodec[InvalidInput] = DeriveJsonCodec.gen
    implicit lazy val schema: Schema[InvalidInput] = Schema.derived
  }

  case class NotFound(message: String) extends VaccinationError

  object NotFound {
    implicit lazy val codec: JsonCodec[NotFound] = DeriveJsonCodec.gen
    implicit lazy val schema: Schema[NotFound] = Schema.derived
  }
}