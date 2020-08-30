package com.pauldaniv.retrofit2.client


import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition
import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.beans.factory.support.BeanDefinitionBuilder
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar
import org.springframework.core.type.AnnotationMetadata
import org.springframework.core.type.filter.AnnotationTypeFilter
import org.springframework.util.ClassUtils

class RetrofitClientsRegistrar : ImportBeanDefinitionRegistrar {

  override fun registerBeanDefinitions(metadata: AnnotationMetadata, registry: BeanDefinitionRegistry) {

    val scanner: ClassPathScanningCandidateComponentProvider = object : ClassPathScanningCandidateComponentProvider(
        false) {
      override fun isCandidateComponent(
          beanDefinition: AnnotatedBeanDefinition): Boolean {
        return (beanDefinition.metadata.isIndependent
            && !beanDefinition.metadata.isAnnotation)
      }
    }
    scanner.addIncludeFilter(AnnotationTypeFilter(RetrofitClient::class.java))

    val configurationAttributes = metadata.getAnnotationAttributes(EnableRetrofitClients::class.java.canonicalName)


    val findCandidateComponents = mutableListOf(ClassUtils.getPackageName(metadata.className))
    val basePackgase = configurationAttributes?.get("basePackages") as Array<String>?

    if (basePackgase != null) {
      findCandidateComponents.addAll(basePackgase)
    }

    findCandidateComponents
        .flatMap { scanner.findCandidateComponents(it) }
        .forEach { candidateComponent: BeanDefinition? ->
          val beanDefinition = candidateComponent as AnnotatedBeanDefinition
          val attributes = beanDefinition.metadata.getAnnotationAttributes(RetrofitClient::class.java.canonicalName)

          val retrofitClientFactoryBeanBuilder = BeanDefinitionBuilder
              .genericBeanDefinition(RetrofitClientFactoryBean::class.java)

          val beanClassName = beanDefinition.beanClassName
          val beanName = beanClassName!!.substringAfterLast(".").substringAfterLast("$").decapitalize()
          val clientName = if (attributes?.get("name") != "") attributes?.get("name") as String else beanName
          retrofitClientFactoryBeanBuilder.addPropertyValue("name", clientName)
          retrofitClientFactoryBeanBuilder.addPropertyValue("type", beanClassName)
          retrofitClientFactoryBeanBuilder.setLazyInit(true)

          registry.registerBeanDefinition(
              clientName,
              retrofitClientFactoryBeanBuilder.beanDefinition)
        }
  }
}
