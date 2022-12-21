package com.example.accessdata

import android.Manifest
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
/*
*In this project i used VIEW BINDING
 */
class MainActivity : AppCompatActivity() {
    private  var binding:ActivityMainBinding?=null//creating an varriable for view binding
    //It create an activity result launcher to open an intent i,e: To Open Gallery
    val opengallery:ActivityResultLauncher<Intent> =registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()){
            result->
        /**Here we get the result return from the lambda function   get the returned result from the lambda and check the result code and the data returned
         * And check the result code as (result.resultcode==RESULT_OK) and check the data to be not null (result.data!=null)
         */
        if(result.resultCode=== RESULT_OK && result.data!=null){
            binding?.background?.setImageURI(result.data?.data)
        }

    }

    /** Here we create an ActivityResultLauncher with MultiplePermissions so we can request for multiple permission like location storage etc
     * For this project we use only storage permission
     */
    val requestpermissionn:ActivityResultLauncher<Array<String>> =registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()){
        /**
         *->Here it returns a Map of permission name as key with boolean as value
         *->we have to loop through the MAP and get the value i,e MAP<String,Boolean>
         */
            permissions->permissions.entries.forEach() {

        //it.key gives us the name of the permission
        val permissionName = it.key
        //Here it it store whether our permission granted or not in boolean value i,e: True or False
        val isgranted=it.value
        //if permission is granted show a toast and perform operation i,e; ACCESSING THE GALLERY
        if(isgranted){
            Toast.makeText(this,"Permission is granted for accessing gallery",Toast.LENGTH_LONG).show()
            //Here we create an intent to pick image from external storage
            var intent=Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)//it will gives the image path
            //By using the intent launcher here we created above launch the pick intent i.e:Opening gallery
            opengallery.launch(intent)
        }else{
            //Here we use array of permission so we have to check whether the specific permission is granted or not
            if(permissionName==android.Manifest.permission.READ_EXTERNAL_STORAGE){
                Toast.makeText(this,"You denied the permission",Toast.LENGTH_LONG).show()
            }
        }

    }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)//set the content view by view binding
        //applying OnClickListener to the Acess Images Button
        binding?.btnAccess?.setOnClickListener {
            requestStoragePermission()
        }
    }
    /**
      *->It Shows rationale dialog for displaying why the app needs permission
      *->Only shown if the user has denied the permission request previously
     */
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
    //Here we create a method to request Storage permission
    private fun requestStoragePermission(){
        //Here we Check if the permission was denied and show rationale
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)){
            //Here We call the rationale dialog to tell the user why they need to allow permission request
            showRationaleDialog("Drawing App","Drawing app needs to access your external storage")
        }else{
            /*Here we can add multiple permission but for this project only add storage permission to access the data i,e:android.Manifest.permission.READ_EXTERNAL_STORAGE
             *since it is an array of permission you can add multiple permission as per your requirments
             */
            requestpermissionn.launch(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE))

        }
    }
}
