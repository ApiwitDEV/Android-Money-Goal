pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
//    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
//    repositories {
//        google()
//        mavenCentral()
//    }
    dependencyResolutionManagement {
        repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
        repositories {
            google()
            mavenCentral()

            // Add the new repositories starting on the next line...
            maven {
                url = uri("/Users/bas/Documents/GitHub/Money Goal/flutter_module/build/host/outputs/repo")
                // This is relative to the location of the build.gradle file
                // if using a relative path.
            }

            maven {
                url = uri("https://storage.googleapis.com/download.flutter.io")
            }
            // ...to before this line
        }
    }
}

rootProject.name = "Money Goal"
include(":Money Goal App")
include(":data")
include(":domain")
include(":Health Goal App")
include(":feature:authentication")
