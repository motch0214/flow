package com.eighthours.flow.utility

import com.orhanobut.logger.FormatStrategy
import com.orhanobut.logger.LogcatLogStrategy
import com.orhanobut.logger.Logger

class SimpleFormatStrategy(
        private val defaultTag: String)
    : FormatStrategy {

    private val logStrategy = LogcatLogStrategy()

    override fun log(priority: Int, tag: String?, message: String) {
        val thread = Thread.currentThread().name
        logStrategy.log(priority, defaultTag, "[$thread] ${modifyMessage(message)}")
    }

    private fun trace(): String {
        val trace = Thread.currentThread().stackTrace.let { elements ->
            val index = elements.indexOfFirst { it.className == Logger::class.java.name } + 1
            elements[index]
        }
        val method = "${simpleClassName(trace)}#${trace.methodName}"
        return "$method (${trace.fileName}:${trace.lineNumber})"
    }

    private fun simpleClassName(element: StackTraceElement): String {
        val lastIndex = element.className.lastIndexOf(".")
        return element.className.substring(lastIndex + 1)
    }

    private fun modifyMessage(original: String): String {
        return if (original == "Empty/NULL log message")
            trace()
        else if (original.startsWith(" : "))
            original.substring(3)
        else
            "$original at ${trace()}"
    }
}
