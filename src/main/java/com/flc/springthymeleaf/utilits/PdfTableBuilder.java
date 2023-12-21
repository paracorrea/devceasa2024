package com.flc.springthymeleaf.utilits;

import java.io.IOException;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.property.HorizontalAlignment;

public class PdfTableBuilder {

	
	 public static void addLogo(Document document, String imagePath, float width, float height) {
	        try {
	            Image logo = new Image(ImageDataFactory.create(imagePath));
	            logo.setWidth(width);
	            logo.setHeight(height);
	            logo.setHorizontalAlignment(HorizontalAlignment.RIGHT);
	            document.add(logo);
	        } catch (IOException e) {
	            // Lidar com a exceção, por exemplo, imprimir a pilha de exceções
	            e.printStackTrace();
	        }
	    }
	
}
