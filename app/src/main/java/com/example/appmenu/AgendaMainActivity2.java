package com.example.appmenu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import java.util.ArrayList;

public class AgendaMainActivity2 extends AppCompatActivity {

    EditText edtBuscarId;
    LinearLayout layoutResultado, layoutEdicion;

    TextView tvResultCedula, tvResultNombre, tvResultApellido;
    TextView tvResultTelefono, tvResultCorreo, tvResultDireccion;
    TextView tvResultFecha, tvResultSexo, tvResultGustos, tvResultPreferencias;

    EditText edtEditNombre, edtEditApellido, edtEditTelefono;
    EditText edtEditCorreo, edtEditDireccion, edtEditFecha;

    Button btnEditar, btnGuardarCambios, btnCancelarEdicion;

    Usuario usuarioActual = null;

    ArrayList<String> gustosEditados       = new ArrayList<>();
    ArrayList<String> preferenciasEditadas = new ArrayList<>();

    ActivityResultLauncher<Intent> launcherGustosEditar =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    gustosEditados = (ArrayList<String>) result.getData().getSerializableExtra(GustosActivity.KEY_GUSTOS);
                    Toast.makeText(this, "Gustos actualizados: " + gustosEditados.size(), Toast.LENGTH_SHORT).show();
                }
            });

    ActivityResultLauncher<Intent> launcherPrefsEditar =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    preferenciasEditadas = (ArrayList<String>) result.getData().getSerializableExtra(PreferenciasActivity.KEY_PREFERENCIAS);
                    Toast.makeText(this, "Preferencias actualizadas: " + preferenciasEditadas.size(), Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_agenda_main2);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        edtBuscarId          = findViewById(R.id.edtBuscarId);
        layoutResultado      = findViewById(R.id.layoutResultado);
        layoutEdicion        = findViewById(R.id.layoutEdicion);

        tvResultCedula       = findViewById(R.id.tvResultCedula);
        tvResultNombre       = findViewById(R.id.tvResultNombre);
        tvResultApellido     = findViewById(R.id.tvResultApellido);
        tvResultTelefono     = findViewById(R.id.tvResultTelefono);
        tvResultCorreo       = findViewById(R.id.tvResultCorreo);
        tvResultDireccion    = findViewById(R.id.tvResultDireccion);
        tvResultFecha        = findViewById(R.id.tvResultFecha);
        tvResultSexo         = findViewById(R.id.tvResultSexo);
        tvResultGustos       = findViewById(R.id.tvResultGustos);
        tvResultPreferencias = findViewById(R.id.tvResultPreferencias);

        edtEditNombre        = findViewById(R.id.edtEditNombre);
        edtEditApellido      = findViewById(R.id.edtEditApellido);
        edtEditTelefono      = findViewById(R.id.edtEditTelefono);
        edtEditCorreo        = findViewById(R.id.edtEditCorreo);
        edtEditDireccion     = findViewById(R.id.edtEditDireccion);
        edtEditFecha         = findViewById(R.id.edtEditFecha);

        btnEditar            = findViewById(R.id.btnEditar);
        btnGuardarCambios    = findViewById(R.id.btnGuardarCambios);
        btnCancelarEdicion   = findViewById(R.id.btnCancelarEdicion);

        layoutResultado.setVisibility(View.GONE);
        layoutEdicion.setVisibility(View.GONE);

        // ── Si la lista está vacía, cargar desde archivo serializado ──────────
        if (AgendaMainActivity.listaUsuarios.isEmpty()) {
            try (ObjectInputStream ois = new ObjectInputStream(
                    openFileInput(AgendaMainActivity.FILE_NAME))) {
                AgendaMainActivity.listaUsuarios = (ArrayList<Usuario>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    // ── Buscar por cédula ─────────────────────────────────────────────────────
    public void buscarUsuario(View v) {
        String idBuscar = edtBuscarId.getText().toString().trim();
        if (idBuscar.isEmpty()) {
            Toast.makeText(this, "Ingresa una cédula", Toast.LENGTH_SHORT).show();
            return;
        }

        final Usuario[] encontrado = {null};
        AgendaMainActivity.listaUsuarios.forEach(u -> {
            if (u.getCedula().equals(idBuscar)) encontrado[0] = u;
        });

        if (encontrado[0] != null) {
            usuarioActual = encontrado[0];
            mostrarUsuario(usuarioActual);
            layoutEdicion.setVisibility(View.GONE);
        } else {
            layoutResultado.setVisibility(View.GONE);
            Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
        }
    }

    private void mostrarUsuario(Usuario u) {
        tvResultCedula.setText("Cédula: "         + u.getCedula());
        tvResultNombre.setText("Nombre: "          + u.getNombre());
        tvResultApellido.setText("Apellido: "      + u.getApellido());
        tvResultTelefono.setText("Teléfono: "      + u.getTelefono());
        tvResultCorreo.setText("Correo: "          + u.getCorreo());
        tvResultDireccion.setText("Dirección: "    + u.getDireccion());
        tvResultFecha.setText("Fecha nacimiento: " + u.getFechaNacimiento());
        tvResultSexo.setText("Sexo: "              + u.getSexo());

        StringBuilder sbG = new StringBuilder();
        u.getGustos().forEach(g -> sbG.append("• ").append(g).append("\n"));
        tvResultGustos.setText("Gustos:\n" + (sbG.length() > 0 ? sbG.toString().trim() : "Ninguno"));

        StringBuilder sbP = new StringBuilder();
        u.getPreferencias().forEach(p -> sbP.append("• ").append(p).append("\n"));
        tvResultPreferencias.setText("Preferencias:\n" + (sbP.length() > 0 ? sbP.toString().trim() : "Ninguna"));

        layoutResultado.setVisibility(View.VISIBLE);
    }

    // ── Activar edición ───────────────────────────────────────────────────────
    public void activarEdicion(View v) {
        if (usuarioActual == null) return;

        edtEditNombre.setText(usuarioActual.getNombre());
        edtEditApellido.setText(usuarioActual.getApellido());
        edtEditTelefono.setText(usuarioActual.getTelefono());
        edtEditCorreo.setText(usuarioActual.getCorreo());
        edtEditDireccion.setText(usuarioActual.getDireccion());
        edtEditFecha.setText(usuarioActual.getFechaNacimiento());
        gustosEditados       = new ArrayList<>(usuarioActual.getGustos());
        preferenciasEditadas = new ArrayList<>(usuarioActual.getPreferencias());

        layoutEdicion.setVisibility(View.VISIBLE);
    }

    // ── Gustos en modo modificar ──────────────────────────────────────────────
    public void editarGustos(View v) {
        Intent i = new Intent(this, GustosActivity.class);
        i.putExtra(GustosActivity.KEY_GUSTOS, gustosEditados);
        launcherGustosEditar.launch(i);
    }

    // ── Preferencias en modo modificar ───────────────────────────────────────
    public void editarPreferencias(View v) {
        Intent i = new Intent(this, PreferenciasActivity.class);
        i.putExtra(PreferenciasActivity.KEY_PREFERENCIAS, preferenciasEditadas);
        launcherPrefsEditar.launch(i);
    }

    // ── Guardar cambios y persistir en archivo ────────────────────────────────
    public void guardarCambios(View v) {
        if (usuarioActual == null) return;

        usuarioActual.setNombre(edtEditNombre.getText().toString().trim());
        usuarioActual.setApellido(edtEditApellido.getText().toString().trim());
        usuarioActual.setTelefono(edtEditTelefono.getText().toString().trim());
        usuarioActual.setCorreo(edtEditCorreo.getText().toString().trim());
        usuarioActual.setDireccion(edtEditDireccion.getText().toString().trim());
        usuarioActual.setFechaNacimiento(edtEditFecha.getText().toString().trim());
        usuarioActual.setGustos(gustosEditados);
        usuarioActual.setPreferencias(preferenciasEditadas);

        AgendaMainActivity.guardarUsuarios(this); // ── Persistir serializado ──

        Toast.makeText(this, "Usuario actualizado correctamente", Toast.LENGTH_SHORT).show();
        mostrarUsuario(usuarioActual);
        layoutEdicion.setVisibility(View.GONE);
    }

    public void cancelarEdicion(View v) {
        layoutEdicion.setVisibility(View.GONE);
    }

    public void regresar(View v) { finish(); }
}