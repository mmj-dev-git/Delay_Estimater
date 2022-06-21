package com.example.webrtcdelayestimator

import android.Manifest.permission
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioManager
import android.net.Uri
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Build.VERSION
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.text.format.Formatter
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.webrtcdelayestimator.CommonMethods.assetFilePath
import com.example.webrtcdelayestimator.CommonMethods.short2byte
import com.example.webrtcdelayestimator.Constants.ROOM_PORT
import java.io.ByteArrayInputStream
import java.io.DataInputStream
import java.io.IOException
import java.net.SocketException
import java.nio.ByteBuffer
import java.nio.ShortBuffer
import java.util.*
import kotlin.concurrent.schedule

class MainClass : AppCompatActivity(), View.OnClickListener {
    private val PERMISSION_REQUEST_CODE = 9002
    val TAG1: String = "MainClass"
    val audio = Audio()
    lateinit var endCall: Button
    lateinit var connect: Button
    lateinit var showIp: Button
    lateinit var client: Button
    lateinit var server: Button
    lateinit var startwavFileReader: Button
    lateinit var startRealTimeRecording: Button
    lateinit var readFromWav: Button
    lateinit var playWave: Button
    lateinit var revertBack: Button
    lateinit var waveFileListener: Button
    lateinit var ConstatintContainer: ConstraintLayout
    lateinit var etIp: EditText
    lateinit var etWavFileName: EditText

    lateinit var tvIp: TextView
    lateinit var tvStatus: TextView

    private val wavFileDataForPlaying = CircularBuffer()
    private var udp: UDP? = null
    var fileName: String? = null

    companion object {
        // Used to load the 'myapplication2' library on application startup.
        init {
            System.loadLibrary("webrtcdelayestimator")
        }
    }

