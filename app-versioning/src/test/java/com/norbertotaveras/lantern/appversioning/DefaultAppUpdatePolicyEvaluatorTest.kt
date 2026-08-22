package com.norbertotaveras.lantern.appversioning

import com.norbertotaveras.lantern.core.SdkResult
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DefaultAppUpdatePolicyEvaluatorTest {
    private val evaluator = DefaultAppUpdatePolicyEvaluator()

    @Test
    fun evaluateReturnsForceUpdateWhenCurrentVersionIsBelowMinimumSupportedVersion() {
        val result = evaluator.evaluate(
            currentVersion = AppVersion(1, 0, 0),
            policy = AppUpdatePolicy(
                minimumSupportedVersion = AppVersion(1, 1, 0),
                latestVersion = AppVersion(1, 2, 0)
            )
        )

        assertTrue(result is SdkResult.Success)
        val state = (result as SdkResult.Success).data
        assertEquals(AppUpdateRequirement.ForceUpdate, state.requirement)
        assertTrue(state.isUpdateRequired)
    }

    @Test
    fun evaluateReturnsSoftUpdateWhenCurrentVersionIsBelowLatestVersion() {
        val result = evaluator.evaluate(
            currentVersion = AppVersion(1, 1, 0),
            policy = AppUpdatePolicy(
                minimumSupportedVersion = AppVersion(1, 0, 0),
                latestVersion = AppVersion(1, 2, 0)
            )
        )

        assertTrue(result is SdkResult.Success)
        val state = (result as SdkResult.Success).data
        assertEquals(AppUpdateRequirement.SoftUpdate, state.requirement)
        assertTrue(state.isUpdateRequired)
    }

    @Test
    fun evaluateReturnsNoUpdateWhenCurrentVersionIsLatest() {
        val result = evaluator.evaluate(
            currentVersion = AppVersion(1, 2, 0),
            policy = AppUpdatePolicy(
                minimumSupportedVersion = AppVersion(1, 0, 0),
                latestVersion = AppVersion(1, 2, 0)
            )
        )

        assertTrue(result is SdkResult.Success)
        val state = (result as SdkResult.Success).data
        assertEquals(AppUpdateRequirement.None, state.requirement)
        assertFalse(state.isUpdateRequired)
    }

    @Test
    fun evaluateRejectsPolicyWhenMinimumSupportedVersionIsNewerThanLatestVersion() {
        val result = evaluator.evaluate(
            currentVersion = AppVersion(1, 0, 0),
            policy = AppUpdatePolicy(
                minimumSupportedVersion = AppVersion(2, 0, 0),
                latestVersion = AppVersion(1, 9, 0)
            )
        )

        assertTrue(result is SdkResult.Failure)
        assertEquals(AppVersionErrorCodes.INVALID_POLICY, (result as SdkResult.Failure).error.code)
    }
}
