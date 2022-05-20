package Controller;

import ServicioRest.CitaRest;
import Util.JPAUtil;
import javax.persistence.EntityManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackageClasses = CitaRest.class)
public class CitasBootApplication {

   public static EntityManager jpa = JPAUtil.getEntityManagerFactory().createEntityManager();

    public static void main(String[] args) {
        System.setProperty("illegal-access", "permit");
        SpringApplication.run(CitasBootApplication.class, args);
    }

}
