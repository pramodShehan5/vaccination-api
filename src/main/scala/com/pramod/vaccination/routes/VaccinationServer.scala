package com.pramod.vaccination.routes

import com.pramod.vaccination.exception
import com.pramod.vaccination.exception.VaccinationError
import com.pramod.vaccination.model.{VaccinationDetails, Vaccinations}
import com.pramod.vaccination.service.VaccinationService
import zio.*
import sttp.apispec.openapi.circe.yaml.*
import sttp.model.StatusCode
import sttp.tapir.PublicEndpoint
import sttp.tapir.docs.openapi.OpenAPIDocsInterpreter
import sttp.tapir.json.zio.*
import sttp.tapir.server.ziohttp.ZioHttpInterpreter
import sttp.tapir.swagger.SwaggerUI
import sttp.tapir.ztapir.*
import zhttp.http.{Http, HttpApp, Request, Response}

trait VaccinationServer {
  def httpRoutes: ZIO[Any, Nothing, HttpApp[Any, Throwable]]
}

object VaccinationServer {

  lazy val live: ZLayer[VaccinationService, Nothing, VaccinationServer] = ZLayer {
    for {
      vaccinationService <- ZIO.service[VaccinationService]
    } yield VaccinationServerLive(vaccinationService)
  }

  def httpRoutes: ZIO[VaccinationServer, Nothing, HttpApp[Any, Throwable]] =
    ZIO.serviceWithZIO[VaccinationServer](_.httpRoutes)
}