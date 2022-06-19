package com.tugasakhir.remindme.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.tugasakhir.remindme.API.APIRequestData;
import com.tugasakhir.remindme.API.RetroServer;
import com.tugasakhir.remindme.Activity.MainActivity;
import com.tugasakhir.remindme.Activity.UbahActivity;
import com.tugasakhir.remindme.Model.DataModel;
import com.tugasakhir.remindme.Model.ResponseModel;
import com.tugasakhir.remindme.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterData extends RecyclerView.Adapter<AdapterData.HolderData>{
    private Context ctx;
    private List<DataModel> listData;
    private List<DataModel> listTodo;
    private int idTodo;

    public AdapterData(Context ctx, List<DataModel> listData) {
        this.ctx = ctx;
        this.listData = listData;
    }

    @NonNull
    @Override
    public HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
        HolderData holder = new HolderData(layout);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull HolderData holder, int position) {
        DataModel dm = listData.get(position);

        holder.tvId.setText(String.valueOf(dm.getId()));
        holder.tvTitle.setText(dm.getTitle());
        holder.tvDescription.setText(dm.getDescription());
        holder.tvDate.setText(dm.getDate());
    }

    @Override
    public int getItemCount() {
        return listData.size();

    }

    public class HolderData extends RecyclerView.ViewHolder {
        TextView tvId, tvTitle, tvDescription, tvDate;

        public HolderData(@NonNull View itemView) {
            super(itemView);

            tvId = itemView.findViewById(R.id.tv_id);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvDescription = itemView.findViewById(R.id.tv_description);
            tvDate = itemView.findViewById(R.id.tv_date);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    AlertDialog.Builder dialogPesan = new AlertDialog.Builder(ctx);
                    dialogPesan.setMessage("Pilih Operasi yang Akan Dilakukan");
                    dialogPesan.setTitle("Perhatian");
                    dialogPesan.setIcon(R.mipmap.ic_launcher_round);
                    dialogPesan.setCancelable(true);

                    idTodo = Integer.parseInt(tvId.getText().toString());

                    dialogPesan.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            deleteData();
                            dialogInterface.dismiss();
                            Handler hand = new Handler();
                            hand.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    ((MainActivity) ctx).retrieveData();
                                }
                            }, 1000);
                        }
                    });

                    dialogPesan.setNegativeButton("Ubah", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            getData();
                            dialogInterface.dismiss();
                        }
                    });

                    dialogPesan.show();

                    return false;
                }
            });
        }

        private void deleteData(){
            APIRequestData ardData = RetroServer.konekRetrofit().create(APIRequestData.class);
            Call<ResponseModel> hapusData = ardData.ardDeleteData(idTodo);

            hapusData.enqueue(new Callback<ResponseModel>() {
                @Override
                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                    int kode = response.body().getKode();
                    String pesan = response.body().getPesan();

                    Toast.makeText(ctx, "Kode : "+kode+" | Pesan : "+pesan, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<ResponseModel> call, Throwable t) {
                    Toast.makeText(ctx, "Gagal Menghubungi Server : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void getData(){
            APIRequestData ardData = RetroServer.konekRetrofit().create(APIRequestData.class);
            Call<ResponseModel> ambilData = ardData.ardGetData(idTodo);

            ambilData.enqueue(new Callback<ResponseModel>() {
                @Override
                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                    int kode = response.body().getKode();
                    String pesan = response.body().getPesan();
                    listTodo = response.body().getData();

                    int varIdLaundry = listTodo.get(0).getId();
                    String varTitle = listTodo.get(0).getTitle();
                    String varDescription = listTodo.get(0).getDescription();
                    String varDate = listTodo.get(0).getDate();

                    //Toast.makeText(ctx, "Kode : "+kode+" | Pesan : "+pesan+ " | Data : "+varIdLaundry+" | "+varNamaLaundry + " | "+varAlamatLaundry+" | "+varTeleponLaundry, Toast.LENGTH_SHORT).show();

                    Intent kirim = new Intent(ctx, UbahActivity.class);
                    kirim.putExtra("xId", varIdLaundry);
                    kirim.putExtra("xTitle", varTitle);
                    kirim.putExtra("xDescription", varDescription);
                    kirim.putExtra("xDate", varDate);
                    ctx.startActivity(kirim);
                }

                @Override
                public void onFailure(Call<ResponseModel> call, Throwable t) {
                    Toast.makeText(ctx, "Gagal Menghubungi Server : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
