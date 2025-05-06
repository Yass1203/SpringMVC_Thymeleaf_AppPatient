package ma.enset.patient_app.web;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import ma.enset.patient_app.entities.Patient;
import ma.enset.patient_app.repository.PatientRepository;

import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@AllArgsConstructor
public class PatientController {

    private final LocalValidatorFactoryBean defaultValidator;
    private PatientRepository patientRepository;
    @GetMapping("/user/index")

    public String index(Model model, @RequestParam (name = "page",defaultValue = "0") int page ,
                           @RequestParam (name= "size" , defaultValue = "4") int size ,
                           @RequestParam (name= "keyword" , defaultValue = "") String kw
    ){
        Page<Patient> pagePatients =patientRepository.findByNameContains(kw,PageRequest.of(page,size));
        model.addAttribute("patientList",pagePatients.getContent() );
        model.addAttribute("pages", new int[pagePatients.getTotalPages()]);
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", kw);
        return "patients";
    }
    @GetMapping("/admin/delete")
    public String delete(Long id , String keyword , int page ){
        patientRepository.deleteById(id);
        return "redirect:/user/index?page"+page+"&keyword"+keyword;
    }

    @GetMapping("/")
    public String home(){
        return "redirect:/user/index";
    }

    @GetMapping("/Patient")
    @ResponseBody
    public List<Patient> listPatients(){
        return patientRepository.findAll();
    }

    @GetMapping("/admin/formPatients")
    public String formPatients(Model model){
        model.addAttribute("patient" ,new Patient());
            return "formPatients";
    }

    @PostMapping(path = "/admin/save")
    public String save(Model model , @Valid Patient patient, BindingResult bindingResult ,
                       @RequestParam(defaultValue = "0") int page ,
                       @RequestParam(defaultValue ="") String keyword)

    {
        if(bindingResult.hasErrors()) return "formPatients";
        patientRepository.save(patient);

        return "redirect:/index?page="+page+"&keyword="+keyword;
    }

    @GetMapping("/admin/editPatient")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String editPatient(Model model , Long id , String keyword , int page ){
       Patient patient = patientRepository.findById(id).orElse(null);
       if(patient == null) throw new RuntimeException("Patient not found");
       model.addAttribute("patient", patient);
       model.addAttribute("keyword", keyword);
       model.addAttribute("page", page);
       return "editPatient";
    }


}
