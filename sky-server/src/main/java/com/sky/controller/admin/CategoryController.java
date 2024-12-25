package com.sky.controller.admin;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 分类管理
 */
@RestController
@RequestMapping("/admin/category")
@Api(tags = "Category api")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;


    @PostMapping
    @ApiOperation("Add new category")
    public Result<String> save(@RequestBody CategoryDTO categoryDTO){
        log.info("Add new category:{}", categoryDTO);
        categoryService.save(categoryDTO);
        return Result.success();
    }


    @GetMapping("/page")
    @ApiOperation("Get Gategory by page")
    public Result<PageResult> page(CategoryPageQueryDTO categoryPageQueryDTO){
        log.info("Get Gategory by page:{}", categoryPageQueryDTO);
        PageResult pageResult = categoryService.pageQuery(categoryPageQueryDTO);
        return Result.success(pageResult);
    }


    @DeleteMapping
    @ApiOperation("Delete category")
    public Result<String> deleteById(Long id){
        log.info("Delete category:{}", id);
        categoryService.deleteById(id);
        return Result.success();
    }


    @PutMapping
    @ApiOperation("update Category")
    public Result<String> update(@RequestBody CategoryDTO categoryDTO){
        categoryService.update(categoryDTO);
        return Result.success();
    }


    @PostMapping("/status/{status}")
    @ApiOperation("Start/Stop category")
    public Result<String> startOrStop(@PathVariable("status") Integer status, Long id){
        categoryService.startOrStop(status,id);
        return Result.success();
    }


    @GetMapping("/list")
    @ApiOperation("Search Category")
    public Result<List<Category>> list(Integer type){
        List<Category> list = categoryService.list(type);
        return Result.success(list);
    }
}
