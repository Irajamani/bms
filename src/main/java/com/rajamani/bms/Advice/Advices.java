package com.rajamani.bms.Advice;

import com.rajamani.bms.Exception.NotFoundException;
import com.rajamani.bms.Service.UserService;
import com.rajamani.bms.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class Advices {

    @Autowired
    private UserService userService;

    @ExceptionHandler(NotFoundException.class)
    public String handleNotFoundException(NotFoundException e, Model model){
        model.addAttribute("status", 400);
        model.addAttribute("excepton", e);
        return "common/err";
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, Model model) {
        model.addAttribute("status", 500);
        model.addAttribute("exception", e);

        return "common/error";
    }

    @ModelAttribute
    public void addCommonAttributes(@AuthenticationPrincipal
                                            UserDetails userDetails, Model model) {
        if (userDetails != null) {
            User user =
                    userService.getUserByName(userDetails.getUsername());
            model.addAttribute("user", user);
        }
    }
}

