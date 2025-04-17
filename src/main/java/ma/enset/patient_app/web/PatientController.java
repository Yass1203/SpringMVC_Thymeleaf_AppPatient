package ma.enset.patient_app.web;

import lombok.AllArgsConstructor;
import ma.enset.patient_app.entities.Patient;
import ma.enset.patient_app.repository.PatientRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@AllArgsConstructor
public class PatientController {

    private PatientRepository patientRepository;
    @GetMapping("/index")

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
    @GetMapping("/delete")
    public String delete(Long id , String keyword , int page ){
        patientRepository.deleteById(id);
        return "redirect:/index?page"+page+"&keyword"+keyword;
    }
}
