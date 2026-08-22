# Permissions

`permissions` provides Android-version-aware runtime permission checks and requests.

```kotlin
implementation("com.norbertotaveras.lantern:lantern-permissions:$lanternVersion")
```

## Use It For

- Checking runtime permission state.
- Requesting one or more permissions.
- Mapping SDK permission types to Android manifest permissions.
- Modeling granted, denied, rationale, and permanently denied states.
- Handling Android API differences for notifications, media, Bluetooth, camera, microphone, location, and contacts.

## Basic Usage

```kotlin
val permissionManager = AndroidPermissionManager(
    context = context,
    requestLauncher = permissionRequestLauncher,
    rationaleProvider = PermissionRationaleProvider { manifestPermission ->
        activity.shouldShowRequestPermissionRationale(manifestPermission)
    }
)

val cameraState = permissionManager.check(SdkPermission.Camera)
val result = permissionManager.request(SdkPermission.Camera)
```

## Request Launcher Boundary

The app provides `PermissionRequestLauncher` because permission dialogs are lifecycle-bound UI work.

```kotlin
val launcher = PermissionRequestLauncher { manifestPermissions ->
    requestPermissionsFromActivityResultLauncher(manifestPermissions)
}
```

The SDK handles normalization, permission mapping, and result modeling around that app-owned launcher.

## Multiple Permissions

```kotlin
val result = permissionManager.requestMultiple(
    listOf(
        SdkPermission.Camera,
        SdkPermission.Microphone
    )
)
```

Duplicate SDK permissions are treated as one logical request.

## Boundaries

The permissions module does not own UI prompts beyond invoking the app-provided launcher. Compose and Activity Result registration remain in the app.
