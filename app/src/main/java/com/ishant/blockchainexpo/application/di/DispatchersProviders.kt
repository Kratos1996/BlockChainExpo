package com.ishant.blockchainexpo.application.di

import kotlinx.coroutines.CoroutineDispatcher

interface DispatchersProviders {
    val main: CoroutineDispatcher
    val io: CoroutineDispatcher
    val default: CoroutineDispatcher
    val unconfined: CoroutineDispatcher
}