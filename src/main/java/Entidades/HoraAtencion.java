/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entidades;

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
public class HoraAtencion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idhoraatencion;
    
    @Column(name = "hora", nullable = false)
    private String hora;
    
    @Column(name = "abreviatura", nullable = true)
    private String abreviatura;

    public HoraAtencion() {
    }

    public int getIdhoraatencion() {
        return idhoraatencion;
    }

    public void setIdhoraatencion(int idhoraatencion) {
        this.idhoraatencion = idhoraatencion;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }
    
    public HoraAtencion getHoraatencion(){
        return this;
    }

    @Override
    public String toString() {
        return this.hora+" "+this.abreviatura;// Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }

}
