package com.free4developer.sampleserver.common

import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.stereotype.Component
import kotlin.reflect.KClass

@Component
object ApplicationContextProvider: ApplicationContextAware {

    private lateinit var applicationContext: ApplicationContext
    private val beanCaching = mutableMapOf<KClass<*>, Any>()

    override fun setApplicationContext(applicationContext: ApplicationContext) {
        this.applicationContext = applicationContext
    }

    fun <T: Any> getBean(kClass: KClass<T>): T =
        this.beanCaching[kClass]
            ?.let { it as T }
            ?: run {
                val bean = applicationContext.getBean(kClass.java)
                this.beanCaching[kClass] = bean
                bean
            }
}