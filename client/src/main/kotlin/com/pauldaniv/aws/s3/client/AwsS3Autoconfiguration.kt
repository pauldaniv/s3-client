package com.pauldaniv.aws.s3.client

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client

@Configuration
@EnableConfigurationProperties(S3Config::class)
class AwsS3Autoconfiguration {

  @Bean
  @ConditionalOnMissingBean
  fun awsClient(s3Config: S3Config): S3Client {
    return S3Client.builder()
        .region(Region.of(s3Config.region))
        .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
        .build()
  }

  @Bean
  @ConditionalOnMissingBean
  fun s3StorageClient(s3Config: S3Config, s3: S3Client): S3StorageClient = S3StorageClient(s3Config, s3)
}
