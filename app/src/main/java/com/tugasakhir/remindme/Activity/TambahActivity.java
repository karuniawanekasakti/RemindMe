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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tugasakhir.remindme.API.APIRequestData;
import com.tugasakhir.remindme.API.RetroServer;
import com.tugasakhir.remindme.Model.ResponseModel;
import com.tugasakhir.remindme.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.allyants.notifyme.NotifyMe;

import java.time.DayOfWeek;
import java.time.Month;
import java.time.Year;
import java.util.Calendar;

public class TambahActivity extends AppCompatActivity {

//    extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener

    Calendar now = Calendar.getInstance();
    int mHour,mMinute;
    private EditText etTitle, etDescription, etTime,etDate;
    private FloatingActionButton fabSimpan;
    private String title, description, date;
    private Button btnNotify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah);

        etTitle = findViewById(R.id.et_title);
        etDescription = findViewById(R.id.et_description);
        etTime = findViewById(R.id.et_time);
        etDate = findViewById(R.id.et_date);
        fabSimpan = findViewById(R.id.fabSimpan);

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);


        etTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        TambahActivity.this,
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
                        TambahActivity.this, new DatePickerDialog.OnDateSetListener() {
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

        fabSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = etTitle.getText().toString();
                description = etDescription.getText().toString();
                date = etDate.getText().toString();

                if(title.trim().equals("")){
                    etTitle.setError("Title Harus Diisi");
                }
                else if(description.trim().equals("")){
                    etDescription.setError("Description Harus Diisi");
                }
                else if(date.trim().equals("")){
                    etTime.setError("Date Harus Diisi");
                }
                else{
                    createData();
                }
            }
        });
    }

    private void createData(){
        APIRequestData ardData = RetroServer.konekRetrofit().create(APIRequestData.class);
        Call<ResponseModel> simpanData = ardData.ardCreateData(title, description, date);

        simpanData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                int kode = response.body().getKode();
                String pesan = response.body().getPesan();

                Toast.makeText(TambahActivity.this, "Kode : "+kode+" | Pesan : "+pesan, Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(TambahActivity.this, "Gagal Menghubungi Server | "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

//    public void onDateSet (DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
//        now.set(Calendar.YEAR, year);
//        now.set(Calendar.MONTH, monthOfYear);
//        now.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//        tpd.show(getFragmentManager(), "TimePickerDialog");
//
//    }
//
//    public void onTimeSet (TimePickerDialog view, int hourOfDay, int minute, int second) {
//        now.set(Calendar.HOUR_OF_DAY, hourOfDay);
//        now.set(Calendar.MINUTE, minute);
//        now.set(Calendar.SECOND, second);
//
//        NotifyMe notifyMe = new NotifyMe.Builder(getApplicationContext())
//                .title(etTitle.getText().toString())
//                .content(etDescription.getText().toString())
//                .color(255,0,0,255)
//                .led_color(255,255,255,255)
//                .time(now)
//                .addAction(new Intent(),"Snooze",false)
//                .key("test")
//                .addAction(new Intent(),"Dismiss",true,false)
//                .addAction(new Intent(),"Done")
//                .large_icon(R.mipmap.ic_launcher_round)
//                .build();
//    }
}
