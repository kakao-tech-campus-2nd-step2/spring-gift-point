package gift.Service;

import gift.Exception.AuthorizedException;
import gift.Exception.Category.CategoryDuplicatedException;
import gift.Exception.ProductNotFoundException;
import gift.Model.DTO.ProductDTO;
import gift.Model.Entity.CategoryEntity;
import gift.Model.Entity.ProductEntity;
import gift.Model.Entity.MemberEntity;
import gift.Model.Role;
import gift.Repository.CategoryRepository;
import gift.Repository.ProductRepository;
import gift.Repository.MemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, MemberRepository memberRepository, CategoryRepository categoryRepository){
        this.productRepository = productRepository;
        this.memberRepository = memberRepository;
        this.categoryRepository = categoryRepository;
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

        Optional<CategoryEntity> categoryEntityOptional = categoryRepository.findByName(productDTO.categoryName());
        if(categoryEntityOptional.isEmpty()){
            throw new CategoryDuplicatedException("카테고리가 중복됩니다.");
        }

        CategoryEntity categoryEntity = categoryEntityOptional.get();

        productRepository.save(new ProductEntity(categoryEntity, productDTO.name(), productDTO.price(), productDTO.imageUrl()));
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
            CategoryEntity category = p.getCategory();
            dtoList.add(new ProductDTO(p.getId(), category.getName(), p.getName() ,p.getPrice(), p.getImageUrl()));
        }

        return dtoList;
    }

    public void update(String email, Long id, ProductDTO productDTO){
        Optional<MemberEntity> memberOptional = memberRepository.findByEmail(email);
        if(memberOptional.isEmpty()) throw new AuthorizedException("회원 정보가 없습니다.");

        MemberEntity memberEntity = memberOptional.get();
        if(!memberEntity.isAdmin())
            throw new AuthorizedException("관리자가 아닙니다.");

        Optional<CategoryEntity> categoryEntityOptional = categoryRepository.findByName(productDTO.categoryName());
        if(categoryEntityOptional.isEmpty()){
            throw new CategoryDuplicatedException("중복된 카테고리가 이미 있습니다.");
        }

        CategoryEntity categoryEntity = categoryEntityOptional.get();

        ProductEntity productEntity = new ProductEntity(categoryEntity, productDTO.name(), productDTO.price(), productDTO.imageUrl());
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

    public Page<ProductDTO> getPage(String email, int page){
        List<ProductDTO> dtoList = read(email);
        Pageable pageable = PageRequest.of(page, 10);

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

        return new ProductDTO(productEntity.getId(), productEntity.getCategory().getName(), productEntity.getName(),productEntity.getPrice(), productEntity.getImageUrl());
    }

    public List<String> getCategoriesName(){
        List<CategoryEntity> categoryEntityList = categoryRepository.findAll();
        List<String> categoriesName = new ArrayList<>();

        for(CategoryEntity c: categoryEntityList){
            categoriesName.add(c.getName());
        }

        return categoriesName;
    }


}
