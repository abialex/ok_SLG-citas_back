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
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int iddoctor;

    @Column(name = "nombredoctor", nullable = true)
    private String nombredoctor;

    @Column(name = "activo", nullable = true)
    private boolean activo;

    @Column(name = "flag", nullable = true)
    private boolean flag;

    public Doctor() {
    }

    public Doctor(String persona) {
        this.nombredoctor = persona;
    }

    public int getIddoctor() {
        return iddoctor;
    }

    public void setIddoctor(int iddoctor) {
        this.iddoctor = iddoctor;
    }

    public String getNombredoctor() {
        return nombredoctor;
    }

    public void setNombredoctor(String nombredoctor) {
        this.nombredoctor = nombredoctor;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public Doctor getDoctor() {
        return this;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return this.nombredoctor;
    }

}
