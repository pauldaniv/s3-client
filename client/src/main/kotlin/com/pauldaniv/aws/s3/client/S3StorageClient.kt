package com.pauldaniv.aws.s3.client

import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.*
import java.io.InputStream

class S3StorageClient(
    private val s3Config: S3Config,
    private val s3: S3Client
) {
  fun getFile(key: String) = s3.getObject(GetObjectRequest.builder().bucket(s3Config.bucket).key(key).build())

  fun getFileMetadata(key: String) = s3.headObject(HeadObjectRequest.builder().bucket(s3Config.bucket).key(key).build())
      .metadata()

  fun fileExists(key: String) = try {
    s3.headObject(HeadObjectRequest.builder().bucket(s3Config.bucket).key(key).build()).metadata() != null
  } catch (e: NoSuchKeyException) {
    false
  }

  fun uploadFile(key: String, inputStream: InputStream, contentLength: Long) =
      s3.putObject(PutObjectRequest.builder().bucket(s3Config.bucket).key(key)
          .build(), RequestBody.fromInputStream(inputStream, contentLength))

  fun deleteFile(key: String) = s3.deleteObject(DeleteObjectRequest.builder().bucket(s3Config.bucket).key(key).build())
}
