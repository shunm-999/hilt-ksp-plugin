package com.example.hilt_validator

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.validate

private const val HiltAppAnnotation = "HiltAndroidApp"
private const val AndroidEntryPointAnnotation = "AndroidEntryPoint"
private const val HiltViewModelAnnotation = "HiltViewModel"

class HiltValidateProcessor(
    private val logger: KSPLogger
) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val allFile = resolver.getAllFiles()
        val (classDeclarations, next) =
            allFile.map { it.declarations.filterIsInstance<KSClassDeclaration>() }.flatten()
                .partition { it.validate() }

        val matchResults = classDeclarations.mapNotNull { AndroidComponentMatcher.match(it) }

        matchResults.forEach { matchResult ->
            when (matchResult) {
                is MatchResult.Application -> {
                    if (!matchResult.hasAnnotation(HiltAppAnnotation)) {
                        logger.error(
                            "Application class must have @HiltAndroidApp annotation",
                            matchResult.declaration
                        )
                    }
                }

                is MatchResult.ViewModel -> {
                    if (!matchResult.hasAnnotation(HiltViewModelAnnotation)) {
                        logger.error(
                            "ViewModel class must have @HiltViewModel annotation",
                            matchResult.declaration
                        )
                    }
                }

                is MatchResult.Activity,
                is MatchResult.Fragment,
                is MatchResult.Service,
                is MatchResult.BroadcastReceiver -> {
                    if (!matchResult.hasAnnotation(AndroidEntryPointAnnotation)) {
                        logger.error(
                            "Activity, BroadcastReceiver, Fragment, Service class must have @AndroidEntryPoint annotation",
                            matchResult.declaration
                        )
                    }
                }
            }
        }

        return next
    }
}

class HiltValidateProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return HiltValidateProcessor(environment.logger)
    }
}