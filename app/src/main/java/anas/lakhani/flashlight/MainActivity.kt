package anas.lakhani.flashlight

import anas.lakhani.flashlight.databinding.ActivityMainBinding
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() ,OnClickHandlerInterface{

    private lateinit var binding: ActivityMainBinding
    private lateinit var mCameraManager: CameraManager
    var cameraId: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.listener = this


        val isFlashAvailable =
            applicationContext.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)

        if (!isFlashAvailable) {
            showNoFlashError()
            Snackbar.make(binding.root, "Flash Not Available", Snackbar.LENGTH_SHORT).show()
        }

        mCameraManager = getSystemService(CAMERA_SERVICE) as CameraManager
        mCameraManager.setTorchMode("0", false) // OFF
        binding.isLightOn = false
        try {
            cameraId = mCameraManager.cameraIdList[0]
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }


    }

    private fun showNoFlashError() {
        val alert: AlertDialog = AlertDialog.Builder(this).create()
        alert.setTitle("Oops!")
        alert.setMessage("Flash not available in this device...")

        alert.setButton(DialogInterface.BUTTON_POSITIVE,
            "OK",
        ) { _, _ -> finish() }
        alert.show()
    }


    private fun switchFlashLight(status: Boolean) {
        try {
            mCameraManager.setTorchMode(cameraId!!, status)

        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    override fun onClick(view: View?) {
        binding.isLightOn = !binding.isLightOn
        switchFlashLight(binding.isLightOn)
    }

}