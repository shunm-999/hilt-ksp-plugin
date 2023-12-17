package com.example.hilt_validator

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration

class HiltValidateProcessor(
    private val logger: KSPLogger
) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val androidComponentNames = listOf(
            "android.app.Activity",
            "android.app.Service",
            "androidx.lifecycle.ViewModel",
            "androidx.fragment.app.Fragment",
            "android.content.BroadcastReceiver"
        )
        val androidComponentClasses = buildList {
            resolver.getAllFiles().forEach { file ->
                file.declarations.filterIsInstance<KSClassDeclaration>().forEach { declaration ->
                    declaration.superTypes.forEach { superType ->
                        if (androidComponentNames.contains(superType.resolve().declaration.qualifiedName?.asString())) {
                            logger.warn("Found Android component: ${declaration.qualifiedName?.asString()}")
                            add(declaration)
                        }
                    }
                }
            }
        }

        return emptyList()
    }
}

class HiltValidateProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return HiltValidateProcessor(environment.logger)
    }
}