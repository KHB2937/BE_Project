package org.delivery.api.domain.storemenu.business;

import lombok.RequiredArgsConstructor;
import org.delivery.api.annotation.Business;
import org.delivery.api.domain.storemenu.controller.model.StoreMenuRegisterRequest;
import org.delivery.api.domain.storemenu.controller.model.StoreMenuResponse;
import org.delivery.api.domain.storemenu.converter.StoreMenuConverter;
import org.delivery.api.domain.storemenu.service.StoreMenuService;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Business
public class StoreMenuBusiness {

    private final StoreMenuService storeMenuService; // StoreMenuService 의존성 주입
    private final StoreMenuConverter storeMenuConverter; // StoreMenuConverter 의존성 주입

    // 스토어 메뉴 등록 메서드
    public StoreMenuResponse register(StoreMenuRegisterRequest request) {
        // req -> entity -> save -> response
        // 요청 데이터를 엔티티로 변환
        var entity = storeMenuConverter.toEntity(request);
        // 엔티티를 스토어 메뉴 서비스를 통해 등록하고 새로운 엔티티를 반환
        var newEntity = storeMenuService.register(entity);
        // 새로운 엔티티를 응답 객체로 변환
        var response = storeMenuConverter.toResponse(newEntity);
        // 변환된 응답 객체 반환
        return response;
    }

    // 특정 가게에 속하는 스토어 메뉴 검색 메서드
    public List<StoreMenuResponse> search(Long storeId) {
        // 가게 ID를 기반으로 해당 가게에 속하는 스토어 메뉴 목록을 가져옴
        var list = storeMenuService.getStoreMenuByStoreId(storeId);
        // 스트림을 사용하여 각 엔티티를 응답 객체로 변환하고 리스트로 반환
        return list.stream()
                .map(it -> {
                    return storeMenuConverter.toResponse(it);
                })
                .collect(Collectors.toList());
    }
}
