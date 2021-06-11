package ru.geekbrains.imageconverter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter
import ru.geekbrains.imageconverter.data.Image
import ru.geekbrains.imageconverter.databinding.ActivityMainBinding
import ru.geekbrains.imageconverter.presenter.MainPresenter
import ru.geekbrains.imageconverter.presenter.MainView
import ru.geekbrains.imageconverter.ui.ImageConverter
import ru.geekbrains.imageconverter.ui.ImagePicker
import ru.geekbrains.imageconverter.utils.createAlertDialog

class MainActivity : MvpAppCompatActivity(), MainView {

    private lateinit var alertDialog: AlertDialog
    private var disposable: Disposable? = null
    private val presenter: MainPresenter by moxyPresenter {
        MainPresenter(ImageConverter(this), AndroidSchedulers.mainThread())
    }
    private var binding: ActivityMainBinding? = null
    private val activityLauncher = registerForActivityResult(MainActivityContract()) { data ->
        getImage(data)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
    }

    override fun setImage(image: Image?) {
        if (image != null)
            binding?.imageView?.setImageBitmap(image.bitmap)
    }

    override fun init() {
        binding?.chooseButton?.setOnClickListener {
            presenter.convertImage()
        }
        binding?.saveButton?.setOnClickListener {
            presenter.savePngImage()
        }
    }

    override fun openImage() {
        activityLauncher.launch(null)
    }

    private fun getImage(data: Intent?) {
        val imagePicker = ImagePicker()
        disposable = imagePicker.pick(data, contentResolver)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ image ->
                    successPickImage(image)
                }, { throwable ->
                    showError(throwable.localizedMessage)
                })
    }

    private fun successPickImage(image: Image?) {
        presenter.setImage(image)
        Toast.makeText(this, getString(R.string.success), Toast.LENGTH_SHORT).show()
    }

    override fun showProgressConversion() {
        alertDialog = createAlertDialog(
                getString(R.string.loading),
                getString(R.string.loading_message)
        ) { presenter.stopConvert() }
        alertDialog.show()
    }

    override fun showSuccess() {
        Toast.makeText(this, getString(R.string.success_convert), Toast.LENGTH_SHORT).show()
    }

    override fun hideProgressConversion() {
        alertDialog.dismiss()
    }

    override fun showError(errorMessage: String?) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
    }

    override fun showCancelMessage() {
        Toast.makeText(this, getString(R.string.cancel_message), Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        binding = null
        if (disposable != null)
            disposable?.dispose()
        disposable = null
        super.onDestroy()
    }
}