package ru.geekbrains.imageconverter.presenter

import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.Disposable
import moxy.MvpPresenter
import ru.geekbrains.imageconverter.data.Image
import ru.geekbrains.imageconverter.ui.ImageConverter

class MainPresenter(
    private val converter: ImageConverter,
    private val uiScheduler: Scheduler
) : MvpPresenter<MainView>() {

    private var disposable: Disposable? = null
    private var image: Image? = null

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
        viewState.setImage(image)
    }

    fun convertImage() {
        viewState.openImage()
    }

    fun setImage(image: Image?) {
        this.image = image
        viewState.setImage(image)
    }

    fun savePngImage() {
        viewState.showProgressConversion()
        disposable = converter.convert(image)
            .observeOn(uiScheduler)
            .subscribe({
                successSavePngImage()
            }, { throwable ->
                errorSavePngImage(throwable.localizedMessage)
            })
    }

    private fun successSavePngImage() {
        viewState.hideProgressConversion()
        viewState.showSuccess()
    }

    private fun errorSavePngImage(message: String?) {
        viewState.hideProgressConversion()
        viewState.showError(message)
    }

    fun stopConvert() {
        if (disposable != null)
            disposable?.dispose()
        disposable = null
        viewState.showCancelMessage()
    }
}