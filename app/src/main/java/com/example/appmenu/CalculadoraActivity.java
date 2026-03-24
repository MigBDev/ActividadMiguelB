package com.example.appmenu;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CalculadoraActivity extends AppCompatActivity {

    TextView tvTemporal, tvResult, tvMemoria;

    double numero1 = 0, numero2 = 0;
    String operacion = "";
    boolean nuevaOperacion = true;

    // Memoria de la calculadora
    double memoria = 0;
    boolean hayMemoria = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_calculadora);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvTemporal = findViewById(R.id.tvTemporal);
        tvResult   = findViewById(R.id.tvResult);
        tvMemoria  = findViewById(R.id.tvMemoria);
    }

    public void presionarNumero(View view) {
        Button boton = (Button) view;
        String valor = boton.getText().toString();
        if (nuevaOperacion) { tvResult.setText(valor); nuevaOperacion = false; }
        else tvResult.setText(tvResult.getText().toString() + valor);
    }

    public void presionarOperacion(View view) {
        Button boton = (Button) view;
        operacion = boton.getText().toString();
        numero1 = Double.parseDouble(tvResult.getText().toString());
        tvTemporal.setText(numero1 + " " + operacion);
        nuevaOperacion = true;
    }

    public void presionarIgual(View view) {
        numero2 = Double.parseDouble(tvResult.getText().toString());
        double resultado = 0;
        switch (operacion) {
            case "+": resultado = numero1 + numero2; break;
            case "−": resultado = numero1 - numero2; break;
            case "X": resultado = numero1 * numero2; break;
            case "÷":
                if (numero2 != 0) { resultado = numero1 / numero2; }
                else { tvResult.setText("Error"); return; }
                break;
        }
        tvResult.setText(String.valueOf(resultado));
        tvTemporal.setText("");
        nuevaOperacion = true;
    }

    // ── Guardar en memoria el resultado actual ─────────────────────────────────
    public void guardarMemoria(View view) {
        String texto = tvResult.getText().toString();
        if (texto.equals("Error") || texto.isEmpty()) {
            Toast.makeText(this, "No hay resultado para guardar", Toast.LENGTH_SHORT).show();
            return;
        }
        memoria = Double.parseDouble(texto);
        hayMemoria = true;
        tvMemoria.setText("M: " + memoria);
        Toast.makeText(this, "Guardado en memoria: " + memoria, Toast.LENGTH_SHORT).show();
    }

    // ── Recuperar el valor de memoria ─────────────────────────────────────────
    public void recuperarMemoria(View view) {
        if (!hayMemoria) {
            Toast.makeText(this, "Memoria vacía", Toast.LENGTH_SHORT).show();
            return;
        }
        tvResult.setText(String.valueOf(memoria));
        nuevaOperacion = false;
    }

    // ── Limpiar memoria ────────────────────────────────────────────────────────
    public void limpiarMemoria(View view) {
        memoria = 0;
        hayMemoria = false;
        tvMemoria.setText("M: --");
        Toast.makeText(this, "Memoria borrada", Toast.LENGTH_SHORT).show();
    }

    public void borrar(View view) { tvResult.setText("0"); nuevaOperacion = true; }

    public void borrarTodo(View view) {
        tvResult.setText("0"); tvTemporal.setText("0");
        numero1 = 0; numero2 = 0; operacion = ""; nuevaOperacion = true;
    }

    public void regresar(View view) { finish(); }
}