package ma.enset.patient_app;

import ma.enset.patient_app.entities.Patient;
import ma.enset.patient_app.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Date;

@SpringBootApplication
public class PatientAppApplication implements CommandLineRunner {
    @Autowired
    private PatientRepository patientRepository;

    public static void main(String[] args) {
        SpringApplication.run(PatientAppApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        patientRepository.save(new Patient(null ,"Mohammed" ,new Date() , false , 35));
        patientRepository.save(new Patient(null ,"Yassine" ,new Date() , true , 47));
        patientRepository.save(new Patient(null ,"Amine" ,new Date() , false , 85));
    }
}
