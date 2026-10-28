package com.a4b.kmpbase.core.common.log

import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity

object AppLogger {
    fun init(isDebug: Boolean) {
        Logger.setMinSeverity(if (isDebug) Severity.Verbose else Severity.Warn)
        Logger.setTag("KmpBase")
    }

    /**
     * Call inside every expensive Koin factory. On a cold start of an empty screen none of these
     * lines should appear — that is how we verify dependencies are created lazily.
     */
    fun lazyInit(name: String) {
        Logger.withTag("LazyInit").i { "$name created" }
    }
}
