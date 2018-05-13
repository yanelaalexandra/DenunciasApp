package com.pachacama.denunciasapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.pachacama.denunciasapp.R;
import com.pachacama.denunciasapp.interfaces.ApiService;
import com.pachacama.denunciasapp.models.Usuario;
import com.pachacama.denunciasapp.singletons.ApiServiceGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private static final String TAG = LoginActivity.class.getSimpleName();
    private EditText usernameET;
    private EditText passwordET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameET = findViewById(R.id.username_input);
        passwordET = findViewById(R.id.password_input);

        //---------------------------------------------------------------------------------------------
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        String username = sharedPreferences.getString("username", null);

        if(username != null){
            usernameET.setText(username);
            passwordET.requestFocus();
        }
        if(sharedPreferences.getBoolean("islogged", false)){
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            String tipo = sharedPreferences.getString("tipo", "3");
            if(tipo.equals("1")){
                goDashboard2();
                finish();
            }else{
                goDashboard();
                finish();
            }
        }
    }

    public void callLogin(View view) {
        final String user = usernameET.getText().toString();
        String pass = passwordET.getText().toString();

        if (user.isEmpty() || pass .isEmpty()) {
            Toast.makeText(this, "Completar todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService service = ApiServiceGenerator.createService(ApiService.class);
        Call<Usuario> call;
        call = service.login(user, pass);

        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                try {

                    int statusCode = response.code();
                    Log.d(TAG, "HTTP status code: " + statusCode);

                    if (response.isSuccessful()) {

                        Usuario usuario = response.body();

                       SharedPreferences.Editor editor = sharedPreferences.edit();
                        boolean success = editor
                                .putString("username", usuario.getUsername())
                                .putString("fullname" ,usuario.getNombres())
                                .putInt("id", usuario.getId())
                                .putString("tipo", usuario.getTipo())
                                .putBoolean("islogged", true)
                                .commit();

                            if(usuario.getTipo().equals("1")){
                                goDashboard2();
                                finish();
                            }else {
                                goDashboard();
                                finish();
                            }

                    } else {
                        Log.e(TAG, "onError: " + response.errorBody().string());
                        throw new Exception("Usuario o Clave incorrectos");
                    }

                } catch (Throwable t) {
                    try {
                        Log.e(TAG, "onThrowable: " + t.toString(), t);
                        Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    } catch (Throwable x) {
                    }
                }
            }
            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {

            }
        });





    }


    private void goDashboard() {
        startActivity( new Intent(LoginActivity.this, MainActivity.class));
    }

    private void goDashboard2() {
        startActivity(new Intent(LoginActivity.this, Main2Activity.class));
    }

    public void callSingUp(View view) {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
    }




}
