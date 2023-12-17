package com.example.hilt_validator

import com.google.devtools.ksp.symbol.KSClassDeclaration

sealed interface MatchResult {

    val declaration: KSClassDeclaration

    fun hasAnnotation(annotationName: String): Boolean {
        return declaration.annotations.any { it.shortName.asString() == annotationName }
    }

    data class Application(
        override val declaration: KSClassDeclaration
    ) : MatchResult

    data class Activity(
        override val declaration: KSClassDeclaration
    ) : MatchResult

    data class Fragment(
        override val declaration: KSClassDeclaration
    ) : MatchResult

    data class Service(
        override val declaration: KSClassDeclaration
    ) : MatchResult

    data class ViewModel(
        override val declaration: KSClassDeclaration
    ) : MatchResult

    data class BroadcastReceiver(
        override val declaration: KSClassDeclaration
    ) : MatchResult
}

object AndroidComponentMatcher {

    fun match(declaration: KSClassDeclaration): MatchResult? {

        if (declaration.annotations.map { it.shortName.asString() }.contains("Generated")) {
            // Hiltが生成したクラスは無視する
            return null
        }

        val superClassQualifiedNameList =
            declaration.superTypes.mapNotNull { it.resolve().declaration.qualifiedName?.asString() }

        superClassQualifiedNameList.forEach { superClassQualifiedName ->
            val matchResult = match(declaration, superClassQualifiedName)
            if (matchResult != null) {
                return matchResult
            }
        }

        return null
    }

    private fun match(
        declaration: KSClassDeclaration,
        superClassQualifiedName: String
    ): MatchResult? {
        return when (superClassQualifiedName) {
            "android.app.Application" -> MatchResult.Application(declaration)
            "androidx.activity.ComponentActivity" -> MatchResult.Activity(declaration)
            "androidx.fragment.app.Fragment" -> MatchResult.Fragment(declaration)
            "android.app.Service" -> MatchResult.Service(declaration)
            "androidx.lifecycle.ViewModel" -> MatchResult.ViewModel(declaration)
            "android.content.BroadcastReceiver" -> MatchResult.BroadcastReceiver(
                declaration
            )

            else -> null
        }
    }
}