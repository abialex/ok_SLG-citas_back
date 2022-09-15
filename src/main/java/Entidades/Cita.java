/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entidades;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author alexis
 */
@Entity
public class Cita implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idcita;

    @ManyToOne
    @JoinColumn(insertable = true, updatable = true, name = "iddoctor", nullable = false)
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(insertable = true, updatable = true, name = "idhoraatencion", nullable = false)
    private HoraAtencion horaatencion;

    @ManyToOne
    @JoinColumn(insertable = true, updatable = true, name = "idlugar", nullable = true)
    private Lugar lugar;

    @Column(name = "nombrepaciente", nullable = true)
    private String nombrepaciente;

    @Column(name = "minuto", nullable = true)
    private String minuto;

    @Column(name = "fechacita", nullable = true)
    private LocalDate fechacita;

    @Column(name = "razon", nullable = true)
    private String razon;

    @Column(name = "celular", nullable = true)
    private String celular;

    public Cita() {
    }

    public Cita(Doctor doctor, String pacientenombr, HoraAtencion horaatencion, LocalDate fechacita, String razon, String minuto, String celular) {
        this.doctor = doctor;
        this.nombrepaciente = pacientenombr;
        this.horaatencion = horaatencion;
        this.fechacita = fechacita;
        this.razon = razon;
        this.minuto = minuto;
        this.celular = celular;
    }

    public Cita(Doctor doctor, HoraAtencion horaatencion, LocalDate fechacita, String razon, String celular) {
        this.doctor = doctor;
        this.horaatencion = horaatencion;
        this.fechacita = fechacita;
        this.razon = razon;
        this.celular = celular;
    }

    public int getIdcita() {
        return idcita;
    }

    public void setIdcita(int idcita) {
        this.idcita = idcita;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public String getNombrepaciente() {
        return nombrepaciente;
    }

    public void setNombrepaciente(String nombrepaciente) {
        this.nombrepaciente = nombrepaciente;
    }

    public HoraAtencion getHoraatencion() {
        return horaatencion;
    }

    public void setHoraatencion(HoraAtencion horaatencion) {
        this.horaatencion = horaatencion;
    }

    public LocalDate getFechacita() {
        return fechacita;
    }

    public void setFechacita(LocalDate fechacita) {
        this.fechacita = fechacita;
    }

    public String getRazon() {
        return razon;
    }

    public void setRazon(String razon) {
        this.razon = razon;
    }

    public String getMinuto() {
        return minuto;
    }

    public void setMinuto(String minuto) {
        this.minuto = minuto;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public Lugar getLugar() {
        return lugar;
    }

    public void setLugar(Lugar lugar) {
        this.lugar = lugar;
    }

}
