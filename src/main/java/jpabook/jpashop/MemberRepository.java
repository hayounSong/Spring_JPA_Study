package jpabook.jpashop;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepository {

    @PersistenceContext
    private EntityManager em;

    public int save(Member member){
        em.persist(member);
        return member.getId();
    }

    public Member find(int id){
        return em.find(Member.class,id);
    }
}
