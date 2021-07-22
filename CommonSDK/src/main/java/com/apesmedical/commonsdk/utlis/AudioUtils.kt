package com.apesmedical.commonsdk.utlis

import android.content.Context
import android.media.AudioManager

/**
 * @Description:
 * @Author: Liuyang
 * @Create: 2021-05-27
 */
class AudioUtils private constructor(context: Context) {
    private val mAudioManager: AudioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    //获取多媒体最大音量
    val mediaMaxVolume: Int
        get() = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)//音量类型

    /**
     * 设置多媒体音量
     */
    //获取多媒体音量
    var mediaVolume: Int
        get() = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        set(volume) {
            mAudioManager.setStreamVolume(
                AudioManager.STREAM_MUSIC,  //音量类型
                volume, AudioManager.FLAG_PLAY_SOUND
                        or AudioManager.FLAG_VIBRATE
            )
        }

    //获取通话最大音量
    val callMaxVolume: Int
        get() = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL)

    //获取系统音量最大值
    val systemMaxVolume: Int
        get() = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM)

    //获取系统音量
    val systemVolume: Int
        get() = mAudioManager.getStreamVolume(AudioManager.STREAM_SYSTEM)

    //获取提示音量最大值
    val alermMaxVolume: Int
        get() = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM)

    //设置通话音量
    fun setCallVolume(volume: Int) {
        mAudioManager.setStreamVolume(
            AudioManager.STREAM_VOICE_CALL,
            volume,
            AudioManager.STREAM_VOICE_CALL
        )
    }

    // 关闭/打开扬声器播放
    fun setSpeakerStatus(on: Boolean) {
        if (on) { //扬声器
            mAudioManager.isSpeakerphoneOn = true
            mAudioManager.mode = AudioManager.MODE_NORMAL
        } else {
            // 设置最大音量
            val max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL)
            mAudioManager.setStreamVolume(
                AudioManager.STREAM_VOICE_CALL,
                max,
                AudioManager.STREAM_VOICE_CALL
            )
            // 设置成听筒模式
            mAudioManager.mode = AudioManager.MODE_IN_COMMUNICATION
            mAudioManager.isSpeakerphoneOn = false // 关闭扬声器
            mAudioManager.setRouting(
                AudioManager.MODE_NORMAL,
                AudioManager.ROUTE_EARPIECE,
                AudioManager.ROUTE_ALL
            )
        }
    }

    companion object {
        private var mInstance: AudioUtils? = null
        @Synchronized
        fun getInstance(context: Context): AudioUtils? {
            if (mInstance == null) {
                mInstance = AudioUtils(context)
            }
            return mInstance
        }
    }

}