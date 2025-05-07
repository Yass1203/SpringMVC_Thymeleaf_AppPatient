package ma.enset.patient_app;

import ma.enset.patient_app.entities.Patient;
import ma.enset.patient_app.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import java.util.Date;

@SpringBootApplication
public class PatientAppApplication {
    @Autowired
    private PatientRepository patientRepository;

    public static void main(String[] args) {
        SpringApplication.run(PatientAppApplication.class, args);
    }

    //@Bean
    CommandLineRunner commandLineRunner(PatientRepository patientRepository) {
        return args -> {
            patientRepository.save(new Patient(null ,"Mohammed" ,new Date() , false , 711));
            patientRepository.save(new Patient(null ,"Yassine" ,new Date() , true , 470));
            patientRepository.save(new Patient(null ,"Amine" ,new Date() , false , 185));
        };
    }
    @Bean
    CommandLineRunner commandLineRunner(JdbcUserDetailsManager userDetailsManager, JdbcUserDetailsManager jdbcUserDetailsManager) {
        PasswordEncoder passwordEncoder = passwordEncoder();
        return args -> {
            UserDetails user1 = jdbcUserDetailsManager.loadUserByUsername("user10");
                if (user1== null)
                    jdbcUserDetailsManager.createUser(User.withUsername("user10").password(passwordEncoder.encode("12345")).roles("USER").build());

            UserDetails user2 = jdbcUserDetailsManager.loadUserByUsername("user11");
                if (user2== null)
                    jdbcUserDetailsManager.createUser(User.withUsername("user11").password(passwordEncoder.encode("12345")).roles("USER").build());

            UserDetails user3 = jdbcUserDetailsManager.loadUserByUsername("user12");
                if (user2== null)
                    jdbcUserDetailsManager.createUser(User.withUsername("user12").password(passwordEncoder.encode("12345")).roles("USER","ADMIN").build());


        };
    }



    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();

    }


}
