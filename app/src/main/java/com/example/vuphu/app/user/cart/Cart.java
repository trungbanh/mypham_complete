package com.example.vuphu.app.user.cart;

import com.example.vuphu.app.object.ProductInCart;
import com.example.vuphu.app.object.ProductShow;

import java.util.ArrayList;

public class Cart {
    private ArrayList<ProductInCart> productBuys ;
    private ArrayList<String> name ;

    public Cart () {
        this.productBuys = new ArrayList<>();
        name = new ArrayList<>();
    }

    private static Cart Instance;
    public static Cart getInstance() {
        if (Instance == null) {
            Instance = new Cart();

        }
        return Instance;
    }

    public void addProduct (ProductInCart product,String name) {
        productBuys.add(product);
        this.name.add(name);
    }

    public ArrayList<ProductShow> getListProduct (){
        ArrayList<ProductShow> arrayList = new ArrayList<>();
        for (int i = 0 ; i< productBuys.size() ; i++) {
            arrayList.add(new ProductShow(name.get(i).toString(),productBuys.get(i).getQuatityBuy()));
        }

        return  arrayList;
    }

    public String getPostJson () {
        String temp ="{ \"products\": [";
        for (int i = 0 ;i<productBuys.size();i++) {
            temp = temp+"{";
            temp = temp +"\"product\":"+productBuys.get(i).getproduct() +",";
            temp = temp + "\"quatityBuy\":" +productBuys.get(i).getQuatityBuy();
            temp = temp +"},";

        }
        temp = temp.substring(0,temp.length()-2);
        temp= temp+"}]";
        return temp;
    }
/*{
	"products": [
		{
			"product": "5abf3efdb9f9e80cbcb8389a",
			"quatityBuy": 5
		},
		{
			"product": "5ac31651ddf6b41b78dd1657",
			"quatityBuy": 2
		}
	]
}*/



}
