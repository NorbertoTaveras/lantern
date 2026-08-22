package com.norbertotaveras.lantern.auth.core

/**
 * Provider that produced an [AuthSession].
 */
enum class AuthProviderType {
    Firebase,
    Google,
    FirebaseGoogle,
    EmailPassword,
    Anonymous
}
