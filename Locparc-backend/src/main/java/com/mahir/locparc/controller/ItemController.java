package com.mahir.locparc.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.mahir.locparc.dao.*;
import com.mahir.locparc.model.*;
import com.mahir.locparc.model.categories.Category;
import com.mahir.locparc.model.categories.SubCategory;
import com.mahir.locparc.model.manufacturers.Manufacturer;
import com.mahir.locparc.model.manufacturers.Model;
import com.mahir.locparc.view.CategoryView;
import com.mahir.locparc.view.ItemView;
import com.mahir.locparc.view.ManufacturerView;
import com.mahir.locparc.view.SubCategoryView;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/items")
@CrossOrigin
public class ItemController {

//    TODO: Configure user authorisations for this controller

    private final ItemDao itemDao;
    private final CategoryDao categoryDao;
    private final ManufacturerDao manufacturerDao;
    private final SubCategoryDao subCategoryDao;
    private final ModelDao modelDao;

    public ItemController(ItemDao itemDao,
                          CategoryDao categoryDao,
                          ManufacturerDao manufacturerDao,
                          SubCategoryDao subCategoryDao,
                          ModelDao modelDao) {
        this.itemDao = itemDao;
        this.categoryDao = categoryDao;
        this.manufacturerDao = manufacturerDao;
        this.subCategoryDao = subCategoryDao;
        this.modelDao = modelDao;
    }

