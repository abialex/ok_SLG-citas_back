/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServicioRest;

import EntidadesSettings.SettingsDoctor;
import Controller.CitasBootApplication;
import Entidades.Cita;
import Entidades.Doctor;
import Entidades.HoraAtencion;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.JsonAdapter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.print.Doc;
import net.minidev.json.JSONArray;

import net.minidev.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import retrofit2.http.POST;

/**
 *
 * @author alexis
 */
@Component
@RestController

public class CitaRest {

    Gson json = new Gson();

    @GetMapping("/employees")
    String all() {
        List<SettingsDoctor> list = CitasBootApplication.jpa.createQuery("select p from SettingsDoctor p").getResultList();
        Cita ocita = new Cita();
        String cita = json.toJson(list.get(0));// a json
        ocita = json.fromJson(cita, Cita.class);// a objeto de objeto 
        return cita;

    }

    @GetMapping("/SettingsDoctorAll")
    String getSettingsDoctor() {
        List<SettingsDoctor> list = CitasBootApplication.jpa.createQuery("select p from SettingsDoctor p").getResultList();
        return JSONArray.toJSONString(list);
    }

    @PostMapping("/CitaFilter")
    String CitaFilter(@RequestBody JSONObject response) {
        List<String> listcondicion = new ArrayList<>();
        if (response.get("iddoctor") != null) {
            listcondicion.add(" iddoctor= " + response.get("iddoctor"));
        }
        if (response.get("fechaFin") == null) {
            if (response.get("fechaInicio") != null) {
                listcondicion.add(" fechacita = '" + response.get("fechaInicio") + "'");
            }
        } else {
            listcondicion.add(" fechacita between" + " '" + response.get("fechaInicio") + "' and '" + response.get("fechaFin") + "' ");
        }

        if (response.get("razon") != null) {
            listcondicion.add(" razon= '" + response.get("razon") + "'");
        }

        if (response.get("idhoraatencion") != null) {
            listcondicion.add(" idhoraatencion= '" + response.get("idhoraatencion") + "'");
        }

        String condicion = listcondicion.size() == 0 ? "" : "where " + listcondicion.get(0);
        for (int i = 1; i < listcondicion.size(); i++) {
            condicion = condicion + " and " + listcondicion.get(i);
        }

        List<Cita> list = CitasBootApplication.jpa.createQuery("select p from Cita p " + condicion + " order by minuto asc").getResultList();

        String array = "[]";

        if (list.size() > 0) {
            array = "[ ";
            for (int i = 0; i < list.size() - 1; i++) {
                array = array + json.toJson(list.get(i)) + " , ";
            }
            array = array + json.toJson(list.get(list.size() - 1)) + " ] ";
        }
        return array;
    }
    
    @GetMapping("/DoctorAll")
    String getDoctor() {
        List<Doctor> list = CitasBootApplication.jpa.createQuery("select p from Doctor p where flag = false and activo = true order by iddoctor asc").getResultList();
        return JSONArray.toJSONString(list);
    }

    @GetMapping("/HoraAtencionAll")
    String getHoraAtencion() {
        List<HoraAtencion> list = CitasBootApplication.jpa.createQuery("select p from HoraAtencion p order by idhoraatencion ASC").getResultList();
        return JSONArray.toJSONString(list);
    }
    //actualiza las citas que tego cada vez que agrego, elimino o modifico

    @GetMapping("/GetListCitasByFecha/{id}")
    String getCitasUpdate(@PathVariable String id) {
        List<Cita> list = CitasBootApplication.jpa.createQuery("select p from Cita p where EXTRACT(year from fechacita)=" + id + "order by minuto ASC").getResultList();
        String array = "[]";

        if (list.size() > 0) {
            array = "[ ";
            for (int i = 0; i < list.size() - 1; i++) {
                array = array + json.toJson(list.get(i)) + " , ";
            }
            array = array + json.toJson(list.get(list.size() - 1)) + " ] ";
        }
        return array;
    }

    @PostMapping("/AddSettingsDoctor")
    String AddSettingsDoctor(@RequestBody String response) {
        SettingsDoctor oAddSettingsDoctor = json.fromJson(response, SettingsDoctor.class);
        CitasBootApplication.jpa.getTransaction().begin();
        CitasBootApplication.jpa.persist(oAddSettingsDoctor);
        CitasBootApplication.jpa.getTransaction().commit();
        return "ok";
    }

    @PostMapping("/AddCita")
    String AddCita(@RequestBody String response) {
        Cita oAddCita = json.fromJson(response, Cita.class);
        CitasBootApplication.jpa.getTransaction().begin();
        CitasBootApplication.jpa.persist(oAddCita);
        CitasBootApplication.jpa.getTransaction().commit();
        return "ok";
    }

