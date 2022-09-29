/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServicioRest;

import EntidadesSettings.SettingsDoctor;
import Controller.CitasBootApplication;
import Entidades.Address;
import Entidades.Address_Doctor;
import Entidades.Cita;
import Entidades.Doctor;
import Entidades.HoraAtencion;
import Entidades.Rol;
import Util.JPAUtil;
import com.google.gson.Gson;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(CitaRest.class);
    Gson json = new Gson();

    @GetMapping("/employees")
    String all() {
        List<SettingsDoctor> list = CitasBootApplication.jpa.createQuery("select p from SettingsDoctor p").getResultList();
        Cita ocita = new Cita();
        String cita = json.toJson(list.get(0));// a json
        ocita = json.fromJson(cita, Cita.class);// a objeto de objeto 
        return cita;
    }

    @PostMapping("/GetAddress")
    String getAddress(@RequestBody JSONObject response) {
        try {
            DeviceExist(response.getAsString("address"), response.getAsString("nombreDispositivo"));
            Address oaddress = getAddress(response.getAsString("address"));
            return json.toJson(oaddress);

        } catch (Exception e) {
            try {
                String nombre = response.getAsString("nombreDispositivo") + "-" + response.getAsString("version");
                logger.error(e.toString() + " SettingsDoctorAll-" + nombre);

            } catch (JSONException js) {
                logger.error(js.toString() + " SettingsDoctorAll not found NombreDispositivo");
            }

            return "[]";
        }

    }

    @PostMapping("/GetRol")
    String getRol(@RequestBody JSONObject response) {
        try {
            DeviceExist(response.getAsString("address"), response.getAsString("nombreDispositivo"));
            Address oaddress = getAddress(response.getAsString("address"));
            return json.toJson(oaddress.getRol().getRolname());

        } catch (Exception e) {
            try {
                String nombre = response.getAsString("nombreDispositivo") + "-" + response.getAsString("version");
                logger.error(e.toString() + "SettingsDoctorAll-" + nombre);

            } catch (JSONException js) {
                logger.error(js.toString() + "SettingsDoctorAll not found NombreDispositivo");
            }

            return "[]";
        }

    }

    @PostMapping("/SettingsDoctorAll")
    String getSettingsDoctor(@RequestBody JSONObject response) {
        try {

            if (DeviceExist(response.getAsString("address"), response.getAsString("nombreDispositivo"))) {
                Address oaddres=getAddress(response.getAsString("address"));
                List<SettingsDoctor> list = CitasBootApplication.jpa.createQuery("select p from SettingsDoctor p where idaddress="+oaddres.getId()+" or name= '"+response.getAsString("address")+"'").getResultList();
                return JSONArray.toJSONString(list);
            } else {
                return "[]";
            }
        } catch (Exception e) {
            try {
                String nombre = response.getAsString("nombreDispositivo") + "-" + response.getAsString("version");
                logger.error(e.toString() + "SettingsDoctorAll-" + nombre);

            } catch (JSONException js) {
                logger.error(js.toString() + "SettingsDoctorAll not found NombreDispositivo");
            }

            return "[]";
        }

    }

    @PostMapping("/CitaFilter")
    String CitaFilter(@RequestBody JSONObject response) {
        try {
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
        } catch (Exception e) {
            CitasBootApplication.jpa = JPAUtil.getEntityManagerFactory().createEntityManager();
            try {
                String nombre = response.getAsString("nombreDispositivo") + "-" + response.getAsString("version");
                logger.error(e.toString() + " CitaFilter-" + nombre);
            } catch (JSONException js) {
                logger.error(js.toString() + " CitaFilter not found NombreDispositivo");
            }
            return "[]";
        }
    }

    @PostMapping("/DoctorAll")
    String getDoctor(@RequestBody JSONObject response) {
        try {
            if (DeviceExist(response.getAsString("address"), response.getAsString("nombreDispositivo"))) {
                Address oaddress = getAddress(response.getAsString("address"));
                List<Doctor> list = new ArrayList<>();

                if (oaddress.getRol().getRolname().equals("ADMINISTRADOR")) {
                    list = CitasBootApplication.jpa.createQuery("select p from Doctor p where flag = false and activo = true order by iddoctor asc").getResultList();
                } else if (oaddress.getRol().getRolname().equals("OBSERVADOR") || oaddress.getRol().getRolname().equals("ASISTENTA")) {
                    List<Address_Doctor> listaddress_doctor = CitasBootApplication.jpa.createQuery("select p from Address_Doctor p where idaddress=" + oaddress.getId() + " order by id asc").getResultList();
                    for (Address_Doctor address_Doctor : listaddress_doctor) {
                        list.add(address_Doctor.getDoctor());

                    }
                }
                return JSONArray.toJSONString(list);
            } else {
                return "[]";
            }
        } catch (Exception e) {
            try {
                String nombre = response.getAsString("nombreDispositivo") + "-" + response.getAsString("version");
                logger.error(e.toString() + "DoctorAll-" + nombre);

            } catch (JSONException js) {
                logger.error(js.toString() + "DoctorAll not found NombreDispositivo");
            }

            return "[]";
        }
    }

    @PostMapping("/HoraAtencionAll")
    String getHoraAtencion(@RequestBody JSONObject response) {
        try {
            if (DeviceExist(response.getAsString("address"), response.getAsString("nombreDispositivo"))) {
                List<HoraAtencion> list = CitasBootApplication.jpa.createQuery("select p from HoraAtencion p order by idhoraatencion ASC").getResultList();
                return JSONArray.toJSONString(list);
            } else {
                return "[]";
            }
        } catch (Exception e) {
            try {
                String nombre = response.getAsString("nombreDispositivo") + "-" + response.getAsString("version");
                logger.error(e.toString() + "HoraAtencionAll-" + nombre);

            } catch (JSONException js) {
                logger.error(js.toString() + "HoraAtencionAll not found NombreDispositivo");
            }

            return "[]";
        }
    }
    //actualiza las citas que tego cada vez que agrego, elimino o modifico

    @PostMapping("/GetListCitasByFecha")
    String getCitasByFecha(@RequestBody JSONObject response) {
        try {
            if (DeviceExist(response.getAsString("address"), response.getAsString("nombreDispositivo"))) {
                String id = response.get("fecha").toString();
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
        } catch (Exception e) {
            try {
                String nombre = response.getAsString("nombreDispositivo") + "-" + response.getAsString("version");
                logger.error(e.toString() + "GetListCitasByFecha-" + nombre);

            } catch (JSONException js) {
                logger.error(js.toString() + "GetListCitasByFecha not found NombreDispositivo");
            }

            return "[]";
        }
    }

    @PostMapping("/AddSettingsDoctor")
    String AddSettingsDoctor(@RequestBody JSONObject response) {
        try {
            if (DeviceExist(response.getAsString("address"), response.getAsString("nombreDispositivo"))) {
                SettingsDoctor oAddSettingsDoctor = json.fromJson(response.get("data").toString(), SettingsDoctor.class);
                CitasBootApplication.jpa.getTransaction().begin();
                CitasBootApplication.jpa.persist(oAddSettingsDoctor);
                CitasBootApplication.jpa.refresh(oAddSettingsDoctor);
                CitasBootApplication.jpa.getTransaction().commit();
                return json.toJson(oAddSettingsDoctor);
            } else {
                return "sin acceso";
            }
        } catch (Exception e) {
            try {
                String nombre = response.getAsString("nombreDispositivo") + "-" + response.getAsString("version");
                logger.error(e.toString() + "AddSettingsDoctor-" + nombre);

            } catch (JSONException js) {
                logger.error(js.toString() + "AddSettingsDoctor not found NombreDispositivo");
            }

            return "[]";
        }

    }

    @PostMapping("/AddCita")
    String AddCita(@RequestBody JSONObject response) {
        try {

            if (DeviceExist(response.getAsString("address"), response.getAsString("nombreDispositivo"))) {
                Address oaddress = getAddress(response.getAsString("address"));
                if (oaddress.getRol().getRolname().equals("ADMINISTRADOR") || oaddress.getRol().getRolname().equals("ASISTENTA")) {
                    Cita oAddCita = json.fromJson(response.get("data").toString(), Cita.class);
                    CitasBootApplication.jpa.getTransaction().begin();
                    CitasBootApplication.jpa.persist(oAddCita);
                    CitasBootApplication.jpa.getTransaction().commit();
                    return "ok";
                } else {
                    return "sin permiso";
                }
            } else {
                return "sin acceso";
            }
        } catch (Exception e) {
            try {
                String nombre = response.getAsString("nombreDispositivo") + "-" + response.getAsString("version");
                logger.error(e.toString() + "AddCita-" + nombre);

            } catch (JSONException js) {
                logger.error(js.toString() + "AddCita not found NombreDispositivo");
            }

            return "[]";
        }
    }

    @PostMapping("/AddDoctor")
    String AddDoctor(@RequestBody JSONObject response) {
        try {

            if (DeviceExist(response.getAsString("address"), response.getAsString("nombreDispositivo"))) {
                Doctor AddDoctor = json.fromJson(response.get("data").toString(), Doctor.class);
                CitasBootApplication.jpa.getTransaction().begin();
                CitasBootApplication.jpa.persist(AddDoctor);
                CitasBootApplication.jpa.getTransaction().commit();
                return "ok";
            } else {
                return "sin acceso";
            }
        } catch (Exception e) {
            try {
                String nombre = response.getAsString("nombreDispositivo") + "-" + response.getAsString("version");
                logger.error(e.toString() + "AddDoctor-" + nombre);

            } catch (JSONException js) {
                logger.error(js.toString() + "AddDoctor not found NombreDispositivo");
            }

            return "[]";
        }
    }

    //------------------------------------------UPDATE--------------------------------------
    @PostMapping("/UpdateSettingsDoctor")
    String UpdateSettingsDoctor(@RequestBody JSONObject response) {
        try {

            if (DeviceExist(response.getAsString("address"), response.getAsString("nombreDispositivo"))) {
                SettingsDoctor oSettingsDoctorJSON = json.fromJson(response.getAsString("data"), SettingsDoctor.class);
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
        } catch (Exception e) {
            try {
                String nombre = response.getAsString("nombreDispositivo") + "-" + response.getAsString("version");
                logger.error(e.toString() + "UpdateSettingsDoctor-" + nombre);

            } catch (JSONException js) {
                logger.error(js.toString() + "UpdateSettingsDoctor not found NombreDispositivo");
            }

            return "[]";
        }
    }

    @PostMapping("/UpdateCita")
    String UpdateCita(@RequestBody JSONObject response) {
        try {
            if (DeviceExist(response.getAsString("address"), response.getAsString("nombreDispositivo"))) {
                Cita UpdateCitaJSON = json.fromJson(response.getAsString("data"), Cita.class);
                List<Cita> list = CitasBootApplication.jpa.createQuery("select p from Cita p where id=" + UpdateCitaJSON.getIdcita()).getResultList();

                Cita oUpdateCita = list.get(0);
                oUpdateCita.setDoctor(UpdateCitaJSON.getDoctor());
                oUpdateCita.setHoraatencion(UpdateCitaJSON.getHoraatencion());
                oUpdateCita.setNombrepaciente(UpdateCitaJSON.getNombrepaciente());
                oUpdateCita.setMinuto(UpdateCitaJSON.getMinuto());
                oUpdateCita.setFechacita(UpdateCitaJSON.getFechacita());
                oUpdateCita.setRazon(UpdateCitaJSON.getRazon());
                oUpdateCita.setCelular(UpdateCitaJSON.getCelular());

                CitasBootApplication.jpa.getTransaction().begin();
                CitasBootApplication.jpa.persist(oUpdateCita);
                CitasBootApplication.jpa.getTransaction().commit();
                return "ok";
            } else {
                return "sin acceso";
            }
        } catch (Exception e) {
            try {
                String nombre = response.getAsString("nombreDispositivo") + "-" + response.getAsString("version");
                logger.error(e.toString() + "UpdateCita-" + nombre);

            } catch (JSONException js) {
                logger.error(js.toString() + "UpdateCita not found NombreDispositivo");
            }

            return "[]";
        }
    }

    @PostMapping("/UpdateDoctor")
    String UpdateDoctor(@RequestBody JSONObject response) {
        try {
            if (DeviceExist(response.getAsString("address"), response.getAsString("nombreDispositivo"))) {
                Doctor oDoctorJSON = json.fromJson(response.getAsString("data"), Doctor.class);
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
        } catch (Exception e) {
            try {
                String nombre = response.getAsString("nombreDispositivo") + "-" + response.getAsString("version");
                logger.error(e.toString() + "UpdateDoctor-" + nombre);

            } catch (JSONException js) {
                logger.error(js.toString() + "UpdateDoctor not found NombreDispositivo");
            }

            return "[]";
        }
    }

    //------------------------------------------DELETE--------------------------------------
    @PostMapping("/DeleteSettingsDoctor")
    String DeleteSettingsDoctor(@RequestBody JSONObject response) {
        try {

            if (DeviceExist(response.getAsString("address"), response.getAsString("nombreDispositivo"))) {
                String id = response.get("id").toString();
                List<SettingsDoctor> list = CitasBootApplication.jpa.createQuery("select p from SettingsDoctor p where id=" + id).getResultList();
                CitasBootApplication.jpa.getTransaction().begin();
                CitasBootApplication.jpa.remove(list.get(0));
                CitasBootApplication.jpa.getTransaction().commit();

                return "ok";
            } else {
                return "sin acceso";
            }
        } catch (Exception e) {
            try {
                String nombre = response.getAsString("nombreDispositivo") + "-" + response.getAsString("version");
                logger.error(e.toString() + "DeleteSettingsDoctor-" + nombre);

            } catch (JSONException js) {
                logger.error(js.toString() + "DeleteSettingsDoctor not found NombreDispositivo");
            }

            return "[]";
        }
    }

    @PostMapping("/DeleteCita")
    String DeleteCita(@RequestBody JSONObject response) {
        try {

            if (DeviceExist(response.getAsString("address"), response.getAsString("nombreDispositivo"))) {
                String id = response.get("id").toString();
                List<Cita> list = CitasBootApplication.jpa.createQuery("select p from Cita p where id=" + id).getResultList();
                CitasBootApplication.jpa.getTransaction().begin();
                CitasBootApplication.jpa.remove(list.get(0));
                CitasBootApplication.jpa.getTransaction().commit();
                return "ok";
            } else {
                return "sin acceso";
            }
        } catch (Exception e) {
            try {
                String nombre = response.getAsString("nombreDispositivo") + "-" + response.getAsString("version");
                logger.error(e.toString() + "DeleteCita-" + nombre);

            } catch (JSONException js) {
                logger.error(js.toString() + "DeleteCita not found NombreDispositivo");
            }

            return "[]";
        }
    }

    @PostMapping("/DeleteDoctor")
    String DeleteDoctor(@RequestBody JSONObject response) {
        try {
            if (DeviceExist(response.getAsString("address"), response.getAsString("nombreDispositivo"))) {
                String id = response.get("id").toString();
                List<Cita> list = CitasBootApplication.jpa.createQuery("select p from Doctor p where id=" + id).getResultList();
                CitasBootApplication.jpa.getTransaction().begin();
                CitasBootApplication.jpa.remove(list.get(0));
                CitasBootApplication.jpa.getTransaction().commit();
                return "ok";
            } else {
                return "sin acceso";
            }
        } catch (Exception e) {
            try {
                String nombre = response.getAsString("nombreDispositivo") + "-" + response.getAsString("version");
                logger.error(e.toString() + "DeleteDoctor-" + nombre);

            } catch (JSONException js) {
                logger.error(js.toString() + "DeleteDoctor not found NombreDispositivo");
            }

            return "[]";
        }
    }

    boolean DeviceExist(String address, String nombreDispositivo) {
        try {
            //true si existe, false si no existe
            List<Address> listAddress = CitasBootApplication.jpa.createQuery("select p from Address p where address ='" + address + "'").getResultList();
            if (listAddress.isEmpty()) {
                List<Rol> listRol = CitasBootApplication.jpa.createQuery("select p from Rol p where rolname ='" + "ADMINISTRADOR" + "'").getResultList();
                Address oaddresss = new Address();
                oaddresss.setRol(listRol.get(0));
                oaddresss.setAddress(address);
                oaddresss.setNombreDispositivo(nombreDispositivo);
                oaddresss.setActivo(false);
                CitasBootApplication.jpa.getTransaction().begin();
                CitasBootApplication.jpa.persist(oaddresss);
                CitasBootApplication.jpa.getTransaction().commit();
                return false;
            } else {
                if (listAddress.get(0).isActivo()) {
                    return true;
                } else {
                    return false;
                }

            }
        } catch (Exception e) {
            logger.error(e.toString() + " DeviceExist-" + nombreDispositivo);
            return false;
        }
    }

    Address getAddress(String address) {

        List<Address> listAdress = CitasBootApplication.jpa.createQuery("select p from Address p where address ='" + address + "'").getResultList();
        return listAdress.get(0);
    }

    SettingsDoctor getSettingDoctor(Address address) {
        List<SettingsDoctor> listSettingsDoctor = CitasBootApplication.jpa.createQuery("select p from SettingsDoctor p where idaddress ='" + address.getId() + "'").getResultList();
        return listSettingsDoctor.get(0);
    }
}
