package ru.nikitazhelonkin.coinbalance.data.entity


import android.arch.persistence.room.*

@Entity(indices = arrayOf(Index(name = "wallet_index", value = ["wallet_address"])),
        foreignKeys = arrayOf(ForeignKey(
                entity = Wallet::class,
                parentColumns = arrayOf("address"),
                childColumns = arrayOf("wallet_address"),
                onDelete = ForeignKey.CASCADE)))
class Token(@PrimaryKey(autoGenerate = true) @ColumnInfo(name = "mId") val id: Int,
            @ColumnInfo(name = "wallet_address") val walletAddress: String?,
            @ColumnInfo(name = "token_ticker") val tokenTicker: String?,
            @ColumnInfo(name = "token_name") val tokenName: String?,
            @ColumnInfo(name = "balance")  val balance: Float) {


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val token = other as Token?
        return id == token!!.id
    }

    override fun hashCode(): Int {
        return id
    }

}
