package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) // 이런식으로 하면, JPA가 읽기용 모드로 처리함으로써 속도가 빨라지게 된다
// JPA에서 lazy로딩같이, 데이터 변경은 기본적으로 트랜잭션 안에서 이뤄져야한다.

public class MemberService {
    private MemberRepository memberRepository;
    @Autowired // 애 없어도된다! , 생성자가 하나가 있으면 Spring이 자동으로 인식해준다
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    //회원 가입
    @Transactional //이러면 ,readonly는 기본적으로 false
    public Long join(Member member){
        validateDuplicateMember(member); //중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        //Exception
        List<Member> findMembers=memberRepository.findByName(member.getName());
        if(!findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다");
        }
    }
    //회원 전체 조회

    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId){
        return memberRepository.findOne(memberId);
    }
}
