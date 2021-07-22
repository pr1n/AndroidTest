package com.apesmedical.commonsdk.utlis

/**
 * Created by Beetle_Sxy on 12/10/20.
 * 数字省略方法
 */

fun Int.ellipsis(company: String = "w") = NumEllipsisUtlis.ellipsis(this.toLong(), company)
fun Long.ellipsis(company: String = "w") = NumEllipsisUtlis.ellipsis(this, company)
fun String.numEllipsis(company: String = "w") = NumEllipsisUtlis.ellipsis(this.toLong(), company)

object NumEllipsisUtlis {
	fun ellipsis(num: Long, company: String = "w") = if (num >= 10000)
		"${String.format("%.1f", num / 10000f)}$company"
	else num.toString()
}