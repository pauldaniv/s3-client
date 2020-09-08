package com.pauldaniv.aws.s3.client

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.DeleteObjectRequest
import com.amazonaws.services.s3.model.ObjectMetadata
import java.io.InputStream

class S3StorageClient(
    private val s3Config: S3Config,
    private val s3: AmazonS3
) {
  fun getFile(fileName: String) = s3.getObject(s3Config.bucket, fileName)

  fun getFileMetadata(fileName: String) = s3.getObjectMetadata(s3Config.bucket, fileName)

  fun fileExists(fileName: String): Boolean = s3.doesObjectExist(s3Config.bucket, fileName)

  fun uploadFile(fileName: String, inputStream: InputStream, metadata: ObjectMetadata) =
      s3.putObject(s3Config.bucket, fileName, inputStream, metadata)

  fun deleteFile(fileName: String) = s3.deleteObject(DeleteObjectRequest(s3Config.bucket, fileName))
}
