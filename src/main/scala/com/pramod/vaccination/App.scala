package com.pramod.vaccination

import com.pramod.vaccination.config.HttpConfig
import com.pramod.vaccination.routes.VaccinationServer
import zio.ZIO
import zhttp.service.Server

object App {
    def server = ZIO.scoped {
      for {
        config  <- ZIO.service[HttpConfig]
        httpApp <- VaccinationServer.httpRoutes
        start <- Server(httpApp).withBinding(config.host, config.port).make.orDie
        _ <- ZIO.logInfo(s"Server started on port: ${start.port}")
        _ <- ZIO.never
      } yield ()
    }
}