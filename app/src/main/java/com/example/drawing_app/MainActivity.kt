package com.example.drawing_app

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.get


class MainActivity : AppCompatActivity() {

    private var drawView : DrawView? = null
    private var brushSizeBtn : ImageButton? = null
    private var mImageButtonCurrentPaint : ImageButton? = null



    val openGalleryLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result ->
            if(result.resultCode == RESULT_OK && result.data != null){
                var backgroundImage : ImageView = findViewById(R.id.iv_background)
                backgroundImage.setImageURI(result.data?.data)
            }
        }


    // requestPermission allows for use of different aspects of Android system, such as
    // reading/writing files, accessing camera/bluetooth/ect...
    private val requestPermission : ActivityResultLauncher<Array<String>> =
        // An array is used to store each permission
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
                permissions ->
                permissions.entries.forEach{
                    val permissionName = it.key
                    val isGranted = it.value

                    if(isGranted) {
                        Toast.makeText(this,
                            "Permission granted to access gallery", Toast.LENGTH_SHORT).show()

                        val pickIntent = Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                        openGalleryLauncher.launch(pickIntent)

                    } else {
                        if (permissionName == Manifest.permission.READ_EXTERNAL_STORAGE) {
                            Toast.makeText(this,
                                "Permission denied", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val ibGallery: ImageButton = findViewById(R.id.imbtn_select_image)
        ibGallery.setOnClickListener {
            requestStoragePermission()
        }


        drawView = findViewById(R.id.drawing_view)
        drawView?.changeBrushSize(20.toFloat())

        val llPaintColors = findViewById<LinearLayout>(R.id.ll_paint_colors)

        mImageButtonCurrentPaint = llPaintColors[0] as ImageButton


        brushSizeBtn = findViewById(R.id.imbtn_brush)
        brushSizeBtn!!.setOnClickListener {
            showBrushSizeDialog()
        }
    }

    private fun showBrushSizeDialog(){
        val brushDialog = Dialog(this)
        brushDialog.setContentView(R.layout.dialog_brush_size)
        brushDialog.setTitle("Set brush size")

        val smallBtn : ImageButton = brushDialog.findViewById(R.id.imbtn_small_brush)
        val mediumBtn : ImageButton = brushDialog.findViewById(R.id.imbtn_medium_brush)
        val largeBtn : ImageButton = brushDialog.findViewById(R.id.imbtn_large_brush)

        smallBtn.setOnClickListener{
            drawView?.changeBrushSize(10.toFloat())
            brushDialog.dismiss()
        }
        mediumBtn.setOnClickListener{
            drawView?.changeBrushSize(20.toFloat())
            brushDialog.dismiss()
        }
        largeBtn.setOnClickListener{
            drawView?.changeBrushSize(30.toFloat())
            brushDialog.dismiss()
        }

        brushDialog.show()
    }
    
    fun paintClicked(view: View){
        if (view != mImageButtonCurrentPaint){
            val imageButton = view as ImageButton
            val colorTag = imageButton.tag.toString()
            drawView!!.selectColor(colorTag)

            imageButton.setImageDrawable(
                ContextCompat.getDrawable(this, R.drawable.pallet_selected)
            )

            mImageButtonCurrentPaint!!.setImageDrawable(
                ContextCompat.getDrawable(this, R.drawable.pallet_normal)
            )

            mImageButtonCurrentPaint = view

        }
    }

    private fun requestStoragePermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
        ){
            showRationaleDialog("Drawing App requires gallery access",
                "Drawing app requires access to external storage.")
        } else {
            requestPermission.launch(arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE
                // TODO: Add writing to external storage permission
            ))
        }
    }

    private fun showRationaleDialog(title: String, message: String){
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(title)
            .setMessage(message)
            .setPositiveButton("Cancel"){dialog, _ -> dialog.dismiss()}
        builder.create().show()
    }

}