package com.example.hyperlist

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.hyperlist.services.AlarmService
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector

import kotlinx.android.synthetic.main.activity_scan_alarm.*

class ScanAlarmActivity : AppCompatActivity() {

    private lateinit var detector: BarcodeDetector
    private lateinit var cameraSource: CameraSource
    private var numberOfTaps = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_alarm)

        detector = BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.ALL_FORMATS).build()
        detector.setProcessor(object : Detector.Processor<Barcode> {
            override fun release() {}
            override fun receiveDetections(detections: Detector.Detections<Barcode>?) {
                val barcodes=detections?.detectedItems
                if (barcodes!!.size()>0){
                    val intent = Intent(this@ScanAlarmActivity, AlarmService::class.java)
                    intent.putExtra("alarm_state", "alarm_off")
                    startService(intent)
                }
            }
        })

        cameraSource = CameraSource.Builder(this,detector).setRequestedPreviewSize(1024,768).setRequestedFps(25f).setAutoFocusEnabled(true).build()
        sv_barcode.holder.addCallback (object: SurfaceHolder.Callback2{
            override fun surfaceRedrawNeeded (holder: SurfaceHolder?) {}
            override fun surfaceChanged (holder: SurfaceHolder?, format: Int, width: Int, height: Int) {}
            override fun surfaceDestroyed(holder: SurfaceHolder?) {
                cameraSource.stop()
            }
            override fun surfaceCreated(holder: SurfaceHolder?) {
                if (ContextCompat.checkSelfPermission(this@ScanAlarmActivity, android.Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED){
                    cameraSource.start(holder)
                } else{
                    this.let { ActivityCompat.requestPermissions(this@ScanAlarmActivity, arrayOf(android.Manifest.permission.CAMERA),123) }
                }

            }
        })

        scannerTapButton.setOnClickListener { view ->
            numberOfTaps += 1
            if (numberOfTaps == 3) {
                val intent = Intent(this, AlarmService::class.java)
                intent.putExtra("alarm_state", "alarm_off")
                startService(intent)
                val intentGoBack = Intent(this, MainActivity::class.java)
                startActivity(intentGoBack)
                finish()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode==123){
            if(grantResults.isNotEmpty()&& grantResults[0]==PackageManager.PERMISSION_GRANTED){
                cameraSource.start(sv_barcode.holder)}
            else{
                Toast.makeText(this,"Barcode scanners requires permission to access camera", Toast.LENGTH_SHORT).show()}
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        detector.release()
        cameraSource.stop()
        cameraSource.release()
    }

}
