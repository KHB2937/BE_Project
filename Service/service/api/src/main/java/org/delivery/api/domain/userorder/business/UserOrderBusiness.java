package org.delivery.api.domain.userorder.business;

import lombok.RequiredArgsConstructor;

import org.delivery.api.annotation.Business;
import org.delivery.api.domain.store.converter.StoreConverter;
import org.delivery.api.domain.store.service.StoreService;
import org.delivery.api.domain.storemenu.converter.StoreMenuConverter;
import org.delivery.api.domain.storemenu.service.StoreMenuService;
import org.delivery.api.domain.user.model.User;
import org.delivery.api.domain.userorder.controller.model.UserOrderDetailResponse;
import org.delivery.api.domain.userorder.controller.model.UserOrderRequest;
import org.delivery.api.domain.userorder.controller.model.UserOrderResponse;
import org.delivery.api.domain.userorder.converter.UserOrderConverter;
import org.delivery.api.domain.userorder.producer.UserOrderProducer;
import org.delivery.api.domain.userorder.service.UserOrderService;
import org.delivery.api.domain.userordermenu.converter.UserOrderMenuConverter;
import org.delivery.api.domain.userordermenu.service.UserOrderMenuService;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Business
public class UserOrderBusiness {

    private final UserOrderService userOrderService;

    private final UserOrderConverter userOrderConverter;

    private final StoreMenuService storeMenuService;
    private final StoreMenuConverter storeMenuConverter;

    private final UserOrderMenuConverter userOrderMenuConverter;
    private final UserOrderMenuService userOrderMenuService;

    private final StoreService storeService;
    private final StoreConverter storeConverter;
    private final UserOrderProducer userOrderProducer;


    // 1. 사용자 , 메뉴 id
    // 2. userOrder 생성
    // 3. userOrderMenu 생성
    // 4. 응답 생성
    // 사용자 주문 생성
    public UserOrderResponse userOrder(User user, UserOrderRequest body) {
        // 요청된 메뉴 ID 리스트를 사용하여 해당 메뉴 엔티티들을 검색하고 목록을 생성
        var storeMenuEntityList = body.getStoreMenuIdList()
                .stream()
                .map(it -> storeMenuService.getStoreMenuWithThrow(it))
                .collect(Collectors.toList());

        // 사용자 주문 엔티티를 생성
        var userOrderEntity = userOrderConverter.toEntity(user, body.getStoreId(),storeMenuEntityList);

        // 주문 생성
        var newUserOrderEntity = userOrderService.order(userOrderEntity);

        // 맵핑
        // 각 메뉴와 사용자 주문을 연결하는 사용자 주문 메뉴 엔티티 목록을 생성
        var userOrderMenuEntityList = storeMenuEntityList.stream()
                .map(it ->{
                    var userOrderMenuEntity = userOrderMenuConverter.toEntity(newUserOrderEntity, it);
                    return userOrderMenuEntity;
                })
                .collect(Collectors.toList());

        // 주문내역 기록 남기기
        userOrderMenuEntityList.forEach(it ->{
            userOrderMenuService.order(it);
        });

        // 비동기로 가맹점에 주문 알리기
        userOrderProducer.sendOrder(newUserOrderEntity);


        // 응답 생성
        return userOrderConverter.toResponse(newUserOrderEntity);
    }

