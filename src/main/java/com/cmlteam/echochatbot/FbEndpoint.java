package com.cmlteam.echochatbot;

import com.google.gson.Gson;
import openchat.api.messenger.MessengerClient;
import openchat.api.messenger.json.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class FbEndpoint {
    private static final String VERIFY_TOKEN = "token1";
    private static final Logger log = LoggerFactory.getLogger(FbEndpoint.class);

    @RequestMapping(
            value = "/webhook",
            method = RequestMethod.GET,
            params = {"hub.mode", "hub.challenge", "hub.verify_token"},
            headers = "Accept=plain/text")
    public String subscribe(
            @RequestParam("hub.mode") String mode,
            @RequestParam("hub.challenge") String challenge,
            @RequestParam("hub.verify_token") String token
    ) {
        if ("subscribe".equals(mode) && VERIFY_TOKEN.equals(token)) {
            return challenge;
        }
        return challenge;
    }

    @RequestMapping(value = "/webhook", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public void postWebHook(HttpEntity httpEntity) throws Exception {
        try {
            log.info("Received Web Hook request, {}", httpEntity.getBody());

            MessengerClient messengerClient = new MessengerClient("EAABkraH3fg4BAJgGB6NwmPsvL8EGmZAN1cGMnrDHV9o5FHRLi2WTuixMLPisqNQKoe1eBfUxLob1wRNQpW3f1j2rNkcuwK3eyplJwdftKGvZCU0pwW99bdc9CuDeO2SXCm2pFKF8GKjgqQZAAND7tbNAMANbsNZB3PKgTz84VgZDZD");

            String json = new Gson().toJson(httpEntity.getBody());
            Callback callback = Callback.parse(json);


            messengerClient.sendText(callback.entry.get(0).messaging.get(0).sender.id, callback.entry.get(0).messaging.get(0).message.text);
        }
        catch(Exception e) {
            log.error("error processing request", e);
        }
    }
}
