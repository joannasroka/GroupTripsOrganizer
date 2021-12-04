package com.sroka.grouptripsorganizer.controller.account_activation;


import com.sroka.grouptripsorganizer.controller.BaseController;
import com.sroka.grouptripsorganizer.dto.account_activation.AccountActivationDto;
import com.sroka.grouptripsorganizer.security.AuthenticationContextService;
import com.sroka.grouptripsorganizer.service.account_activation.VerificationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

import static org.springframework.web.bind.annotation.RequestMethod.HEAD;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/tokens")
public class VerificationTokenController extends BaseController {
    private final VerificationTokenService verificationTokenService;

    @RequestMapping(method = HEAD)
    public void validateToken(@RequestParam String token) {
        verificationTokenService.validateToken(token);
    }


    @PostMapping
    public void activateAccount(@RequestBody AccountActivationDto accountActivation) {
        verificationTokenService.activateAccount(accountActivation);
    }

    @PostMapping("/resend/users/{userEmail}")
    public void resendActivationToken(@PathVariable("userEmail") String userEmail,
                                      @RequestParam String language) {
        verificationTokenService.resend(userEmail, language);
    }
}
