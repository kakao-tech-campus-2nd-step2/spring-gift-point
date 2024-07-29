package gift.service;

import gift.common.util.JwtUtil;
import gift.dto.MemberRequest;
import gift.entity.Member;
import gift.entity.MemberRole;
import gift.entity.Role;
import gift.repository.MemberRepository;
import gift.repository.MemberRoleRepository;
import gift.repository.RoleRepository;
import java.util.Optional;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;
    private final MemberRoleRepository memberRoleRepository;
    private final JwtUtil jwtUtil;

    public MemberService(MemberRepository memberRepository, JwtUtil jwtUtil,
        RoleRepository roleRepository, MemberRoleRepository memberRoleRepository) {
        this.memberRepository = memberRepository;
        this.jwtUtil = jwtUtil;
        this.roleRepository = roleRepository;
        this.memberRoleRepository = memberRoleRepository;
    }

    public String register(MemberRequest memberRequest) {
        Optional<Member> existingMember = memberRepository.findByEmail(memberRequest.getEmail());
        if (existingMember.isPresent()) {
            throw new DuplicateKeyException("이미 존재하는 이메일 : " + memberRequest.getEmail());
        }

        Member member = new Member(memberRequest.getEmail(), memberRequest.getPassword());
        memberRepository.save(member);

        Role userRole = roleRepository.findByName("ROLE_USER");
        memberRoleRepository.save(new MemberRole(member, userRole));

        return jwtUtil.createToken(member.getEmail());
    }

    public String login(String email, String password) {
        return memberRepository.findByEmail(email)
            .filter(member -> member.getPassword().equals(password))
            .map(member -> jwtUtil.createToken(member.getEmail()))
            .orElse(null);
    }

    public Member getMemberFromToken(String token) {
        try {
            String email = jwtUtil.extractEmail(token);
            Optional<Member> member = memberRepository.findByEmail(email);
            return member.orElse(null);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("token이 잘못되었습니다.");
        }
    }

    public boolean hasRole(String token, String role) {
        Member member = getMemberFromToken(token);
        if (member == null) {
            return false;
        }
        return member.getMemberRoles().stream()
            .anyMatch(memberRole -> memberRole.getRole().getName().equals(role));
    }

}
