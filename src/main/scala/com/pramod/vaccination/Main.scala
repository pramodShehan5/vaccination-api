package com.pramod.vaccination

import com.pramod.vaccination.config.HttpConfig
import com.pramod.vaccination.routes.VaccinationServer
import com.pramod.vaccination.service.VaccinationService
import zhttp.service.Server
import zio.{Scope, ZIO, ZIOAppArgs, ZIOAppDefault, ZLayer}
import io.netty.channel.{ChannelFactory, ServerChannel}
import zhttp.service.EventLoopGroup
import zhttp.service.server.ServerChannelFactory

object Main extends ZIOAppDefault {

  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] = {
    App.server.provide(VaccinationServer.live,
      VaccinationService.live,
      HttpServerSettings.default,
      HttpConfig.live,
      ZLayer.Debug.tree)
  }
}