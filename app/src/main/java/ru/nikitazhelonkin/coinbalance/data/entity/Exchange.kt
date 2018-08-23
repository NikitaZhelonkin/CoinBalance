package ru.nikitazhelonkin.coinbalance.data.entity


import android.arch.persistence.room.*
import ru.nikitazhelonkin.coinbalance.data.db.ExchangeServiceConverter

@Entity(indices = [(Index(name = "exchange_index", value = arrayOf("service", "api_key"), unique = true))])
class Exchange(@field:PrimaryKey(autoGenerate = true) @field:ColumnInfo(name = "mId") override val id: Int,
                    @field:TypeConverters(ExchangeServiceConverter::class)
                    @field:ColumnInfo(name = "service") val service: ExchangeService?,
                    @field:ColumnInfo(name = "api_key") val apiKey: String?,
                    @field:ColumnInfo(name = "api_secret") val apiSecret: String?,
                    @field:ColumnInfo(name = "title") var title: String?,
                    @field:ColumnInfo(name = "position") override var position: Int = -1,
                    @field:ColumnInfo(name = "created_at") override val createdAt: Long = System.currentTimeMillis(),
                    @field:ColumnInfo(name = "status") var status: Int = STATUS_NONE,
                    @field:ColumnInfo(name = "error_message") var errorMessage: String? = null) : ListItem {

    @Ignore
    constructor(service: ExchangeService, apiKey: String, apiSecret: String, title: String?) :
            this(id = 0, service = service, apiKey = apiKey, apiSecret = apiSecret, title = title)


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        val exchange = other as Exchange?

        return id == exchange!!.id
    }

    override fun hashCode(): Int {
        return id
    }

    override fun toString(): String {
        return "Exchange(mId=$id, service=$service, title=$title)"
    }

    companion object {
        const val STATUS_NONE = 0
        const val STATUS_ERROR = -1
        const val STATUS_OK = 1
    }

}
