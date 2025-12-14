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
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://pos-mobile-test.cdn.adyen.com/adyen-pos-android")
            credentials(HttpHeaderCredentials::class) {
                name = "x-api-key"
                value = "AQExhmfxL4LIaBNEw0m/n3Q5qf3VaY9UCJ1rW2ZZ03a/zDQYC5UOqdp/KudqUlSejGF+fhDBXVsNvuR83LVYjEgiTGAH-6p0p0SQFQGPRJQzfX3yoFTh1Ou0ppDxYbe/Nmj33I68=-i1i(ez33_mRy+}U7J9h"
            }
            authentication {
                create<HttpHeaderAuthentication>("header")
            }
        }
    }
}

rootProject.name = "Tap To Pay SDK"
include(":app")
 