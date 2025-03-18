/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tm2batch.util;

import com.tm2batch.service.LogService;
import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 *
 * @author miker
 */
public class ImageUtils {
    
   public static BufferedImage makeColorTransparent(BufferedImage imageIn, Color color, int tolerance) 
   {
        try 
        {
            int width = imageIn.getWidth();
            int height = imageIn.getHeight();
            BufferedImage transparentImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int pixelColor = imageIn.getRGB(x, y);
                    if (isSimilarColor(pixelColor, color.getRGB(), tolerance)) {
                        transparentImage.setRGB(x, y, 0x00FFFFFF & pixelColor); // Set alpha to 0 (transparent)
                    } else {
                        transparentImage.setRGB(x, y, pixelColor); // Copy original color
                    }
                }
            }
            return transparentImage;
        } 
        catch( Exception e )
        {
            LogService.logIt(e, "ImageUtils.makeColorTransparent() " );
            return imageIn;
        }
    }

    private static boolean isSimilarColor(int color1, int color2, int tolerance) {
         int r1 = (color1 >> 16) & 0xFF;
        int g1 = (color1 >> 8) & 0xFF;
        int b1 = color1 & 0xFF;
        
        int r2 = (color2 >> 16) & 0xFF;
        int g2 = (color2 >> 8) & 0xFF;
        int b2 = color2 & 0xFF;

        return (Math.abs(r1 - r2) <= tolerance &&
                Math.abs(g1 - g2) <= tolerance &&
                Math.abs(b1 - b2) <= tolerance);
    }    
    
}
