# KmpBase

Kotlin Multiplatform (Android + iOS) starter with Compose Multiplatform. Networking, images, pagination, database, preferences, DI, navigation, logging, permissions, crash reporting, tests and lint are already wired in. You build features on top of it.

## Requirements
- JDK 17, Android Studio (Meerkat or newer) with the Kotlin Multiplatform plugin
- Xcode 16+ for iOS
- `local.properties` with `sdk.dir=...` (Android Studio creates it)

No secrets are needed to build.

## Run
| What | Command |
|---|---|
| Android debug APK | `./gradlew :androidApp:assembleDebug` |
| Android release (R8 + resource shrinking) | `./gradlew :androidApp:assembleRelease` |
| iOS | Open `iosApp/iosApp.xcodeproj` in Xcode and Run. The Gradle framework build runs automatically. |
| iOS framework only | `./gradlew :composeApp:linkDebugFrameworkIosSimulatorArm64` |
| All tests | `./gradlew allTests testAndroidHostTest` |
| Lint | `./gradlew detekt` |
| Prod environment | add `-Pbuildkonfig.flavor=prod` (values are in `composeApp/build.gradle.kts`) |

## Modules
```
build-logic/        Convention plugins: kmpbase.kmp.library / .kmp.compose / .kmp.feature
androidApp/         Android application: Application, MainActivity, Chucker, Firebase (opt-in)
composeApp/         Shared app root: App(), AppNavHost, initKoin, iOS MainViewController
iosApp/             Xcode project (SwiftUI host)
core/common         AppResult/AppError, AppDispatchers, AppConfig, Kermit logger, telemetry interfaces
core/network        Ktor HttpClient (OkHttp / Darwin), JSON, auth, timeouts, safeApiCall
core/database       Room KMP + bundled SQLite, generic CacheDao
core/datastore      Preferences DataStore + typed AppPreferences
core/designsystem   AppTheme, spacing, Coil ImageLoader, AppImage, Loading/Error/Empty views, PagingList
core/ui             UiState, AppError.userMessage(), ObserveAsEvents, permissions (moko)
features/           Your feature modules
```

## Library stack
Versions live in `gradle/libs.versions.toml`. The baseline matches SMApp: Kotlin 2.3.20, CMP 1.10.3, AGP 8.13.2, KSP 2.3.7, Ktor 3.4.2, Koin 4.2.1, Coil 3.4.0, Paging 3.4.2, Room 2.8.4 and DataStore 1.2.1. Kermit, BuildKonfig, moko-permissions and Turbine are added on top.

**Catalog-only libraries** are declared but not used by any module yet: Media3/ExoPlayer, Rive, Markdown renderer, YouTube player, Compottie (Lottie), Coil video, moko camera/location/gallery, and Firebase Perf. A catalog entry costs nothing. To use one, add `implementation(libs.xxx)` in the module that needs it.

Rive, for example, ships native `.so` files that R8 can't strip. It would add MBs to every APK even if no screen used it.

**Toolchain upgrades:** bump Kotlin, CMP, KSP, AGP and Room together in one PR. They have to be compatible with each other.

R8 is pinned in the root `build.gradle.kts` because AGP 8.13's bundled R8 can't read Kotlin 2.3 metadata. Remove that pin when AGP is upgraded.

## Startup rules (lazy initialization)
`Application.onCreate` and iOS `init` only **register** dependencies. Nothing heavy is created at launch.
- Koin `single {}` definitions are lazy, and the codebase never uses `createdAtStart = true`. Feature modules go into `featureModules` in `composeApp/.../di/KoinInit.kt` as `lazyModule { }`, which loads them off the main thread.
- The `HttpClient`, Room database, DataStore and Coil `ImageLoader` are each built on first use. Every factory calls `AppLogger.lazyInit("Name")`, so `adb logcat | grep LazyInit` shows exactly what was created and when. On a cold start of an empty screen there are no such lines.
- Remote Config is fetched after the first frame (see `App()`).
- On Android, the emoji2 startup initializer is removed. Chucker and LeakCanary are debug-only, and release builds use Chucker's no-op.
- Each module depends only on what it uses (`implementation` over `api`). Android-only libraries go in `androidMain` so they never reach the iOS framework.

