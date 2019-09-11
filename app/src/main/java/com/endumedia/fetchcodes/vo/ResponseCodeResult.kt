package com.endumedia.fetchcodes.vo

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


/**
 * Created by Nino on 11.09.19
 */
@Entity
data class ResponseCodeResult(@PrimaryKey(autoGenerate = true) val id: Long, val path: String,
                              @SerializedName("response_code") val responseCode: String)