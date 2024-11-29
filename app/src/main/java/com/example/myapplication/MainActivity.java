package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.api.ServiceUsuarioApi;
import com.example.myapplication.entity.Usuario;
import com.example.myapplication.util.ConnectionRest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    EditText etNombre, etApellido, etTelefono, etEmail, etPassword, etDni;
    Spinner spnRol, spnEstado;
    Button btnRegistrar;

    ServiceUsuarioApi apiUsuario = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etNombre = findViewById(R.id.etNombre);
        etApellido = findViewById(R.id.etApellido);
        etTelefono = findViewById(R.id.etTelefono);
        etDni = findViewById(R.id.etDni);
        spnRol = findViewById(R.id.idRol);
        spnEstado = findViewById(R.id.idEstado);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnRegistrar = findViewById(R.id.btnRegistrar);

        apiUsuario = ConnectionRest.getConnection().create(ServiceUsuarioApi.class);


        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String nombre = etNombre.getText().toString();
                String apellido = etApellido.getText().toString();
                String telefono = etTelefono.getText().toString();
                String dni = etDni.getText().toString();
                String txtRol = spnRol.getSelectedItem().toString();
                int idRol = spnRol.getSelectedItemPosition();
                String txtEstado = spnEstado.getSelectedItem().toString();
                int idEstado = spnEstado.getSelectedItemPosition();
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                if (!validarDni(dni)) {
                    Toast.makeText(MainActivity.this, "El DNI debe tener 8 caracteres y no debe repetirse.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!validarEmail(email)) {
                    Toast.makeText(MainActivity.this, "El email ya está registrado.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!validarNombre(nombre)) {
                    Toast.makeText(MainActivity.this, "El nombre debe tener entre 6 y 30 caracteres.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!validarPassword(password)) {
                    Toast.makeText(MainActivity.this, "La contraseña debe contener letras, números y símbolos.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (idRol == 0 || idEstado == 0) {
                    Toast.makeText(MainActivity.this, "Debes seleccionar un rol y un estado.", Toast.LENGTH_SHORT).show();
                    return;
                }


                Usuario obj = new Usuario();
                obj.setNombre(nombre);
                obj.setApellido(apellido);
                obj.setTelefono(telefono);
                obj.setDni(dni);
                obj.setRol(txtRol);
                obj.setEstado(txtEstado);
                obj.setEmail(email);
                obj.setPassword(password);


                insertaUsuario(obj);

                String msg = "Bienvenido \n";
                msg += "Nombres: " + obj.getNombre() + "\n";
                msg += "Apellidos: " + obj.getApellido() + "\n";
                msg += "Telefóno: " + obj.getTelefono() + "\n";
                msg += "DNI: " + obj.getDni() + "\n";
                msg += "Rol: " + obj.getRol() + "\n";
                msg += "Estado: " + obj.getEstado() + "\n";
                msg += "Email: " + obj.getEmail() + "\n";
                msg += "Contraseña: " + obj.getPassword();

                mensaje(msg);
            }
            public void insertaUsuario(Usuario obj){
                Call<Usuario> call = apiUsuario.guardarUsuario(obj);
                call.enqueue(new Callback<Usuario>() {
                    @Override
                    public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                        if (response.isSuccessful()){
                            Usuario obj = response.body();
                            if (obj == null) {
                                mensaje("ERROR" + "No se insertó");
                            }else{
                                mensaje("ÉXITO"+ "Se insertó correctamente");
                            }
                        }else{
                            mensaje("ERROR" + "Error en la respuesta");
                        }
                    }

                    @Override
                    public void onFailure(Call<Usuario> call, Throwable t) {
                        mensaje("ERROR"+ t.getMessage());
                    }
                });
            }
        });
    }

    void mensaje(String msg){
        Toast toast1 = Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_LONG);
        toast1.show();
    }

    private boolean validarDni(String dni) {
        return dni.length() == 8 && dni.matches("\\d+");
    }


    private boolean validarEmail(String email) {
        String emailPattern = "^[a-zA-Z0-9._-]+@[a-z]+\\.[a-z]+$";
        return email.matches(emailPattern);
    }


    private boolean validarNombre(String nombre) {
        return nombre.length() >= 6 && nombre.length() <= 30 && nombre.matches("[a-zA-Z ]+");
    }


    private boolean validarPassword(String password) {
        Pattern pattern = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+={}:;,.?]).+$");
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
}