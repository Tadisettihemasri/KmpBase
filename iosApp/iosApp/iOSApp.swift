import SwiftUI
import ComposeApp

@main
struct iOSApp: App {
    init() {
        // Registers Koin definitions only; every dependency is created lazily on first use.
        KoinIosKt.startKoinIos()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
