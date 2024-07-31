package gift.Service;

import gift.Exception.Login.AuthorizedException;
import gift.Exception.Category.CategoryDuplicatedException;
import gift.Exception.ProductNotFoundException;
import gift.Model.DTO.ProductDTO;
import gift.Model.Entity.CategoryEntity;
import gift.Model.Entity.ProductEntity;
import gift.Model.Entity.MemberEntity;
import gift.Repository.CategoryRepository;
import gift.Repository.ProductRepository;
import gift.Repository.MemberRepository;
import gift.Repository.WishRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;
    private final WishRepository wishRepository;

    public ProductService(ProductRepository productRepository, MemberRepository memberRepository, CategoryRepository categoryRepository, WishRepository wishRepository){
        this.productRepository = productRepository;
        this.memberRepository = memberRepository;
        this.categoryRepository = categoryRepository;
        this.wishRepository = wishRepository;
    }

    public void create(String email, ProductDTO productDTO){
        Optional<MemberEntity> memberOptional = memberRepository.findByEmail(email);
        if(memberOptional.isEmpty()) {
            throw new AuthorizedException("회원 정보가 없습니다.");
        }

        MemberEntity memberEntity = memberOptional.get();
        if(!memberEntity.isAdmin()) {
            throw new AuthorizedException("관리자가 아닙니다.");
        }

        Optional<CategoryEntity> categoryEntityOptional = categoryRepository.findById(productDTO.categoryId());
        if(categoryEntityOptional.isEmpty()){
            throw new CategoryDuplicatedException("카테고리가 중복됩니다.");
        }

        CategoryEntity categoryEntity = categoryEntityOptional.get();

        productRepository.save(new ProductEntity(productDTO.name(), productDTO.price(), productDTO.imageUrl(), categoryEntity));
    }

    public List<ProductDTO> read(String email){
        Optional<MemberEntity> memberOptional = memberRepository.findByEmail(email);
        if(memberOptional.isEmpty()) throw new AuthorizedException("회원 정보가 없습니다.");

        MemberEntity memberEntity = memberOptional.get();
        if(!memberEntity.isAdmin() && !memberEntity.isConsumer())
            throw new AuthorizedException("접근 권한이 없습니다.");
        List<ProductEntity> entityList = productRepository.findAll();
        List<ProductDTO> dtoList = new ArrayList<>();

        for(ProductEntity p: entityList){
            if(wishRepository.findByMemberIdAndProductId(memberEntity.getId(), p.getId()).isPresent()){
                dtoList.add(p.mapToDTO(true));
                continue;
            }
            dtoList.add(p.mapToDTO(false));
        }

        return dtoList;
    }

    public void update(String email, Long id, ProductDTO productDTO){
        Optional<MemberEntity> memberOptional = memberRepository.findByEmail(email);
        if(memberOptional.isEmpty()) throw new AuthorizedException("회원 정보가 없습니다.");

        MemberEntity memberEntity = memberOptional.get();
        if(!memberEntity.isAdmin())
            throw new AuthorizedException("관리자가 아닙니다.");

        Optional<CategoryEntity> categoryEntityOptional = categoryRepository.findById(productDTO.categoryId());
        if(categoryEntityOptional.isEmpty()){
            throw new CategoryDuplicatedException("중복된 카테고리가 이미 있습니다.");
        }

        CategoryEntity categoryEntity = categoryEntityOptional.get();

        ProductEntity productEntity = new ProductEntity(productDTO.name(), productDTO.price(), productDTO.imageUrl(), categoryEntity);
        productEntity.setId(id);

        productRepository.save(productEntity);
    }

    public void delete(String email, Long id){
        Optional<MemberEntity> memberOptional = memberRepository.findByEmail(email);
        if(memberOptional.isEmpty()) throw new AuthorizedException("회원정보가 없습니다.");

        MemberEntity memberEntity = memberOptional.get();
        if(!memberEntity.isAdmin())
            throw new AuthorizedException("관리자가 아닙니다.");

        productRepository.deleteById(id);
    }

    public Page<ProductDTO> getPage(String email, int page, int size, String sort){
        List<ProductDTO> dtoList = read(email);
        Sort sortType = Sort.by(Sort.Direction.DESC, sort);
        Pageable pageable = PageRequest.of(page, size, sortType);

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), dtoList.size());

        List<ProductDTO> subList = dtoList.subList(start, end);

        return new PageImpl<>(subList, pageable, dtoList.size());
    }

    public ProductDTO getById(String email, Long id){
        Optional<MemberEntity> memberOptional = memberRepository.findByEmail(email);
        if(memberOptional.isEmpty()) throw new AuthorizedException("회원정보가 없습니다.");

        MemberEntity memberEntity = memberOptional.get();
        if(!memberEntity.isAdmin() && !memberEntity.isConsumer())
            throw new AuthorizedException("접근 권한이 없습니다.");

        Optional<ProductEntity> productEntityOptional = productRepository.findById(id);

        if(productEntityOptional.isEmpty()){
            throw new ProductNotFoundException("제품을 찾을 수 없습니다.");
        }
        ProductEntity productEntity = productEntityOptional.get();

        if(wishRepository.findByMemberIdAndProductId(memberEntity.getId(), productEntity.getId()).isPresent()){
            return productEntity.mapToDTO(true);
        }
        return productEntity.mapToDTO(false);
    }

    public List<Long> getCategoriesId(){
        List<CategoryEntity> categoryEntityList = categoryRepository.findAll();
        List<Long> categoriesId = new ArrayList<>();

        for(CategoryEntity c: categoryEntityList){
            categoriesId.add(c.getId());
        }

        return categoriesId;
    }


}