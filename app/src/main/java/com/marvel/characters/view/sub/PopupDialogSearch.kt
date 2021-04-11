package com.marvel.characters.view.sub

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.marvel.characters.R
import com.marvel.characters.adapter.RecyclerCharacterSearchAdapter
import com.marvel.characters.databinding.PopupDialogSearchBinding
import com.marvel.characters.model.CharacterModel
import com.marvel.characters.observer.OnRecyclerItemClickListener
import com.marvel.characters.remoteConnection.JsonParser
import com.marvel.characters.remoteConnection.URL
import com.marvel.characters.remoteConnection.remoteService.RemoteCallback
import com.marvel.characters.remoteConnection.remoteService.startGetMethodUsingRX
import com.marvel.characters.remoteConnection.setup.getDefaultParams
import com.marvel.characters.view.activity.baseActivity.BaseActivity
import io.reactivex.disposables.CompositeDisposable


class PopupDialogSearch : BaseDialogFragment() {

    internal var activity: BaseActivity? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity) {
            activity = context
        }
    }

    lateinit var binding: PopupDialogSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (activity == null) activity = getActivity() as BaseActivity?
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.popup_dialog_search, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    internal lateinit var dialog: Dialog

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (activity == null)
            activity = requireActivity() as BaseActivity?
        dialog = Dialog(requireActivity())
        dialog = object : Dialog(requireActivity(), R.style.FullWidthDialogThemeWithoutAnimation) {}

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawableResource(R.color.transparent)
        dialog.window!!.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
        )
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window!!.setGravity(Gravity.CENTER_HORIZONTAL or Gravity.TOP)
        dialog.setCanceledOnTouchOutside(true)

        //hide navigation bar flags code
        dialog.window!!.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        )
        dialog.show()
        val uiOptions: Int = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
        dialog.window!!.decorView.systemUiVisibility = uiOptions
        dialog.window!!.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
        return dialog
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initializeViews()
        setListener()
    }

    var characterModels: ArrayList<CharacterModel>? = ArrayList()
    private fun initializeViews() {
        binding.progressBarPopupdialogSearch.visibility = View.GONE
    }

    private fun setList() {
        binding.listViewPopupdialogSearch.layoutManager =
            GridLayoutManager(activity, 1, RecyclerView.VERTICAL, false)
        binding.listViewPopupdialogSearch.adapter = RecyclerCharacterSearchAdapter(
            characterModels!!, object : OnRecyclerItemClickListener {
                override fun onRecyclerItemClickListener(position: Int) {
                }
            })
    }

    private fun setListener() {
        dialog.setOnDismissListener {
            // your code after dissmiss dialog
            compositeDisposable.dispose()
        }

        binding.tvCancel.setOnClickListener {
            dismissAllowingStateLoss()
        }
        binding.ivSearchPopupdialogSearch.setOnClickListener {
        }

        binding.edttxtSearchPopupdialogSearch.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH
                && binding.edttxtSearchPopupdialogSearch.text.toString().isNotEmpty()
            ) {
                return@OnEditorActionListener true
            }
            false
        })

        binding.edttxtSearchPopupdialogSearch.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(
                s: CharSequence, start: Int, before: Int,
                count: Int
            ) {
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {
            }

            override fun afterTextChanged(editable: Editable) {
                val text: String = binding.edttxtSearchPopupdialogSearch.text.toString().trim()
                if (text.isEmpty()) {
                    binding.ivSearchPopupdialogSearch.isEnabled = false
                    binding.progressBarPopupdialogSearch.visibility = View.GONE
                } else {
                    binding.ivSearchPopupdialogSearch.isEnabled = true
                    if (text.length >= 3) {
                        //send search words to server
                        getSearchDataApi(binding.edttxtSearchPopupdialogSearch.text.toString())
                    }
                }
            }
        })
    }

    var compositeDisposable = CompositeDisposable()

    fun getSearchDataApi(keyWord: String) {
        var params = getDefaultParams(activity?.application!!, HashMap())
        params["limit"] = 10
        //items to skip
        params["offset"] = 0
        params["nameStartsWith"] = keyWord
        
        compositeDisposable.add(
            startGetMethodUsingRX(
                URL.getCharactersUrl(),
                params,
                object : RemoteCallback {
                    override fun onStartConnection() {
                        binding.progressBarPopupdialogSearch.visibility = View.VISIBLE
                    }

                    override fun onFailureConnection(errorMessage: Any?) {
                        binding.progressBarPopupdialogSearch.visibility = View.GONE
                    }

                    override fun onSuccessConnection(response: Any?) {
                        binding.progressBarPopupdialogSearch.visibility = View.GONE
                        try {
                            var responseModel =
                                JsonParser().getCharactersListResponseModel(response.toString())
                            if (responseModel != null) {
                                characterModels = responseModel.data.results
                                if (!characterModels.isNullOrEmpty()) {
                                    setList()
                                }
                            } else {
                            }
                        } catch (e: Exception) {
                        }
                    }

                    override fun onLoginAgain(errorMessage: Any?) {
                        onLoginAgain(errorMessage)
                    }
                })
        )
    }

}