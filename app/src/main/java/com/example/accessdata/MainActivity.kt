package com.example.accessdata

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.accessdata.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private  var binding:ActivityMainBinding?=null
    val opengallery:ActivityResultLauncher<Intent> =registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()){
            result->
        if(result.resultCode=== RESULT_OK && result.data!=null){

            binding?.background?.setImageURI(result.data?.data)
        }

    }

    //requesting permission varriable
    val requestpermissionn:ActivityResultLauncher<Array<String>> =registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()){
            permissions->permissions.entries.forEach() {
        val permissionName = it.key
        val isgranted=it.value
        if(isgranted){
            Toast.makeText(this,"Permission is granted for accessing gallery",Toast.LENGTH_LONG).show()
            var intent=Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)//it will gives the image path
            opengallery.launch(intent)
        }else{
            if(permissionName==android.Manifest.permission.READ_EXTERNAL_STORAGE){
                Toast.makeText(this,"You denied the permission",Toast.LENGTH_LONG).show()
            }
        }

    }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding?.root)
        setSupportActionBar(binding?.toolBarExcercise)

        binding?.btnAccess?.setOnClickListener {
            requestpermission()
        }




    }
    //request dialough
    private fun showRationaleDialog(
        title: String,
        message: String,
    ) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(title)
            .setMessage(message)
            .setPositiveButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
        builder.create().show()
    }
    private fun requestpermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)){
            showRationaleDialog("Drawing App","Drawing app needs to access your external storage")
        }else{
            //add permisssiions that you required
            requestpermissionn.launch(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.WRITE_EXTERNAL_STORAGE))
        }
    }






}
