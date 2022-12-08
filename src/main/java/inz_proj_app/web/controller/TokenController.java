package inz_proj_app.web.controller;

import inz_proj_app.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/token")
public class TokenController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String token(Model model){
        return "token";
    }

    @PostMapping
    public String activateUser(String email, String token){
        userService.activateUser(email,token);

//        if (result.hasErrors()){
//            return "registration";
//        }
        return "redirect:/login";
    }
}
