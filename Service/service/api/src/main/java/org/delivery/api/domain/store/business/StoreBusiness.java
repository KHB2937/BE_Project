package org.delivery.api.domain.store.business;

import lombok.RequiredArgsConstructor;
import org.delivery.api.annotation.Business;
import org.delivery.api.domain.store.controller.model.StoreRegisterRequest;
import org.delivery.api.domain.store.controller.model.StoreResponse;
import org.delivery.api.domain.store.converter.StoreConverter;
import org.delivery.api.domain.store.service.StoreService;
import org.delivery.db.store.enums.StoreCategory;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Business
public class StoreBusiness {

    private final StoreService storeService;   // StoreService 의존성 주입
    private final StoreConverter storeConverter; // StoreConverter 의존성 주입

    // 가게 등록 메서드
    public StoreResponse register(StoreRegisterRequest storeRegisterRequest) {
        // 요청 데이터를 엔티티로 변환
        var entity = storeConverter.toEntity(storeRegisterRequest);
        // 엔티티를 가게 서비스를 통해 등록하고 새로운 엔티티를 반환
        var newEntity = storeService.register(entity);
        // 새로운 엔티티를 응답 객체로 변환
        var response = storeConverter.toResponse(newEntity);
        // 변환된 응답 객체 반환
        return response;
    }

    // 특정 카테고리로 가게 검색 메서드
    public List<StoreResponse> searchCategory(StoreCategory storeCategory) {
        // 가게 서비스를 통해 특정 카테고리로 가게를 검색하고 결과를 리스트로 받음
        var storeList = storeService.searchByCategory(storeCategory);
        // 검색된 엔티티 리스트를 스트림으로 처리하고, 각 엔티티를 응답 객체로 변환하여 리스트로 반환
        return storeList.stream()
                .map(storeConverter::toResponse)
                .collect(Collectors.toList());
    }
}

