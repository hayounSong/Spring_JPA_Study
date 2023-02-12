package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="orders")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {
    @Id @GeneratedValue
    @Column(name="order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @OneToMany(mappedBy ="order",cascade = CascadeType.ALL)
    //Order를 저장하면, orderItems도 같이 저장하게 되는 기능
    private List<OrderItem> orderItems=new ArrayList<OrderItem>();

    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    //order를 persist해주면, delivery도 persist 해준다.
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // 주문상태 ORDER,CANCEL

    //연간관계 편의 메서드(양방향 관계 일때 해당한다)

    public void setMember(Member member){
        this.member=member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem){
        this.orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery){
        this.delivery=delivery;
        delivery.setOrder(this);
    }

    // 이렇게 연관관계 메서드는 실질적으로 컨트롤하는쪽에서 다뤄주는게 좋다

    //생성 메서드, 연간관계를 정리해주는 메서드
    public static Order createOrder(Member member,Delivery delivery,OrderItem... orderItems){
        Order order=new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for(OrderItem orderItem:orderItems){
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    //비즈니스 로직 (주문 취소)

    public void cancel(){
        if(delivery.getStatus()==DeliveryStatus.COMP){
            throw new IllegalStateException("이미 배송 완료된 상품은 취소가 불가능합니다.");
        }
        this.setStatus(OrderStatus.CANCEL);

        for(OrderItem orderItem:orderItems){
            orderItem.cancel();
        }
    }

    // 전체 주문 가격조회 로직

    public int getTotalPrice(){
        int totalPrice=0;
        for(OrderItem orderItem:orderItems){
            totalPrice=totalPrice+orderItem.getTotalPrice();
        }
        return totalPrice;
    }
}

