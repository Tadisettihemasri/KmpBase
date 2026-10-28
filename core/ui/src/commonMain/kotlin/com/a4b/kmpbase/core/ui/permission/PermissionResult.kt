package com.a4b.kmpbase.core.ui.permission

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import dev.icerock.moko.permissions.DeniedAlwaysException
import dev.icerock.moko.permissions.DeniedException
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.RequestCanceledException
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory

enum class PermissionResult { Granted, Denied, DeniedAlways }

/** Controller bound to the current screen's lifecycle. Create it in the screen that asks. */
@Composable
fun rememberPermissionsController(): PermissionsController {
    val factory = rememberPermissionsControllerFactory()
    val controller = remember(factory) { factory.createPermissionsController() }
    BindEffect(controller)
    return controller
}

/** On [PermissionResult.DeniedAlways], offer `controller.openAppSettings()`. */
suspend fun PermissionsController.request(permission: Permission): PermissionResult = try {
    providePermission(permission)
    PermissionResult.Granted
} catch (_: DeniedAlwaysException) {
    PermissionResult.DeniedAlways
} catch (_: DeniedException) {
    PermissionResult.Denied
} catch (_: RequestCanceledException) {
    PermissionResult.Denied
}
