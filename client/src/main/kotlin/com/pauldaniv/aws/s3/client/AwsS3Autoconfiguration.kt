package com.pauldaniv.aws.s3.client

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(S3Config::class)
class AwsS3Autoconfiguration {

  @Bean
  @ConditionalOnMissingBean
  fun awsClient(s3Config: S3Config): AmazonS3 {
    val credentials = BasicAWSCredentials(s3Config.credentials.accessKey, s3Config.credentials.secretKey)
    val credentialsProvider = AWSStaticCredentialsProvider(credentials)

    val defaultClient = AmazonS3ClientBuilder.standard().withCredentials(credentialsProvider)
    defaultClient.region = s3Config.region
    return defaultClient.build()
  }

  @Bean
  @ConditionalOnMissingBean
  fun s3StorageClient(s3Config: S3Config, s3: AmazonS3): S3StorageClient = S3StorageClient(s3Config, s3)
}
