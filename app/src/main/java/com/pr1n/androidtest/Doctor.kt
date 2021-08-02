package com.pr1n.androidtest


import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class Doctors: ArrayList<Doctor>()

@Serializable
data class Doctor(
    @SerializedName("avatar")
    val avatar: String,
    @SerializedName("comment_num")
    val commentNum: Int,
    @SerializedName("department")
    val department: String,
    @SerializedName("good")
    val good: String,
    @SerializedName("hospital_level")
    val hospitalLevel: String,
    @SerializedName("hospital_name")
    val hospitalName: String,
    @SerializedName("job")
    val job: String,
    @SerializedName("live_id")
    val liveId: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("score")
    val score: String,
    @SerializedName("service_num")
    val serviceNum: Int,
    @SerializedName("status")
    val status: Int,
    @SerializedName("user_id")
    val userId: Int
)