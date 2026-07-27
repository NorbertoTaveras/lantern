package com.norbertotaveras.mobilefoundationframework.screens

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.norbertotaveras.mobilefoundation.permissions.AndroidPermissionManager
import com.norbertotaveras.mobilefoundation.permissions.PermissionRequestLauncher
import com.norbertotaveras.mobilefoundation.permissions.PermissionState
import com.norbertotaveras.mobilefoundation.permissions.PermissionStatus
import com.norbertotaveras.mobilefoundation.permissions.SdkPermission
import com.norbertotaveras.mobilefoundationframework.components.DemoMetric
import com.norbertotaveras.mobilefoundationframework.components.DemoSection
import com.norbertotaveras.mobilefoundationframework.components.FeatureScreen
import com.norbertotaveras.mobilefoundationframework.components.InfoRow
import com.norbertotaveras.mobilefoundationframework.components.MetricRow
import com.norbertotaveras.mobilefoundationframework.components.PrimaryDemoButton
import com.norbertotaveras.mobilefoundationframework.components.SecondaryDemoButton
import com.norbertotaveras.mobilefoundationframework.components.StatusPill
import com.norbertotaveras.mobilefoundationframework.components.StatusMessage
import kotlin.coroutines.resume
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine

@Composable
fun PermissionsScreen() {
    val context = LocalContext.current
    val activity = context.findActivity()
    val coroutineScope = rememberCoroutineScope()
    val requestLauncher = rememberPermissionRequestLauncher()
    val permissionManager = remember(context, activity, requestLauncher) {
        AndroidPermissionManager(
            context = context,
            requestLauncher = requestLauncher,
            rationaleProvider = { manifestPermission ->
                activity?.let {
                    ActivityCompat.shouldShowRequestPermissionRationale(
                        it,
                        manifestPermission
                    )
                } ?: false
            }
        )
    }

    var states by remember { mutableStateOf(permissionManager.checkMultiple(demoPermissions.map { it.permission })) }
    var isLoading by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf<String?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    fun refreshStates() {
        states = permissionManager.checkMultiple(demoPermissions.map { it.permission })
    }

    fun requestPermissions(permissions: List<SdkPermission>) {
        coroutineScope.launch {
            isLoading = true
            message = null
            errorMessage = null

            val result = permissionManager.requestMultiple(permissions)
            states = states + result.states
            isLoading = false
            message = if (result.allGranted) {
                "Requested permissions granted."
            } else {
                "Permission request finished."
            }
            errorMessage = result.error?.message
        }
    }

    LaunchedEffect(permissionManager) {
        refreshStates()
    }

    FeatureScreen(
        title = "Permissions",
        subtitle = "Exercise the runtime permission resolver without adding UI code to the SDK module.",
        icon = Icons.Filled.PrivacyTip,
        status = "${states.values.count { it.isGranted }}/${demoPermissions.size} granted"
    ) {
        MetricRow(
            metrics = listOf(
                DemoMetric(label = "Granted", value = states.values.count { it.isGranted }.toString()),
                DemoMetric(label = "Tracked", value = demoPermissions.size.toString()),
                DemoMetric(label = "Common request", value = commonDemoPermissions.size.toString())
            )
        )

        DemoSection(
            title = "Permission controls",
            description = "Requests are launched by the sample app and resolved through the permissions SDK module.",
            leadingIcon = Icons.Filled.PrivacyTip
        ) {
            PrimaryDemoButton(
                text = "Request common demo permissions",
                icon = Icons.Filled.CheckCircle,
                enabled = !isLoading,
                onClick = {
                    requestPermissions(commonDemoPermissions)
                }
            )

            SecondaryDemoButton(
                text = "Refresh permission state",
                icon = Icons.Filled.Refresh,
                enabled = !isLoading,
                onClick = {
                    refreshStates()
                    message = "Permission state refreshed."
                    errorMessage = null
                }
            )
        }

        DemoSection(
            title = "Live permission state",
            description = "Each row checks the current app grant state and can request its mapped Android permission.",
            leadingIcon = Icons.Filled.CheckCircle
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                demoPermissions.forEach { item ->
                    PermissionDemoRow(
                        item = item,
                        state = states[item.permission],
                        enabled = !isLoading,
                        onRequest = {
                            requestPermissions(listOf(item.permission))
                        }
                    )
                }
            }
        }

        DemoSection(
            title = "Resolver behavior",
            description = "The module maps SDK permission names to platform permissions based on Android version.",
            leadingIcon = Icons.Filled.PrivacyTip
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                InfoRow(label = "SDK module", value = "permissions")
                InfoRow(label = "Request owner", value = "Sample app")
                InfoRow(label = "Compose in SDK", value = "None")
                InfoRow(label = "Android resolver", value = "Live")
            }
        }

        StatusMessage(
            message = message,
            errorMessage = errorMessage
        )
    }
}

