package org.delivery.api.domain.userorder.converter;

import org.delivery.api.annotation.Converter;
import org.delivery.api.domain.user.model.User;
import org.delivery.api.domain.userorder.controller.model.UserOrderResponse;
import org.delivery.db.storemenu.StoreMenuEntity;
import org.delivery.db.userorder.UserOrderEntity;

import java.math.BigDecimal;
import java.util.List;

@Converter
public class UserOrderConverter {

    // 사용자와 가게 메뉴 목록으로부터 사용자 주문 엔티티를 생성하는 메서드
    public UserOrderEntity toEntity(User user, Long storeId,List<StoreMenuEntity> storeMenuEntityList) {



        // 가게 메뉴 목록에서 각 메뉴의 가격을 가져와 총 금액 계산
        var totalAmount = storeMenuEntityList.stream()
                .map(it -> it.getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 사용자 주문 엔티티를 생성하고 필드를 설정
        return UserOrderEntity.builder()
                .userId(user.getId())
                .storeId(storeId)
                .amount(totalAmount)
                .build();
    }

    // 사용자 주문 엔티티를 사용자 주문 응답 객체로 변환하는 메서드
    public UserOrderResponse toResponse(UserOrderEntity userOrderEntity) {
        // 사용자 주문 응답 객체를 생성하고 엔티티에서 필드를 가져와 설정
        return UserOrderResponse.builder()
                .id(userOrderEntity.getId())
                .status(userOrderEntity.getStatus())
                .amount(userOrderEntity.getAmount())
                .orderedAt(userOrderEntity.getOrderedAt())
                .acceptedAt(userOrderEntity.getAcceptedAt())
                .cookingStartedAt(userOrderEntity.getCookingStartedAt())
                .deliveryStartedAt(userOrderEntity.getDeliveryStartedAt())
                .receivedAt(userOrderEntity.getReceivedAt())
                .build();
    }
}

