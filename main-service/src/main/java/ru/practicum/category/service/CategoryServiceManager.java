package ru.practicum.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.CategoryDtoNew;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.error.exception.ForbiddenOperationException;
import ru.practicum.error.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceManager implements CategoryService {
    private final CategoryMapper categoryMapper;
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;//репозиторий событий

    @Override
    @Transactional
    public CategoryDto createCategoryAdmin(CategoryDtoNew categoryDtoNew) {
        log.info("Создание новой категории админом {}.", categoryDtoNew);
        return categoryMapper.toCategoryDto(categoryRepository.save(categoryMapper.toCategory(categoryDtoNew)));
    }

    @Override
    @Transactional
    public CategoryDto updateCategoryAdmin(CategoryDtoNew categoryDtoNew, Long catId) {
        Category category = categoryMapper.toCategory(categoryDtoNew);
        if (!categoryRepository.existsById(catId)) {
            throw new ResourceNotFoundException(Category.class, catId);
        }
        category.setId(catId);
        log.info("Обновление админом категории с id {} на {}.", catId, categoryDtoNew);
        return categoryMapper.toCategoryDto(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public void deleteCategoryAdmin(Long catId) {
        findCategoryByIdOrThrow(catId);
        if (!eventRepository.findEventsByCategoryId(catId).isEmpty()) {//поиск в репозитории событий
            throw new ForbiddenOperationException("Невозможно удалить. В категории содержатся события.");
        }
        categoryRepository.deleteById(catId);
        log.info("Админ удалил категорию с id {}", catId);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDto getByIDCategoryPublic(Long catId) {
        CategoryDto categoryDto = categoryMapper.toCategoryDto(findCategoryByIdOrThrow(catId));
        log.info("Получение публичного доступа категории с id {}", catId);
        return categoryDto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDto> getAllCategoriesPublic(Integer from, Integer size) {
        log.info("Получение публичного списка всех категорий from={}, size={}", from, size);
        return categoryMapper.toCategoryDto(categoryRepository.findAll(PageRequest.of(from / size, size)).toList());
    }

    private Category findCategoryByIdOrThrow(Long catId) {
        Optional<Category> category = categoryRepository.findById(catId);
        if (category.isPresent()) {
            return category.get();
        } else {
            throw new ResourceNotFoundException(Category.class, catId);
        }
    }
}