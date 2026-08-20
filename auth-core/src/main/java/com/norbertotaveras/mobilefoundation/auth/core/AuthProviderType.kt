package com.norbertotaveras.mobilefoundation.auth.core

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
