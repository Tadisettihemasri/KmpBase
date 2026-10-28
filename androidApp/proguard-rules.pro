# kotlinx.serialization, Ktor, Koin, Room and Coil ship their own consumer rules.
# Add app-specific keeps below (e.g. classes accessed via reflection).

# Ktor references JVM-only classes that do not exist on Android.
-dontwarn org.slf4j.impl.StaticLoggerBinder
-dontwarn java.lang.management.ManagementFactory
-dontwarn java.lang.management.RuntimeMXBean

# Keep line numbers for readable Crashlytics stack traces.
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile
