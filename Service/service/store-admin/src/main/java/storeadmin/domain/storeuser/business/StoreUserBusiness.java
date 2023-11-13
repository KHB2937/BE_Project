package storeadmin.domain.storeuser.business;

import lombok.RequiredArgsConstructor;
import org.delivery.db.store.StoreRepository;
import org.delivery.db.store.enums.StoreStatus;

import org.springframework.stereotype.Service;
import storeadmin.domain.storeuser.controller.model.StoreUserRegisterRequest;
import storeadmin.domain.storeuser.controller.model.StoreUserResponse;
import storeadmin.domain.storeuser.converter.StoreUserConverter;
import storeadmin.domain.storeuser.service.StoreUserService;

@RequiredArgsConstructor
@Service
public class StoreUserBusiness {

    private final StoreUserConverter storeUserConverter;
    private final StoreUserService storeUserService;

    private final StoreRepository storeRepository;  // TODO SERVICE 로 변경하기


    public StoreUserResponse register(
        StoreUserRegisterRequest request
    ){
        var storeEntity = storeRepository.findFirstByNameAndStatusOrderByIdDesc(request.getStoreName(), StoreStatus.REGISTERED);

        var entity = storeUserConverter.toEntity(request, storeEntity.get());

        var newEntity = storeUserService.register(entity);

        var response = storeUserConverter.toResponse(newEntity, storeEntity.get());

        return response;
    }
}
