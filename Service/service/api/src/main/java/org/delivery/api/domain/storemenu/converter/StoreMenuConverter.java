package org.delivery.api.domain.storemenu.converter;

import org.delivery.api.annotation.Converter;
import org.delivery.api.common.error.ErrorCode;
import org.delivery.api.common.exception.ApiException;
import org.delivery.api.domain.storemenu.controller.model.StoreMenuRegisterRequest;
import org.delivery.api.domain.storemenu.controller.model.StoreMenuResponse;
import org.delivery.db.storemenu.StoreMenuEntity;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Converter
public class StoreMenuConverter {

    // StoreMenuRegisterRequest를 StoreMenuEntity로 변환하는 메서드
    public StoreMenuEntity toEntity(StoreMenuRegisterRequest request) {
        return Optional.ofNullable(request)
                .map(it -> {
                    // StoreMenuEntity를 생성하고 요청에서 받은 정보로 필드를 설정
                    return StoreMenuEntity.builder()
                            .storeId(request.getStoreId())
                            .name(request.getName())
                            .amount(request.getAmount())
                            .thumbnailUrl(request.getThumbnailUrl())
                            .build();
                })
                // 요청이 null인 경우 ApiException을 발생시킴
                .orElseThrow(() -> new ApiException(ErrorCode.NULL_POINT));
    }

    // StoreMenuEntity를 StoreMenuResponse로 변환하는 메서드
    public StoreMenuResponse toResponse(StoreMenuEntity storeMenuEntity) {
        return Optional.ofNullable(storeMenuEntity)
                .map(it -> {
                    // StoreMenuResponse를 생성하고 엔티티에서 필드를 가져와 설정
                    return StoreMenuResponse.builder()
                            .id(storeMenuEntity.getId())
                            .name(storeMenuEntity.getName())
                            .storeId(storeMenuEntity.getStoreId())
                            .amount(storeMenuEntity.getAmount())
                            .status(storeMenuEntity.getStatus())
                            .thumbnailUrl(storeMenuEntity.getThumbnailUrl())
                            .likeCount(storeMenuEntity.getLikeCount())
                            .sequence(storeMenuEntity.getSequence())
                            .build();
                })
                // 엔티티가 null인 경우 ApiException을 발생시킴
                .orElseThrow(() -> new ApiException(ErrorCode.NULL_POINT));
    }

    // 스토어 메뉴 엔티티 목록을 스토어 메뉴 응답 객체 목록으로 변환하는 메서드
    public List<StoreMenuResponse> toResponse(List<StoreMenuEntity> list) {
        // 주어진 스토어 메뉴 엔티티 목록을 스트림으로 변환하고, 각 엔티티를 스토어 메뉴 응답 객체로 변환한 후 리스트로 수집
        return list.stream()
                .map(it -> toResponse(it))
                .collect(Collectors.toList());
    }

}
