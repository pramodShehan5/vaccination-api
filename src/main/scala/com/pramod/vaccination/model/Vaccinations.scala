package com.pramod.vaccination.model

import sttp.tapir.Schema
import zio.json.{DeriveJsonCodec, JsonCodec}

case class Vaccinations(vaccinationList: List[VaccinationDetails])

object Vaccinations {
  implicit val jsonCodec: JsonCodec[Vaccinations] = DeriveJsonCodec.gen
  implicit val schema: Schema[Vaccinations] = Schema.derived
}