package com.example.myquiizapp


import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import java.util.*

class ActivityCamera : AppCompatActivity() {

    private lateinit var mSurfaceView: SurfaceView
    private lateinit var mSurfaceHolder: SurfaceHolder
    private lateinit var mCameraManager: CameraManager
    private var mCameraDevice: CameraDevice? = null
    private var mCameraCaptureSession: CameraCaptureSession? = null
    private lateinit var mBackgroundThread: HandlerThread
    private lateinit var mBackgroundHandler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        mSurfaceView = findViewById(R.id.surfaceView)
        mSurfaceHolder = mSurfaceView.holder
        mSurfaceHolder.addCallback(mSurfaceHolderCallback)

        mCameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
    }

    private val mSurfaceHolderCallback = object : SurfaceHolder.Callback {
        override fun surfaceCreated(holder: SurfaceHolder) {
            openCamera()
        }

        override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}

        override fun surfaceDestroyed(holder: SurfaceHolder) {
            closeCamera()
        }
    }

    private fun openCamera() {
        try {

            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                return
            }
            mCameraManager.openCamera("0", mCameraDeviceStateCallback, mBackgroundHandler)
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    private val mCameraDeviceStateCallback = object : CameraDevice.StateCallback() {
        override fun onOpened(camera: CameraDevice) {
            mCameraDevice = camera
            try {
                camera.createCaptureSession(Arrays.asList(mSurfaceHolder.surface), mSessionStateCallback, mBackgroundHandler)
            } catch (e: CameraAccessException) {
                e.printStackTrace()
            }
        }

        override fun onDisconnected(camera: CameraDevice) {
            camera.close()
            mCameraDevice = null
        }

        override fun onError(camera: CameraDevice, error: Int) {
            camera.close()
            mCameraDevice = null
            Log.e("CameraActivity", "CameraDevice.StateCallback onError: $error")
            Toast.makeText(this@ActivityCamera, "Error opening camera", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private val mSessionStateCallback = object : CameraCaptureSession.StateCallback() {
        override fun onConfigured(session: CameraCaptureSession) {
            mCameraCaptureSession = session
            try {
                session.setRepeatingRequest(mCameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW).build(), null, mBackgroundHandler)
            } catch (e: CameraAccessException) {
                e.printStackTrace()
            }
        }

        override fun onConfigureFailed(session: CameraCaptureSession) {
            Log.e("CameraActivity", "CameraCaptureSession.StateCallback onConfigureFailed")
            Toast.makeText(this@ActivityCamera, "Failed to configure camera", Toast.LENGTH_SHORT).show()
        }
    }

    private fun closeCamera() {
        mCameraCaptureSession?.close()
        mCameraCaptureSession = null
        mCameraDevice?.close()
        mCameraDevice = null
    }

    override fun onResume() {
        super.onResume()
        startBackgroundThread()
    }

    override fun onPause() {
        super.onPause()
        stopBackgroundThread()
    }

    private fun startBackgroundThread() {
        mBackgroundThread = HandlerThread("CameraBackground")
        mBackgroundThread.start()
        mBackgroundHandler = Handler(mBackgroundThread.looper)
    }

    private fun stopBackgroundThread() {
        if (::mBackgroundThread.isInitialized) {
            mBackgroundThread.quitSafely()
            try {
                mBackgroundThread.join()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }

    companion object {
        private const val REQUEST_CAMERA_PERMISSION = 200
    }






    }
