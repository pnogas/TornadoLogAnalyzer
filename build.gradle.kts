plugins {
    kotlin("jvm") version "1.3.72"
    id("application")
    id("org.openjfx.javafxplugin") version "0.0.8"
    id("org.beryx.jlink") version "2.12.0"
}

group = "com.paulnogas.log.analyzer"
version = "1.0-SNAPSHOT"

javafx {
    version = "11.0.2"
    modules = mutableListOf(
            "javafx.base",
            "javafx.controls",
            "javafx.fxml",
            "javafx.graphics",
            "javafx.media",
            "javafx.swing",
            "javafx.web")
}

application {
    mainClassName = "$moduleName/com.paulnogas.loganalyzer.app.BaseApplication"
}

jlink {
    //options = listOf("--strip-debug", "--compress", "2", "--no-header-files", "--no-man-pages")
    launcher {
        name = "app.module"
    }
    addExtraDependencies("javafx")
}

repositories {
    mavenCentral()
    maven { setUrl("https://oss.sonatype.org/content/repositories/snapshots") }
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("no.tornado:tornadofx:2.0.0-SNAPSHOT")
    implementation("com.squareup.moshi:moshi:1.8.0")
    implementation("io.reactivex.rxjava2:rxkotlin:2.4.0")
    implementation("org.slf4j:slf4j-api:1.7.28")
    implementation("ch.qos.logback:logback-classic:1.2.3")
    implementation("ch.qos.logback:logback-core:1.2.3")
    implementation("javax.servlet:javax.servlet-api:4.0.1")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.4.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.4.2")
    implementation("org.testfx:testfx-junit5:4.0.16-alpha")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "11"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "11"
    }
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
    //jvmArgs(listOf("--add-exports","javafx.graphics/com.sun.javafx.application=org.testfx"))
}


//import java.text.SimpleDateFormat
//
//        buildscript {
//            ext.kotlin_version = "1.3.41"
//            ext.tornadofx_version = "1.7.17"
//            ext.junit_version = "5.5.1"
//            ext.klint_gradle_version = "8.2.0"
//            ext.httpclient_version = "4.5.9"
//            ext.kxkotlin_version = "2.4.0"
//            ext.slf4j_api_version = "1.7.28"
//            ext.logback_classic_version = "1.2.3"
//            ext.logback_core_version = "1.2.3"
//            ext.moshi_version = "1.8.0"
//
//            repositories {
//                mavenLocal()
//                gradlePluginPortal()
//                mavenCentral()
//            }
//            dependencies {
//                classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version"
//                classpath "org.jlleitschuh.gradle:ktlint-gradle:$klint_gradle_version"
//            }
//        }
//
//plugins {
//    id "application"
//    id 'com.github.johnrengelman.shadow' version '5.1.0'
//    id "org.jetbrains.kotlin.jvm" version "1.3.41"
//    id "org.jlleitschuh.gradle.ktlint" version "8.2.0"
//    id "io.gitlab.arturbosch.detekt" version "1.0.1"
//}
//
//apply from: "$rootDir/e2e.gradle"
//
//detekt {
//    toolVersion = "1.0.1"
//    input = files("src/main/kotlin")
//    filters = ".*/resources/.*,.*/build/.*"
//    config = files("detekt-ruleset.yml")
//    //baseline = file("my-detekt-baseline.xml") // Just if you want to create a baseline file.
//}
//
//ktlint {
//    ignoreFailures = true
//}
//
//
//compileKotlin {
//    kotlinOptions.jvmTarget = "1.8"
//}
//
//repositories {
//    mavenLocal()
//    mavenCentral()
//    maven {
//        url "https://oss.sonatype.org/content/repositories/snapshots/"
//    }
//}
//
//dependencies {
//    implementation(
//            "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin_version",
//            "org.jetbrains.kotlin:kotlin-reflect:$kotlin_version",
//            "no.tornado:tornadofx:$tornadofx_version",
//            "org.apache.httpcomponents:httpclient:$httpclient_version",
//            "io.reactivex.rxjava2:rxkotlin:$kxkotlin_version",
//            "org.slf4j:slf4j-api:$slf4j_api_version",
//            "ch.qos.logback:logback-classic:$logback_classic_version",
//            "ch.qos.logback:logback-core:$logback_core_version",
//            "com.squareup.moshi:moshi:$moshi_version"
//    )
//    testImplementation(
//            "org.junit.jupiter:junit-jupiter-api:$junit_version",
//            'org.hamcrest:hamcrest:2.1',
//            )
//    testRuntime("org.junit.jupiter:junit-jupiter-engine:$junit_version")
//}
//
//sourceSets {
//    main.java.srcDirs += 'src/main/kotlin/'
//    test.java.srcDirs += 'src/test/kotlin/'
//}
//
//mainClassName = 'com.paulnogas.loganalyzer.app.BaseApplication'
//
//jar {
//    manifest {
//        attributes (
//                'Built-By'       : System.properties['user.name'],
//        'Build-Timestamp': new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(new Date()),
//        'Build-Revision' : "1.4",
//        'Created-By'     : "Gradle ${gradle.gradleVersion}",
//        'Build-Jdk'      : "${System.properties['java.version']} (${System.properties['java.vendor']} ${System.properties['java.vm.version']})",
//        'Build-OS'       : "${System.properties['os.name']} ${System.properties['os.arch']} ${System.properties['os.version']}",
//        )
//    }
//}
//
//shadowJar {
//    archiveBaseName = "LogAnalyzer"
//    archiveAppendix = null
//    archiveVersion = "1.4"
//    archiveClassifier = null
//}