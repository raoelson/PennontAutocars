package soluces.com.pennontautocars.com.Model;

public class Product {
	String name;
	  int price;
	  int image;
	  boolean box;
	  

	  public Product(String _describe, int _price, int _image, boolean _box) {
	    name = _describe;
	    price = _price;
	    image = _image;
	    box = _box;
	  }

	public boolean isBox() {
		return box;
	}

	public void setBox(boolean box) {
		this.box = box;
	}

	public int getImage() {
		return image;
	}

	public void setImage(int image) {
		this.image = image;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}
}
