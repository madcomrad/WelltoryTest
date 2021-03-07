package com.welltory.test.utils

fun <T> List<T>.setAt(element: T, index: Int = lastIndex): List<T> {
    return toMutableList().apply {
        set(index, element)
    }
}

fun <T> List<T>.addAt(element: T, index: Int = size): List<T> {
    return toMutableList().apply {
        add(index, element)
    }
}
