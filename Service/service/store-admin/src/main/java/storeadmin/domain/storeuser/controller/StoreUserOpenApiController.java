package storeadmin.domain.storeuser.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import storeadmin.domain.storeuser.business.StoreUserBusiness;
import storeadmin.domain.storeuser.controller.model.StoreUserRegisterRequest;
import storeadmin.domain.storeuser.controller.model.StoreUserResponse;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/open-api/store-user")
public class StoreUserOpenApiController {

    private final StoreUserBusiness storeUserBusiness;

    @PostMapping("")
    public StoreUserResponse register(
        @Valid
        @RequestBody StoreUserRegisterRequest request
    ){
        var response = storeUserBusiness.register(request);
        return response;
    }
}
