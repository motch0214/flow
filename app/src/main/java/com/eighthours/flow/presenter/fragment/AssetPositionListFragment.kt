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
import com.eighthours.flow.domain.entity.Position
import com.eighthours.flow.presenter.adapter.PositionListAdapter
import com.eighthours.flow.presenter.behavior.AssetPositionListBehavior
import com.eighthours.flow.presenter.utility.FragmentAccessor
import com.eighthours.flow.presenter.utility.Menu
import com.eighthours.flow.presenter.viewmodel.AccountViewModel
import com.eighthours.flow.presenter.viewmodel.AssetPositionViewModel
import com.eighthours.flow.utility.ManagedDisposable
import com.eighthours.flow.utility.MultiTimeDisposer
import com.orhanobut.logger.Logger

class AssetPositionListFragment
    : Fragment(), ManagedDisposable by MultiTimeDisposer(), FragmentAccessor {

    private lateinit var accountVm: AccountViewModel

    private lateinit var vm: AssetPositionViewModel

    private lateinit var behavior: AssetPositionListBehavior

    override fun onCreate(savedInstanceState: Bundle?) {
        Logger.v("")
        super.onCreate(savedInstanceState)
        ViewModelProviders.of(activity, component.vmFactory()).let {
            accountVm = it.get(AccountViewModel::class.java)
            vm = it.get(AssetPositionViewModel::class.java)
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
        behavior = managed(AssetPositionListBehavior(adapter, vm, accountVm))

        val binding = FragmentPositionListBinding.bind(view)

        binding.list.adapter = adapter
        binding.list.layoutManager = LinearLayoutManager(activity)

        // Menu
        managed(behavior.menu.subscribe { (view, pos) ->
            val anchor = view.findViewById(R.id.menu)
            Menu(activity, anchor, R.menu.menu_item_position,
                    R.id.action_reconcile to { openReconcile(pos) }
            ).show()
        })
    }

    private fun openReconcile(position: Position) {
        val fragment = ReconcileFragment(position)
        fragment.show(fragmentManager, "reconcile")
    }

    override fun onDestroyView() {
        Logger.v("")
        super.onDestroyView()
        dispose()
    }
}
