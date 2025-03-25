package com.example.baozi_list.util

import java.lang.reflect.ParameterizedType


inline fun <reified E,TT : E> getGenericsClass(javaClass: Class<*>): Class<TT> {
    val actualTypeArguments = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments
    val vh =actualTypeArguments.map {
            it as? Class<*>
        }

    return vh.first {
        if (it != null) {
            E::class.java.isAssignableFrom(it)
        } else {
            false
        }
    } as Class<TT>
}


inline fun <reified E, TT : E> getGenericsObject(javaClass: Class<*>, arg: Map<Class<*>, Any?>): TT {

    val args = arg.keys.toTypedArray()
    val values = arg.values.toTypedArray()

    val declaredConstructor = getGenericsClass<E, TT>(javaClass).getDeclaredConstructor(*args)

    return declaredConstructor.newInstance(*values) as TT
}