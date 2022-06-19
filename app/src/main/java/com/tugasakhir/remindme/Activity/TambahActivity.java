package com.tugasakhir.remindme.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;

public class TambahActivity extends AppCompatActivity {

//    extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener

    Calendar now = Calendar.getInstance();
    TimePickerDialog tpd;
    DatePickerDialog dpd;
    private TextView etDate;
    private EditText etTitle, etDescription;
    private FloatingActionButton fabSimpan;
    private String title, description, date;
    private Button btnNotify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah);

        etTitle = findViewById(R.id.et_title);
        etDescription = findViewById(R.id.et_description);
        etDate = findViewById(R.id.et_date);
        fabSimpan = findViewById(R.id.fabSimpan);
//        btnNotify = findViewById(R.id.btnNotify);

//        dpd = DatePickerDialog.newInstance(
//                TambahActivity.this,
//                now.get(Calendar.YEAR),
//                now.get(Calendar.MONTH),
//                now.get(Calendar.DAY_OF_MONTH)
//        );
//
//        tpd = TimePickerDialog.newInstance(
//                TambahActivity.this,
//                now.get(Calendar.HOUR_OF_DAY),
//                now.get(Calendar.MINUTE),
//                now.get(Calendar.SECOND),
//                false
//        );
//
//
//        btnNotify.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dpd.show(getFragmentManager(),"Datepickerdialog");
//            }
//        });
//
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
                    etDate.setError("Date Harus Diisi");
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
