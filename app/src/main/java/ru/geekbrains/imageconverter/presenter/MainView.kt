package ru.geekbrains.imageconverter.presenter

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType
import ru.geekbrains.imageconverter.data.Image

@StateStrategyType(AddToEndSingleStrategy::class)
interface MainView : MvpView {
    fun setImage(image: Image?)
    fun init()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun openImage()
    fun showProgressConversion()
    fun showSuccess()
    fun hideProgressConversion()
    fun showError(errorMessage: String?)
    fun showCancelMessage()
}