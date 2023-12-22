package com.example.initialapp.feature.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.popTo
import com.arkivanov.decompose.value.Value
import com.example.initialapp.feature.canvas.component.CanvasComponent
import com.example.initialapp.feature.canvas.component.CanvasComponentImpl
import com.example.initialapp.feature.root.RootComponent.Child
import com.example.initialapp.feature.test.TestComponent
import com.example.initialapp.feature.test.TestComponentImpl
import kotlinx.serialization.Serializable

interface RootComponent {
    val stack: Value<ChildStack<*, Child>>

    fun onBackClicked(toIndex: Int)

    fun onCanvasButtonClick()
    fun onTest1ButtonClick()
    fun onTest2ButtonClick()

    sealed class Child {
        class CanvasChild(val component: CanvasComponent) : Child()
        class Test1Child(val component: TestComponent) : Child()
        class Test2Child(val component: TestComponent) : Child()
    }
}

class RootComponentImpl(
    context: ComponentContext
): ComponentContext by context, RootComponent {
    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, Child>> =
        childStack(
            source = navigation,
            serializer = Config.serializer(),
            initialStack = { listOf(Config.Canvas) },
            childFactory = ::createChild,
        )

    private fun createChild(config: Config, childComponentContext: ComponentContext): Child =
        when (config) {
            is Config.Canvas -> Child.CanvasChild(CanvasComponentImpl(childComponentContext))
            is Config.Test1 -> Child.Test1Child(TestComponentImpl(childComponentContext))
            is Config.Test2 -> Child.Test2Child(TestComponentImpl(childComponentContext))
        }

    override fun onBackClicked(toIndex: Int) {
        navigation.popTo(index = toIndex)
    }

    override fun onCanvasButtonClick() {
        navigation.bringToFront(Config.Canvas)
    }

    override fun onTest1ButtonClick() {
        navigation.bringToFront(Config.Test1)
    }

    override fun onTest2ButtonClick() {
        navigation.bringToFront(Config.Test2)
    }

    @Serializable
    private sealed interface Config {
        @Serializable
        data object Canvas: Config
        @Serializable
        data object Test1: Config
        @Serializable
        data object Test2: Config
    }
}

