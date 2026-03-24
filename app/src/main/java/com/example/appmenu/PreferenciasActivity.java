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

public class PreferenciasActivity extends AppCompatActivity {

    public static final String KEY_PREFERENCIAS = "preferencias_seleccionadas";

    CheckBox cbTecnologia, cbNaturaleza, cbArte, cbCiencia, cbModa,
            cbPolitica, cbReligion, cbSalud, cbEmprendimiento, cbAnimales;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_preferencias);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        cbTecnologia     = findViewById(R.id.cbPTecnologia);
        cbNaturaleza     = findViewById(R.id.cbPNaturaleza);
        cbArte           = findViewById(R.id.cbPArte);
        cbCiencia        = findViewById(R.id.cbPCiencia);
        cbModa           = findViewById(R.id.cbPModa);
        cbPolitica       = findViewById(R.id.cbPPolitica);
        cbReligion       = findViewById(R.id.cbPReligion);
        cbSalud          = findViewById(R.id.cbPSalud);
        cbEmprendimiento = findViewById(R.id.cbPEmprendimiento);
        cbAnimales       = findViewById(R.id.cbPAnimales);

        ArrayList<String> previas = (ArrayList<String>) getIntent().getSerializableExtra(KEY_PREFERENCIAS);
        if (previas != null) {
            previas.forEach(p -> {
                switch (p) {
                    case "Tecnología":      cbTecnologia.setChecked(true);     break;
                    case "Naturaleza":      cbNaturaleza.setChecked(true);     break;
                    case "Arte":            cbArte.setChecked(true);           break;
                    case "Ciencia":         cbCiencia.setChecked(true);        break;
                    case "Moda":            cbModa.setChecked(true);           break;
                    case "Política":        cbPolitica.setChecked(true);       break;
                    case "Religión":        cbReligion.setChecked(true);       break;
                    case "Salud":           cbSalud.setChecked(true);          break;
                    case "Emprendimiento":  cbEmprendimiento.setChecked(true); break;
                    case "Animales":        cbAnimales.setChecked(true);       break;
                }
            });
        }
    }

    public void confirmarPreferencias(View v) {
        ArrayList<String> seleccionadas = new ArrayList<>();

        if (cbTecnologia.isChecked())     seleccionadas.add("Tecnología");
        if (cbNaturaleza.isChecked())     seleccionadas.add("Naturaleza");
        if (cbArte.isChecked())           seleccionadas.add("Arte");
        if (cbCiencia.isChecked())        seleccionadas.add("Ciencia");
        if (cbModa.isChecked())           seleccionadas.add("Moda");
        if (cbPolitica.isChecked())       seleccionadas.add("Política");
        if (cbReligion.isChecked())       seleccionadas.add("Religión");
        if (cbSalud.isChecked())          seleccionadas.add("Salud");
        if (cbEmprendimiento.isChecked()) seleccionadas.add("Emprendimiento");
        if (cbAnimales.isChecked())       seleccionadas.add("Animales");

        if (seleccionadas.isEmpty()) {
            Toast.makeText(this, "Selecciona al menos una preferencia", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent resultado = new Intent();
        resultado.putExtra(KEY_PREFERENCIAS, seleccionadas);
        setResult(RESULT_OK, resultado);
        finish();
    }

    public void regresar(View v) {
        setResult(RESULT_CANCELED);
        finish();
    }
}