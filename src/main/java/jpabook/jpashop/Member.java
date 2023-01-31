package jpabook.jpashop;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "Member") @Getter @Setter
public class Member {
    @Id @GeneratedValue
    private int id;
    private String username;

}
