package jpabook.jpashop.service;

import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private OrderRepository orderRepository;
    private MemberRepository memberRepository;
    private ItemRepository itemRepository;
@Autowired
    public OrderService(OrderRepository orderRepository, MemberRepository memberRepository, ItemRepository itemRepository) {
        this.orderRepository = orderRepository;
    this.memberRepository = memberRepository;
    this.itemRepository = itemRepository;
};


    //주문
    @Transactional
    public Long order(Long memberId,Long itemId,int count){
        Member member=memberRepository.findOne(memberId);
        Item item=itemRepository.findOne(itemId);
        //배송정보 생성
        Delivery delivery=new Delivery();
        delivery.setAddress(member.getAddress());
        //주문 상품 생성
        OrderItem orderItem=OrderItem.createOrderItem(item,item.getPrice(),count);
        // 주문 생성
        Order order=Order.createOrder(member,delivery,orderItem);

        orderRepository.save(order);
        //Cashcade 옵션때문에, order가 persist될때 연결된 delivery와 orderItem 모두 persist됨, private Owner나 라이프사이클이 같을 경우 cashcade를 써주자
        return order.getId();
    }

    //취소
    @Transactional
    public void cancelOrder(Long orderId){
        Order order =orderRepository.findOne(orderId);
        order.cancel();
    }
    //검색

//    public List<Order> findOrders(OrderSearch orderSearch){
//        return orderRepository.findAll(orderSearch);
//    }
}
