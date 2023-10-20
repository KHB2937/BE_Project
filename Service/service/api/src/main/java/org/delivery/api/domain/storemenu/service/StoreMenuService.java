package org.delivery.api.domain.storemenu.service;

import lombok.RequiredArgsConstructor;
import org.delivery.api.common.error.ErrorCode;
import org.delivery.api.common.exception.ApiException;
import org.delivery.db.storemenu.StoreMenuEntity;
import org.delivery.db.storemenu.enums.StoreMenuRepository;
import org.delivery.db.storemenu.enums.StoreMenuStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class StoreMenuService {

    private final StoreMenuRepository storeMenuRepository; // StoreMenuRepository 의존성 주입

    // 특정 ID의 유효한 스토어 메뉴 가져오기
    public StoreMenuEntity getStoreMenuWithThrow(Long id) {
        // 주어진 ID와 스토어 메뉴 상태(REGISTERED)를 기반으로 스토어 메뉴 엔티티를 검색
        var entity = storeMenuRepository.findFirstByIdAndStatusOrderByIdDesc(id, StoreMenuStatus.REGISTERED);
        // 검색 결과가 없을 경우 예외 (ApiException)를 발생시킴
        return entity.orElseThrow(() -> new ApiException(ErrorCode.NULL_POINT));
    }

    // 특정 가게 ID에 속하는 모든 유효한 스토어 메뉴 가져오기
    public List<StoreMenuEntity> getStoreMenuByStoreId(Long storeId) {
        // 주어진 가게 ID와 스토어 메뉴 상태(REGISTERED)를 기반으로 스토어 메뉴 엔티티 목록을 검색
        return storeMenuRepository.findAllByStoreIdAndStatusOrderBySequenceDesc(storeId, StoreMenuStatus.REGISTERED);
    }

    // 스토어 메뉴 등록
    public StoreMenuEntity register(StoreMenuEntity storeMenuEntity) {
        return Optional.ofNullable(storeMenuEntity)
                .map(it -> {
                    // 스토어 메뉴 엔티티의 상태를 REGISTERED로 설정
                    it.setStatus(StoreMenuStatus.REGISTERED);
                    // 스토어 메뉴 엔티티를 저장하고 반환
                    return storeMenuRepository.save(it);
                })
                // 스토어 메뉴 엔티티가 null일 경우 ApiException을 발생시킴
                .orElseThrow(() -> new ApiException(ErrorCode.NULL_POINT));
    }
}