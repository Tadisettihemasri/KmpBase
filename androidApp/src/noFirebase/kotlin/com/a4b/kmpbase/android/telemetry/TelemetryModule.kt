package com.a4b.kmpbase.android.telemetry

import org.koin.core.module.Module
import org.koin.dsl.module

/** Firebase not configured: keep core:common's no-op telemetry. */
internal fun telemetryModule(): Module = module { }
