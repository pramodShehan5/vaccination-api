package com.pramod.vaccination.model

import sttp.tapir.Schema
import zio.json.{DeriveJsonCodec, JsonCodec}

case class VaccinationDetails(vaccinationId: Int,
                              vaccinationName: String,
                              vaccinationCountry: String)

object VaccinationDetails {
  implicit val jsonCodec: JsonCodec[VaccinationDetails] = DeriveJsonCodec.gen
  implicit val schema: Schema[VaccinationDetails] = Schema.derived
}