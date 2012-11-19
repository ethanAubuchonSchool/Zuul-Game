package view;
import javax.swing.ImageIcon;

import model.object.Item;

public class FirstPersonItem extends Item{
	private ImageIcon image;
	
	public FirstPersonItem(String itemName, double itemWeight, String imageSource) {
		super(itemName, itemWeight);
		// TODO Auto-generated constructor stub
		setImage(new ImageIcon(FirstPersonMonster.class.getResource("/img/firstperson/item/"+imageSource)));
	}

	public ImageIcon getImage() {
		return image;
	}

	public void setImage(ImageIcon image) {
		this.image = image;
	}
	
}