@Composable
private fun PermissionDemoRow(
    item: DemoPermissionItem,
    state: PermissionState?,
    enabled: Boolean,
    onRequest: () -> Unit
) {
    OutlinedCard(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(28.dp)
                )

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.titleSmall
                    )

                    Text(
                        text = item.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    StatusPill(text = state.statusLabel())
                }
            }

            SecondaryDemoButton(
                text = "Request",
                icon = null,
                enabled = enabled && state?.status != PermissionStatus.Granted,
                onClick = onRequest
            )
        }
    }
}

@Composable
private fun rememberPermissionRequestLauncher(): PermissionRequestLauncher {
    var continuation by remember {
        mutableStateOf<CancellableContinuation<Map<String, Boolean>>?>(null)
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        continuation?.resume(result)
        continuation = null
    }

    return remember(launcher) {
        PermissionRequestLauncher { manifestPermissions ->
            suspendCancellableCoroutine { nextContinuation ->
                continuation?.cancel(
                    CancellationException("A new permission request was started.")
                )
                continuation = nextContinuation
                nextContinuation.invokeOnCancellation {
                    continuation = null
                }
                launcher.launch(manifestPermissions.toTypedArray())
            }
        }
    }
}

private fun PermissionState?.statusLabel(): String {
    val status = this?.status ?: PermissionStatus.NotDetermined
    val label = when (status) {
        PermissionStatus.Granted -> "Granted"
        PermissionStatus.Denied -> "Denied"
        PermissionStatus.PermanentlyDenied -> "Permanently denied"
        PermissionStatus.NotDetermined -> "Not determined"
        PermissionStatus.Unsupported -> "Unsupported"
    }

    return if (this?.shouldShowRationale == true) {
        "$label - rationale"
    } else {
        label
    }
}

private tailrec fun Context.findActivity(): Activity? {
    return when (this) {
        is Activity -> this
        is ContextWrapper -> baseContext.findActivity()
        else -> null
    }
}

private data class DemoPermissionItem(
    val permission: SdkPermission,
    val title: String,
    val description: String,
    val icon: ImageVector
)

private val demoPermissions = listOf(
    DemoPermissionItem(
        permission = SdkPermission.Camera,
        title = "Camera",
        description = "Maps to android.permission.CAMERA.",
        icon = Icons.Filled.CameraAlt
    ),
    DemoPermissionItem(
        permission = SdkPermission.FineLocation,
        title = "Fine location",
        description = "Maps to precise foreground location.",
        icon = Icons.Filled.LocationOn
    ),
    DemoPermissionItem(
        permission = SdkPermission.Microphone,
        title = "Microphone",
        description = "Maps to audio recording permission.",
        icon = Icons.Filled.Mic
    ),
    DemoPermissionItem(
        permission = SdkPermission.Notifications,
        title = "Notifications",
        description = "Required on Android 13 and newer.",
        icon = Icons.Filled.Notifications
    ),
    DemoPermissionItem(
        permission = SdkPermission.ReadMediaImages,
        title = "Media images",
        description = "Uses Android 13 media permission or legacy storage read.",
        icon = Icons.Filled.Image
    ),
    DemoPermissionItem(
        permission = SdkPermission.BluetoothConnect,
        title = "Bluetooth connect",
        description = "Runtime permission on Android 12 and newer.",
        icon = Icons.Filled.Bluetooth
    ),
    DemoPermissionItem(
        permission = SdkPermission.Contacts,
        title = "Contacts",
        description = "Maps to read contacts permission.",
        icon = Icons.Filled.Contacts
    )
)

private val commonDemoPermissions = listOf(
    SdkPermission.Camera,
    SdkPermission.FineLocation,
    SdkPermission.Microphone,
    SdkPermission.Notifications,
    SdkPermission.ReadMediaImages
)
