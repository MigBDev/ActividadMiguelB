package com.example.appmenu;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PanelAdminActivity extends AppCompatActivity {

    EditText edtUsername, edtPassword;
    LinearLayout layoutListaUsuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_panel_admin);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        edtUsername          = findViewById(R.id.edtAdminUsername);
        edtPassword          = findViewById(R.id.edtAdminPassword);
        layoutListaUsuarios  = findViewById(R.id.layoutListaUsuarios);
    }

    // ── Crear usuario de login ─────────────────────────────────────────────────
    public void crearUsuarioLogin(View v) {
        String username = edtUsername.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Completa usuario y contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

        // Verificar que no exista ya ese username con forEach
        final boolean[] existe = {false};
        LoginMenuActivity.usuariosLogin.forEach(u -> {
            if (u.getUsername().equals(username)) existe[0] = true;
        });

        if (existe[0]) {
            Toast.makeText(this, "Ese usuario ya existe", Toast.LENGTH_SHORT).show();
            return;
        }

        UsuarioLogin nuevo = new UsuarioLogin(username, password);
        LoginMenuActivity.usuariosLogin.add(nuevo);

        edtUsername.setText("");
        edtPassword.setText("");
        Toast.makeText(this, "Usuario creado: " + username, Toast.LENGTH_SHORT).show();
    }

    // ── Lista de todos los usuarios ────────────────────────────────
    public void verUsuarios(View v) {
        layoutListaUsuarios.removeAllViews();

        if (LoginMenuActivity.usuariosLogin.isEmpty()) {
            TextView tv = new TextView(this);
            tv.setText("No hay usuarios creados aún.");
            tv.setPadding(0, 8, 0, 8);
            layoutListaUsuarios.addView(tv);
            return;
        }


        LoginMenuActivity.usuariosLogin.forEach(u -> {
            TextView tv = new TextView(this);
            tv.setText("Usuario: " + u.getUsername() + "   |   Contraseña: " + u.getPassword());
            tv.setPadding(0, 10, 0, 10);
            tv.setTextSize(14f);
            layoutListaUsuarios.addView(tv);
        });
    }

    public void regresar(View v) { finish(); }
}