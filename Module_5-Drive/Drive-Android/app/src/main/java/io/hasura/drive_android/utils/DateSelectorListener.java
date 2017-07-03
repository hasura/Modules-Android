package io.hasura.drive_android.utils;

import android.app.DatePickerDialog;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Jaison on 27/03/16.
 */
public class DateSelectorListener implements View.OnClickListener {

    Calendar myCalendar = Calendar.getInstance();
    private EditText editText;
    private String format;
    private Boolean setMinDate = false;
    private Boolean setMaxDate = false;

    public DateSelectorListener(EditText editText, Boolean setMaxDate, String format) {
        this.editText = editText;
        this.format = format;
        this.setMaxDate = setMaxDate;
    }

    public DateSelectorListener(EditText editText, String format, Boolean setMinDate) {
        this.editText = editText;
        this.format = format;
        this.setMinDate = setMinDate;
    }

    @Override
    public void onClick(View v) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(editText.getContext(), onDateSetListener, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));

        if (setMinDate)
            datePickerDialog.getDatePicker().setMinDate(new Date().getTime());

        if (setMaxDate)
            datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());

        datePickerDialog.show();
    }

    DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            String myFormat = format;
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);
            editText.setText(sdf.format(myCalendar.getTime()));
        }
    };
}
