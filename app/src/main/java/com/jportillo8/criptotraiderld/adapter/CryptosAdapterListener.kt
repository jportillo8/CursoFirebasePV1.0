package com.jportillo8.criptotraiderld.adapter

import com.jportillo8.criptotraiderld.model.Crypto

interface CryptosAdapterListener {

    fun onBuyCryptoClicked(crypto: Crypto)
}