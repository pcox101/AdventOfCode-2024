plugins {
    kotlin("jvm") version "1.9.21"
}

sourceSets {
    main {
        kotlin.srcDir("src")
    }
}

tasks {
    wrapper {
        gradleVersion = "8.5"
    }
}

tasks.test {
    useJUnitPlatform()
}

dependencies {
    // Other dependencies.
    testImplementation(kotlin("test"))
}