    external fun stringFromJNI(): String

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_class)

        endCall = findViewById(R.id.endCall);
        connect = findViewById(R.id.connect);
        showIp = findViewById(R.id.showIp);
        etIp = findViewById(R.id.etIp);
        tvIp = findViewById(R.id.tvIp);
        tvStatus = findViewById(R.id.tvStatus);

        client = findViewById(R.id.client);
        server = findViewById(R.id.server);
        revertBack = findViewById(R.id.revertBack);
        startRealTimeRecording = findViewById(R.id.startRealTimeRecording);
        readFromWav = findViewById(R.id.readFromWav);
        ConstatintContainer = findViewById(R.id.ConstatintContainer);
        playWave = findViewById(R.id.playWave);
        waveFileListener = findViewById(R.id.waveFileListener);
        startwavFileReader = findViewById(R.id.startwavFileReader);
        etWavFileName = findViewById(R.id.etWavFileName);

        endCall.setOnClickListener(this)
        showIp.setOnClickListener(this)
        connect.setOnClickListener(this)
        client.setOnClickListener(this)
        server.setOnClickListener(this)
        revertBack.setOnClickListener(this)
        readFromWav.setOnClickListener(this)
        startRealTimeRecording.setOnClickListener(this)
        playWave.setOnClickListener(this)
        waveFileListener.setOnClickListener(this)
        startwavFileReader.setOnClickListener(this)


        if (!checkPermission()) {
            requestPermission()
        }

        //Audio Recorder Premission

        if ((ContextCompat.checkSelfPermission(this, permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) || (ContextCompat.checkSelfPermission(this,
                permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, arrayOf(permission.RECORD_AUDIO, permission.WRITE_EXTERNAL_STORAGE), 1000)
        } else {
            initializeAudio()
        }

        setSpeaker()
    }

    private fun initializeAudio() {
        audio.inititalize()
        audio.startRecording()
        audio.startPlaying()
    }

    private fun startRecordingAndSendData() {
        Coroutines.default {
            while (true) {
                val recordedData = audio.recordedData
                val recordedfloatArray = shortMe(recordedData)?.let { floatMe(it) };
                fillNearEndBuffer(recordedfloatArray)
                udp!!.send(recordedData)
                Log.d("MyTAGKotlin", "Near End Float: ${Arrays.toString(recordedfloatArray)} ")
                Log.d("MyTAGKotlin", "Near End Float size: ${recordedfloatArray?.size} ")

                if (recordedfloatArray != null) {
                    Log.d("RevertBack", "FloatArraySize: ${recordedfloatArray.size}")
                }
                Log.d("RevertBack", "RecordedDataSingle ${recordedData.size}")
            }
        }
    }

    private fun startPlayingAndReceiveData() {
        Coroutines.default {
            while (true) {
                val dataFromServer = ByteArray(960 * 2) { 0 }
                val data = udp!!.receiver(dataFromServer)
                var playingFloatArray = shortMe(data)?.let { floatMe(it) };
                fillFarEndBuffer(playingFloatArray)
                audio.play(data)
                Log.d("MyTAGKotlin", "Far End Float: ${Arrays.toString(playingFloatArray)} ")
                Log.d("MyTAGKotlin", "Far End Float size: ${playingFloatArray?.size} ")

                Log.d(TAG1, "Packet Received ${data.size} || ${data.contentToString()}")
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun readWavFile() {
        try {
            val audioFileAbsolutePath = assetFilePath(this, fileName)
            if (audioFileAbsolutePath == null) {
                fileName = "";
                tvStatus.text = "No File Found on: ${Constants.FILEPATH}"
            } else {
                tvStatus.text = "File Found: Please Start Listener On Other Phone"
                waveFileListener.visibility = View.GONE
                playWave.visibility = View.VISIBLE
                val audioFileProcess = AudioFileProcess()
                val audioData: FloatArray = audioFileProcess.ReadingAudioFile(audioFileAbsolutePath)
                wavFileDataForPlaying.add(float32ToInt16(audioData))
            }
        } catch (e: java.lang.Exception) {
            tvStatus.append(e.localizedMessage)
        }
    }

    private fun connectWithOtherPhone(ip: String) {
        try {
            Coroutines.io {
                udp = UDP(ROOM_PORT, ip.trim())
//                startPlayingAndReceiveData() removed it for here
                Timer().schedule(30000) {
                    //calling a function
                    processData()
                }
            }
        } catch (e: SocketException) {
            Toast.makeText(this, e.localizedMessage, Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            Toast.makeText(this, e.localizedMessage, Toast.LENGTH_SHORT).show()
        }
    }

    private fun wavFileAudioListener() {
        Coroutines.default {
            while (true) {
                val dataFromServer = ByteArray(960 * 2) { 0 }
                val recordedData = audio.recordedData
                Log.d("dataya","${recordedData.contentToString()}")
                val data = udp!!.receiver(dataFromServer)
                audio.play(data)
            }
        }
    }

    override fun onClick(view: View?) {
        if (view == endCall) {
            EndCall()
        }
        else if (view == client) {
            setClient()
        }
        else if (view == server) {
            ConstatintContainer.visibility = View.VISIBLE
            server.visibility = View.GONE
            revertBack.visibility = View.GONE
            client.visibility = View.GONE
            showIp.visibility = View.VISIBLE
        }
        else if (view == revertBack) {
            revertBack.visibility = View.GONE
            tvStatus.text = "Playing RevertBack"
            startRevertBack()
        }
        else if (view == connect) {
            Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show()
            startRealTimeRecording.visibility = View.VISIBLE
            startwavFileReader.visibility = View.VISIBLE
            connect.visibility = View.GONE
            tvStatus.visibility = View.VISIBLE
            etIp.visibility = View.GONE
            tvIp.visibility = View.GONE
            tvStatus.text = " Please choose option Realtime/Wav\n"

            connectWithOtherPhone(etIp.text.toString())

        }
        else if (view == showIp) {
            showIpAddress()
        }
        else if (view == startRealTimeRecording) {
            startPlayingAndReceiveData()
            startRecordingAndSendData()

            tvStatus.text = "Sending Real Time Data"
            endCall.visibility = View.VISIBLE
            showIp.visibility = View.GONE
            startwavFileReader.visibility = View.GONE

        }
        else if (view == startwavFileReader) {
            startwavFileReader.visibility = View.GONE
            startRealTimeRecording.visibility = View.GONE
            waveFileListener.visibility = View.VISIBLE
            readFromWav.visibility = View.VISIBLE
            etWavFileName.visibility = View.VISIBLE
            showIp.visibility = View.GONE
            tvStatus.text = "Place Your WavFiles In And Enter File Name: "
        }
        else if (view == readFromWav) {
            fileName = etWavFileName.text.toString()
            if (fileName!!.isNotEmpty()) {
                if (VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    readWavFile()
                }
            } else {
                Toast.makeText(this, "Please Enter File Name", Toast.LENGTH_SHORT).show()
            }
        }
        else if (view == playWave) {
            tvStatus.text = "Playing"
            Coroutines.io {
                while (wavFileDataForPlaying.getSize() >= 960) {
                    val wavFileData = wavFileDataForPlaying.popData(960)
                    audio.play(wavFileData)
                    udp!!.send(short2byte(wavFileData))
                }
            }
        }
        else if (view == waveFileListener) {
            readFromWav.visibility = View.GONE
            etWavFileName.visibility = View.GONE
            waveFileListener.visibility = View.GONE
            tvStatus.text = "Listening For Sound From Other Phone"
            wavFileAudioListener()
        }
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    fun toFloatarr(bytes: ByteArray?): FloatArray? {
        val bas = ByteArrayInputStream(bytes)
        val ds = DataInputStream(bas)
        val fArr = FloatArray(2048 / 4) // 4 bytes per float
        for (i in fArr.indices) {
            fArr[i] = ds.readFloat()
        }
        return fArr
    }

    fun byteToFloat(input_stream: ByteArray?): FloatArray? {
        val sbuf: ShortBuffer
        sbuf = ByteBuffer.wrap(input_stream).asShortBuffer()
        val audioShorts = ShortArray(sbuf.capacity())
        sbuf[audioShorts]
        val audioFloats = FloatArray(audioShorts.size)
        for (i in audioShorts.indices) {
            audioFloats[i] = java.lang.Short.reverseBytes(audioShorts[i]).toFloat() / 0x8000
        }
        return audioFloats
    }

    fun floatMe(pcms: ShortArray): FloatArray? {
        val floaters = FloatArray(pcms.size)
        for (i in pcms.indices) {
            floaters[i] = pcms[i].toFloat()
        }
        return floaters
    }

    fun shortMe(bytes: ByteArray): ShortArray? {
        val out = ShortArray(bytes.size / 2) // will drop last byte if odd number
        val bb = ByteBuffer.wrap(bytes)
        for (i in out.indices) {
            out[i] = bb.short
        }
        return out
    }

    private fun setClient() {
        showIp.visibility = View.VISIBLE
        client.visibility = View.GONE
        server.visibility = View.GONE
        showIp.visibility = View.VISIBLE

        Coroutines.io {
            udp = UDP(ROOM_PORT)

            while (true) {
                val dataFromServer = ByteArray(1024 * 2)
                var data = udp!!.receiver(dataFromServer)
                audio.play(data)
                Log.d(TAG1, "Packet Received ${data.size} || ${data.contentToString()}")
            }
        }
    }

    private fun EndCall() {
        try {
            audio.stopPlaying()
        } catch (e: java.lang.Exception) {
        }
        try {
            audio.stopRecording()
        } catch (e: java.lang.Exception) {
        }
        try {
            udp!!.closeSocket()
        } catch (e: java.lang.Exception) {
        }

        Toast.makeText(this, "Call Ended", Toast.LENGTH_SHORT).show()
    }

    private fun showIpAddress() {
        val wm = applicationContext.getSystemService(WIFI_SERVICE) as WifiManager
        val ip = Formatter.formatIpAddress(wm.connectionInfo.ipAddress)
        tvIp.text = ip
        tvStatus.append("Device IP: " + ip)
    }

    private fun checkPermission(): Boolean {
        return if (VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Environment.isExternalStorageManager()
        } else {
            val result = ContextCompat.checkSelfPermission(this@MainClass, permission.READ_EXTERNAL_STORAGE)
            val result1 = ContextCompat.checkSelfPermission(this@MainClass, permission.WRITE_EXTERNAL_STORAGE)
            val result2 = ContextCompat.checkSelfPermission(this@MainClass, permission.RECORD_AUDIO)

            result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun requestPermission() {
        if (VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                intent.addCategory("android.intent.category.DEFAULT")
                intent.data = Uri.parse(String.format("package:%s", applicationContext.packageName))
                startActivityForResult(intent, 2296)
            } catch (e: Exception) {
                val intent = Intent()
                intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                startActivityForResult(intent, 2296)
            }
        } else {
            //below android 11
            ActivityCompat.requestPermissions(this@MainClass, arrayOf(permission.WRITE_EXTERNAL_STORAGE, permission.RECORD_AUDIO), PERMISSION_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    // perform action when allow permission success
                } else {
                    Toast.makeText(this, "Allow permission for storage access!", Toast.LENGTH_SHORT).show()
                }
            }
        } else if (requestCode == 1000) {
            initializeAudio()
        }
    }

    fun setSpeaker() {
        val audioManager: AudioManager = this.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        audioManager.mode = AudioManager.MODE_IN_COMMUNICATION
        audioManager.isSpeakerphoneOn = true
    }

    fun float32ToInt16(arr: FloatArray): ShortArray {  //int[]
        // logic is built to support the numpy.int16 output
        val int16Arr = ShortArray(arr.size)
        for (i in arr.indices) {
            if (arr.get(i) < 0) {
                if (arr.get(i) > -1) {
                    int16Arr[i] = 0
                } else {
                    int16Arr[i] = Math.ceil(arr.get(i).toDouble()).toInt().toShort()
                }
            } else if (arr.get(i) > 0) {
                int16Arr[i] = Math.floor(arr.get(i).toDouble()).toInt().toShort()
            } else {
                int16Arr[i] = 0
            }
        }
        return int16Arr
    }

    private fun startRevertBack() {
        showIp.visibility = View.VISIBLE
        client.visibility = View.GONE
        server.visibility = View.GONE
        showIp.visibility = View.VISIBLE

        Coroutines.io {

            while (true) {
                audio.play(audio.recordedData)
            }
        }
    }

    external fun processData();
    external fun fillNearEndBuffer(recordedData: FloatArray?);
    external fun fillFarEndBuffer(playingData: FloatArray?);
}