package com.example.voiceapp

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.view.MotionEvent
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Locale

class MainActivity : AppCompatActivity() {


    private var speechRecognizer:SpeechRecognizer? = null
    private var editText:EditText? = null
    private var micBtn:ImageView? = null


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (ContextCompat.checkSelfPermission(
                this, Manifest.
                permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
            )
        {
            checkPermissions()
        }
        editText = findViewById(R.id.texts)
        micBtn = findViewById(R.id.buttons)
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
        val SpeechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        SpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        SpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())

        speechRecognizer!!.setRecognitionListener(object: RecognitionListener{
            override fun onReadyForSpeech(p0: Bundle?) {}

            override fun onBeginningOfSpeech() {
                editText!!.setText("")
                editText!!.setHint("Listeners...")
            }

            override fun onRmsChanged(p0: Float) {}


            override fun onBufferReceived(p0: ByteArray?) {}

            override fun onEndOfSpeech() {}

            override fun onError(p0: Int) {}

            override fun onResults(bundle: Bundle) {
                micBtn!!.setImageResource(R.drawable.mic_off)
                val data =  bundle!!.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                editText!!.setText(data!![0])

            }

            override fun onPartialResults(p0: Bundle?) {}

            override fun onEvent(p0: Int, p1: Bundle?) {}

        })

        micBtn!!.setOnTouchListener{ view, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_UP){
                speechRecognizer!!.stopListening()
            }
            if (motionEvent.action == MotionEvent.ACTION_DOWN){
                micBtn!!.setImageResource(R.drawable.mic)
                speechRecognizer!!.startListening(SpeechRecognizerIntent)
            }
            
            false
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        speechRecognizer!!.destroy()
    }
    private fun checkPermissions(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.RECORD_AUDIO),
                RecordAudioRequestCode
            )
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == RecordAudioRequestCode
            && grantResults.isNotEmpty()
            ){
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
        }
    }

    companion object{
        const val RecordAudioRequestCode = 1
    }
}
