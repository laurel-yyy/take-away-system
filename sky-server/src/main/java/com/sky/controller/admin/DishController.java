package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/admin/dish")
@Api(tags = "Dish api")
@Slf4j

public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private RedisTemplate redisTemplate;


    @PostMapping
    @ApiOperation("Add new dish")
    public Result save(@RequestBody DishDTO dishDTO){
        log.info("add new dish {}", dishDTO);
        dishService.saveWithFlavor(dishDTO);

        String key = "dish_" + dishDTO.getCategoryId();
        cleanCache(key);

        return Result.success();
    }


    @GetMapping("/page")
    @ApiOperation("dish page query")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO) {
        log.info("dish page query{}", dishPageQueryDTO);
        PageResult pageResult = dishService.pageQuery(dishPageQueryDTO);

        return Result.success(pageResult);
    }


    @DeleteMapping
    @ApiOperation("Delete dish")
    public Result delete(@RequestParam List<Long> ids){
        log.info("delete dish {}", ids);
        dishService.deleteBatch(ids);

        cleanCache("*dish_*");
        return Result.success();
    }


    @GetMapping("/{id}")
    @ApiOperation("Get Dish by Id")
    public Result<DishVO> getById(@PathVariable Long id){
        log.info("getById {}", id);
        DishVO dishVO = dishService.getByIdWithFlavor(id);
        return Result.success(dishVO);
    }


    @PutMapping
    @ApiOperation("Update dish info")
    public Result update(@RequestBody DishDTO dishDTO){
        log.info("update dish {}", dishDTO);
        dishService.updateWithFlavor(dishDTO);

        cleanCache("*dish_*");

        return Result.success();

    }


    @PostMapping("/status/{status}")
    @ApiOperation("Start/Stop dish")
    public Result startOrStop(@PathVariable Integer status, Long id){
        dishService.startOrStop(status, id);

        cleanCache("*dish_*");

        return Result.success();

    }


    @GetMapping("/list")
    @ApiOperation("Get dish by categoryId")
    public Result<List<Dish>> list(Long categoryId){
        List<Dish> list= dishService.list(categoryId);
        return Result.success(list);
    }

    private void cleanCache(String pattern){
        Set keys = redisTemplate.keys(pattern);
        redisTemplate.delete(keys);
    }
}
