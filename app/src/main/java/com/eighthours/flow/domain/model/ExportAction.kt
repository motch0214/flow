package com.eighthours.flow.domain.model

import com.eighthours.flow.domain.repository.Repository
import com.eighthours.flow.utility.async
import com.orhanobut.logger.Logger
import java.io.Writer
import javax.inject.Inject

class ExportAction
@Inject constructor(
        private val repository: Repository) {

    fun exportWith(writer: Writer) = async {
        writer.use {
            Logger.d("export all positions")
            export(it)
        }
    }

    private fun export(writer: Appendable) {
        val accountMap = repository.account().findAll().map { it.id!! to it }.toMap()

        writer.appendln("Group\tType\tName\tMonth\tAmount\tUpdated")
        repository.position().findAll()
                .map { position ->
                    val account = accountMap.getValue(position.accountId)
                    PositionBean(
                            group = accountMap.getValue(account.groupId!!).name,
                            type = account.type,
                            name = account.name,
                            month = position.month,
                            amount = position.amount,
                            updatedDateTime = position.updatedDateTime!!)
                }
                .forEach {
                    with(it) {
                        writer.appendln("$group\t$type\t$name\t$month\t$amount\t$updatedDateTime")
                    }
                }
    }
}
