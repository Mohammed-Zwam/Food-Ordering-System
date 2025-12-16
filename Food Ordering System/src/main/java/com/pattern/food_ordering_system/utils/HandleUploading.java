package com.pattern.food_ordering_system.utils;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.UUID;

public class HandleUploading {
    public static String upload(ImageView logoPreview, String imageName) {
        Image image = logoPreview.getImage();
        if (image != null) {
            try {
                BufferedImage bImage = SwingFXUtils.fromFXImage(image, null);

                File folder = new File("src/main/resources/users-images");

                // create folder if not exist
                if (!folder.exists()) {
                    folder.mkdirs();
                }
                String imgType = imageName.substring(imageName.lastIndexOf('.') + 1);
                String newImageName = UUID.randomUUID().toString() + '.' + imgType;
                System.out.println(newImageName);
                File outputFile = new File(folder, newImageName);

                ImageIO.write(bImage, imgType, outputFile);
                return newImageName;
            } catch (Exception e) {
                e.printStackTrace();
                return "default";
            }
        } else {
            AlertHandler.showInfo("Operation Failed", "Failed To Save");
            return "default";
        }
    }
}
