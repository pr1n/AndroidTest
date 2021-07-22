package com.pr1n.androidtest

import com.apesmedical.commonsdk.utlis.KVUtlis
import com.apesmedical.commonsdk.utlis.mmkv
import com.library.sdk.ext.logi
import kotlinx.android.parcel.Parcelize

object UserManager {
    var user by UserDelegate
        private set

    val token get() = user.token
    val userId get() = user.userId
    val nickName get() = user.nickName
    val type
        get() = when (user.type) {
            TypeValue.TYPE_1.type -> TypeValue.TYPE_1
            TypeValue.TYPE_2.type -> TypeValue.TYPE_2
            TypeValue.TYPE_3.type -> TypeValue.TYPE_3
            TypeValue.TYPE_4.type -> TypeValue.TYPE_4
            else -> throw IllegalArgumentException()
        }

    val isLoged get() = token.isNotBlank() && userId > 0L

    val isType1 get() = TypeValue.TYPE_1 == type
    val isType2 get() = TypeValue.TYPE_2 == type
    val isType3 get() = TypeValue.TYPE_3 == type
    val isType4 get() = TypeValue.TYPE_4 == type

    fun login() {
        if (isLoged){
            logi("Loged")
            return
        }
        user = User("token", 10086L, "Pr1n", 4)
    }

    fun logout() {
        if (isLoged) user = User.Empty
        else throw Exception("Not Login.")
    }

    fun clearAll() = KVUtlis.clearAll()
}

object UserDelegate {
    var userJson by mmkv("")
}

@Parcelize
data class User(val token: String, val userId: Long, val nickName: String, val type: Int) {
    companion object {
        val Empty = User("", 0L, "", 1)
    }
}

@JvmInline
value class TypeValue private constructor(val type: Int) {
    companion object {
        val TYPE_1 = TypeValue(1)
        val TYPE_2 = TypeValue(2)
        val TYPE_3 = TypeValue(3)
        val TYPE_4 = TypeValue(4)
    }

    fun getUserTypeName() = when (type) {
        TYPE_1.type -> "Type_1"
        TYPE_2.type -> "Type_2"
        TYPE_3.type -> "Type_3"
        TYPE_4.type -> "Type_4"
        else -> throw IllegalArgumentException()
    }

    override fun toString() = getUserTypeName()
}