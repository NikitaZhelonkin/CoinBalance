package ru.nikitazhelonkin.coinbalance.data.entity


import android.arch.persistence.room.*
import org.parceler.Parcel
import org.parceler.ParcelConstructor

@Entity(indices = [(Index(name = "index_name", value = ["address"], unique = true))])
@Parcel(Parcel.Serialization.BEAN)
class Wallet @ParcelConstructor
constructor(@field:PrimaryKey(autoGenerate = true) @field:ColumnInfo(name = "mId") override val id: Int,
            @field:ColumnInfo(name = "coin_ticker") val coinTicker: String?,
            @field:ColumnInfo(name = "address") val address: String?,
            @field:ColumnInfo(name = "alias") var alias: String?,
            @field:ColumnInfo(name = "balance") var balance: Float,
            @field:ColumnInfo(name = "position") override var position: Int,
            @field:ColumnInfo(name = "create_at") override val createdAt: Long,
            @field:ColumnInfo(name = "status") var status: Int) : ListItem {


    @Ignore
    constructor(coinTicker: String, address: String, alias: String?) :
            this(0, coinTicker, address, alias, 0f, -1, System.currentTimeMillis(), STATUS_NONE)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        val wallet = other as Wallet?

        return id == wallet!!.id
    }

    override fun hashCode(): Int {
        return id
    }

    companion object {
        const val STATUS_NONE = 0
        const val STATUS_ERROR = -1
        const val STATUS_OK = 1
    }

}
