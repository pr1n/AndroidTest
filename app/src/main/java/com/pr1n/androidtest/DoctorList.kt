package com.pr1n.androidtest


import com.google.gson.annotations.SerializedName

data class DoctorList(
    @SerializedName("banner")
    val banner: List<Banner>,
    @SerializedName("doctor")
    val doctor: List<Doctor>
){
    data class Doctor(
        @SerializedName("avatar")
        val avatar: String?,
        @SerializedName("comment_num")
        val commentNum: Int?,
        @SerializedName("department")
        val department: String?,
        @SerializedName("good")
        val good: String?,
        @SerializedName("hospital_level")
        val hospitalLevel: String?,
        @SerializedName("hospital_name")
        val hospitalName: String?,
        @SerializedName("job")
        val job: String?,
        @SerializedName("name")
        val name: String?,
        @SerializedName("score")
        val score: Int?,
        @SerializedName("service_fee")
        val serviceFee: String?,
        @SerializedName("service_num")
        val serviceNum: Int?,
        @SerializedName("user_id")
        val userId: Int?
    )

    data class Banner(
        @SerializedName("content")
        val content: String,
        @SerializedName("image")
        val image: String,
        @SerializedName("title")
        val title: String,
        @SerializedName("type")
        val type: Int
    )
}



