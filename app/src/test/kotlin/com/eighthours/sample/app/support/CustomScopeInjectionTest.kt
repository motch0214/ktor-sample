package com.eighthours.sample.app.support

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.koin.core.context.KoinContextHandler
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.definition.BeanDefinition
import org.koin.core.error.NoBeanDefFoundException
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.TypeQualifier
import org.koin.dsl.ScopeDSL
import org.koin.dsl.module
import org.koin.ext.getOrCreateScope
import org.koin.ext.getScopeId
import org.koin.test.KoinTest

interface CustomScope

inline fun <reified T> CustomScope.get(): T =
    KoinContextHandler.get().getScope(getScopeId()).get { parametersOf(this) }

inline fun <reified T> ScopeDSL.define(): BeanDefinition<T> {
    return factory { (scope: CustomScope) ->
        T::class.constructors.iterator().next()
            .call(get<CustomScopeInjectionTest.Injected>(TypeQualifier(scope::class)))
    }
}

class CustomScopeInjectionTest : KoinTest {

    object ParentScope : CustomScope

    object ChildScope : CustomScope

    class Injected(val name: String)

    class ParentComponent(val value: Injected) {
        override fun toString() = "Parent(name=${value.name})"
    }

    class ChildComponent(val value: Injected) {
        override fun toString() = "Child(name=${value.name})"
    }

    @Test
    fun test() {
        startKoin {
            modules(module {
                single(createdAtStart = true) {
                    ChildScope.getOrCreateScope().also { it.linkTo(ParentScope.getOrCreateScope()) }
                }
                single(TypeQualifier(ParentScope::class)) { Injected("parent") }
                single(TypeQualifier(ChildScope::class)) { Injected("child") }

                scope<ParentScope> {
                    define<ParentComponent>()
                }
                scope<ChildScope> {
                    define<ChildComponent>()
                }
            })
        }

        assertThat(ParentScope.get<ParentComponent>().toString())
            .isEqualTo("Parent(name=parent)")
        assertThat(ChildScope.get<ChildComponent>().toString())
            .isEqualTo("Child(name=child)")

        assertThat(ParentScope.get<ParentComponent>().value)
            .isSameAs(ParentScope.get<ParentComponent>().value)

        assertThat(ChildScope.get<ParentComponent>().toString())
            .isEqualTo("Parent(name=child)")
        assertThrows<NoBeanDefFoundException> {
            ParentScope.get<ChildComponent>()
        }
    }

    @AfterEach
    fun close() {
        stopKoin()
    }
}
