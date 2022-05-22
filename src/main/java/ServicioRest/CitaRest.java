/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServicioRest;

import EntidadesSettings.SettingsDoctor;
import Controller.CitasBootApplication;
import Entidades.Address;
import Entidades.Cita;
import Entidades.Doctor;
import Entidades.HoraAtencion;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/SettingsDoctorAll")
    String getSettingsDoctor(@RequestBody String response) {
        org.json.JSONObject jsonResponse = new org.json.JSONObject(response);
        if (DeviceExist(jsonResponse.getString("address"), jsonResponse.getString("nombreDispositivo"))) {
            List<SettingsDoctor> list = CitasBootApplication.jpa.createQuery("select p from SettingsDoctor p").getResultList();
            return JSONArray.toJSONString(list);
        } else {
            return "[]";
        }
    }

    @PostMapping("/CitaFilter")
    String CitaFilter(@RequestBody JSONObject response) {
        if (DeviceExist(response.getAsString("address"), response.getAsString("nombreDispositivo"))) {
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
        } else {
            return "[]";
        }
    }

    @PostMapping("/DoctorAll")
    String getDoctor(@RequestBody String response) {
        org.json.JSONObject jsonResponse = new org.json.JSONObject(response);
        if (DeviceExist(jsonResponse.getString("address"), jsonResponse.getString("nombreDispositivo"))) {
            List<Doctor> list = CitasBootApplication.jpa.createQuery("select p from Doctor p where flag = false and activo = true order by iddoctor asc").getResultList();
            return JSONArray.toJSONString(list);
        } else {
            return "[]";
        }
    }

    @PostMapping("/HoraAtencionAll")
    String getHoraAtencion(@RequestBody String response) {
        org.json.JSONObject jsonResponse = new org.json.JSONObject(response);
        if (DeviceExist(jsonResponse.getString("address"), jsonResponse.getString("nombreDispositivo"))) {
            List<HoraAtencion> list = CitasBootApplication.jpa.createQuery("select p from HoraAtencion p order by idhoraatencion ASC").getResultList();
            return JSONArray.toJSONString(list);
        } else {
            return "[]";
        }
    }
    //actualiza las citas que tego cada vez que agrego, elimino o modifico

    @PostMapping("/GetListCitasByFecha")
    String getCitasByFecha(@RequestBody String response) {
        org.json.JSONObject jsonResponse = new org.json.JSONObject(response);
        if (DeviceExist(jsonResponse.getString("address"), jsonResponse.getString("nombreDispositivo"))) {
            String id = jsonResponse.get("fecha").toString();
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
        } else {
            return "[ ]";
        }
    }

    @PostMapping("/AddSettingsDoctor")
    String AddSettingsDoctor(@RequestBody String response) {
        org.json.JSONObject jsonResponse = new org.json.JSONObject(response);
        if (DeviceExist(jsonResponse.getString("address"), jsonResponse.getString("nombreDispositivo"))) {
            SettingsDoctor oAddSettingsDoctor = json.fromJson(jsonResponse.get("data").toString(), SettingsDoctor.class);
            CitasBootApplication.jpa.getTransaction().begin();
            CitasBootApplication.jpa.persist(oAddSettingsDoctor);
            CitasBootApplication.jpa.refresh(oAddSettingsDoctor);
            CitasBootApplication.jpa.getTransaction().commit();
            return json.toJson(oAddSettingsDoctor);
        } else {
            return "sin acceso";
        }
    }

    @PostMapping("/AddCita")
    String AddCita(@RequestBody String response) {
        org.json.JSONObject jsonResponse = new org.json.JSONObject(response);
        if (DeviceExist(jsonResponse.getString("address"), jsonResponse.getString("nombreDispositivo"))) {
            Cita oAddCita = json.fromJson(jsonResponse.get("data").toString(), Cita.class);
            CitasBootApplication.jpa.getTransaction().begin();
            CitasBootApplication.jpa.persist(oAddCita);
            CitasBootApplication.jpa.getTransaction().commit();
            return "ok";
        } else {
            return "sin acceso";
        }
    }

    @PostMapping("/AddDoctor")
    String AddDoctor(@RequestBody String response) {
        org.json.JSONObject jsonResponse = new org.json.JSONObject(response);
        if (DeviceExist(jsonResponse.getString("address"), jsonResponse.getString("nombreDispositivo"))) {
            Doctor AddDoctor = json.fromJson(jsonResponse.get("data").toString(), Doctor.class);
            CitasBootApplication.jpa.getTransaction().begin();
            CitasBootApplication.jpa.persist(AddDoctor);
            CitasBootApplication.jpa.getTransaction().commit();
            return "ok";
        } else {
            return "sin acceso";
        }
    }

    //------------------------------------------UPDATE--------------------------------------
    @PostMapping("/UpdateSettingsDoctor")
    String UpdateSettingsDoctor(@RequestBody String response) {
        org.json.JSONObject jsonResponse = new org.json.JSONObject(response);
        if (DeviceExist(jsonResponse.getString("address"), jsonResponse.getString("nombreDispositivo"))) {
            SettingsDoctor oSettingsDoctorJSON = json.fromJson(jsonResponse.getString("data"), SettingsDoctor.class);
            List<SettingsDoctor> list = CitasBootApplication.jpa.createQuery("select p from SettingsDoctor p where id=" + oSettingsDoctorJSON.getId()).getResultList();
            SettingsDoctor oSettingsDoctorUpdate = list.get(0);
            oSettingsDoctorUpdate.setDoctor(oSettingsDoctorJSON.getDoctor());
            oSettingsDoctorUpdate.setName(oSettingsDoctorJSON.getName());

            CitasBootApplication.jpa.getTransaction().begin();
            CitasBootApplication.jpa.persist(oSettingsDoctorUpdate);
            CitasBootApplication.jpa.getTransaction().commit();
            return "ok";
        } else {
            return "sin acceso";
        }
    }

    @PostMapping("/UpdateCita")
    String UpdateCita(@RequestBody String response) {
        org.json.JSONObject jsonResponse = new org.json.JSONObject(response);
        if (DeviceExist(jsonResponse.getString("address"), jsonResponse.getString("nombreDispositivo"))) {
            Cita UpdateCitaJSON = json.fromJson(jsonResponse.getString("data"), Cita.class);
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
        } else {
            return "sin acceso";
        }
    }

    @PostMapping("/UpdateDoctor")
    String UpdateDoctor(@RequestBody String response) {
        org.json.JSONObject jsonResponse = new org.json.JSONObject(response);
        if (DeviceExist(jsonResponse.getString("address"), jsonResponse.getString("nombreDispositivo"))) {
            Doctor oDoctorJSON = json.fromJson(jsonResponse.getString("data"), Doctor.class);
            List<Doctor> list = CitasBootApplication.jpa.createQuery("select p from Doctor p where id=" + oDoctorJSON.getIddoctor()).getResultList();
            Doctor oDoctorUpdate = list.get(0);
            oDoctorUpdate.setActivo(oDoctorJSON.isActivo());
            oDoctorUpdate.setFlag(oDoctorJSON.isFlag());
            oDoctorUpdate.setNombredoctor(oDoctorJSON.getNombredoctor());

            CitasBootApplication.jpa.getTransaction().begin();
            CitasBootApplication.jpa.persist(oDoctorUpdate);
            CitasBootApplication.jpa.getTransaction().commit();
            return "ok";
        } else {
            return "sin acceso";
        }
    }

    //------------------------------------------DELETE--------------------------------------
    @PostMapping("/DeleteSettingsDoctor")
    String DeleteSettingsDoctor(@RequestBody String response) {
        org.json.JSONObject jsonResponse = new org.json.JSONObject(response);
        if (DeviceExist(jsonResponse.getString("address"), jsonResponse.getString("nombreDispositivo"))) {
            String id = jsonResponse.get("id").toString();
            List<SettingsDoctor> list = CitasBootApplication.jpa.createQuery("select p from SettingsDoctor p where id=" + id).getResultList();
            CitasBootApplication.jpa.getTransaction().begin();
            CitasBootApplication.jpa.remove(list.get(0));
            CitasBootApplication.jpa.getTransaction().commit();

            return "ok";
        } else {
            return "sin acceso";
        }
    }

    @PostMapping("/DeleteCita")
    String DeleteCita(@RequestBody String response) {
        org.json.JSONObject jsonResponse = new org.json.JSONObject(response);
        if (DeviceExist(jsonResponse.getString("address"), jsonResponse.getString("nombreDispositivo"))) {
            String id = jsonResponse.get("id").toString();
            List<Cita> list = CitasBootApplication.jpa.createQuery("select p from Cita p where id=" + id).getResultList();
            CitasBootApplication.jpa.getTransaction().begin();
            CitasBootApplication.jpa.remove(list.get(0));
            CitasBootApplication.jpa.getTransaction().commit();
            return "ok";
        } else {
            return "sin acceso";
        }
    }

    @PostMapping("/DeleteDoctor")
    String DeleteDoctor(@RequestBody String response) {
        org.json.JSONObject jsonResponse = new org.json.JSONObject(response);
        if (DeviceExist(jsonResponse.getString("address"), jsonResponse.getString("nombreDispositivo"))) {
            String id = jsonResponse.get("id").toString();
            List<Cita> list = CitasBootApplication.jpa.createQuery("select p from Doctor p where id=" + id).getResultList();
            CitasBootApplication.jpa.getTransaction().begin();
            CitasBootApplication.jpa.remove(list.get(0));
            CitasBootApplication.jpa.getTransaction().commit();
            return "ok";
        } else {
            return "sin acceso";
        }
    }

    boolean DeviceExist(String address, String nombreDispositivo) {
        //true si existe, false si no existe
        List<Address> listAddress = CitasBootApplication.jpa.createQuery("select p from Address p where address ='" + address + "'").getResultList();
        if (listAddress.isEmpty()) {
            Address oaddresss=new Address();
            oaddresss.setAddress(address);
            oaddresss.setNombreDispositivo(nombreDispositivo);
            oaddresss.setActivo(false);
            CitasBootApplication.jpa.getTransaction().begin();
            CitasBootApplication.jpa.persist(oaddresss);
            CitasBootApplication.jpa.getTransaction().commit();
            return false;
        } else {
            if(listAddress.get(0).isActivo()){
                return true;
            }
            else{
                return false;
            }
            
        }
    }
}
