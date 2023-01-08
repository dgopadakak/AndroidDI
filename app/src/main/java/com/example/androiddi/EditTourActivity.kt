package com.example.androiddi

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.util.*

class EditTourActivity : AppCompatActivity()
{
    private lateinit var editCountry: EditText
    private lateinit var editDuration: EditText
    private lateinit var editRate: EditText
    private lateinit var editStartDate: EditText
    private lateinit var editEndDate: EditText
    private lateinit var editCost: EditText
    private lateinit var editIsAvailable: EditText
    private lateinit var editComment: EditText

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_tour)

        editCountry = findViewById(R.id.editTextExamName)
        editDuration = findViewById(R.id.editTextTeacherName)
        editRate = findViewById(R.id.editTextAuditory)
        editStartDate = findViewById(R.id.editTextDate)
        editEndDate = findViewById(R.id.editTextTime)
        editCost = findViewById(R.id.editTextPeople)
        editIsAvailable = findViewById(R.id.editTextAbstract)
        editComment = findViewById(R.id.editTextComment)

        val action = intent.getSerializableExtra("action") as Int

        findViewById<Button>(R.id.button_confirm).setOnClickListener { confirmChanges(action) }

        if (action == 2)
        {
            editCountry.setText(intent.getSerializableExtra("country") as String)
            editDuration.setText(intent.getSerializableExtra("duration") as String)
            editRate.setText(intent.getSerializableExtra("rate") as String)
            editStartDate.setText(intent.getSerializableExtra("startDate") as String)
            editEndDate.setText(intent.getSerializableExtra("endDate") as String)
            editCost.setText(intent.getSerializableExtra("cost") as String)
            if (intent.getSerializableExtra("isAvailable") as String == "1")
            {
                editIsAvailable.setText("да")
            }
            else
            {
                editIsAvailable.setText("нет")
            }
            editComment.setText(intent.getSerializableExtra("comment") as String)
        }
    }

    private fun confirmChanges(action: Int)
    {
        if (editCountry.text.toString() != "" && editDuration.text.toString() != ""
            && editRate.text.toString() != "" && editStartDate.text.toString() != ""
            && editEndDate.text.toString() != "" && editCost.text.toString() != ""
            && editIsAvailable.text.toString() != "")
        {
            if (editIsAvailable.text.toString().trim().lowercase(Locale.ROOT) == "да"
                || editIsAvailable.text.toString().trim().lowercase(Locale.ROOT) == "нет")
            {
                if (isDateValid(editStartDate.text.toString().trim())
                    && isDateValid(editEndDate.text.toString().trim()))
                {
                    val intent = Intent(this@EditTourActivity,
                        MainActivity::class.java)
                    intent.putExtra("action", action)
                    intent.putExtra("country", editCountry.text.toString().trim())
                    intent.putExtra("duration", editDuration.text.toString().trim())
                    intent.putExtra("rate", editRate.text.toString().trim().toInt())
                    intent.putExtra("startDate", editStartDate.text.toString().trim())
                    intent.putExtra("endDate", editEndDate.text.toString().trim())
                    intent.putExtra("cost", editCost.text.toString().trim().toInt())
                    if (editIsAvailable.text.toString().trim().lowercase(Locale.ROOT) == "да")
                    {
                        intent.putExtra("isAvailable", 1)
                    }
                    else
                    {
                        intent.putExtra("isAvailable", 0)
                    }
                    intent.putExtra("comment", editComment.text.toString().trim())
                    setResult(RESULT_OK, intent)
                    finish()
                }
                else
                {
                    Snackbar.make(findViewById(R.id.button_confirm),
                        "Проверьте даты!", Snackbar.LENGTH_LONG)
                        .show()
                }
            }
            else
            {
                Snackbar.make(findViewById(R.id.button_confirm),
                    "Поле \"${getString(R.string.is_complicated)}\" поддерживает только " +
                            "значения \"да\" или \"нет\"!", Snackbar.LENGTH_LONG)
                    .show()
            }
        }
        else
        {
            Snackbar.make(findViewById(R.id.button_confirm),
                "Заполните обязательные поля!", Snackbar.LENGTH_LONG)
                .show()
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun isDateValid(date: String?): Boolean
    {
        val myFormat = SimpleDateFormat("dd.MM.yyyy")
        myFormat.isLenient = false
        return try
        {
            if (date != null)
            {
                myFormat.parse(date)
            }
            true
        }
        catch (e: Exception)
        {
            false
        }
    }
}