    @GetMapping("/admin/")
    public ResponseEntity<List<Item>> findAll() {

        List<Item> items = itemDao.findAll();
        if (!items.isEmpty()) return new ResponseEntity<>(items, HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // TODO: approve returned item


    @GetMapping("/admin/{id}")
    public ResponseEntity<Item> findById(@PathVariable Long id) {

        return itemDao.findById(id)
                .map(item     -> new ResponseEntity<>(item, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @JsonView({ItemView.class})
    @Transactional
    @PostMapping("/admin/")
    public ResponseEntity<Item> addItem(@RequestBody Item item) {

        if(item == null || item.getSubCategory() == null )
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Optional<SubCategory> optionalSubCategory =
                subCategoryDao.findByName(item.getSubCategory().getName());
        Optional<Model> optionalModel = Optional.empty();
        if (item.getModel() != null) {
            optionalModel = modelDao.findByReference(item.getModel().getReference());
        }
        try {
            if (optionalSubCategory.isPresent()) {
                SubCategory subCategory = optionalSubCategory.get();
                item.setSubCategory(subCategory);

                if (optionalModel.isPresent()) {
                    Model model = optionalModel.get();
                    item.setModel(model);
                }

                return new ResponseEntity<>(itemDao.save(item), HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }


    @JsonView({ItemView.class})
    @Transactional
    @PostMapping("/admin/multiple")
    // TODO: refactor
    public ResponseEntity<List<Item>> addItems(@RequestBody List<Item> items) {

        if (items.isEmpty()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<Item> savedItems = new ArrayList<>();
        try {
            Optional<SubCategory> optionalSubCategory = Optional.empty();
            Optional<Model> optionalModel             = Optional.empty();

            if (items.get(0).getSubCategory() != null)
                optionalSubCategory = subCategoryDao
                        .findByName(items.get(0).getSubCategory().getName());


            if (items.get(0).getModel() != null)
                optionalModel = modelDao
                        .findByReference(items.get(0).getModel().getReference());


            SubCategory subCategory;
            Model model;

            for (Item item : items) {

                if (optionalSubCategory.isPresent()) {

                    subCategory = optionalSubCategory.get();

                    if (optionalModel.isPresent()) {
                        model = optionalModel.get();
                        item.setModel(model);
                    }
                    item.setSubCategory(subCategory);
                    savedItems.add(itemDao.save(item));
                }
            }
            return new ResponseEntity<>(savedItems, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }


    @DeleteMapping("/admin/{itemId}")
    public ResponseEntity<?> deleteItem(@PathVariable Long itemId) {
        Optional<Item> optionalItem = itemDao.findById(itemId);
        if (optionalItem.isPresent()) {
            Item item = optionalItem.get();
            itemDao.delete(item);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @GetMapping("/available")
    public ResponseEntity<List<Item>> findAllAvailableItems() {

        List<Item> items = itemDao.findAllAvailableItems();
        if (!items.isEmpty()) return new ResponseEntity<>(items, HttpStatus.OK);

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }


    @GetMapping("/available/search/{name}")
    @JsonView({ItemView.class})
    public ResponseEntity<List<Item>> findByName(@PathVariable String name) {

        List<Item> items = itemDao.findAvailableItemsByName(name);
        if (!items.isEmpty())
            return new ResponseEntity<>(items, HttpStatus.OK);


        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @GetMapping("/available/sub-category/{subCategories}")
    @JsonView({ItemView.class})
    public ResponseEntity<List<Item>> findByName(@PathVariable List<String> subCategories) {

        List<Item> items = itemDao.findAvailableItemsBySubCategories(subCategories);

        if (!items.isEmpty()) return new ResponseEntity<>(items, HttpStatus.OK);

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @GetMapping("/available/{name}/{subCategories}")
    @JsonView({ItemView.class})
    public ResponseEntity<List<Item>> findByNameAndSubCategories(@PathVariable String name,
                                                                 @PathVariable ArrayList<String> subCategories) {

        List<Item> items = itemDao.findAvailableItemsByNameAndSubCategories(name, subCategories);
        if (!items.isEmpty()) return new ResponseEntity<>(items, HttpStatus.OK);

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }


    @GetMapping("/available/{id}")
    public ResponseEntity<Item> findAvailableItemById(@PathVariable Long id) {

        Optional<Item> optionalItem = itemDao.findAvailableItemById(id);
        if (optionalItem.isPresent()) return new ResponseEntity<>(optionalItem.get(), HttpStatus.OK);

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    // Get manufacturers
    @GetMapping("/categories")
    @JsonView({CategoryView.class})
    public ResponseEntity<List<Category>> getCategories() {

        List<Category> categories = categoryDao.findAll();
        if (!categories.isEmpty()) return new ResponseEntity<>(categories, HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Add category route
    @PostMapping("/admin/categories")
    public ResponseEntity<Category> addCategory(@RequestBody Category category) {

        if (category == null || category.getName().isEmpty())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        if (category.getName().length() < 50) {
            category.setName(category.getName().toUpperCase());
            System.out.println(category.getName());
            categoryDao.save(category);
            return new ResponseEntity<>(category, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    // Delete category route
    @Transactional
    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<Category> deleteCategory(@PathVariable Integer categoryId) {

        Optional<Category> optionalCategory = categoryDao.findById(categoryId);
        if (optionalCategory.isPresent()) {
            try {
                categoryDao.delete(optionalCategory.get());
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Get sub-categories
    @GetMapping("/sub-categories")
    public ResponseEntity<List<SubCategory>> getSubCategories() {

        List<SubCategory> subCategories = subCategoryDao.findAll();
        if (!subCategories.isEmpty())
            return new ResponseEntity<>(subCategories, HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Get sub-category by id
    @GetMapping("/sub-categories/{subCategoryId}")
    public ResponseEntity<SubCategory> getSubCategoryById(@PathVariable Integer subCategoryId) {

        Optional<SubCategory> optionalSubCategory = subCategoryDao.findById(subCategoryId);
        if (optionalSubCategory.isPresent())
            return new ResponseEntity<>(optionalSubCategory.get(), HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Create sub-category route
    @Transactional
    @PostMapping("/admin/sub-categories")

    public ResponseEntity<SubCategory> addSubCategory(@RequestBody SubCategory subCategory) {

        if (subCategory == null ||
                subCategory.getName().isEmpty() ||
                subCategory.getCategory() == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        if (subCategory.getName().length() < 50) {
            try {
                subCategoryDao.save(subCategory);
                return new ResponseEntity<>(subCategory, HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    // Delete a sub-category
    @Transactional
    @DeleteMapping("/admin/sub-categories/{subCategoryId}")
    public ResponseEntity<SubCategory> deleteSubCategory(@PathVariable Integer subCategoryId) {

        Optional<SubCategory> optionalSubCategory = subCategoryDao.findById(subCategoryId);
        if (optionalSubCategory.isPresent()) {
            try {
                subCategoryDao.delete(optionalSubCategory.get());
                return new ResponseEntity<>(HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    // Get manufacturers
    @GetMapping("/manufacturers")
    @JsonView({ManufacturerView.class})
    public ResponseEntity<List<Manufacturer>> getManufacturers() {

        List<Manufacturer> manufacturers = manufacturerDao.findAll();
        if (!manufacturers.isEmpty())
            return new ResponseEntity<>(manufacturers, HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Route for adding a manufacturer
    @Transactional
    @PostMapping("/admin/manufacturers")
    public ResponseEntity<Manufacturer> addManufacturer(@RequestBody Manufacturer manufacturer) {

        if (manufacturer == null ||
                manufacturer.getName().isEmpty())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        if (manufacturer.getName().length() < 50) {
            try {
                manufacturer.setName(manufacturer.getName().toUpperCase());
                manufacturerDao.save(manufacturer);
                return new ResponseEntity<>(manufacturer, HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    // Delete a manufacturer
    @Transactional
    @DeleteMapping("/admin/manufacturers/{manufacturerId}")
    public ResponseEntity<Manufacturer> deleteManufacturer(@PathVariable Integer manufacturerId) {

        Optional<Manufacturer> optionalManufacturer = manufacturerDao.findById(manufacturerId);
        if (optionalManufacturer.isPresent()) {
            try {
                manufacturerDao.delete(optionalManufacturer.get());
                return new ResponseEntity<>(HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Get models
    @GetMapping("/models")
    public ResponseEntity<List<Model>> getModels() {

        List<Model> models = modelDao.findAll();
        if (!models.isEmpty())
            return new ResponseEntity<>(models, HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Route for adding a model
    @Transactional
    @PostMapping("/admin/models")
    public ResponseEntity<Model> addModel(@RequestBody Model model) {

        if (model == null ||
                model.getReference().isEmpty() ||
                model.getManufacturer() == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        if (model.getReference().length() < 50) {
            try {
                modelDao.save(model);
                return new ResponseEntity<>(model, HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    // Delete a model
    @Transactional
    @DeleteMapping("/admin/models/{modelId}")
    public ResponseEntity<Model> deleteModel(@PathVariable Long modelId) {
        Optional<Model> optionalModel = modelDao.findById(modelId);
        if (optionalModel.isPresent()) {
            try {
                modelDao.delete(optionalModel.get());
                return new ResponseEntity<>(HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


}
