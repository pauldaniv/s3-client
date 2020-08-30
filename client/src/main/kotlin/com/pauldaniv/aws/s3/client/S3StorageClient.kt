package com.pauldaniv.aws.s3.client

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.DeleteObjectRequest
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import java.io.InputStream

class S3StorageClient(
    private val s3Config: S3Config,
    private val s3: AmazonS3
) {

  fun uploadFile(fileName: String, data: InputStream, metadata: ObjectMetadata) {
    uploadFileTos3bucket(fileName, data, metadata)
  }

  fun fileExists(fileName: String): Boolean = s3.doesObjectExist(s3Config.bucket, fileName)

  private fun uploadFileTos3bucket(fileName: String, inputStream: InputStream, metadata: ObjectMetadata) {
    s3.putObject(PutObjectRequest(s3Config.bucket, fileName, inputStream, metadata))
  }

  fun deleteFileFromS3Bucket(fileUrl: String): String? {
    val fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1)
    s3.deleteObject(DeleteObjectRequest(s3Config.bucket, fileName))
    return "Successfully deleted"
  }
}
