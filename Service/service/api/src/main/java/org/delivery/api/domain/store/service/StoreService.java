package org.delivery.api.domain.store.service;

import lombok.RequiredArgsConstructor;
import org.delivery.api.common.error.ErrorCode;
import org.delivery.api.common.exception.ApiException;
import org.delivery.db.store.StoreEntity;
import org.delivery.db.store.StoreRepository;
import org.delivery.db.store.enums.StoreCategory;
import org.delivery.db.store.enums.StoreStatus;
import org.springframework.stereotype.Service;

import java.lang.ref.PhantomReference;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class StoreService {

    private final StoreRepository storeRepository; // StoreRepository 의존성 주입

    // 유효한 스토어 가져오기
    public StoreEntity getStoreWithThrow(Long id) {
        // 주어진 ID와 스토어 상태(REGISTERED)를 기반으로 스토어 엔티티를 검색
        var entity = storeRepository.findFirstByIdAndStatusOrderByIdDesc(id, StoreStatus.REGISTERED);
        // 검색 결과가 없을 경우 예외 (ApiException)를 발생시킴
        return entity.orElseThrow(() -> new ApiException(ErrorCode.NULL_POINT));
    }

    // 스토어 등록
    public StoreEntity register(StoreEntity storeEntity) {
        return Optional.ofNullable(storeEntity)
                .map(it -> {
                    // 스토어 엔티티의 필드 설정 (별점, 상태 등)
                    it.setStar(0);
                    it.setStatus(StoreStatus.REGISTERED);
                    // TODO: 등록일시 추가하기 (현재는 주석 처리된 상태)

                    // 스토어 엔티티를 저장하고 반환
                    return storeRepository.save(it);
                })
                // 스토어 엔티티가 null일 경우 예외 (ApiException)를 발생시킴
                .orElseThrow(() -> new ApiException(ErrorCode.NULL_POINT));
    }

    // 카테고리로 스토어 검색
    public List<StoreEntity> searchByCategory(StoreCategory storeCategory) {
        // 주어진 카테고리와 스토어 상태(REGISTERED)를 기반으로 스토어 엔티티 목록을 검색
        var list = storeRepository.findAllByStatusAndCategoryOrderByStarDesc(
                StoreStatus.REGISTERED,
                storeCategory
        );
        // 검색된 엔티티 목록 반환
        return list;
    }

    // 전체 스토어 검색
    public List<StoreEntity> registerStore() {
        // 스토어 상태(REGISTERED)를 기반으로 모든 스토어 엔티티 목록을 검색
        var list = storeRepository.findAllByStatusOrderByIdDesc(StoreStatus.REGISTERED);
        // 검색된 엔티티 목록 반환
        return list;
    }
}
