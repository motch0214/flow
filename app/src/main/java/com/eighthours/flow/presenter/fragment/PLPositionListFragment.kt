package com.eighthours.flow.presenter.fragment

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eighthours.flow.R
import com.eighthours.flow.databinding.FragmentPositionListBinding
import com.eighthours.flow.presenter.activity.MainActivity
import com.eighthours.flow.presenter.adapter.PositionListAdapter
import com.eighthours.flow.presenter.behavior.MainBehavior
import com.eighthours.flow.presenter.behavior.PLPositionListBehavior
import com.eighthours.flow.presenter.fragment.picker.DatePickerFragment
import com.eighthours.flow.presenter.utility.FragmentAccessor
import com.eighthours.flow.presenter.viewmodel.AccountViewModel
import com.eighthours.flow.presenter.viewmodel.PLPositionViewModel
import com.eighthours.flow.utility.ManagedDisposable
import com.eighthours.flow.utility.MultiTimeDisposer
import com.orhanobut.logger.Logger
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth

class PLPositionListFragment
    : Fragment(), ManagedDisposable by MultiTimeDisposer(), FragmentAccessor,
        DatePickerFragment.OnDateSetListener {

    private lateinit var accountVm: AccountViewModel

    private lateinit var vm: PLPositionViewModel

    private lateinit var behavior: PLPositionListBehavior

    private lateinit var mainBehavior: MainBehavior

    override fun onCreate(savedInstanceState: Bundle?) {
        Logger.v("")
        super.onCreate(savedInstanceState)
        ViewModelProviders.of(activity, component.vmFactory()).let {
            accountVm = it.get(AccountViewModel::class.java)
            vm = it.get(PLPositionViewModel::class.java)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Logger.v("")
        return inflater.inflate(R.layout.fragment_position_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Logger.v("")
        super.onViewCreated(view, savedInstanceState)

        val adapter = PositionListAdapter(activity)
        behavior = managed(PLPositionListBehavior(adapter, vm, accountVm))
        mainBehavior = (activity as MainActivity).behavior

        val binding = FragmentPositionListBinding.bind(view)

        binding.list.adapter = adapter
        binding.list.layoutManager = LinearLayoutManager(activity)

        // Month picker
        mainBehavior.month.command.subscribe {
            val fragment = DatePickerFragment(this, mainBehavior.month.get()!!.toDate())
            fragment.show(fragmentManager, "datePicker")
        }
    }

    private fun YearMonth.toDate(): LocalDate {
        return LocalDate.of(year, month, 1)
    }

    override fun onDateSet(date: LocalDate) {
        val month = YearMonth.of(date.year, date.month)
        vm.changeMonth(month)
        mainBehavior.month.update(month)
    }

    override fun onDestroyView() {
        Logger.v("")
        super.onDestroyView()
        dispose()
    }
}
