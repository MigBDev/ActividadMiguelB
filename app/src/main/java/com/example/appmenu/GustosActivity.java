package com.example.appmenu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class GustosActivity extends AppCompatActivity {


    public static final String KEY_GUSTOS = "gustos_seleccionados";

    CheckBox cbMusica, cbDeporte, cbCine, cbLectura, cbViajes,
            cbCocina, cbJuegos, cbFotografia, cbBaile, cbTeatro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_gustos);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        cbMusica       = findViewById(R.id.cbGMusica);
        cbDeporte      = findViewById(R.id.cbGDeporte);
        cbCine         = findViewById(R.id.cbGCine);
        cbLectura      = findViewById(R.id.cbGLectura);
        cbViajes       = findViewById(R.id.cbGViajes);
        cbCocina       = findViewById(R.id.cbGCocina);
        cbJuegos       = findViewById(R.id.cbGJuegos);
        cbFotografia   = findViewById(R.id.cbGFotografia);
        cbBaile        = findViewById(R.id.cbGBaile);
        cbTeatro       = findViewById(R.id.cbGTeatro);

        ArrayList<String> previos = (ArrayList<String>) getIntent().getSerializableExtra(KEY_GUSTOS);
        if (previos != null) {
            previos.forEach(g -> {
                switch (g) {
                    case "Música":       cbMusica.setChecked(true);      break;
                    case "Deporte":      cbDeporte.setChecked(true);     break;
                    case "Cine":         cbCine.setChecked(true);        break;
                    case "Lectura":      cbLectura.setChecked(true);     break;
                    case "Viajes":       cbViajes.setChecked(true);      break;
                    case "Cocina":       cbCocina.setChecked(true);      break;
                    case "Juegos":       cbJuegos.setChecked(true);      break;
                    case "Fotografía":   cbFotografia.setChecked(true);  break;
                    case "Baile":        cbBaile.setChecked(true);       break;
                    case "Teatro":       cbTeatro.setChecked(true);      break;
                }
            });
        }
    }

    public void confirmarGustos(View v) {
        ArrayList<String> seleccionados = new ArrayList<>();

        if (cbMusica.isChecked())     seleccionados.add("Música");
        if (cbDeporte.isChecked())    seleccionados.add("Deporte");
        if (cbCine.isChecked())       seleccionados.add("Cine");
        if (cbLectura.isChecked())    seleccionados.add("Lectura");
        if (cbViajes.isChecked())     seleccionados.add("Viajes");
        if (cbCocina.isChecked())     seleccionados.add("Cocina");
        if (cbJuegos.isChecked())     seleccionados.add("Juegos");
        if (cbFotografia.isChecked()) seleccionados.add("Fotografía");
        if (cbBaile.isChecked())      seleccionados.add("Baile");
        if (cbTeatro.isChecked())     seleccionados.add("Teatro");

        if (seleccionados.isEmpty()) {
            Toast.makeText(this, "Selecciona al menos un gusto", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent resultado = new Intent();
        resultado.putExtra(KEY_GUSTOS, seleccionados);
        setResult(RESULT_OK, resultado);
        finish();
    }

    public void regresar(View v) {
        setResult(RESULT_CANCELED);
        finish();
    }
}