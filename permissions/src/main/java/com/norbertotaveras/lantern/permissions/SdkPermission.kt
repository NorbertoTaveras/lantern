package com.norbertotaveras.lantern.permissions

/**
 * SDK-level runtime permissions resolved to Android manifest permissions by platform version.
 */
enum class SdkPermission {
    /** Camera access. */
    Camera,

    /** Precise foreground location access. */
    FineLocation,

    /** Approximate foreground location access. */
    CoarseLocation,

    /** Background location access on Android versions that require it at runtime. */
    BackgroundLocation,

    /** Microphone recording access. */
    Microphone,

    /** Notification posting access on Android versions that require it at runtime. */
    Notifications,

    /** Nearby Bluetooth scan access on Android versions that require it at runtime. */
    BluetoothScan,

    /** Nearby Bluetooth connect access on Android versions that require it at runtime. */
    BluetoothConnect,

    /** Nearby Bluetooth advertise access on Android versions that require it at runtime. */
    BluetoothAdvertise,

    /** Read contacts access. */
    Contacts,

    /** Read image media access. */
    ReadMediaImages,

    /** Read video media access. */
    ReadMediaVideo,

    /** Read audio media access. */
    ReadMediaAudio
}
