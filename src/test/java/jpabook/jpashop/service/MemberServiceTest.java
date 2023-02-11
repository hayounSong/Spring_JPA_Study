package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
// 스프링하고 같이 실행되는거에 대한 설정
@SpringBootTest
@Transactional
// Test이기에 기본적으로 롤백이된다.
public class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Test
//    @Rollback(value = false)
    public void 회원가입() throws Exception{

        //given
        Member member= new Member();
        member.setName("kim");
        //when
        Long saveId=memberService.join(member);

        //then
//        em.flush() 이게 있으면 rollback 없이도 DB에 반영가능
        Assert.assertEquals(member,memberRepository.findOne(saveId));
    }


    @Test(expected = IllegalStateException.class)
    //이렇게 나와야할 에러를 expected로 지정해줌으로써, 손쉽게 TestCode 작성 가능했다!
    public void 중복검사() throws  Exception{
        //given


        Member member1=new Member();
        member1.setName("kim");

        Member member2=new Member();
        member2.setName("kim");
        //when

        memberService.join(member1);
        memberService.join(member2);


        //then

        Assert.fail("예외가 발생해야한다");
    }
}