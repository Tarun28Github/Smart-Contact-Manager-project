package com.scm.helper;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpSession;

@Component
public class SessionMessage {

    public static void messageRemove(){
        try {
            System.out.println("Removing message form session");
            @SuppressWarnings("null")
            HttpSession session = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest().getSession();
            session.removeAttribute("message");    
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
