package com.norbertotaveras.mobilefoundation.core

@Deprecated(
    message = "Use Environment instead.",
    replaceWith = ReplaceWith("Environment")
)
enum class Enviroment {
    Development,
    Staging,
    Production;

    fun toEnvironment(): Environment {
        return when (this) {
            Development -> Environment.Development
            Staging -> Environment.Staging
            Production -> Environment.Production
        }
    }
}
