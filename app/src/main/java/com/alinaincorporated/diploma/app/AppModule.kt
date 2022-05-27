package com.alinaincorporated.diploma.app

import com.alinaincorporated.diploma.database.TransactionsDatabase
import org.koin.dsl.module

val AppModule = module {

    single { TransactionsDatabase.create(get()) }
    single { get<TransactionsDatabase>().transactionDao() }

}