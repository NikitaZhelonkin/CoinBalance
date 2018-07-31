package ru.nikitazhelonkin.coinbalance.data.entity

class WalletBalance @JvmOverloads constructor(val balance: String, val tokenList: List<Token>? = null)
