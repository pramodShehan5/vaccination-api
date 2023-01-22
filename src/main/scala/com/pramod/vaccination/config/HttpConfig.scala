package com.pramod.vaccination.config

import com.typesafe.config.ConfigFactory
import zio.*
import zio.config.*
import zio.config.magnolia.*
import zio.config.syntax.*
import zio.config.typesafe.TypesafeConfigSource
import zio.config.typesafe.TypesafeConfigSource.fromTypesafeConfig

case class HttpConfig(port: Int,
                      host: String)

object HttpConfig {
  val live: ZLayer[Any, ReadError[String], HttpConfig] =
    ZLayer {
      read {
        descriptor[HttpConfig].from(
          TypesafeConfigSource.fromResourcePath
            .at(PropertyTreePath.$("http"))
        )
      }
    }
}