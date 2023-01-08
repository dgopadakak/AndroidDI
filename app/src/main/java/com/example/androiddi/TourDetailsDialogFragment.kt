package com.example.androiddi

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

class TourDetailsDialogFragment: android.app.DialogFragment()
{
    private val exceptionTag = "PharmacyDetailsDialogFragment"

    interface OnInputListenerSortId
    {
        fun sendInputSortId(sortId: Int)
    }

    lateinit var onInputListenerSortId: OnInputListenerSortId

    private lateinit var textViewCountryTitle: TextView
    private lateinit var textViewCountry: TextView
    private lateinit var textViewDurationTitle: TextView
    private lateinit var textViewDuration: TextView
    private lateinit var textViewRateTitle: TextView
    private lateinit var textViewRate: TextView
    private lateinit var textViewStartDateTitle: TextView
    private lateinit var textViewStartDate: TextView
    private lateinit var textViewEndDateTitle: TextView
    private lateinit var textViewEndDate: TextView
    private lateinit var textViewMaxCostTitle: TextView
    private lateinit var textViewMaxCost: TextView
    private lateinit var textViewIsAvailableTitle: TextView
    private lateinit var textViewIsAvailable: TextView
    private lateinit var textViewCommentTitle: TextView
    private lateinit var textViewComment: TextView
    private lateinit var buttonDel: Button
    private lateinit var buttonEdit: Button
    private lateinit var buttonOk: Button
    private lateinit var textViewCurrSort: TextView

    private var currentIdForSort: Int = -1

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View
    {
        val view: View = inflater!!.inflate(R.layout.tour_details, container, false)
        textViewCountryTitle = view.findViewById(R.id.textViewExamNameTitle)
        textViewCountry = view.findViewById(R.id.textViewExamName)
        textViewDurationTitle = view.findViewById(R.id.textViewTeacherNameTitle)
        textViewDuration = view.findViewById(R.id.textViewTeacherName)
        textViewRateTitle = view.findViewById(R.id.textViewAuditoryTitle)
        textViewRate = view.findViewById(R.id.textViewAuditory)
        textViewStartDateTitle = view.findViewById(R.id.textViewDateTitle)
        textViewStartDate = view.findViewById(R.id.textViewDate)
        textViewEndDateTitle = view.findViewById(R.id.textViewTimeTitle)
        textViewEndDate = view.findViewById(R.id.textViewTime)
        textViewMaxCostTitle = view.findViewById(R.id.textViewPeopleTitle)
        textViewMaxCost = view.findViewById(R.id.textViewPeople)
        textViewIsAvailableTitle = view.findViewById(R.id.textViewAbstractTitle)
        textViewIsAvailable = view.findViewById(R.id.textViewAbstract)
        textViewCommentTitle = view.findViewById(R.id.textViewCommentTitle)
        textViewComment = view.findViewById(R.id.textViewComment)
        buttonDel = view.findViewById(R.id.button_details_delete)
        buttonEdit = view.findViewById(R.id.button_details_edit)
        buttonOk = view.findViewById(R.id.button_details_ok)
        textViewCurrSort = view.findViewById(R.id.textViewCurrentSort)

        textViewCountryTitle.setOnLongClickListener { setSortId(0) }
        textViewCountry.setOnLongClickListener { setSortId(0) }
        textViewDurationTitle.setOnLongClickListener { setSortId(1) }
        textViewDuration.setOnLongClickListener { setSortId(1) }
        textViewRateTitle.setOnLongClickListener { setSortId(2) }
        textViewRate.setOnLongClickListener { setSortId(2) }
        textViewStartDateTitle.setOnLongClickListener { setSortId(3) }
        textViewStartDate.setOnLongClickListener { setSortId(3) }
        textViewEndDateTitle.setOnLongClickListener { setSortId(4) }
        textViewEndDate.setOnLongClickListener { setSortId(4) }
        textViewMaxCostTitle.setOnLongClickListener { setSortId(5) }
        textViewMaxCost.setOnLongClickListener { setSortId(5) }
        textViewIsAvailableTitle.setOnLongClickListener { setSortId(6) }
        textViewIsAvailable.setOnLongClickListener { setSortId(6) }
        textViewCommentTitle.setOnLongClickListener { setSortId(7) }
        textViewComment.setOnLongClickListener { setSortId(7) }

        buttonDel.setOnClickListener { returnDel() }
        buttonEdit.setOnClickListener { returnEdit() }
        buttonOk.setOnClickListener { returnIdForSort() }

        val arguments: Bundle = getArguments()
        textViewCountry.text = arguments.getString("country")
        textViewDuration.text = arguments.getString("duration")
        textViewRate.text = arguments.getString("rate")
        textViewStartDate.text = arguments.getString("startDate")
        textViewEndDate.text = arguments.getString("endDate")
        textViewMaxCost.text = arguments.getString("cost")
        if (arguments.getString("isAvailable") == "1")
        {
            textViewIsAvailable.text = "да"
        }
        else
        {
            textViewIsAvailable.text = "нет"
        }
        textViewComment.text = arguments.getString("comment")
        if (arguments.getString("connection") != "1")
        {
            buttonDel.isEnabled = false
            buttonEdit.isEnabled = false
        }

        return view
    }

    override fun onAttach(activity: Activity?)
    {
        super.onAttach(activity)
        try {
            onInputListenerSortId = getActivity() as OnInputListenerSortId
        }
        catch (e: ClassCastException)
        {
            Log.e(exceptionTag, "onAttach: ClassCastException: " + e.message)
        }
    }

    private fun setSortId(id: Int): Boolean
    {
        currentIdForSort = id
        if (currentIdForSort == 0)
        {
            textViewCurrSort.text = "Сортировка по стране"
        }
        else if (currentIdForSort == 1)
        {
            textViewCurrSort.text = "Сортировка по длительности"
        }
        else if (currentIdForSort == 2)
        {
            textViewCurrSort.text = "Сортировка по рейтингу отеля"
        }
        else if (currentIdForSort == 3)
        {
            textViewCurrSort.text = "Сортировка по дате начала"
        }
        else if (currentIdForSort == 4)
        {
            textViewCurrSort.text = "Сортировка по дате окончания"
        }
        else if (currentIdForSort == 5)
        {
            textViewCurrSort.text = "Сортировка по цене"
        }
        else if (currentIdForSort == 6)
        {
            textViewCurrSort.text = "Сортировка по доступности"
        }
        else if (currentIdForSort == 7)
        {
            textViewCurrSort.text = "Сортировка по описанию"
        }
        val vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(VibrationEffect.createOneShot(200
            , VibrationEffect.DEFAULT_AMPLITUDE))
        return true
    }

    private fun returnIdForSort()
    {
        onInputListenerSortId.sendInputSortId(currentIdForSort)
        dialog.dismiss()
    }

    private fun returnDel()
    {
        currentIdForSort = 8
        returnIdForSort()
    }

    private fun returnEdit()
    {
        currentIdForSort = 9
        returnIdForSort()
    }
}