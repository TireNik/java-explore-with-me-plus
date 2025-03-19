package ru.practicum.category.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.CategoryDtoNew;
import ru.practicum.category.service.CategoryService;

@Validated
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/categories")
public class CategoryControllerAdmin {
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryDto> create(@RequestBody @Valid CategoryDtoNew newCategoryDto) {
        log.info("Эндпоинт /admin/categories. POST запрос  на создание админом ноговой категории {}.",
                newCategoryDto);
        return new ResponseEntity<>(categoryService.createCategoryAdmin(newCategoryDto), HttpStatus.CREATED);
    }

    @PatchMapping("/{catId}")
    public ResponseEntity<CategoryDto> update(@RequestBody @Valid CategoryDtoNew newCategoryDto,
                                              @PathVariable Long catId) {
        log.info("Эндпоинт /admin/categories/{}. PATCH запрос по  на обновление админом категории с id {}.", catId,
                catId);
        return new ResponseEntity<>(categoryService.updateCategoryAdmin(newCategoryDto, catId), HttpStatus.OK);
    }

    @DeleteMapping("/{catId}")
    public ResponseEntity<?> delete(@PathVariable Long catId) {
        categoryService.deleteCategoryAdmin(catId);
        log.info("Эндпоинт /admin/categories/{}. DELETE запрос на удаление админом категории с id {}.", catId,
                catId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}