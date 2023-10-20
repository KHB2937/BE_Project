package org.delivery.api.domain.userorder.controller.model;

import lombok.*;
import org.delivery.api.domain.store.controller.model.StoreResponse;
import org.delivery.api.domain.storemenu.controller.model.StoreMenuResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserOrderDetailResponse {

    private UserOrderResponse userOrderResponse;
    private StoreResponse storeResponse;
    private List<StoreMenuResponse> storeMenuResponseList;
}
