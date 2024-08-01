package gift.service;

import gift.common.enums.Role;
import gift.common.exception.DuplicateDataException;
import gift.common.exception.EntityNotFoundException;
import gift.controller.dto.request.MemberRequest;
import gift.controller.dto.response.MemberResponse;
import gift.controller.dto.response.PagingResponse;
import gift.controller.dto.response.PointResponse;
import gift.model.Member;
import gift.repository.MemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    public void signUp(String email, String password, String name, Role role) {
        if (memberRepository.existsByEmail(email)) {
            throw new DuplicateDataException("이미 가입된 회원입니다.", "Duplicate Email");
        }
        Member member = new Member(email, password, name, role);
        memberRepository.save(member);
    }

    @Transactional(readOnly = true)
    public PagingResponse<MemberResponse> findAllMemberPaging(Pageable pageable) {
        Page<MemberResponse> pages = memberRepository.findAll(pageable)
                .map(MemberResponse::from);
        return PagingResponse.from(pages);
    }

    @Transactional(readOnly = true)
    public MemberResponse findById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(()->
                        new EntityNotFoundException("Member with productId " + id + " not found"));
        return MemberResponse.from(member);
    }

    @Transactional
    public void updateById(Long id, MemberRequest request) {
        Member member = memberRepository.findById(id)
                .orElseThrow(()->
                        new EntityNotFoundException("Member with productId " + id + " not found"));
        member.updateMember(request.email(), request.password(), request.role());
    }

    @Transactional(readOnly = true)
    public PointResponse findPointById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("존재하지 않는 멤버입니다."));
        return PointResponse.of(member.getPoint());
    }

    @Transactional
    public void deleteById(Long id) {
        memberRepository.deleteById(id);
    }
}
