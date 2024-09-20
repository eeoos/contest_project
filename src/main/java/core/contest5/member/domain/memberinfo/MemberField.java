package core.contest5.member.domain.memberinfo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import core.contest5.member.domain.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberField {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fieldName; // 관심 분야의 이름

    @JsonIgnore
    @OneToOne(mappedBy = "memberField", cascade = CascadeType.ALL, orphanRemoval = true)
    private Member member;

    public MemberField(String fieldName) {
        this.fieldName = fieldName;
    }
}
