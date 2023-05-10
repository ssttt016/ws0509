package com.kbstar.controller;

import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonArrayFormatVisitor;
import com.kbstar.dto.Adm;
import com.kbstar.dto.Cust;
import com.kbstar.dto.Sales;
import com.kbstar.service.AdmService;
import com.kbstar.service.SalesService;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@Slf4j
public class MainController {
    @Value("${adminserver}")
    String adminserver;
    @Autowired
    private BCryptPasswordEncoder encoder;
    @Autowired
    AdmService admService;
    @Autowired
    SalesService salesService;
    @RequestMapping("/")
    public String main(Model model) throws Exception {
        model.addAttribute("adminserver",adminserver);
        return "index";
    }

    @RequestMapping("/livechart")
    public String livechart(Model model){
        model.addAttribute("center","livechart");
        model.addAttribute("adminserver",adminserver);
        return "index";
    }
    @RequestMapping("/charts")
    public String charts(Model model) {
        model.addAttribute("center", "charts");
        return "index";
    }
    @RequestMapping("/login")
    public String login(Model model) {
        model.addAttribute("center","login");
        return "index";
    }
    @RequestMapping("/logouts")
    public String logout(Model model, HttpSession session){
        if(session != null){
            session.invalidate();
        }
        return "redirect:/";
    }
    @RequestMapping("/loginimpl")
    public String loginimpl(Model model, String id, String pwd, HttpSession session) throws Exception {
        log.info("-------------------------------------------"+id+" "+pwd);
        Adm adm = null;
        String nextPage = "loginfail";
        try {
            adm = admService.get(id);
            if(adm != null && encoder.matches(pwd,adm.getPwd())){
                nextPage = "loginok";
                session.setMaxInactiveInterval(100000);
                session.setAttribute("loginadm",adm);
            }
        } catch (Exception e) {
            throw new Exception("시스템 장애 잠시 후 다시 로그인 하세요.");
        }
        model.addAttribute("center",nextPage);
        return "index";
    }
    @RequestMapping("/adminfo")
    public String adminfo(Model model, String id) throws Exception {
        Adm adm = null;
        try {
            adm = admService.get(id);
        } catch (Exception e) {
            throw new Exception("시스템 장애");
        }
        model.addAttribute("adminfo",adm);
        model.addAttribute("center","adminfo");
        return "index";
    }
    @RequestMapping("/adminfoimpl")
    public String adminfoimpl(Model model, Adm adm) throws Exception {
        try {
            log.info(adm.toString());
            adm.setPwd(encoder.encode(adm.getPwd()));
            admService.modify(adm);
        } catch (Exception e) {
            throw new Exception("시스템 장애");
        }
        return "redirect:/adminfo?id="+adm.getId();
    }
    @RequestMapping("/register")
    public String register(Model model) {
        model.addAttribute("center", "register");
        return "index";
    }
    @RequestMapping("/registerimpl")
    public String registerimpl(Model model, Adm adm, HttpSession session) throws Exception {
        try {
            adm.setPwd(encoder.encode(adm.getPwd()));
            admService.register(adm);
            session.setMaxInactiveInterval(100000);
            session.setAttribute("loginadm",adm);
        } catch (Exception e) {
            throw new Exception("가입 오류");
        }
        model.addAttribute("loginadm",adm);
//        model.addAttribute("center","registerok");
        return "redirect:/";
    }
    @RequestMapping("/websocket")
    public String websocket(Model model) {
        model.addAttribute("center","websocket");
        model.addAttribute("adminserver",adminserver);
        return "index";
    }

}