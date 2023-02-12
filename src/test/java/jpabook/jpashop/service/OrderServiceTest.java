package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {

    @PersistenceContext
    EntityManager em;
    @Autowired OrderService orderService;
    @Autowired
    OrderRepository orderRepository;
    @Test
    public void 상품주문() throws Exception{

        //given
        Member member = createMember();
        Book book = createBook("자서전",10000,10);
        //when
        int orderCount=2;
        Long orderId=orderService.order(member.getId(), book.getId(),orderCount);
        //then
        Order getOrder=orderRepository.findOne(orderId);

        Assert.assertEquals("상품 주문시 상태는 ORDER", OrderStatus.ORDER,getOrder.getStatus());
        Assert.assertEquals("주문한 상품 종류 수가 정확해야한다",1,getOrder.getOrderItems().size());
        Assert.assertEquals("주문 가격은 가격 * 수량이다",10000*orderCount,getOrder.getTotalPrice());
        Assert.assertEquals("주문시 재고가 줄어야한다",8,book.getStockQuantity());
    }

    private Member createMember() {
        Member member=new Member();
        member.setName("송하윤");
        member.setAddress(new Address("서울","강가","12"));
        em.persist(member);
        return member;
    }

    private Book createBook(String name,int price,int stockQuantitiy) {
        Book book= new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantitiy);
        em.persist(book);
        return book;
    }


    @Test
    public void 주문취소() throws Exception{
        Member member=createMember();
        Item item=createBook("진양철자서전",10000,10);

        int orderCount=2;

        Long orderId= orderService.order(member.getId(), item.getId(), orderCount);

        orderService.cancelOrder(orderId);

        Order getOrder=orderRepository.findOne(orderId);
        assertEquals("주문 취소시 상태는 cancel",OrderStatus.CANCEL,getOrder.getStatus());
        assertEquals("재고 증가해야함",10,item.getStockQuantity());
    }

    @Test(expected = NotEnoughStockException.class)
    public void 상품주문_재고수량초과() throws Exception{
        Member member=createMember();
        Item item=createBook("진양철자서전",10000,10);

        int orderItem=11;

        orderService.order(member.getId(), item.getId(),orderItem);
    }
}