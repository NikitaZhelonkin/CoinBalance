package ru.nikitazhelonkin.coinbalance.data.entity


import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey

@Entity(indices = [(Index(name = "exchange_id_index", value = ["exchange_id"]))],
        foreignKeys = [(ForeignKey(entity = Exchange::class, parentColumns = arrayOf("mId"),
                childColumns = arrayOf("exchange_id"), onDelete = ForeignKey.CASCADE))])
class ExchangeBalance(@field:PrimaryKey(autoGenerate = true) @field:ColumnInfo(name = "mId") val id: Int,
                      @field:ColumnInfo(name = "exchange_id") val exchangeId: Int,
                      @field:ColumnInfo(name = "coin_ticker") val coinTicker: String?,
                      @field:ColumnInfo(name = "balance") val balance: Float) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        val that = other as ExchangeBalance?

        return id == that!!.id
    }

    override fun hashCode(): Int {
        return id
    }

    override fun toString(): String {
        return "ExchangeBalance{" +
                "mId=" + id +
                ", mExchangeId=" + exchangeId +
                ", mCoinTicker='" + coinTicker + '\''.toString() +
                ", mBalance=" + balance +
                '}'.toString()
    }
}
