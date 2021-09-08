package com.pr1n.androidtest

import com.pr1n.repository.remote.base.OkHttpRemoteService
import com.pr1n.repository.remote.base.ParameterizedTypeImpl
import com.pr1n.repository.remote.base.Post
import com.apesmedical.commonsdk.http.ResponseApes
import com.google.gson.Gson
import com.library.sdk.ext.toBoolean
import com.pr1n.repository.entity.DoctorList
import com.pr1n.repository.ext.requestByData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
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
    fun test() {
        println("-----> ${"1".toInt().toBoolean()}")
//        val result = con<Doctor>()
//        result
    }

    inline fun <reified T> con(): ResponseApes<List<T>>? {
        return convert(T::class.java)
    }

    fun <T> convert(clazz: Class<T>): ResponseApes<List<T>>? {
        val type = ParameterizedTypeImpl(List::class.java, arrayOf(clazz))
        val newType = ParameterizedTypeImpl(ResponseApes::class.java, arrayOf(type))
        return Gson().fromJson<ResponseApes<List<T>>>(jsonStr, newType)
    }

    val jsonStr = """
        {
    "code": 200,
    "message": "success",
    "data": [
        {
            "user_id": 7,
            "name": "随缘医生",
            "avatar": "https://stage-yys-pic.apehorde.cn/admin/image/1611648073607466.png",
            "job": "主治医师",
            "department": "内科",
            "score": "5.0",
            "service_num": 45,
            "comment_num": 0,
            "good": "急诊救治",
            "hospital_name": "西京医院",
            "hospital_level": "三甲",
            "status": 0,
            "live_id": 1
        },
        {
            "user_id": 12,
            "name": "王玉华",
            "avatar": "https://stage-yys-pic.apehorde.cn/admin/image/1608863965654143.png",
            "job": "主任医师",
            "department": "外科/重症医学科",
            "score": "5.0",
            "service_num": 5,
            "comment_num": 0,
            "good": "佝偻病、睡眠障碍、贫血、小儿牛奶蛋白过敏、婴儿喂养问题、便秘",
            "hospital_name": "",
            "hospital_level": "",
            "status": 0,
            "live_id": 5
        },
        {
            "user_id": 14,
            "name": "徐宇",
            "avatar": "https://stage-yys-pic.apehorde.cn/admin/image/1609126392513581.png",
            "job": "主任医师",
            "department": "外科/重症医学科",
            "score": "5.0",
            "service_num": 2,
            "comment_num": 0,
            "good": "痤疮、玫瑰痤疮、皮炎、湿疹、脱发、荨麻疹",
            "hospital_name": "",
            "hospital_level": "",
            "status": 0,
            "live_id": 6
        },
        {
            "user_id": 16,
            "name": "邱柳",
            "avatar": "https://stage-yys-pic.apehorde.cn/admin/image/1608281903622797.png",
            "job": "主任医师",
            "department": "外科/重症医学科",
            "score": "5.0",
            "service_num": 0,
            "comment_num": 0,
            "good": "佝偻病、睡眠障碍、贫血、小儿牛奶蛋白过敏、婴儿喂养问题、便秘",
            "hospital_name": "",
            "hospital_level": "",
            "status": 0,
            "live_id": 7
        },
        {
            "user_id": 18,
            "name": "潘军",
            "avatar": "https://stage-yys-pic.apehorde.cn/admin/image/160828452048169.png",
            "job": "主任医师",
            "department": "外科/重症医学科",
            "score": "5.0",
            "service_num": 0,
            "comment_num": 0,
            "good": "感冒、呼吸衰竭、急性上呼吸道感染",
            "hospital_name": "",
            "hospital_level": "",
            "status": 0,
            "live_id": 8
        },
        {
            "user_id": 304,
            "name": "张桂香",
            "avatar": "https://stage-yys-pic.apehorde.cn/admin/image/1608791173542942.png",
            "job": "主任医师",
            "department": "外科/重症医学科",
            "score": "5.0",
            "service_num": 0,
            "comment_num": 0,
            "good": "呼吸道感染、发热、腹痛、头晕、胸痛、恶心与呕吐",
            "hospital_name": "",
            "hospital_level": "",
            "status": 0,
            "live_id": 10
        },
        {
            "user_id": 336,
            "name": "赵瑾医生",
            "avatar": "https://stage-yys-pic.apehorde.cn/admin/image/161166912909917.png",
            "job": "主治医师",
            "department": "外科/神经外科",
            "score": "5.0",
            "service_num": 364,
            "comment_num": 18,
            "good": "外科伤痛处理，伤口消毒",
            "hospital_name": "",
            "hospital_level": "",
            "status": 0,
            "live_id": 20
        },
        {
            "user_id": 395,
            "name": "周医生",
            "avatar": "https://stage-yys-pic.apehorde.cn/admin/image/1611912150460893.png",
            "job": "主治医师",
            "department": "外科/普外科",
            "score": "5.0",
            "service_num": 0,
            "comment_num": 0,
            "good": "呼吸道感染、发热、腹痛、头晕、胸痛、恶心与呕吐",
            "hospital_name": "",
            "hospital_level": "",
            "status": 0,
            "live_id": 26
        },
        {
            "user_id": 398,
            "name": "商医生",
            "avatar": "https://stage-yys-pic.apehorde.cn/admin/image/1609411188112822.png",
            "job": "主任医师",
            "department": "外科/重症医学科",
            "score": "5.0",
            "service_num": 65,
            "comment_num": 0,
            "good": "呼吸道感染、发热、腹痛、头晕、胸痛、恶心与呕吐",
            "hospital_name": "",
            "hospital_level": "",
            "status": 0,
            "live_id": 27
        },
        {
            "user_id": 568,
            "name": "韩亚平",
            "avatar": "https://stage-yys-pic.apehorde.cn/admin/image/1610000719224940.png",
            "job": "住院医师",
            "department": "预防保健科",
            "score": "5.0",
            "service_num": 87,
            "comment_num": 1,
            "good": "佝偻病、睡眠障碍、贫血、小儿牛奶蛋白过敏、婴儿喂养问题、便秘",
            "hospital_name": "",
            "hospital_level": "",
            "status": 0,
            "live_id": 43
        }
    ]
}
    """.trimIndent()

    @Test
    fun testFlow() {
        val flow = flow {
            emit("1")
        }

        GlobalScope.launch {
            flow.collect {
                println("flow -> $it")
            }
            flow.toList().forEach(::println)
        }

        Thread.sleep(5000)
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
        //class com.pr1n.repository.entity.DoctorList
        // cannot be cast to class com.apesmedical.commonsdk.http.ResponseApes
        // (com.pr1n.repository.entity.DoctorList and
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