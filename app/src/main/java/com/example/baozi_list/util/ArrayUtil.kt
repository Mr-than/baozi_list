package com.example.baozi_list.util

fun <T> Array<T>.lastElement():T{
    val lastIndex = this.lastIndex
    return this[lastIndex]
}