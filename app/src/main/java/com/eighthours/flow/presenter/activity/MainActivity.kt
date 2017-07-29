package com.eighthours.flow.presenter.activity

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.eighthours.flow.R
import com.eighthours.flow.databinding.ActivityMainBinding
import com.eighthours.flow.presenter.adapter.SectionsPagerAdapter
import com.eighthours.flow.presenter.behavior.MainBehavior
import com.eighthours.flow.presenter.fragment.CashflowFragment
import com.eighthours.flow.presenter.fragment.EditAccountFragment
import com.eighthours.flow.utility.ManagedDisposable
import com.eighthours.flow.utility.MultiTimeDisposer
import com.orhanobut.logger.Logger

class MainActivity
    : AppCompatActivity(), ManagedDisposable by MultiTimeDisposer() {

    lateinit var behavior: MainBehavior

    override fun onCreate(savedInstanceState: Bundle?) {
        Logger.v("")
        super.onCreate(savedInstanceState)

        val pager = SectionsPagerAdapter(supportFragmentManager)
        behavior = managed(MainBehavior(pager))

        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.behavior = behavior

        setSupportActionBar(binding.toolbar)

        // Set up the ViewPager with the sections adapter.
        binding.container.adapter = pager
        binding.container.addOnPageChangeListener(pager.listener)

        // New cashflow
        behavior.createCashflow.subscribe {
            Logger.d("open cashflow")
            val fragment = CashflowFragment()
            fragment.show(supportFragmentManager, "cashflow")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent context in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_add_account -> {
                Logger.d("select menu for adding account")
                EditAccountFragment().show(supportFragmentManager, "createAccount")
                true
            }
            R.id.action_export -> {
                Logger.d("select menu for exporting")
                // TODO Export
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        Logger.v("")
        super.onDestroy()
    }
}
