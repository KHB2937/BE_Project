package org.delivery.api.domain.userorder.service;

import lombok.RequiredArgsConstructor;
import org.delivery.api.common.error.ErrorCode;
import org.delivery.api.common.exception.ApiException;
import org.delivery.db.userorder.UserOrderEntity;
import org.delivery.db.userorder.UserOrderRepository;
import org.delivery.db.userorder.enums.UserOrderStatus;
import org.springframework.stereotype.Service;

import javax.swing.plaf.PanelUI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserOrderService {

    private final UserOrderRepository userOrderRepository; // UserOrderRepository 의존성 주입

    // 특정 ID 및 사용자 ID에 해당하는 사용자 주문 엔티티 가져오기 (상태 무시)
    public UserOrderEntity getUserOrderWithOutStatusWithThrow(Long id, Long userId) {
        // 주어진 ID와 사용자 ID를 기반으로 사용자 주문 엔티티를 검색 (상태 무시)
        return userOrderRepository.findAllByIdAndUserId(id, userId)
                .orElseThrow(() -> new ApiException(ErrorCode.NULL_POINT));
    }

    // 특정 ID 및 사용자 ID에 해당하는 유효한 사용자 주문 엔티티 가져오기
    public UserOrderEntity getUserOrderWithThrow(Long id, Long userId) {
        // 주어진 ID, 사용자 ID 및 사용자 주문 상태(REGISTERED)를 기반으로 사용자 주문 엔티티를 검색
        return userOrderRepository.findAllByIdAndStatusAndUserId(id, UserOrderStatus.REGISTERED, userId)
                .orElseThrow(() -> new ApiException(ErrorCode.NULL_POINT));
    }

    // 특정 사용자 ID에 해당하는 모든 유효한 사용자 주문 엔티티 목록 가져오기
    public List<UserOrderEntity> getUserOrderList(Long userId) {
        // 주어진 사용자 ID와 사용자 주문 상태(REGISTERED)를 기반으로 사용자 주문 엔티티 목록을 검색
        return userOrderRepository.findAllByUserIdAndStatusOrderByIdDesc(userId, UserOrderStatus.REGISTERED);
    }

    // 특정 사용자 ID에 해당하는 모든 사용자 주문 엔티티 목록 중 상태 리스트에 속하는 주문 가져오기
    public List<UserOrderEntity> getUserOrderList(Long userId, List<UserOrderStatus> statusList) {
        // 주어진 사용자 ID와 주문 상태 리스트를 기반으로 사용자 주문 엔티티 목록을 검색
        return userOrderRepository.findAllByUserIdAndStatusInOrderByIdDesc(userId, statusList);
    }

    // 현재 진행 중인 주문 엔티티 목록 가져오기
    public List<UserOrderEntity> current(Long userId) {
        return getUserOrderList(
                userId,
                List.of(
                        UserOrderStatus.ORDER,
                        UserOrderStatus.COOKING,
                        UserOrderStatus.DELIVERY,
                        UserOrderStatus.ACCEPT
                )
        );
    }

    // 과거에 주문한 주문 엔티티 목록 가져오기
    public List<UserOrderEntity> history(Long userId) {
        return getUserOrderList(
                userId,
                List.of(
                        UserOrderStatus.RECEIVE
                )
        );
    }

    // 주문 생성 (주문 엔티티를 생성하고 상태를 ORDER로 설정)
    public UserOrderEntity order(UserOrderEntity userOrderEntity) {
        return Optional.ofNullable(userOrderEntity)
                .map(it -> {
                    it.setStatus(UserOrderStatus.ORDER);
                    it.setOrderedAt(LocalDateTime.now());
                    return userOrderRepository.save(it);
                })
                .orElseThrow(() -> new ApiException(ErrorCode.NULL_POINT));
    }

    // 주문 상태 변경
    public UserOrderEntity setStatus(UserOrderEntity userOrderEntity, UserOrderStatus status) {
        userOrderEntity.setStatus(status);
        return userOrderRepository.save(userOrderEntity);
    }

    // 주문 확인
    public UserOrderEntity accept(UserOrderEntity userOrderEntity) {
        userOrderEntity.setAcceptedAt(LocalDateTime.now());
        return setStatus(userOrderEntity, UserOrderStatus.ACCEPT);
    }

    // 조리 시작
    public UserOrderEntity cooking(UserOrderEntity userOrderEntity) {
        userOrderEntity.setCookingStartedAt(LocalDateTime.now());
        return setStatus(userOrderEntity, UserOrderStatus.COOKING);
    }

    // 배달 시작
    public UserOrderEntity delivery(UserOrderEntity userOrderEntity) {
        userOrderEntity.setDeliveryStartedAt(LocalDateTime.now());
        return setStatus(userOrderEntity, UserOrderStatus.DELIVERY);
    }

    // 배달 완료
    public UserOrderEntity receive(UserOrderEntity userOrderEntity) {
        userOrderEntity.setReceivedAt(LocalDateTime.now());
        return setStatus(userOrderEntity, UserOrderStatus.RECEIVE);
    }
}
