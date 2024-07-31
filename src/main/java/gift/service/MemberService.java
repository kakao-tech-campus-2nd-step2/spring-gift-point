package gift.service;

import gift.common.enums.Role;
import gift.common.exception.DuplicateDataException;
import gift.common.exception.EntityNotFoundException;
import gift.controller.dto.request.MemberRequest;
import gift.controller.dto.response.MemberResponse;
import gift.controller.dto.response.PagingResponse;
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
    public void signUp(String email, String password, Role role) {
        if (memberRepository.existsByEmail(email)) {
            throw new DuplicateDataException("Email already exists", "Duplicate Email");
        }
        Member member = new Member(email, password, role);
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
                        new EntityNotFoundException("Member with id " + id + " not found"));
        return MemberResponse.from(member);
    }

    @Transactional
    public void updateById(Long id, MemberRequest request) {
        Member member = memberRepository.findById(id)
                .orElseThrow(()->
                        new EntityNotFoundException("Member with id " + id + " not found"));
        member.updateMember(request.email(), request.password(), request.role());
    }

    @Transactional
    public void deleteById(Long id) {
        memberRepository.deleteById(id);
    }
}
