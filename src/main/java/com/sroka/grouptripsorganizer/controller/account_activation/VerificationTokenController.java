package com.sroka.grouptripsorganizer.controller.account_activation;


import com.sroka.grouptripsorganizer.controller.BaseController;
import com.sroka.grouptripsorganizer.dto.AccountActivationDto;
import com.sroka.grouptripsorganizer.security.AuthenticationContextService;
import com.sroka.grouptripsorganizer.service.account_activation.VerificationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static org.springframework.web.bind.annotation.RequestMethod.HEAD;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/tokens")
public class VerificationTokenController extends BaseController {
    private final VerificationTokenService verificationTokenService;
    private final AuthenticationContextService authenticationContextService;

    @RequestMapping(method = HEAD)
    public void validateToken(@RequestParam String token) {
        verificationTokenService.validateToken(token);
    }


    @PostMapping
    public void activateAccount(@RequestBody AccountActivationDto accountActivation) {
        verificationTokenService.activateAccount(accountActivation);
    }

    // CHANGED HERE: user email instead of admin id and user id
    @GetMapping("/resend/users/{userEmail}")
    public void resendActivationToken(@PathVariable("userEmail") String userEmail) {
        verificationTokenService.resend(userEmail);
    }
}
