package com.nino.blindbox.annotation

@MustBeDocumented
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS)
annotation class ActivityOption(
    val reqLogin: Boolean = false, //是否需要登录
)