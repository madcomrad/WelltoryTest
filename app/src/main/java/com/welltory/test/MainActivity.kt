package com.welltory.test

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import com.welltory.test.model.ViewState
import com.welltory.test.utils.isPermissionGranted
import com.welltory.test.utils.isPermissionsGranted
import com.welltory.test.utils.setDelayClickListener
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val hintText = findViewById<TextView>(R.id.hint)
        val button = findViewById<Button>(R.id.button)
        button.setDelayClickListener {
            tryClickTheButton()
        }
        viewModel.viewState.observe(this) { viewState ->
            hintText.text = viewState.hintText
            button.text = viewState.buttonText
            button.isEnabled = viewState.isButtonEnabled
            button.isInvisible = !viewState.isButtonVisible
            hintText.setTextColor(
                when (viewState.resultState) {
                    ViewState.ResultState.Failure -> ContextCompat.getColor(this, android.R.color.holo_red_light)
                    else -> ContextCompat.getColor(this, android.R.color.black)
                }
            )
        }
    }

    private fun tryClickTheButton() {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q || isPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE) -> viewModel.onStartWork()
            else -> requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), PERMISSION_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (isPermissionsGranted(permissions, grantResults, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                viewModel.onStartWork()
            } else if (!shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                Toast.makeText(this, "External storage permission is required - please go to the app settings", Toast.LENGTH_LONG).show()
            }
        }
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 61552
    }
}
