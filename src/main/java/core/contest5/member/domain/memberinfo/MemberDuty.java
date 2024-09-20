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
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class MemberDuty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String dutyName;

    @JsonIgnore
    @OneToOne(mappedBy = "memberDuty", cascade = CascadeType.ALL, orphanRemoval = true)
    private Member member;

    public MemberDuty(String dutyName) {
        this.dutyName = dutyName;
    }
}
