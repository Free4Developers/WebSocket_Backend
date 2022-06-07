package com.free4developer.sampleserver.helper

import java.util.*
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse

fun HttpServletResponse.setCookie(name: String, value: String, expireDate: Date) {
    val cookie = Cookie(name, value)
    cookie.isHttpOnly = true
    cookie.maxAge = (expireDate.time - Date(System.currentTimeMillis()).time).toInt() / 1000

    this.addCookie(cookie)
}