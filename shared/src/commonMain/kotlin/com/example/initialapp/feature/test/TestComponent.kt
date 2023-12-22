package com.example.initialapp.feature.test

import com.arkivanov.decompose.ComponentContext

interface TestComponent {
}

class TestComponentImpl(
    context: ComponentContext
) : TestComponent, ComponentContext by context {

}

