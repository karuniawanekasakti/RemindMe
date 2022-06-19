package com.tugasakhir.remindme.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tugasakhir.remindme.API.APIRequestData;
import com.tugasakhir.remindme.API.RetroServer;
import com.tugasakhir.remindme.Model.ResponseModel;
import com.tugasakhir.remindme.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UbahActivity extends AppCompatActivity {
    private int xId;
    private String xTitle, xDescription, xDate;
    private EditText etTitle, etDescription, etDate;
    private FloatingActionButton fabUbah;
    private String yTitle, yDescription, yDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah);

        Intent terima = getIntent();
        xId = terima.getIntExtra("xId", -1);
        xTitle = terima.getStringExtra("xTitle");
        xDescription = terima.getStringExtra("xDescription");
        xDate = terima.getStringExtra("xDate");

        etTitle = findViewById(R.id.et_title);
        etDescription = findViewById(R.id.et_description);
        etDate = findViewById(R.id.et_date);
        fabUbah = findViewById(R.id.fabUbah);

        etTitle.setText(xTitle);
        etDescription.setText(xDescription);
        etDate.setText(xDate);

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