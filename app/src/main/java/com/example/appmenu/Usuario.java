package com.example.appmenu;

import java.io.Serializable;
import java.util.ArrayList;

public class Usuario implements Serializable {

    private String cedula;
    private String nombre;
    private String apellido;
    private String telefono;
    private String correo;
    private String direccion;
    private String fechaNacimiento;
    private String sexo;

    private ArrayList<String> gustos        = new ArrayList<>();
    private ArrayList<String> preferencias  = new ArrayList<>();

    public String getCedula()                        { return cedula; }
    public void setCedula(String cedula)             { this.cedula = cedula; }

    public String getNombre()                        { return nombre; }
    public void setNombre(String nombre)             { this.nombre = nombre; }

    public String getApellido()                      { return apellido; }
    public void setApellido(String apellido)         { this.apellido = apellido; }

    public String getTelefono()                      { return telefono; }
    public void setTelefono(String telefono)         { this.telefono = telefono; }

    public String getCorreo()                        { return correo; }
    public void setCorreo(String correo)             { this.correo = correo; }

    public String getDireccion()                     { return direccion; }
    public void setDireccion(String direccion)       { this.direccion = direccion; }

    public String getFechaNacimiento()               { return fechaNacimiento; }
    public void setFechaNacimiento(String f)         { this.fechaNacimiento = f; }

    public String getSexo()                          { return sexo; }
    public void setSexo(String sexo)                 { this.sexo = sexo; }

    public ArrayList<String> getGustos()             { return gustos; }
    public void setGustos(ArrayList<String> g)       { this.gustos = g; }

    public ArrayList<String> getPreferencias()       { return preferencias; }
    public void setPreferencias(ArrayList<String> p) { this.preferencias = p; }
}