package com.example.drawing_app

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class MainActivity : AppCompatActivity() {

    private var drawView : DrawView? = null
    private var brushSizeBtn : ImageButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawView = findViewById(R.id.drawing_view)
        drawView?.changeBrushSize(20.toFloat())

        brushSizeBtn = findViewById(R.id.imbtn_brush)
        brushSizeBtn!!.setOnClickListener {
            showBrushSizeDialog()
        }
    }

    private fun showBrushSizeDialog(){
        var brushDialog = Dialog(this)
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
}