    @PostMapping("/AddDoctor")
    String AddDoctor(@RequestBody String response) {
        Doctor AddDoctor = json.fromJson(response, Doctor.class);
        CitasBootApplication.jpa.getTransaction().begin();
        CitasBootApplication.jpa.persist(AddDoctor);
        CitasBootApplication.jpa.getTransaction().commit();
        return "ok";
    }

    //------------------------------------------UPDATE--------------------------------------
    @PostMapping("/UpdateSettingsDoctor")
    String UpdateSettingsDoctor(@RequestBody String response) {
        SettingsDoctor oSettingsDoctorJSON = json.fromJson(response, SettingsDoctor.class);
        List<SettingsDoctor> list = CitasBootApplication.jpa.createQuery("select p from SettingsDoctor p where id=" + oSettingsDoctorJSON.getId()).getResultList();

        SettingsDoctor oSettingsDoctorUpdate = list.get(0);
        oSettingsDoctorUpdate.setDoctor(oSettingsDoctorJSON.getDoctor());
        oSettingsDoctorUpdate.setName(oSettingsDoctorJSON.getName());

        CitasBootApplication.jpa.getTransaction().begin();
        CitasBootApplication.jpa.persist(oSettingsDoctorUpdate);
        CitasBootApplication.jpa.getTransaction().commit();
        return "ok";
    }

    @PostMapping("/UpdateCita")
    String UpdateCita(@RequestBody String response) {
        Cita UpdateCitaJSON = json.fromJson(response, Cita.class);
        List<Cita> list = CitasBootApplication.jpa.createQuery("select p from Cita p where id=" + UpdateCitaJSON.getIdcita()).getResultList();

        Cita oUpdateCita = list.get(0);
        oUpdateCita.setDoctor(UpdateCitaJSON.getDoctor());
        oUpdateCita.setHoraatencion(UpdateCitaJSON.getHoraatencion());
        oUpdateCita.setNombrepaciente(UpdateCitaJSON.getNombrepaciente());
        oUpdateCita.setMinuto(UpdateCitaJSON.getMinuto());
        oUpdateCita.setFechacita(UpdateCitaJSON.getFechacita());
        oUpdateCita.setRazon(UpdateCitaJSON.getRazon());

        CitasBootApplication.jpa.getTransaction().begin();
        CitasBootApplication.jpa.persist(oUpdateCita);
        CitasBootApplication.jpa.getTransaction().commit();
        return "ok";
    }

    @PostMapping("/UpdateDoctor")
    String UpdateDoctor(@RequestBody String response) {
        Doctor oDoctorJSON = json.fromJson(response, Doctor.class);
        List<Doctor> list = CitasBootApplication.jpa.createQuery("select p from Doctor p where id=" + oDoctorJSON.getIddoctor()).getResultList();
        System.out.println(oDoctorJSON.getNombredoctor());
        Doctor oDoctorUpdate = list.get(0);
        oDoctorUpdate.setActivo(oDoctorJSON.isActivo());
        oDoctorUpdate.setFlag(oDoctorJSON.isFlag());
        oDoctorUpdate.setNombredoctor(oDoctorJSON.getNombredoctor());

        CitasBootApplication.jpa.getTransaction().begin();
        CitasBootApplication.jpa.persist(oDoctorUpdate);
        CitasBootApplication.jpa.getTransaction().commit();
        return "ok";
    }

    //------------------------------------------DELETE--------------------------------------
    @DeleteMapping("/DeleteSettingsDoctor/{id}")
    String DeleteSettingsDoctor(@PathVariable String id) {
        List<SettingsDoctor> list = CitasBootApplication.jpa.createQuery("select p from SettingsDoctor p where id=" + id).getResultList();
        CitasBootApplication.jpa.getTransaction().begin();
        CitasBootApplication.jpa.remove(list.get(0));
        CitasBootApplication.jpa.getTransaction().commit();

        return "ok";
    }

    @DeleteMapping("/DeleteCita/{id}")
    String DeleteCita(@PathVariable String id) {
        List<Cita> list = CitasBootApplication.jpa.createQuery("select p from Cita p where id=" + id).getResultList();
        CitasBootApplication.jpa.getTransaction().begin();
        CitasBootApplication.jpa.remove(list.get(0));
        CitasBootApplication.jpa.getTransaction().commit();

        return "ok";
    }

    @DeleteMapping("/DeleteDoctor/{id}")
    String DeleteDoctor(@PathVariable String id) {
        List<Cita> list = CitasBootApplication.jpa.createQuery("select p from Doctor p where id=" + id).getResultList();
        CitasBootApplication.jpa.getTransaction().begin();
        CitasBootApplication.jpa.remove(list.get(0));
        CitasBootApplication.jpa.getTransaction().commit();
        return "ok";
    }

}
