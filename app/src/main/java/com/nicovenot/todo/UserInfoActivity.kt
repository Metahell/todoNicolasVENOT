package com.nicovenot.todo

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.google.android.material.snackbar.Snackbar
import com.google.modernstorage.mediastore.FileType
import com.google.modernstorage.mediastore.MediaStoreRepository
import com.google.modernstorage.mediastore.SharedPrimary
import com.nicovenot.todo.databinding.ActivityUserBinding
import com.nicovenot.todo.databinding.FragmentTaskListBinding
import com.nicovenot.todo.model.UserInfo
import com.nicovenot.todo.network.Api
import com.nicovenot.todo.viewModel.UserInfoViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
@RequiresApi(Build.VERSION_CODES.M)
class UserInfoActivity : AppCompatActivity() {

    val mediaStore by lazy { MediaStoreRepository(this) }
    private lateinit var photoUri: Uri

    private val viewModel = UserInfoViewModel();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        lifecycleScope.launchWhenStarted {
            photoUri = mediaStore.createMediaUri(
                filename = "picture.jpg",
                type = FileType.IMAGE,
                location = SharedPrimary
            ).getOrThrow()
        }

        findViewById<Button>(R.id.take_picture_button).setOnClickListener {
            launchCameraWithPermission()
        }

        findViewById<Button>(R.id.upload_image_button).setOnClickListener {
            galleryLauncher.launch("image/*")
        }

        lifecycleScope.launch {
            val info = viewModel.getInfo()
            findViewById<EditText>(R.id.name).setText(info?.lastName);
            findViewById<EditText>(R.id.fstname).setText(info?.firstName);
            findViewById<EditText>(R.id.mail).setText(info?.email);
        }

        findViewById<Button>(R.id.save_data).setOnClickListener {
            val name = findViewById<EditText>(R.id.name).text.toString()
            val fname = findViewById<EditText>(R.id.fstname).text.toString()
            val email = findViewById<EditText>(R.id.mail).text.toString()
            viewModel.updateData(UserInfo(email, fname, name))
            ok()
        }
    }

    private val cameraPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { accepted ->
            if (accepted) {
                launchCameraWithPermission()
            } else {
                showExplanation()
            }
        }

    private fun launchAppSettings() {
        // Cet intent permet d'ouvrir les param??tres de l'app (pour modifier les permissions d??j?? refus??es par ex)
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", this.packageName, null)
        )
        // ici pas besoin de v??rifier avant car on vise un ??cran syst??me:
        startActivity(intent)
    }

    private fun launchCameraWithPermission() {
        val camPermission = Manifest.permission.CAMERA
        val permissionStatus = checkSelfPermission(camPermission)
        val isAlreadyAccepted = permissionStatus == PackageManager.PERMISSION_GRANTED
        val isExplanationNeeded = shouldShowRequestPermissionRationale(camPermission)
        when {
            isAlreadyAccepted -> {
                launchCamera()
            }
            isExplanationNeeded -> {
                showExplanation()
            }
            else -> {
                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    private fun showExplanation() {
        // ici on construit une pop-up syst??me (Dialog) pour expliquer la n??cessit?? de la demande de permission
        AlertDialog.Builder(this)
            .setMessage("On a besoin de la cam??ra, vraiment!")
            .setPositiveButton("Bon, ok") { _, _ -> launchAppSettings() }
            .setNegativeButton("Nope") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun ok() {
        AlertDialog.Builder(this)
            .setMessage("C'est enregistr?? !")
            .setPositiveButton("OK MERCI BEAUCOUP") { _, _ -> }
            .show()
    }

    private fun handleImage(imageUri: Uri) {
        viewModel.updateAvatar(contentResolver.openInputStream(imageUri)!!.readBytes())
        ok();
    }

    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { accepted ->
            if (accepted) handleImage(photoUri)
            else Snackbar.make(findViewById(R.id.upload_image_button), "??chec!", Snackbar.LENGTH_LONG)
        }


    private fun launchCamera() {
        cameraLauncher.launch(photoUri);
    }

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()){ uri: Uri? ->
        if (uri != null) {
            handleImage(uri)
        }
    }
}