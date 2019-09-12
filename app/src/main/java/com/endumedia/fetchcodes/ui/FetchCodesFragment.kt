package com.endumedia.fetchcodes.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.endumedia.fetchcodes.R
import com.endumedia.fetchcodes.di.Injectable
import com.endumedia.fetchcodes.repository.Status
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject


/**
 * Created by Nino on 11.09.19
 */
class FetchCodesFragment : Fragment(), Injectable {


    val tvResponseCode by lazy { view?.findViewById<TextView>(R.id.tvResponseCode) }
    val tvResponseCodeCount by lazy { view?.findViewById<TextView>(R.id.tvTimesFetched) }
    val pbLoading by lazy { view?.findViewById<View>(R.id.pbLoading) }
    val btFetch by lazy { view?.findViewById<Button>(R.id.btFetch) }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val model by lazy {
        ViewModelProviders.of(this, viewModelFactory)
            .get(FetchCodesViewModel::class.java) }

    override fun onCreateView(inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_fetch_codes, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        model.responseCode.observe(this, Observer { result ->
            result?.let {
                tvResponseCode?.text = result.responseCode
            }
        })

        model.responseCodeCount.observe(this, Observer { count ->
            tvResponseCodeCount?.text = count.toString()
        })

        model.networkState.observe(this, Observer { state ->
            when (state.status) {
                Status.RUNNING -> {
                    pbLoading?.visibility = View.VISIBLE
                    btFetch?.isEnabled = false
                }
                Status.SUCCESS, Status.FAILED -> {
                    pbLoading?.visibility = View.INVISIBLE
                    btFetch?.isEnabled = true
                    // If its error, message should not be empty
                    state.msg?.let {
                        Snackbar.make(view, it, Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        })

        btFetch?.setOnClickListener { model.fetch() }
    }
}