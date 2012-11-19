package view;
import javax.swing.*;

import model.object.Monster;

public class FirstPersonMonster extends Monster{
	private ImageIcon image;
	
	public FirstPersonMonster(String name, int health, String imageSource) {
		super(name, health);
		setImage(new ImageIcon(FirstPersonMonster.class.getResource("/img/firstperson/monster/"+imageSource)));
	}

	public ImageIcon getImage() {
		return image;
	}

	public void setImage(ImageIcon image) {
		this.image = image;
	}

}
