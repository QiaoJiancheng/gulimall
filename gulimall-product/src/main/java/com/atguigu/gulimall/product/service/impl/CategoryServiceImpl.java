package com.atguigu.gulimall.product.service.impl;

import com.atguigu.gulimall.product.dao.CategoryBrandRelationDao;
import com.atguigu.gulimall.product.service.CategoryBrandRelationService;
import com.atguigu.gulimall.product.vo.Catelog2Vo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.common.utils.Query;

import com.atguigu.gulimall.product.dao.CategoryDao;
import com.atguigu.gulimall.product.entity.CategoryEntity;
import com.atguigu.gulimall.product.service.CategoryService;
import org.springframework.transaction.annotation.Transactional;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

//    @Autowired
//    private CategoryBrandRelationService categoryBrandRelationService;

    @Autowired
    private CategoryBrandRelationDao categoryBrandRelationDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listTree() {
        // 查询所有的分类
        List<CategoryEntity> categoryEntities = baseMapper.selectList(null);
        // 将分类变为树形结构
        List<CategoryEntity> collect = categoryEntities
                .stream()
                .filter(categoryEntity -> categoryEntity.getParentCid() == 0)
                .map(categoryEntity -> {
                    categoryEntity.setChildren(childrens(categoryEntity, categoryEntities));
                    return categoryEntity;
                })
                .sorted((o1, o2) -> o1.getSort() - o2.getSort())
                .collect(Collectors.toList());
        return collect;


    }

    @Override
    public void removeMenuByIds(List<Long> asList) {
        // TODO 检查当前删除的菜单是否被其他地方引用
        baseMapper.deleteBatchIds(asList);
    }


    /**
     * 找到catelog的完整路径
     *
     * @param catelogId
     * @return
     */
    @Override
    public Long[] findCategoryPath(Long catelogId) {
        List<Long> parentPath = findParentPath(catelogId, new ArrayList<>());
        Collections.reverse(parentPath);
        return parentPath.toArray(new Long[0]);
    }

    @Transactional
    @Override
    public void updateCascade(CategoryEntity category) {
        this.updateById(category);
        if (!StringUtils.isEmpty(category.getName())) {
            categoryBrandRelationDao.updateCategoryCasecade(category.getCatId(), category.getName());
        }
//        categoryBrandRelationService.updateCategory(category.getCatId(), category.getName());
    }

    @Override
    public List<CategoryEntity> getLevel1Categorys() {
        return this.list(new QueryWrapper<CategoryEntity>().eq("parent_cid", 0));
    }

    @Override
    public Map<String, List<Catelog2Vo>> getCatelogJson() {
        // 查询一级分类
        List<CategoryEntity> level1Categorys = this.getLevel1Categorys();
        Map<String, List<Catelog2Vo>> catelogJson = level1Categorys.stream().collect(Collectors.toMap(k -> k.getCatId().toString(), v -> {
            List<Catelog2Vo> catelog2Vos = null;
            // 获取一级菜单分类下的二级分类菜单
            List<CategoryEntity> categoryEntities2 = this.list(new QueryWrapper<CategoryEntity>().eq("parent_cid", v.getCatId()));
            if (categoryEntities2 != null) {
                catelog2Vos = categoryEntities2.stream().map(categoryEntity2 -> {
                    Catelog2Vo catelog2Vo = new Catelog2Vo(categoryEntity2.getParentCid().toString(), null, categoryEntity2.getCatId().toString(), categoryEntity2.getName());
                    // 获取当前二级菜单下的三级菜单并封装成vo
                    List<CategoryEntity> categoryEntities3 = this.list(new QueryWrapper<CategoryEntity>().eq("parent_cid", categoryEntity2.getCatId()));
                    if (categoryEntities3 != null) {
                        List<Catelog2Vo.Catelog3Vo> collect = categoryEntities3.stream().map(categoryEntity3 -> {
                            Catelog2Vo.Catelog3Vo catelog3Vo = new Catelog2Vo.Catelog3Vo(categoryEntity2.getCatId().toString(), categoryEntity3.getCatId().toString(), categoryEntity3.getName());
                            return catelog3Vo;
                        }).collect(Collectors.toList());
                        catelog2Vo.setCatalog3List(collect);
                    }
                    return catelog2Vo;
                }).collect(Collectors.toList());
            }
            return catelog2Vos;
        }));
        return catelogJson;
    }

    public List<Long> findParentPath(Long catelogId, List<Long> paths) {
        paths.add(catelogId);
        CategoryEntity categoryEntity = this.getById(catelogId);
        if (categoryEntity.getParentCid() != 0) {
            findParentPath(categoryEntity.getParentCid(), paths);
        }
        return paths;
    }

    public List<CategoryEntity> childrens(CategoryEntity root, List<CategoryEntity> categoryEntities) {
        List<CategoryEntity> collect = categoryEntities
                .stream()
                .filter(categoryEntity -> categoryEntity.getParentCid() == root.getCatId())
                .map(categoryEntity -> {
                    categoryEntity.setChildren(childrens(categoryEntity, categoryEntities));
                    return categoryEntity;
                })
                .sorted((o1, o2) -> (o1.getSort() == null ? 0 : o1.getSort()) - (o2.getSort() == null ? 0 : o2.getSort()))
                .collect(Collectors.toList());
        return collect;
    }

}