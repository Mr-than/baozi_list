package com.example.baozi_list.util

fun <T> List<T>.lastElement():T{
    val lastIndex = this.lastIndex
    return this[lastIndex]
}