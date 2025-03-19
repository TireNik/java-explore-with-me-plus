package ru.practicum.category.mapper;

import org.mapstruct.Mapper;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.CategoryDtoNew;
import ru.practicum.category.model.Category;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category toCategory(CategoryDtoNew categoryDtoNew);

    CategoryDto toCategoryDto(Category category);

    List<CategoryDto> toCategoryDto(List<Category> categories);
}