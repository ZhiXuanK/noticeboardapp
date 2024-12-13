package vttp.batch5.ssf.noticeboard.controllers;

import java.io.StringReader;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.validation.Valid;
import vttp.batch5.ssf.noticeboard.models.Notice;
import vttp.batch5.ssf.noticeboard.services.NoticeService;

// Use this class to write your request handlers

@Controller
@RequestMapping
public class NoticeController {

    @Autowired
    private NoticeService svc;

    @GetMapping
    public String getLandingPage(
        Model model
    ){
        model.addAttribute("notice", new Notice());
        return "notice";
    }

    @PostMapping("/notice")
    public String postNotice(
        @Valid @ModelAttribute Notice notice,
        BindingResult bindings,
        Model model
    ){
        //fail form validation
        if (bindings.hasErrors()){
            return "notice";
        }

        Map<String, String> response = svc.postToNoticeServer(notice);
        JsonObject obj = Json.createReader(new StringReader(response.get("message"))).readObject();

        if (Integer.parseInt(response.get("statusCode")) == 200){
            model.addAttribute("id", obj.getString("message/id"));
            return "view2";
        } else {
            model.addAttribute("error", obj.getString("message/id"));
            return "view3";
        }

    }

    @ResponseBody
    @GetMapping("/status")
    public ResponseEntity<String> getStatus(){
        HttpStatusCode healthStatus = svc.getHealthStatus();

        return ResponseEntity.status(healthStatus)
            .contentType(MediaType.APPLICATION_JSON)
            .body("{}");
    }


}
