package com.tugasakhir.remindme.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.allyants.notifyme.NotifyMe;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tugasakhir.remindme.API.APIRequestData;
import com.tugasakhir.remindme.API.RetroServer;
import com.tugasakhir.remindme.Model.ResponseModel;
import com.tugasakhir.remindme.R;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UbahActivity extends AppCompatActivity {

    Calendar now = Calendar.getInstance();
    int mHour,mMinute;
    private int xId;
    private String xTitle, xDescription, xDate,xTime;
    private EditText etTitle, etDescription, etDate,etTime;
    private FloatingActionButton fabUbah;
    private String yTitle, yDescription, yDate,yTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah);

        Intent terima = getIntent();
        xId = terima.getIntExtra("xId", -1);
        xTitle = terima.getStringExtra("xTitle");
        xDescription = terima.getStringExtra("xDescription");
        xDate = terima.getStringExtra("xDate");
        xTime = terima.getStringExtra("xTime");

        etTitle = findViewById(R.id.et_title);
        etDescription = findViewById(R.id.et_description);
        etDate = findViewById(R.id.et_date);
        etTime = findViewById(R.id.et_time);
        fabUbah = findViewById(R.id.fabUbah);

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        etTitle.setText(xTitle);
        etDescription.setText(xDescription);
        etDate.setText(xDate);

        etTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        UbahActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                mHour = hourOfDay;
                                mMinute =  minute;
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(0,0,0,mHour,mMinute);
                                etTime.setText(DateFormat.format("hh:mm aa",calendar));
                                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                intent.putExtra("test","I am a String");
                                NotifyMe notifyMe = new NotifyMe.Builder(getApplicationContext())
                                        .title(etTitle.getText().toString())
                                        .content(etDescription.getText().toString())
                                        .color(255,0,0,255)
                                        .led_color(255,255,255,255)
                                        .time(calendar)
                                        .addAction(intent,"Snooze",false)
                                        .key("test")
                                        .addAction(new Intent(),"Dismiss",true,false)
                                        .addAction(intent,"Done")
                                        .large_icon(R.mipmap.ic_launcher_round)
                                        .build();
                            }
                        },12,0,false
                );
                timePickerDialog.updateTime(mHour,mMinute);
                timePickerDialog.show();
            }
        });

        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        UbahActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month+1;
                        String date = day+"/"+month+"/"+year;
                        etDate.setText(date);
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });

        fabUbah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yTitle = etTitle.getText().toString();
                yDescription = etDescription.getText().toString();
                yDate = etDate.getText().toString();

                updateData();
            }
        });
    }

    private void updateData(){
        APIRequestData ardData = RetroServer.konekRetrofit().create(APIRequestData.class);
        Call<ResponseModel> ubahData = ardData.ardUpdateData(xId, yTitle, yDescription, yDate);

        ubahData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                int kode = response.body().getKode();
                String pesan = response.body().getPesan();

                Toast.makeText(UbahActivity.this, "Kode : "+kode+" | Pesan : "+pesan, Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(UbahActivity.this, "Gagal Menghubungi Server | "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}