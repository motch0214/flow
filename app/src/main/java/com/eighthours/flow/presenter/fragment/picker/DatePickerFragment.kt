package com.eighthours.flow.presenter.fragment.picker

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.widget.DatePicker
import com.eighthours.flow.presenter.utility.BundleKey
import com.eighthours.flow.presenter.utility.get
import com.eighthours.flow.presenter.utility.put
import com.orhanobut.logger.Logger
import org.threeten.bp.LocalDate

class DatePickerFragment()
    : DialogFragment() {

    companion object {
        val DATE = BundleKey<LocalDate>("DATE")
    }

    constructor(fragment: Fragment, initial: LocalDate) : this() {
        setTargetFragment(fragment, 0)
        arguments = Bundle().apply {
            put(DATE, initial)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        Logger.v("")
        val date = arguments.get(DATE) ?: LocalDate.now()
        return DatePickerDialog(activity, Listener(), date.year, date.monthValue - 1, date.dayOfMonth)
    }

    interface OnDateSetListener {
        fun onDateSet(date: LocalDate)
    }

    private inner class Listener
        : DatePickerDialog.OnDateSetListener {

        override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
            val date = LocalDate.of(year, month + 1, dayOfMonth)
            Logger.d("$date picked")
            (targetFragment as OnDateSetListener).onDateSet(date)
            dismiss()
        }
    }
}
