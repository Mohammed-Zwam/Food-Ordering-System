package com.pattern.food_ordering_system.model.restaurant;

import com.pattern.food_ordering_system.entity.MenuItem;
import com.pattern.food_ordering_system.model.user.Restaurant;
import com.pattern.food_ordering_system.model.user.UserFactory;

import java.util.ArrayList;

public class Menu implements MenuComponent {
    
    private String categoryName;
    private ArrayList<MenuComponent> menuComponents;

    public Menu() {
        this.menuComponents = new ArrayList<>();
    }

    public Menu(String categoryName) {
        this.menuComponents = new ArrayList<>();
        this.categoryName = categoryName;
    }

    public void remove(MenuComponent menuComponent) {
        menuComponents.remove(menuComponent);
    }

    public boolean add(MenuComponent menuComponent) {
        return menuComponents.add(menuComponent);
    }

    public void add(String category, MenuItem item) {
        Menu subMenu = null;
        if (this.categoryName != null && this.categoryName.equalsIgnoreCase(category)) {
            subMenu = this;
        }

        for (MenuComponent child : menuComponents) {
            if (child.getName().equalsIgnoreCase(category)) {
                subMenu = (Menu) child;
            }
        }

        if (subMenu == null) {
            subMenu = new Menu(); // Add New Category
            subMenu.setCategoryName(category);
            this.add(subMenu);
        }

        subMenu.add(item);
        item.setParent(subMenu);
    }

    public void remove(MenuItem menuItem) {
        Menu subMenu = (Menu) menuItem.getParent();
        if (subMenu.menuComponents.size() == 1) {
            Restaurant restaurant = (Restaurant) UserFactory.getUser();
            restaurant.getMenu().remove(subMenu);
        } else subMenu.remove((MenuComponent) menuItem); // To Call First Method
    }


    public ArrayList<String> getAllMenuCategories() {
        ArrayList<String> categories = new ArrayList<>();
        for (MenuComponent child : menuComponents) {
            if (child instanceof Menu) {
                categories.add(child.getName());
            }
        }
        return categories;
    }

    public ArrayList<MenuComponent> getMenuComponents() {
        return menuComponents;
    }

    public void setMenuComponents(ArrayList<MenuComponent> menuComponents) {
        this.menuComponents = menuComponents;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public String getName() {
        return this.categoryName;
    }
}