## Adding a feature
1. `features/home/build.gradle.kts`:
   ```kotlin
   plugins { alias(libs.plugins.kmpbase.kmp.feature) }
   kotlin { sourceSets { commonMain.dependencies { implementation(projects.core.network) } } }
   ```
   The feature plugin adds core:common, core:ui, core:designsystem, Koin, navigation and serialization. Add data modules (network, database, datastore) only when the feature needs them.
2. In `settings.gradle.kts`: `include(":features:home")`. In `composeApp/build.gradle.kts`: `implementation(projects.features.home)`.
3. In the feature, declare `@Serializable data object HomeRoute`, a `NavGraphBuilder.homeScreen()` extension, and `val homeModule = lazyModule { viewModelOf(::HomeViewModel); singleOf(::HomeRepository) }`.
4. Register `homeModule` in `featureModules`, and call `homeScreen()` from `AppNavHost`.

Patterns to use:
- **API calls:** `safeApiCall { client.get("path").body<Dto>() }` returns `AppResult`. Map it with `toUiState()`, and show errors with `error.userMessage()`.
- **Paged lists:** a `PagingSource` with `Pager(...).flow.cachedIn(viewModelScope)` in the ViewModel. In the UI, `collectAsLazyPagingItems()` feeds `PagingList(...)`.
- **Images:** always use `AppImage(model = url, ...)`.
- **One-off events:** a `Channel` in the ViewModel, collected with `ObserveAsEvents(vm.events) { ... }`.
- **Permissions:** `val pc = rememberPermissionsController()` then `scope.launch { pc.request(Permission.REMOTE_NOTIFICATION) }`.

## Firebase (optional)
Firebase belongs to **this app's own Firebase project**. It is not shared with SMApp.
1. In the Firebase console, create or select a project. Add an Android app with package `com.a4b.kmpbase`, and an iOS app with bundle id `com.a4b.kmpbase`.
2. Put `google-services.json` in `androidApp/`. It is gitignored, so CI should inject it.
   - The build then applies the google-services and Crashlytics plugins, adds the Firebase SDKs, and compiles `androidApp/src/firebase/`. That directory overrides the no-op `CrashReporter`, `AnalyticsTracker`, `RemoteConfig` and `PushTokenProvider` with Firebase versions.
   - Without the file, the app ships zero Firebase code.
3. Lazy defaults (set in the manifest):
   - Crashlytics starts at launch so it can catch crashes during startup.
   - Analytics collection is off until you call `AnalyticsTracker.setCollectionEnabled(true)`.
   - FCM auto-init is off. Call `PushTokenProvider.token()` after notification permission is granted.
4. iOS: add the Firebase SDK in Xcode via Swift Package Manager, along with `GoogleService-Info.plist`. Then implement the telemetry interfaces in Swift, or with a KMP Firebase wrapper, and bind them in a platform Koin module.

## Renaming for a new app
1. Change `applicationId` and `namespace` in `androidApp/build.gradle.kts`.
2. Change `PRODUCT_NAME` and `PRODUCT_BUNDLE_IDENTIFIER` in `iosApp/Configuration/Config.xcconfig`.
3. Change `rootProject.name` in `settings.gradle.kts` and the `com.a4b.kmpbase` prefix in `build-logic/.../ProjectExtensions.kt`.
4. Move source packages with Android Studio's refactor tool.
5. Replace the placeholder launcher icon, the iOS AppIcon and the colors in `core/designsystem/.../theme/Color.kt`.

## Release signing
Create a gitignored `keystore.properties` at the repo root with `storeFile`, `storePassword`, `keyAlias` and `keyPassword`. Release builds then sign automatically. Without the file, release builds are unsigned.

## Compose compiler reports
`./gradlew assembleRelease -PcomposeCompilerReports=true` writes the reports to each module's `build/compose_reports`. Stable types are configured in `compose_stability.conf`.
