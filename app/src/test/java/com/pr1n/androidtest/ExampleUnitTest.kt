package com.pr1n.androidtest

import com.apesmedical.commonsdk.base.newbase.OkHttpRemoteService
import com.apesmedical.commonsdk.base.newbase.Post
import com.apesmedical.commonsdk.base.newbase.RxHttpRemoteService
import com.apesmedical.commonsdk.base.newbase.ext.requestByData
import com.apesmedical.commonsdk.http.ResponseApes
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }


    @Test
    fun testHttp() {
        GlobalScope.launch {
//            val result = RxHttpRemoteService().requestByData<ResponseApes<DoctorList>>(Post {
//                setUrl("http://test-yys-api.xadazhihui.cn/consult/list")
//                addParam("page", 1)
//            })
            val result = OkHttpRemoteService().requestByData<ResponseApes<DoctorList>>(Post {
                setUrl("http://test-yys-api.xadazhihui.cn/consult/list")
                addParam("page", 1)
            })
            println("result -> ${Gson().toJson(result)}")
        }.start()
        //class com.pr1n.androidtest.DoctorList
        // cannot be cast to class com.apesmedical.commonsdk.http.ResponseApes
        // (com.pr1n.androidtest.DoctorList and
        // com.apesmedical.commonsdk.http.ResponseApes are in unnamed module of loader 'app')

//        GlobalScope.launch {
//            OkHttpBuilder(Post{
//                setUrl("http://test-yys-api.xadazhihui.cn/consult/list")
//                addHeader("headerKey", "headerValue")
//                addQuerys("key" to "value", "key1" to "value1")
//                addParams("page" to 1, "page1" to 1, "page2" to 1)
//                addFile("file", File("C:/Users/PrinceMan/AndroidStudioProjects/AndroidTest/CommonSDK/src/main/java/com/apesmedical/commonsdk/base/newbase/RxHttpRemoteService.kt"))
//            }).newCall().asFlow<DoctorList>()
//                .collectLatest {
//                    println("it.data -> ${it.data}")
//                }
//        }.start()
        Thread.sleep(5000)
    }
}