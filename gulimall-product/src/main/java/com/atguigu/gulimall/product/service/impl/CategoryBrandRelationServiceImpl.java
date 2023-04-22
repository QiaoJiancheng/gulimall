package com.atguigu.gulimall.product.service.impl;

import com.atguigu.gulimall.product.entity.BrandEntity;
import com.atguigu.gulimall.product.entity.CategoryEntity;
import com.atguigu.gulimall.product.service.BrandService;
import com.atguigu.gulimall.product.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.common.utils.Query;

import com.atguigu.gulimall.product.dao.CategoryBrandRelationDao;
import com.atguigu.gulimall.product.entity.CategoryBrandRelationEntity;
import com.atguigu.gulimall.product.service.CategoryBrandRelationService;


@Service("categoryBrandRelationService")
public class CategoryBrandRelationServiceImpl extends ServiceImpl<CategoryBrandRelationDao, CategoryBrandRelationEntity> implements CategoryBrandRelationService {

    @Autowired
    private BrandService brandService;

    @Autowired
    private CategoryService categoryService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryBrandRelationEntity> page = this.page(
                new Query<CategoryBrandRelationEntity>().getPage(params),
                new QueryWrapper<CategoryBrandRelationEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryBrandRelationEntity> list(Long brandId) {
        return this.baseMapper.selectList(
                new QueryWrapper<CategoryBrandRelationEntity>().eq("brand_id", brandId)
        );
    }

    @Override
    public void saveDetail(CategoryBrandRelationEntity categoryBrandRelation) {

        Long brandId = categoryBrandRelation.getBrandId();
        BrandEntity brandEntity = brandService.getById(brandId);
        categoryBrandRelation.setBrandName(brandEntity.getName());

        Long catelogId = categoryBrandRelation.getCatelogId();
        CategoryEntity categoryEntity = categoryService.getById(catelogId);
        categoryBrandRelation.setCatelogName(categoryEntity.getName());

        this.save(categoryBrandRelation);
    }

    @Override
    public void updateBrand(Long brandId, String name) {
        CategoryBrandRelationEntity categoryBrandRelation = new CategoryBrandRelationEntity();
        categoryBrandRelation.setBrandId(brandId);
        categoryBrandRelation.setBrandName(name);

        this.update(
                categoryBrandRelation,
                new QueryWrapper<CategoryBrandRelationEntity>().eq("brand_id", categoryBrandRelation.getBrandId())
        );
    }

    @Override
    public void updateCategory(Long catId, String name) {
        CategoryBrandRelationEntity categoryBrandRelation = new CategoryBrandRelationEntity();
        categoryBrandRelation.setCatelogId(catId);
        categoryBrandRelation.setCatelogName(name);

        this.update(
                categoryBrandRelation,
                new QueryWrapper<CategoryBrandRelationEntity>().eq("catelog_id", categoryBrandRelation.getCatelogId())
        );
    }

}