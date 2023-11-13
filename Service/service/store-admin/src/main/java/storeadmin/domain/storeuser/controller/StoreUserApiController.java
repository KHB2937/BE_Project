package storeadmin.domain.storeuser.controller;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import storeadmin.domain.authorization.model.UserSession;
import storeadmin.domain.storeuser.controller.model.StoreUserResponse;
import storeadmin.domain.storeuser.converter.StoreUserConverter;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/store-user")
public class StoreUserApiController {

    private final StoreUserConverter storeUserConverter;

    @GetMapping("/me")
    public StoreUserResponse me(
        @Parameter(hidden = true)
        @AuthenticationPrincipal UserSession userSession
    ){
        return storeUserConverter.toResponse(userSession);
    }
}
