package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jpabook.jpashop.domain.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderRepository {

    @PersistenceContext
    private EntityManager em;


    public void save(Order order){
    em.persist(order);
    }

    public Order findOne(Long id){
    return em.find(Order.class,id);
    }

    public List<Order> findAllByString(OrderSearch orderSearch){
        return em.createQuery("select o from Order o join o.member m"+
                        " where o.status=:status"+" and m.name like :name",Order.class)
                .setParameter("name",orderSearch.getMemberName())
                .setParameter("status",orderSearch.getOrderStatus())
                .setMaxResults(1000) //최대 1000
                .getResultList();

    }

    //JPA 크리테리아로 동적 쿼리 생성
    public List<Order> findAllByCriteria(OrderSearch orderSearch){

    }
}
