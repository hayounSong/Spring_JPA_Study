package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jpabook.jpashop.domain.item.Item;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ItemRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(Item item){
        if(item.getId()==null){
            em.persist(item);
        }
        //아이템은 처음에 id가 없기에, jpa의 persist
        else{
            em.merge(item);
        }
        //이건 업데이트의 경우

    }

    public Item findOne(Long id){
        return em.find(Item.class,id);
    }

    public List<Item> findAll(){
        return em.createQuery("select i from Item i",Item.class).getResultList();
    }

}
