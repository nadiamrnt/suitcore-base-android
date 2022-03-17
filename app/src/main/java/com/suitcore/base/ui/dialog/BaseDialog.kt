package com.suitcore.base.ui.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.suitcore.R
import com.suitcore.databinding.BaseDialogBinding

class BaseDialog private constructor(var context: Context,
                                     var type: Type,
                                     var dismiss: Boolean = false,
                                     var title: String? = null,
                                     var content: String? = null,
                                     var submitBtnText: String? = null,
                                     var cancelBtnText: String? = null,
                                     var singleButton: Boolean = true,
                                     var basicButton: Boolean = true,
                                     var listener: BaseDialogInterface? = null,
                                     var imgContent: Int?,
                                     var hideAllButton: Boolean = false,
                                     var showPanelButton: Boolean = false
) {

    enum class Type {
        BASE, ALERT, CONFIRM,
    }

    data class BuildBaseDialog(
            var dismiss: Boolean = false,
            var content: String? = "Please Wait"
    ){
        fun onBackPressedDismiss(dismiss: Boolean = false) = apply { this.dismiss = dismiss  }
        fun setContent(content: String?) = apply { this.content = content }
        fun build(context: Context) = BaseDialog(context, Type.BASE, dismiss, null, content,
                null, null, singleButton = true, basicButton = true,
                listener = null, imgContent = null, hideAllButton = false, showPanelButton = false)
    }

    data class BuildAlertDialog(
            var dismiss: Boolean = false,
            var title: String? = null,
            var content: String? = null,
            var submitBtnText: String? = null,
            var basicButton: Boolean = true,
            var panelButton: Boolean = false,
            var hideButton: Boolean = false,
            var listener: BaseDialogInterface? = null,
            var imgContent: Int? = null
    ){
        fun onBackPressedDismiss(dismiss: Boolean = false) = apply { this.dismiss = dismiss  }
        fun setTitle(title: String?) = apply { this.title = title }
        fun setContent(content: String?) = apply { this.content = content }
        fun useMaterialButton(basicButton: Boolean) = apply { this.basicButton = basicButton }
        fun setListener(listener: BaseDialogInterface?) = apply{ this.listener = listener  }
        fun setSubmitButtonText(textSubmit: String?) = apply { this.submitBtnText = textSubmit}
        fun setImageContent(imgContent: Int?) = apply { this.imgContent = imgContent }
        fun showPanelButton(panelButton: Boolean) = apply { this.panelButton = panelButton }
        fun hideAllButton(hideButton: Boolean) = apply { this.hideButton = hideButton }
        fun build(context: Context) = BaseDialog(context, Type.ALERT, dismiss, title, content,
                submitBtnText, null, singleButton = true, basicButton = basicButton,
                listener = null, imgContent = imgContent, hideAllButton = hideButton,  showPanelButton = panelButton)
    }

    data class BuildConfirmationDialog(
            var dismiss: Boolean = false,
            var title: String? = null,
            var content: String? = null,
            var submitBtnText: String? = null,
            var cancelBtnText: String? = null,
            var listener: BaseDialogInterface? = null,
            var singleButton: Boolean = true,
            var basicButton: Boolean = true,
            var panelButton: Boolean = false,
            var hideButton: Boolean = false,
            var imgContent: Int? = null
    ){
        fun onBackPressedDismiss(dismiss: Boolean = false) = apply { this.dismiss = dismiss  }
        fun setTitle(title: String?) = apply { this.title = title }
        fun setContent(content: String?) = apply { this.content = content }
        fun setListener(listener: BaseDialogInterface?) = apply{ this.listener = listener  }
        fun useMaterialButton(basicButton: Boolean) = apply { this.basicButton = basicButton }
        fun setSingleButton(singleButton: Boolean) = apply { this.singleButton = singleButton }
        fun setSubmitButtonText(TextBtn: String?) = apply { this.submitBtnText = TextBtn}
        fun setCancelButtonText(TextBtn: String?) = apply { this.cancelBtnText = TextBtn}
        fun showPanelButton(panelButton: Boolean) = apply { this.panelButton = panelButton }
        fun hideAllButton(hideButton: Boolean) = apply { this.hideButton = hideButton }
        fun setImageContent(imgContent: Int?) = apply { this.imgContent = imgContent }
        fun build(context: Context) = BaseDialog(context, Type.CONFIRM, dismiss, title,
                content, submitBtnText, cancelBtnText, singleButton, basicButton = basicButton,
                listener = listener, imgContent = imgContent,  hideAllButton = hideButton,  showPanelButton = panelButton)
    }

    init {
        when(type){
            Type.BASE -> {
                createBaseDialog()
            }
            Type.CONFIRM -> {
                createConfirmationDialog()
            }
            Type.ALERT -> {
                createAlertDialog()
            }
        }
    }

    private lateinit var binding: BaseDialogBinding

    private var dialogSpass : Dialog? = null

    private fun initDialog(){
        dialogSpass = Dialog(context)
        dialogSpass?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogSpass?.setCanceledOnTouchOutside(false)
        dialogSpass?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        binding = BaseDialogBinding.inflate(LayoutInflater.from(context))
        dialogSpass?.setContentView(binding.root)
        dialogSpass?.setOnCancelListener {if (dismiss) { dismissDialog()}}

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialogSpass?.window?.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        dialogSpass?.window?.attributes = lp
    }

    private fun createBaseDialog() {
        initDialog()
        basicDialog()
        initializeView()
    }


    private fun createAlertDialog() {
        initDialog()
        alertDialog()
        initializeView()
    }

    private fun createConfirmationDialog() {
        initDialog()
        confirmDialog()
        initializeView()
    }

    private fun initializeView() {
        binding.tvTitleDialog.visibility = if(title!=null){
            binding.tvTitleDialog.text = title
            View.VISIBLE
        }else{
            View.GONE
        }

        binding.imgContentDialog.visibility = if(imgContent!=null) {
            binding.imgContentDialog.setBackgroundResource(imgContent!!)
            View.VISIBLE
        }else {
            binding.imgContentDialog.setBackgroundResource(0)
            View.GONE
        }

        if(Type.BASE == type){
            binding.tvProgressBar.visibility = if(content!=null){
                binding.tvProgressBar.text = content
                View.VISIBLE
            }else{
                View.GONE
            }
        }else{
            binding.tvContentDialog.visibility = if(content!=null){
                binding.tvContentDialog.text = content
                View.VISIBLE
            }else{
                View.GONE
            }
        }

        binding.tvBtnSubmit.text = if(submitBtnText!=null) submitBtnText else context.getString(R.string.txt_yes)

        binding.tvBtnCancel.text = if(cancelBtnText!=null) cancelBtnText else context.getString(R.string.txt_no)


        //Listener
        binding.relSubmitDialog.setOnClickListener {
            listener?.onSubmitClick()
            dismissDialog()
        }
        binding.relCancelDialog.setOnClickListener {
            listener?.onDismissClick()
            dismissDialog()
        }

        //using basic button
        if(basicButton)
            binding.relSubmitDialog.setBackgroundResource(0)
        binding.tvBtnSubmit.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
        binding.tvBtnCancel.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
        binding.relCancelDialog.setBackgroundResource(0)


        //hide all layout button
        if(hideAllButton)
            binding.layButton.visibility = View.GONE


        //panel close
        binding.imgCloseDialog.visibility = if(showPanelButton){
            binding.imgCloseDialog.setOnClickListener {
                dismissDialog()
            }
            View.VISIBLE
        }else {
            View.GONE
        }
    }

    private fun basicDialog(){
        binding.loadingInterface.visibility = View.VISIBLE
        binding.dialogInterface.visibility = View.GONE
        binding.layButton.visibility = View.GONE
        binding.parentLayout.setBackgroundResource(0)
    }

    private fun alertDialog(){
        binding.dialogInterface.visibility = View.VISIBLE
        binding.relSubmitDialog.visibility = View.VISIBLE
        binding.relCancelDialog.visibility= View.GONE
        binding.loadingInterface.visibility = View.GONE
    }

    private fun confirmDialog(){
        binding.dialogInterface.visibility = View.VISIBLE
        binding.layButton.visibility = View.VISIBLE
        binding.relSubmitDialog.visibility = View.VISIBLE
        binding.relCancelDialog.visibility = if(!singleButton){
            View.VISIBLE
        }else{
            View.GONE
        }
        binding.relSubmitDialog.visibility = View.VISIBLE
        binding.loadingInterface.visibility = View.GONE
    }

    fun show() {
        dialogSpass?.show()
    }

    fun dismissDialog() {
        dialogSpass?.dismiss()
        if (listener != null) {
            listener?.onDismissClick()
        }
    }

    fun isShowing(): Boolean {
        return dialogSpass!!.isShowing
    }

}