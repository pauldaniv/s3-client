package com.pauldaniv.aws.s3.client

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("s3")
data class S3Config(
    val credentials: Credentials,
    val region: String,
    val bucket: String,
    val endpoint: String
) {
  data class Credentials(val accessKey: String, val secretKey: String)
}
