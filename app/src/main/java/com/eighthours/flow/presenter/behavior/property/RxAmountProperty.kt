package com.eighthours.flow.presenter.behavior.property

import android.databinding.ObservableInt
import android.view.View
import com.eighthours.flow.domain.entity.Amount
import com.eighthours.flow.domain.entity.ZERO
import com.eighthours.flow.presenter.utility.AmountFormat
import com.eighthours.flow.presenter.utility.Formatter
import com.eighthours.flow.presenter.utility.property.RxProperty
import io.reactivex.Observable
import java.text.ParseException

class RxAmountProperty(
        initial: Amount? = null,
        obsVisibility: Observable<Int> = Observable.just(View.VISIBLE))
    : RxProperty<Amount>(initial, obsVisibility) {

    var length = ObservableInt()

    override fun onTextChanged(text: String?) {
        length.set(text?.length ?: 0)
        length.notifyChange()
    }

    override fun format(value: Amount): String? {
        return value.takeUnless { it == ZERO }?.let { Formatter.format(it) }
    }

    override fun parse(string: String?): Amount {
        return try {
            AmountFormat.parse(string) as Amount
        } catch (e: ParseException) {
            ZERO
        }
    }
}
