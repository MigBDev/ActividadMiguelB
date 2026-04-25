package com.example.appmenu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class AgendaMainActivity extends AppCompatActivity {

    static final int REQ_GUSTOS       = 1;
    static final int REQ_PREFERENCIAS = 2;

    static final String FILE_NAME = "usuarios.dat";

    EditText edtCedula, edtNombre, edtApellido;
    EditText edtTelefono, edtCorreo, edtDireccion;
    EditText edtFechaNacimiento;
    RadioGroup rgSexo;
    RadioButton rbFemenino, rbMasculino;

    Button btnRegistrar, btnIrBusqueda;
    Button btnSeleccionarGustos, btnSeleccionarPreferencias;

    TextView tvResumenGustos, tvResumenPreferencias;

    ArrayList<String> gustosTemp       = new ArrayList<>();
    ArrayList<String> preferenciasTemp = new ArrayList<>();

    static ArrayList<Usuario> listaUsuarios = new ArrayList<>();

    ActivityResultLauncher<Intent> launherGustos =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    gustosTemp = (ArrayList<String>) result.getData().getSerializableExtra(GustosActivity.KEY_GUSTOS);
                    tvResumenGustos.setText("Gustos seleccionados: " + gustosTemp.size());
                }
            });

    ActivityResultLauncher<Intent> launcherPreferencias =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    preferenciasTemp = (ArrayList<String>) result.getData().getSerializableExtra(PreferenciasActivity.KEY_PREFERENCIAS);
                    tvResumenPreferencias.setText("Preferencias seleccionadas: " + preferenciasTemp.size());
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_agenda_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        edtCedula                  = findViewById(R.id.edtCedula);
        edtNombre                  = findViewById(R.id.edtNombre);
        edtApellido                = findViewById(R.id.edtApellido);
        edtTelefono                = findViewById(R.id.edtTelefono);
        edtCorreo                  = findViewById(R.id.edtCorreo);
        edtDireccion               = findViewById(R.id.edtDireccion);
        edtFechaNacimiento         = findViewById(R.id.edtFechaNacimiento);
        rgSexo                     = findViewById(R.id.rgSexo);
        rbFemenino                 = findViewById(R.id.rbFemenino);
        rbMasculino                = findViewById(R.id.rbMasculino);
        btnRegistrar               = findViewById(R.id.btnRegistrar);
        btnIrBusqueda              = findViewById(R.id.btnIrBusqueda);
        btnSeleccionarGustos       = findViewById(R.id.btnSeleccionarGustos);
        btnSeleccionarPreferencias = findViewById(R.id.btnSeleccionarPreferencias);
        tvResumenGustos            = findViewById(R.id.tvResumenGustos);
        tvResumenPreferencias      = findViewById(R.id.tvResumenPreferencias);

        // ── Cargar usuarios desde archivo al iniciar ──────────────────────────
        listaUsuarios = cargarUsuarios();
    }

    // ── Abrir pantalla de Gustos ──────────────────────────────────────────────
    public void abrirGustos(View v) {
        Intent i = new Intent(this, GustosActivity.class);
        i.putExtra(GustosActivity.KEY_GUSTOS, gustosTemp);
        launherGustos.launch(i);
    }

    // ── Abrir pantalla de Preferencias ───────────────────────────────────────
    public void abrirPreferencias(View v) {
        Intent i = new Intent(this, PreferenciasActivity.class);
        i.putExtra(PreferenciasActivity.KEY_PREFERENCIAS, preferenciasTemp);
        launcherPreferencias.launch(i);
    }

    // ── Registrar usuario ─────────────────────────────────────────────────────
    public void registrarUsuario(View v) {
        String cedula    = edtCedula.getText().toString().trim();
        String nombre    = edtNombre.getText().toString().trim();
        String apellido  = edtApellido.getText().toString().trim();
        String telefono  = edtTelefono.getText().toString().trim();
        String correo    = edtCorreo.getText().toString().trim();
        String direccion = edtDireccion.getText().toString().trim();
        String fecha     = edtFechaNacimiento.getText().toString().trim();

        if (cedula.isEmpty() || nombre.isEmpty() || apellido.isEmpty() ||
                telefono.isEmpty() || correo.isEmpty() || direccion.isEmpty() || fecha.isEmpty()) {
            Toast.makeText(this, "Por favor llena todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }
        if (rgSexo.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Selecciona el sexo", Toast.LENGTH_SHORT).show();
            return;
        }

        final boolean[] duplicado = {false};
        listaUsuarios.forEach(u -> {
            if (u.getCedula().equals(cedula)) duplicado[0] = true;
        });
        if (duplicado[0]) {
            Toast.makeText(this, "Ya existe un usuario con esa cédula", Toast.LENGTH_SHORT).show();
            return;
        }

        String sexo = rbFemenino.isChecked() ? "Femenino" : "Masculino";

        Usuario u = new Usuario();
        u.setCedula(cedula);
        u.setNombre(nombre);
        u.setApellido(apellido);
        u.setTelefono(telefono);
        u.setCorreo(correo);
        u.setDireccion(direccion);
        u.setFechaNacimiento(fecha);
        u.setSexo(sexo);
        u.setGustos(new ArrayList<>(gustosTemp));
        u.setPreferencias(new ArrayList<>(preferenciasTemp));

        listaUsuarios.add(u);
        guardarUsuarios();  // ── Persistir en archivo ──
        Toast.makeText(this, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show();
        limpiarFormulario();
    }

    public void irBusqueda(View v) {
        startActivity(new Intent(this, AgendaMainActivity2.class));
    }

    public void regresarMenu(View v) { finish(); }

    private void limpiarFormulario() {
        edtCedula.setText(""); edtNombre.setText(""); edtApellido.setText("");
        edtTelefono.setText(""); edtCorreo.setText(""); edtDireccion.setText("");
        edtFechaNacimiento.setText("");
        rgSexo.clearCheck();
        gustosTemp.clear();
        preferenciasTemp.clear();
        tvResumenGustos.setText("Gustos seleccionados: 0");
        tvResumenPreferencias.setText("Preferencias seleccionadas: 0");
    }

    // ── Guardar lista completa serializada ────────────────────────────────────
    static void guardarUsuarios(android.content.Context ctx) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                ctx.openFileOutput(FILE_NAME, MODE_PRIVATE))) {
            oos.writeObject(listaUsuarios);
        } catch (IOException e) { e.printStackTrace(); }
    }

    // Sobrecarga para llamarlo desde la misma Activity sin pasar contexto
    private void guardarUsuarios() {
        guardarUsuarios(this);
    }

    // ── Cargar lista deserializando ───────────────────────────────────────────
    @SuppressWarnings("unchecked")
    private ArrayList<Usuario> cargarUsuarios() {
        try (ObjectInputStream ois = new ObjectInputStream(
                openFileInput(FILE_NAME))) {
            return (ArrayList<Usuario>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}