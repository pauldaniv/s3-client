plugins {
  base
  java
  idea
  `maven-publish`
  id("io.freefair.lombok") version "5.1.1"
  id("com.jfrog.bintray") version "1.8.4" apply false
  kotlin("jvm") version "1.3.50" apply false
}

group = "com.pauldaniv.library.template"
version = "1.0-SNAPSHOT"

subprojects {
  apply(plugin = "java")
  apply(plugin = "maven-publish")
  val sourcesJar by tasks.creating(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets["main"].allSource)
  }

  publishing {
    repositories {
      maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/pauldaniv/java-library-template")
        credentials {
          username = project.findProperty("gpr.usr") as String? ?: System.getenv("USERNAME")
          password = project.findProperty("gpr.key") as String? ?: System.getenv("TOKEN")
        }
      }
    }

    publications {
      register<MavenPublication>("gpr") {
        from(components["java"])
        artifact(sourcesJar)
      }
    }
  }
}

allprojects {
  apply(plugin = "java")
  apply(plugin = "idea")
  apply(plugin = "groovy")
  apply(plugin = "io.freefair.lombok")

  repositories {
    jcenter()
    mavenCentral()
    maven {
      name = "GitHubPackages"
      url = uri("https://maven.pkg.github.com/pauldaniv/bom-template")
      credentials {
        username = project.findProperty("gpr.usr") as String? ?: System.getenv("USERNAME")
        password = project.findProperty("gpr.key") as String? ?: System.getenv("TOKEN")
      }
    }
  }

  dependencies {
    implementation(platform("com.paul:bom-template:0.0.+"))
    implementation("org.codehaus.groovy:groovy")
    testImplementation("org.assertj:assertj-core")
  }

  configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
  }

  idea {
    module {
      excludeDirs.addAll(listOf(
          file(".idea"),
          file(".gradle"),
          file("gradle"),
          file("build"),
          file("out")
      ))
    }
  }

  tasks.withType<Test> {
    useJUnitPlatform()
  }

  configurations.all {
    resolutionStrategy.cacheDynamicVersionsFor(1, "minutes")
  }
}