    // 현재 진행 중인 사용자 주문의 상세 정보 목록 가져오기
    public List<UserOrderDetailResponse> current(User user) {
        // 사용자의 현재 진행 중인 주문 엔티티 목록을 가져옴
        var userOrderEntityList =  userOrderService.current(user.getId());

        // 주문 1건씩 처리
        var userOrderDetailResponseList = userOrderEntityList.stream().map(it ->{
            // 사용자가 주문한 주문 메뉴 엔티티 목록을 가져오기
            var userOrderMenuEntityList = userOrderMenuService.getUserOrderMenu(it.getId());

            // 각 주문 메뉴 엔티티를 사용하여 가게 메뉴 엔티티 목록 가져오기
            var storeMenuEntityList = userOrderMenuEntityList.stream()
                    .map(userOrderMenuEntity ->{
                        var storeMenuEntity = storeMenuService.getStoreMenuWithThrow(userOrderMenuEntity.getStoreMenuId());
                        return storeMenuEntity;
                    })
                    .collect(Collectors.toList());

            // 사용자가 주문한 가게 정보 가져오기
            var storeEntity = storeService.getStoreWithThrow(storeMenuEntityList.stream().findFirst().get().getStoreId());

            // 사용자 주문 상세 응답 객체 생성
            return UserOrderDetailResponse.builder()
                    .userOrderResponse(userOrderConverter.toResponse(it))
                    .storeMenuResponseList(storeMenuConverter.toResponse(storeMenuEntityList))
                    .storeResponse(storeConverter.toResponse(storeEntity))
                    .build()
                    ;
        }).collect(Collectors.toList());

        return userOrderDetailResponseList;
    }

    // 사용자의 과거 주문 기록을 가져오는 메서드
    public List<UserOrderDetailResponse> history(User user) {
        // 사용자의 과거 주문 엔티티 목록을 가져옴
        var userOrderEntityList = userOrderService.history(user.getId());

        // 주문 1건씩 처리
        var userOrderDetailResponseList = userOrderEntityList.stream().map(it ->{
            // 사용자가 주문한 주문 메뉴 엔티티 목록을 가져오기
            var userOrderMenuEntityList = userOrderMenuService.getUserOrderMenu(it.getId());

            // 각 주문 메뉴 엔티티를 사용하여 가게 메뉴 엔티티 목록 가져오기
            var storeMenuEntityList = userOrderMenuEntityList.stream()
                    .map(userOrderMenuEntity ->{
                        var storeMenuEntity = storeMenuService.getStoreMenuWithThrow(userOrderMenuEntity.getStoreMenuId());
                        return storeMenuEntity;
                    })
                    .collect(Collectors.toList());

            // 사용자가 주문한 가게 정보 가져오기
            var storeEntity = storeService.getStoreWithThrow(storeMenuEntityList.stream().findFirst().get().getStoreId());

            // 사용자 주문 상세 응답 객체 생성
            return UserOrderDetailResponse.builder()
                    .userOrderResponse(userOrderConverter.toResponse(it))
                    .storeMenuResponseList(storeMenuConverter.toResponse(storeMenuEntityList))
                    .storeResponse(storeConverter.toResponse(storeEntity))
                    .build()
                    ;
        }).collect(Collectors.toList());

        return userOrderDetailResponseList;
    }

    // 특정 주문의 상세 정보를 가져오는 메서드
    public UserOrderDetailResponse read(User user, Long orderId) {
        // 특정 주문 ID와 사용자 ID로 주문 엔티티 가져오기
        var userOrderEntity = userOrderService.getUserOrderWithOutStatusWithThrow(orderId, user.getId());

        // 사용자가 주문한 주문 메뉴 엔티티 목록을 가져오기
        var userOrderMenuEntityList = userOrderMenuService.getUserOrderMenu(userOrderEntity.getId());

        // 각 주문 메뉴 엔티티를 사용하여 가게 메뉴 엔티티 목록 가져오기
        var storeMenuEntityList = userOrderMenuEntityList.stream()
                .map(userOrderMenuEntity ->{
                    var storeMenuEntity = storeMenuService.getStoreMenuWithThrow(userOrderMenuEntity.getStoreMenuId());
                    return storeMenuEntity;
                })
                .collect(Collectors.toList());

        // 사용자가 주문한 가게 정보 가져오기
        var storeEntity = storeService.getStoreWithThrow(storeMenuEntityList.stream().findFirst().get().getStoreId());

        // 사용자 주문 상세 응답 객체 생성
        return UserOrderDetailResponse.builder()
                .userOrderResponse(userOrderConverter.toResponse(userOrderEntity))
                .storeMenuResponseList(storeMenuConverter.toResponse(storeMenuEntityList))
                .storeResponse(storeConverter.toResponse(storeEntity))
                .build()
                ;
    }

}