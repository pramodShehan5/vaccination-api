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

class VaccinationServerLive(vaccinationService: VaccinationService) extends VaccinationServer {

  private val VACCINATION_LIST = List(VaccinationDetails(1, "Pfizer", "USA"),
    VaccinationDetails(2, "Moderna", "Russia"),
    VaccinationDetails(3, "Sinopharm", "China"))

  private val baseEndpoint = endpoint.in("api").in("v1").in("vaccination")

  private val getVaccinationErrorOut = oneOf[VaccinationError](
    oneOfVariant(StatusCode.NotFound, jsonBody[VaccinationError.NotFound].description("Vaccination not found."))
  )

  private val getVaccinationInputErrorOut = oneOf[VaccinationError](
    oneOfVariant(StatusCode.BadRequest, jsonBody[VaccinationError.InvalidInput].description("Invalida input."))
  )

  private val vaccinationList = jsonBody[Vaccinations].example(Vaccinations(VACCINATION_LIST))

  private val vaccinationDetails = jsonBody[VaccinationDetails].example(VACCINATION_LIST(0))

  private val getAllVaccinationsEndpoint =
    baseEndpoint.get
      .out(vaccinationList)
      .errorOut(getVaccinationErrorOut)

  private val getVaccinationEndpoint =
      baseEndpoint.get
        .in(path[Int]("vac_id"))
        .out(vaccinationDetails)
        .errorOut(getVaccinationErrorOut)

  private val putVaccinationEndpoint =
    baseEndpoint.put
      .in(path[Int]("vac_id"))
      .in(vaccinationDetails)
      .out(vaccinationList)
      .errorOut(getVaccinationInputErrorOut)

  private val postVaccinationEndpoint =
    baseEndpoint.post
      .in(vaccinationDetails)
      .out(vaccinationList)
      .errorOut(getVaccinationInputErrorOut)

  private val deleteVaccinationEndpoint =
    baseEndpoint.delete
      .in(path[Int]("vac_id"))
      .errorOut(getVaccinationInputErrorOut)


  private val allRoutes: Http[Any, Throwable, Request, Response] =
    ZioHttpInterpreter().toHttp(List(getAllVaccinationsEndpoint.zServerLogic(_ => vaccinationService.getAllVaccination()),
      getVaccinationEndpoint.zServerLogic(vacId => vaccinationService.getVaccinationById(vacId)),
      postVaccinationEndpoint.zServerLogic(vacDetails => vaccinationService.addVaccination(vacDetails)),
      deleteVaccinationEndpoint.zServerLogic(vacId => vaccinationService.deleteVaccination(vacId)),
      putVaccinationEndpoint.zServerLogic(param => vaccinationService.updateVaccination(param._1, param._2))))

  private val endpoints = {
    val endpoints = List(
      getAllVaccinationsEndpoint,
      getVaccinationEndpoint,
      postVaccinationEndpoint,
      putVaccinationEndpoint,
      deleteVaccinationEndpoint
    )
    endpoints.map(_.tags(List("Vaccination Endpoints")))
  }

  override def httpRoutes: ZIO[Any, Nothing, HttpApp[Any, Throwable]] =
     for {
      openApi       <- ZIO.succeed(OpenAPIDocsInterpreter().toOpenAPI(endpoints, "Vaccination Service", "0.1"))
      routesHttp    <- ZIO.succeed(allRoutes)
      endPointsHttp <- ZIO.succeed(ZioHttpInterpreter().toHttp(SwaggerUI[Task](openApi.toYaml)))
    } yield routesHttp ++ endPointsHttp
}