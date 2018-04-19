package com.example.vuphu.app.user.cart;

import com.example.vuphu.app.object.ProductBuy;

public class Cart {
    private ProductBuy productBuys ;

    public Cart () {

    }

    private static Cart Instance;
    public static Cart getInstance() {
        if (Instance == null) {
            Instance = new Cart();

        }
        return Instance;
    }

    private void addProduct () {

    }



}
