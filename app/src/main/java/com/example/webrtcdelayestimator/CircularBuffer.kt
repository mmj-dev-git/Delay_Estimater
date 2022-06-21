package com.example.webrtcdelayestimator

import android.nfc.Tag
import android.util.Log
class CircularBuffer {
    private val arrayList = ArrayDeque<Short>()

    @Synchronized
    fun add(data: ShortArray) {
        arrayList.addAll(data.toTypedArray())
    }

    @Synchronized
    fun add(data: Short) {
        arrayList.add(data)
    }

    @Synchronized
    fun popData(size: Int): ShortArray {
        val data = ShortArray(size)
        for (i in 0 until size) {
            if (arrayList.isNotEmpty())
                    data[i] = arrayList.removeFirst()


        }
        return data
    }

    fun getTail() : Short{

        if (arrayList.isEmpty()) {
            throw NoSuchElementException()
        }
        return arrayList.removeLast()
    }

    fun getSize() = arrayList.size

    fun removeAll() = arrayList.clear()

}