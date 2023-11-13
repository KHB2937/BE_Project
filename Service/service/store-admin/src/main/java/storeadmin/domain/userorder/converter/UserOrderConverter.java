package storeadmin.domain.userorder.converter;

import org.delivery.db.userorder.UserOrderEntity;

import org.springframework.stereotype.Service;
import storeadmin.domain.userorder.controller.model.UserOrderResponse;

@Service
public class UserOrderConverter {

    public UserOrderResponse toResponse(UserOrderEntity userOrderEntity){
        return UserOrderResponse.builder()
            .id(userOrderEntity.getId())
            .userId(userOrderEntity.getUserId())
            .storeId(userOrderEntity.getStoreId())
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
