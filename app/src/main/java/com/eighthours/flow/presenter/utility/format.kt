package com.eighthours.flow.presenter.utility

import android.content.Context
import com.eighthours.flow.R
import com.eighthours.flow.domain.entity.Amount
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth
import org.threeten.bp.format.DateTimeFormatter
import java.text.DecimalFormat

private fun <E : Enum<E>> Context.format(value: E): String {
    return getString(resources.getIdentifier("${value.javaClass.simpleName}.$value", "string", packageName))
}

object Formatter {

    private lateinit var context: Context

    private lateinit var localDate: DateTimeFormatter

    private lateinit var yearMonth: DateTimeFormatter


    fun init(context: Context) {
        this.context = context
        localDate = DateTimeFormatter.ofPattern(context.getString(R.string.format_local_date))
        yearMonth = DateTimeFormatter.ofPattern(context.getString(R.string.format_year_month))
    }

    fun format(it: LocalDate): String = localDate.format(it)

    fun format(it: YearMonth): String = yearMonth.format(it)

    fun format(it: Amount): String = AmountFormat.format(it)

    fun <E : Enum<E>> format(it: E): String = context.format(it)
}


val AmountFormat = DecimalFormat().apply {
    isParseBigDecimal = true
    groupingSize = 3
}
