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
import com.pachacama.denunciasapp.models.Usuario;
import com.pachacama.denunciasapp.singletons.ApiServiceGenerator;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private SharedPreferences sharedPreferences;
    private RecyclerView DenunciasListPersonal;
    private Integer id;
    private TextView user_actual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        id = sharedPreferences.getInt("id", 99999);
        ShowUser(id);

        String user_act = sharedPreferences.getString("fullname",null);
        user_actual = findViewById(R.id.usuario_actual);
        user_actual.setText(user_act);

        DenunciasListPersonal = findViewById(R.id.recyclerview);
        DenunciasListPersonal.setLayoutManager(new LinearLayoutManager(this));
        DenunciasListPersonal.setAdapter(new DenunciasAdapter(this));
        ListarDenunciasPersonles();


    }

    private void ListarDenunciasPersonles() {
        ApiService service = ApiServiceGenerator.createService(ApiService.class);

        Call<List<Denuncia>> call = service.obtenerListaPersonalDenuncias(id);

        call.enqueue(new Callback<List<Denuncia>>() {
            @Override
            public void onResponse(Call<List<Denuncia>> call, Response<List<Denuncia>> response) {
                try {

                    int statusCode = response.code();
                    Log.d(TAG, "HTTP status code: " + statusCode);

                    if (response.isSuccessful()) {

                        List<Denuncia> denuncias = response.body();
                        DenunciasAdapter denunciasAdapter = (DenunciasAdapter) DenunciasListPersonal.getAdapter();
                        denunciasAdapter.setDenuncias(denuncias);
                        denunciasAdapter.notifyDataSetChanged();

                    } else {
                        Log.e(TAG, "onError: " + response.errorBody().string());
                        throw new Exception("Error en el servicio");
                    }

                } catch (Throwable t) {
                    try {
                        Log.e(TAG, "onThrowable: " + t.toString(), t);
                        Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    }catch (Throwable x){}
                }
            }

            @Override
            public void onFailure(Call<List<Denuncia>> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.toString());
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }

        });

    }


    private void ShowUser(Integer id) {
        ApiService service = ApiServiceGenerator.createService(ApiService.class);
        Call<Usuario> call;
        call = service.showUser(id);

        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                try {
                    int statusCode = response.code();
                    Log.d(TAG, "HTTP status code: " + statusCode);

                    if (response.isSuccessful()) {
                        Usuario usuario = response.body();
                        Log.d(TAG, "Usuario : " + usuario.getNombres());
                        Toast.makeText(MainActivity.this, "Bienvenido "+usuario.getNombres(), Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e(TAG, "onError: " + response.errorBody().string());
                        throw new Exception("Error en el servicio");
                    }

                } catch (Throwable t) {
                    try {
                        Log.e(TAG, "onThrowable: " + t.toString(), t);
                        Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    }catch (Throwable x){}
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.toString());
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    public void callLogout(View view) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("islogged").apply();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    private static final int REGISTER_FORM_REQUEST = 100;

    public void showRegister(View view) {
        startActivityForResult(new Intent(this, RegisterDenunciaActivity.class), REGISTER_FORM_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REGISTER_FORM_REQUEST) {
            ListarDenunciasPersonles();
        }
    }
}
