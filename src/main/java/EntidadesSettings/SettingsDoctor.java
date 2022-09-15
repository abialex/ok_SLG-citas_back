/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package EntidadesSettings;

import Entidades.Address;
import Entidades.Doctor;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class SettingsDoctor implements Comparable<SettingsDoctor> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JoinColumn(insertable = true, updatable = true, name = "iddoctor", nullable = false)
    @ManyToOne
    private Doctor doctor;

    @JoinColumn(insertable = true, updatable = true, name = "idaddress", nullable = true)
    @ManyToOne
    private Address address;

    @Column(name = "name", nullable = false)
    private String name;

    public SettingsDoctor() {
    }

    public SettingsDoctor(Doctor doctor, String name, Address address) {
        this.doctor = doctor;
        this.name = name;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public int compareTo(SettingsDoctor setdoc) {
        return this.name.compareTo(setdoc.getName());
    }
}
