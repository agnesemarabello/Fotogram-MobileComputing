pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }

    plugins {
        id("org.jetbrains.kotlin.plugin.serialization") version "2.0.0" apply false
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()

        maven {
            url = uri("https://api.mapbox.com/downloads/v2/releases/maven")
            authentication {
                create<BasicAuthentication>("basic")
            }
            credentials {
                username = "mapbox"

                password = "sk.eyJ1IjoiYWduZXNlbWFyYWJlbGxvIiwiYSI6ImNta3BvN3RnZzBqczYzZ3NlYmlvYjJxaDAifQ.r2JCQwZ-6qYSTl-Zfuyggw"
            }
        }
    }
}

rootProject.name = "Fotogram"
include(":app")