package org.delivery.api.domain.store.converter;

import org.delivery.api.annotation.Converter;
import org.delivery.api.common.api.Api;
import org.delivery.api.common.error.ErrorCode;
import org.delivery.api.common.exception.ApiException;
import org.delivery.api.domain.store.controller.model.StoreRegisterRequest;
import org.delivery.api.domain.store.controller.model.StoreResponse;
import org.delivery.db.store.StoreEntity;

import java.util.Optional;

@Converter
public class StoreConverter {

    // StoreRegisterRequest를 StoreEntity로 변환하는 메서드
    public StoreEntity toEntity(StoreRegisterRequest request) {
        return Optional.ofNullable(request)
                .map(it -> {
                    // StoreEntity를 생성하고 요청에서 받은 정보로 필드를 설정
                    return StoreEntity.builder()
                            .name(request.getName())
                            .address(request.getAddress())
                            .category(request.getStoreCategory())
                            .minimumAmount(request.getMinimumAmount())
                            .minimumDeliveryAmount(request.getMinimumDeliveryAmount())
                            .thumbnailUrl(request.getThumbnailUrl())
                            .phoneNumber(request.getPhoneNumber())
                            .build();
                })
                // 요청이 null인 경우 ApiException을 발생시킴
                .orElseThrow(() -> new ApiException(ErrorCode.NULL_POINT));
    }

    // StoreEntity를 StoreResponse로 변환하는 메서드
    public StoreResponse toResponse(StoreEntity entity) {
        return Optional.ofNullable(entity)
                .map(it -> {
                    // StoreResponse를 생성하고 엔티티에서 필드를 가져와 설정
                    return StoreResponse.builder()
                            .id(entity.getId())
                            .name(entity.getName())
                            .status(entity.getStatus())
                            .category(entity.getCategory())
                            .address(entity.getAddress())
                            .minimumAmount(entity.getMinimumAmount())
                            .minimumDeliveryAmount(entity.getMinimumDeliveryAmount())
                            .thumbnailUrl(entity.getThumbnailUrl())
                            .phoneNumber(entity.getPhoneNumber())
                            .star(entity.getStar())
                            .build();
                })
                // 엔티티가 null인 경우 ApiException을 발생시킴
                .orElseThrow(() -> new ApiException(ErrorCode.NULL_POINT));
    }
}

