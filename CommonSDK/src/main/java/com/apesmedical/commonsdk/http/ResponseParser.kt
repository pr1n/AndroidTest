package com.apesmedical.commonsdk.http

import com.library.sdk.ext.toast
import kotlinx.serialization.Serializable
import okhttp3.Response
import rxhttp.wrapper.annotation.Parser
import rxhttp.wrapper.exception.ParseException
import rxhttp.wrapper.parse.TypeParser
import rxhttp.wrapper.utils.convertToParameterized
import java.lang.reflect.Type

/**
 * Created by Beetle_Sxy on 2020/10/28.
 * 输入T,输出T,并对code统一判断
 */
@Parser(name = "Response", wrappers = [List::class])
open class ResponseParser<T> : TypeParser<ResultData<T>> {
    /**
     * 此构造方法适用于任意Class对象，但更多用于带泛型的Class对象，如：List<Student>
     *
     * 用法:
     * Java: .asParser(new ResponseParser<List<Student>>(){})
     * Kotlin: .asParser(object : ResponseParser<List<Student>>() {})
     *
     * 注：此构造方法一定要用protected关键字修饰，否则调用此构造方法将拿不到泛型类型
     */
    protected constructor() : super()

    /**
     * 此构造方法仅适用于不带泛型的Class对象，如: Student.class
     *
     * 用法
     * Java: .asParser(new ResponseParser<>(Student.class))   或者  .asResponse(Student.class)
     * Kotlin: .asParser(ResponseParser(Student::class.java)) 或者  .asResponse<Student>()
     */
    constructor(vararg types: Type) : super(*types)

    override fun onParse(response: Response): ResultData<T> {
        val data: ResponseApes<T> = response.convertToParameterized(ResponseApes::class, *types)
        //获取data字段
        var t = data.data
        if (t == null && types[0] === String::class.java) {
            /*
             * 考虑到有些时候服务端会返回：{"errorCode":0,"errorMsg":"关注成功"}  类似没有data的数据
             * 此时code正确，但是data字段为空，直接返回data的话，会报空指针错误，
             * 所以，判断泛型为String类型时，重新赋值，并确保赋值不为null
             */
            @Suppress("UNCHECKED_CAST")
            t = data.message as T
        }
        if (data.code != 200 || t == null) { //code不等于0，说明数据不正确，抛出异常
            if (data.code != 200) data.message.toast()
            throw ParseException(data.code.toString(), data.message, response)
        }
        return Success(t)
    }
}

@Serializable
data class ResponseApes<T>(
    val code: Int = 0,
    val message: String = "",
    var data: T? = null,
)

sealed class ResultData<T>(
    val data: T? = null,
    val message: String? = null,
    val error: Throwable? = null
) {
    fun refresh() {

    }
}

class Loading<T> : ResultData<T>()
class Complete<T> : ResultData<T>()
class Success<T>(data: T) : ResultData<T>(data = data)
class Failure<T>(message: String, throwable: Throwable) :
    ResultData<T>(message = message, error = throwable)