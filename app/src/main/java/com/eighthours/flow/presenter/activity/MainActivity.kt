package com.eighthours.flow.presenter.activity

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.eighthours.flow.R
import com.eighthours.flow.databinding.ActivityMainBinding
import com.eighthours.flow.presenter.adapter.SectionsPagerAdapter
import com.eighthours.flow.presenter.behavior.MainBehavior
import com.eighthours.flow.presenter.fragment.CashflowFragment
import com.eighthours.flow.presenter.fragment.EditAccountFragment
import com.eighthours.flow.presenter.utility.ActivityAccessor
import com.eighthours.flow.utility.ManagedDisposable
import com.eighthours.flow.utility.MultiTimeDisposer
import com.eighthours.flow.utility.UI
import com.eighthours.flow.utility.onSuccess
import com.orhanobut.logger.Logger
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

class MainActivity
    : AppCompatActivity(), ManagedDisposable by MultiTimeDisposer(), ActivityAccessor {

    companion object {
        private const val CREATE_DOCUMENT_REQUEST = 1

        private val DateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")
    }

    lateinit var behavior: MainBehavior

    private lateinit var snackView: View

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

        snackView = binding.container
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_add_account -> {
                Logger.d("select menu for adding account")
                EditAccountFragment().show(supportFragmentManager, "createAccount")
                return true
            }

            R.id.action_export -> {
                Logger.d("select menu for exporting")
                val intent = Intent().apply {
                    action = Intent.ACTION_CREATE_DOCUMENT
                    type = "text/plain"
                    addCategory(Intent.CATEGORY_OPENABLE)
                    val date = DateFormatter.format(LocalDate.now())
                    putExtra(Intent.EXTRA_TITLE, "flow_$date")
                }

                if (intent.resolveActivity(packageManager) != null) {
                    startActivityForResult(intent, CREATE_DOCUMENT_REQUEST)
                } else {
                    Logger.e("application for ACTION_CREATE_DOCUMENT not found.")
                    Snackbar.make(snackView, R.string.error_export_intent, Snackbar.LENGTH_LONG)
                            .show();
                }
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        when (requestCode) {
            CREATE_DOCUMENT_REQUEST -> {
                Logger.d("create a file ${data.data}")
                val writer = contentResolver.openOutputStream(data.data).bufferedWriter()
                component.exportAction().exportWith(writer)
                        .observeOn(UI)
                        .onSuccess {
                            Snackbar.make(snackView, R.string.export_success, Snackbar.LENGTH_LONG)
                                    .show()
                        }
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onDestroy() {
        Logger.v("")
        super.onDestroy()
    }
}
