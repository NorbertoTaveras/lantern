package com.norbertotaveras.lantern.permissions.internal

import com.norbertotaveras.lantern.permissions.PermissionStatus
import com.norbertotaveras.lantern.permissions.SdkPermission
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PermissionMapperTest {

    @Test
    fun `fixed status bypasses declaration and grant checks`() {
        val mapper = PermissionMapper(
            isGranted = { false },
            isDeclared = { false }
        )

        val state = mapper.toState(
            ResolvedPermission(
                permission = SdkPermission.Notifications,
                manifestPermissions = emptyList(),
                fixedStatus = PermissionStatus.Granted
            )
        )

        assertEquals(SdkPermission.Notifications, state.permission)
        assertEquals(PermissionStatus.Granted, state.status)
        assertTrue(state.isGranted)
        assertFalse(state.shouldShowRationale)
    }

    @Test
    fun `empty resolution without fixed status is unsupported`() {
        val mapper = PermissionMapper(
            isGranted = { true },
            isDeclared = { true }
        )

        val state = mapper.toState(
            ResolvedPermission(
                permission = SdkPermission.Camera,
                manifestPermissions = emptyList()
            )
        )

        assertEquals(PermissionStatus.Unsupported, state.status)
        assertFalse(state.isGranted)
    }

    @Test
    fun `missing manifest declaration returns denied`() {
        val mapper = PermissionMapper(
            isGranted = { true },
            isDeclared = { false },
            shouldShowRationale = { true }
        )

        val state = mapper.toState(cameraResolution)

        assertEquals(PermissionStatus.Denied, state.status)
        assertTrue(state.shouldShowRationale)
    }

    @Test
    fun `declared and currently granted permission returns granted`() {
        val mapper = PermissionMapper(
            isGranted = { true },
            isDeclared = { true }
        )

        val state = mapper.toState(cameraResolution)

        assertEquals(PermissionStatus.Granted, state.status)
        assertTrue(state.isGranted)
    }

    @Test
    fun `grant results override current permission checks`() {
        val mapper = PermissionMapper(
            isGranted = { false },
            isDeclared = { true }
        )

        val state = mapper.toState(
            resolution = cameraResolution,
            grantResults = mapOf(android.Manifest.permission.CAMERA to true)
        )

        assertEquals(PermissionStatus.Granted, state.status)
    }

    @Test
    fun `denied grant result without rationale returns permanently denied`() {
        val mapper = PermissionMapper(
            isGranted = { false },
            isDeclared = { true },
            shouldShowRationale = { false }
        )

        val state = mapper.toState(
            resolution = cameraResolution,
            grantResults = mapOf(android.Manifest.permission.CAMERA to false)
        )

        assertEquals(PermissionStatus.PermanentlyDenied, state.status)
        assertFalse(state.shouldShowRationale)
    }

    @Test
    fun `denied grant result with rationale returns denied`() {
        val mapper = PermissionMapper(
            isGranted = { false },
            isDeclared = { true },
            shouldShowRationale = { true }
        )

        val state = mapper.toState(
            resolution = cameraResolution,
            grantResults = mapOf(android.Manifest.permission.CAMERA to false)
        )

        assertEquals(PermissionStatus.Denied, state.status)
        assertTrue(state.shouldShowRationale)
    }

    @Test
    fun `missing grant result does not infer permanently denied`() {
        val mapper = PermissionMapper(
            isGranted = { false },
            isDeclared = { true },
            shouldShowRationale = { false }
        )

        val state = mapper.toState(
            resolution = cameraResolution,
            grantResults = emptyMap()
        )

        assertEquals(PermissionStatus.Denied, state.status)
        assertFalse(state.shouldShowRationale)
    }

    @Test
    fun `any denied manifest permission makes multi-permission state denied`() {
        val mapper = PermissionMapper(
            isGranted = { permission ->
                permission == android.Manifest.permission.ACCESS_FINE_LOCATION
            },
            isDeclared = { true }
        )

        val state = mapper.toState(
            ResolvedPermission(
                permission = SdkPermission.FineLocation,
                manifestPermissions = listOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        )

        assertEquals(PermissionStatus.Denied, state.status)
    }

    private val cameraResolution = ResolvedPermission(
        permission = SdkPermission.Camera,
        manifestPermissions = listOf(android.Manifest.permission.CAMERA)
    )
}
