object Dependencies {

    object Android {

        object Ktx {

            private const val version = "1.9.0"
            const val core = "androidx.core:core-ktx:$version"
        }

        object AppCompat {

            private const val version = "1.6.1"
            const val appCompat = "androidx.appcompat:appcompat:$version"
        }

        object Navigation {

            private const val version = "2.5.3"

            const val fragment = "androidx.navigation:navigation-fragment-ktx:$version"
            const val ui = "androidx.navigation:navigation-ui-ktx:$version"
        }
    }

    object Kotlin {

        object Coroutines {

            private const val version = "1.6.4"
            const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"
        }

        object DateTime {

            private const val version = "0.4.0"
            const val dateTime = "org.jetbrains.kotlinx:kotlinx-datetime:$version"
        }

        object Serialization {

            private const val version = "1.5.0"
            const val json = "org.jetbrains.kotlinx:kotlinx-serialization-json:$version"
        }
    }

    object Hilt {

        private const val version = "2.45"

        const val hilt = "com.google.dagger:hilt-android:$version"
        const val compiler = "com.google.dagger:hilt-compiler:$version"
    }

    object Interface {

        object Material {

            private const val version = "1.8.0"
            const val material = "com.google.android.material:material:$version"
        }

        object Constraint {

            private const val version = "2.1.4"
            const val constraint = "androidx.constraintlayout:constraintlayout:$version"
        }

        object ViewPager {

            private const val version = "1.0.0"
            const val viewPager = "androidx.viewpager2:viewpager2:$version"
        }

        object Coil {

            private const val version = "2.3.0"
            const val coil = "io.coil-kt:coil:$version"
        }
    }

    object Network {

        object Retrofit {

            private const val version = "2.9.0"
            const val retrofit = "com.squareup.retrofit2:retrofit:$version"

            object Scalars {

                const val converter = "com.squareup.retrofit2:converter-scalars:$version"
            }

            object Json {

                private const val version = "0.8.0"
                const val converter = "com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:$version"
            }
        }
    }

    object Persistence {

        object Room {

            private const val version = "2.5.1"

            const val runtime = "androidx.room:room-runtime:$version"
            const val ktx = "androidx.room:room-ktx:$version"
            const val compiler = "androidx.room:room-compiler:$version"
        }

        object DataStore {

            private const val version = "1.0.0"
            const val preferences = "androidx.datastore:datastore-preferences:$version"
        }
    }
}