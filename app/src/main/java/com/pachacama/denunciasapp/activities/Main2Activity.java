package com.pachacama.denunciasapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.pachacama.denunciasapp.R;
import com.pachacama.denunciasapp.adapter.DenunciasAdapter;
import com.pachacama.denunciasapp.interfaces.ApiService;
import com.pachacama.denunciasapp.models.Denuncia;
import com.pachacama.denunciasapp.singletons.ApiServiceGenerator;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Main2Activity extends AppCompatActivity {

    private static final String TAG = Main2Activity.class.getSimpleName();
    private SharedPreferences sharedPreferences;
    private RecyclerView DenunciasList;
    private TextView user_actual;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);



        DenunciasList = findViewById(R.id.recyclerview);
        DenunciasList.setLayoutManager(new LinearLayoutManager(this));
        DenunciasList.setAdapter(new DenunciasAdapter(this));

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String user_act = sharedPreferences.getString("fullname",null);
        user_actual = findViewById(R.id.usuario_actual);
        user_actual.setText(user_act);

        ListarDenunciasTodas();

    }

    private void ListarDenunciasTodas() {
        ApiService service = ApiServiceGenerator.createService(ApiService.class);

        Call<List<Denuncia>> call = service.obtenerListaGeneralDenuncias();

        call.enqueue(new Callback<List<Denuncia>>() {
            @Override
            public void onResponse(Call<List<Denuncia>> call, Response<List<Denuncia>> response) {
                try {

                    int statusCode = response.code();
                    Log.d(TAG, "HTTP status code: " + statusCode);

                    if (response.isSuccessful()) {

                        List<Denuncia> denuncias = response.body();
                        DenunciasAdapter denunciasAdapter = (DenunciasAdapter) DenunciasList.getAdapter();
                        denunciasAdapter.setDenuncias(denuncias);
                        denunciasAdapter.notifyDataSetChanged();

                    } else {
                        Log.e(TAG, "onError: " + response.errorBody().string());
                        throw new Exception("Error en el servicio");
                    }

                } catch (Throwable t) {
                    try {
                        Log.e(TAG, "onThrowable: " + t.toString(), t);
                        Toast.makeText(Main2Activity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    }catch (Throwable x){}
                }
            }

            @Override
            public void onFailure(Call<List<Denuncia>> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.toString());
                Toast.makeText(Main2Activity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }

        });

    }

    public void callLogout(View view) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("islogged").apply();
        startActivity(new Intent(this, LoginActivity.class));
        finish();

    }
}
