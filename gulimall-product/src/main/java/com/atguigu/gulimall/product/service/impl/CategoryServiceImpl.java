package com.atguigu.gulimall.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.atguigu.gulimall.product.dao.CategoryBrandRelationDao;
import com.atguigu.gulimall.product.vo.Catelog2Vo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.common.utils.Query;

import com.atguigu.gulimall.product.dao.CategoryDao;
import com.atguigu.gulimall.product.entity.CategoryEntity;
import com.atguigu.gulimall.product.service.CategoryService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

//    @Autowired
//    private CategoryBrandRelationService categoryBrandRelationService;

    @Autowired
    private CategoryBrandRelationDao categoryBrandRelationDao;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

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
        // 从缓存中查询
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        String catelogJSON = ops.get("catelogJSON");
        if (StringUtils.isEmpty(catelogJSON)) {
            // 缓存中数据错误，需要重新查询数据库获取数据
            Map<String, List<Catelog2Vo>> catelogJsonFromDb = getCatelogJsonFromDb();
            // 将查询到的数据进行返回
            return catelogJsonFromDb;
        }
        // 将从缓存中查询到的数据进行转换并返回
        return JSON.parseObject(catelogJSON, new TypeReference<Map<String, List<Catelog2Vo>>>() {
        });
    }

    public Map<String, List<Catelog2Vo>> getCatelogJsonFromDb() {

        synchronized (this) {

            // 从缓存中查询
            ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
            String catelogJSON = ops.get("catelogJSON");
            if (!StringUtils.isEmpty(catelogJSON)) {
                // 将从缓存中查询到的数据进行转换并返回
                return JSON.parseObject(catelogJSON, new TypeReference<Map<String, List<Catelog2Vo>>>() {
                });
            }

            // 将多次DB查询变为一次
            List<CategoryEntity> categoryEntities = this.baseMapper.selectList(null);

            // 查询一级分类
            List<CategoryEntity> level1Categorys = getParent_cid(categoryEntities, 0L);
            Map<String, List<Catelog2Vo>> catelogJson = level1Categorys.stream().collect(Collectors.toMap(k -> k.getCatId().toString(), v -> {
                List<Catelog2Vo> catelog2Vos = null;
                // 获取一级菜单分类下的二级分类菜单
                List<CategoryEntity> categoryEntities2 = getParent_cid(categoryEntities, v.getCatId());
                if (categoryEntities2 != null) {
                    catelog2Vos = categoryEntities2.stream().map(l2 -> {
                        Catelog2Vo catelog2Vo = new Catelog2Vo(l2.getParentCid().toString(), null, l2.getCatId().toString(), l2.getName());
                        // 获取当前二级菜单下的三级菜单并封装成vo
                        List<CategoryEntity> categoryEntities3 = getParent_cid(categoryEntities, l2.getCatId());
                        if (categoryEntities3 != null) {
                            List<Catelog2Vo.Catelog3Vo> collect = categoryEntities3.stream().map(categoryEntity3 -> {
                                Catelog2Vo.Catelog3Vo catelog3Vo = new Catelog2Vo.Catelog3Vo(l2.getCatId().toString(), categoryEntity3.getCatId().toString(), categoryEntity3.getName());
                                return catelog3Vo;
                            }).collect(Collectors.toList());
                            catelog2Vo.setCatalog3List(collect);
                        }
                        return catelog2Vo;
                    }).collect(Collectors.toList());
                }
                return catelog2Vos;
            }));
            // 将获取的数据存入缓存中
            ops.set("catelogJSON", JSON.toJSONString(catelogJson), 1, TimeUnit.DAYS);
            return catelogJson;
        }
    }

    private List<CategoryEntity> getParent_cid(List<CategoryEntity> categoryEntities, Long parentCid) {
        return categoryEntities.stream().filter(categoryEntity -> categoryEntity.getParentCid() == parentCid).collect(Collectors.toList());
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