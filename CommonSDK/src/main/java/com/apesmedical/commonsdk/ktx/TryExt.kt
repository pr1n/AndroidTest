package com.library.sdk.ext

/**
 * Created by Beetle_Sxy on 2019-06-23.
 */

fun (() -> Unit).tryArea(
	catchArea: (Exception) -> Unit = { it.printStackTrace() },
	finallyArea: () -> Unit = {},
) {
	try {
		this()
	} catch (e: Exception) {
		catchArea(e)
	} finally {
		finallyArea()
	}
}
/*
fun (area: () -> Unit).trye(){

}*/

fun tryArea(catchArea: (Exception) -> Unit = { it.printStackTrace() }, area: () -> Unit) {
	area.tryArea(catchArea)
}