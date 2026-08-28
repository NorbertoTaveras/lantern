/*
 * Copyright (C) 2026 Norberto Taveras
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
