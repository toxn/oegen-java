plugins {
    id("org.jetbrains.kotlin.jvm") version("2.3.21")
    id("org.jetbrains.kotlin.plugin.compose") version("2.3.21")
    id("org.jetbrains.compose") version("1.11.1")
    application
}

repositories {
    mavenCentral()
    google()
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation(compose.material3)
    implementation(compose.materialIconsExtended)

    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation(libs.junit.jupiter.engine)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    implementation(libs.guava)
    implementation(libs.jackson.xml)
    implementation(libs.jackson.kotlin)
    implementation(libs.jackson.yaml)
    implementation(libs.json.schema.validator)

    implementation(libs.sqlite.jdbc)
    implementation(libs.postgresql)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

application {
    mainClass = "fr.cdbs.oegen.ctrl.MainKt"
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
