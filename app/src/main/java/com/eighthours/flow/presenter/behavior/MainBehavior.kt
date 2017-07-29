package com.eighthours.flow.presenter.behavior

import android.databinding.ObservableField
import android.view.View
import com.eighthours.flow.presenter.adapter.Page
import com.eighthours.flow.presenter.adapter.SectionsPagerAdapter
import com.eighthours.flow.presenter.utility.Formatter
import com.eighthours.flow.presenter.utility.property.CommandProperty
import com.eighthours.flow.presenter.utility.property.ObservableProperty
import com.eighthours.flow.utility.Disposer
import com.eighthours.flow.utility.ManagedDisposable
import org.threeten.bp.YearMonth

class MainBehavior(
        pager: SectionsPagerAdapter)
    : ManagedDisposable by Disposer() {

    val title = ObservableField<String>()

    val month = managed(ObservableProperty(YearMonth.now(),
            obsVisibility = pager.pageChanges.map {
                when (it) {
                    Page.PL_LIST -> View.VISIBLE
                    else -> View.GONE
                }
            }) { Formatter.format(it) })

    val createCashflow = managed(CommandProperty<View>())

    init {
        managed(pager.pageChanges.subscribe {
            title.set(Formatter.format(it))
        })
    }
}
