package com.example.filterfeeders;

import android.graphics.Bitmap;
import android.widget.Toast;

import com.mukesh.image_processing.ImageProcessor;

import java.util.ArrayList;

public class BitmapEffects
{
    Bitmap original;
    Bitmap current;

    ImageProcessor imageProcessor = new ImageProcessor();

    ArrayList<String> effects;

    BitmapEffects(Bitmap original)
    {
        this.original = original;
    }

    void setBitmap(Bitmap bmp)
    {
        original = bmp;
    }

    void reset()
    {
        current = original;
    }

    Bitmap applyPerlinEffect(int r, int g, int b)
    {
        if (original != null)
        {
            int width = original.getWidth();
            int height = original.getHeight();

            int[] pixels = new int[width * height];
            original.getPixels(pixels, 0, width, 0, 0, width, height);

            PerlinNoise noise = new PerlinNoise(0);

            // Mask individual pixels
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    byte noiseValueR = (byte)(255 - (Math.round(Clamp((noise.noise(x * 0.005, y * 0.005, 0) + .61) * 255, 0, 255)) * r / 100));
                    byte noiseValueG = (byte)(255 - (Math.round(Clamp((noise.noise(x * 0.005, y * 0.005, 1) + .61) * 255, 0, 255)) * g / 100));
                    byte noiseValueB = (byte)(255 - (Math.round(Clamp((noise.noise(x * 0.005, y * 0.005, 2) + .61) * 255, 0, 255)) * b / 100));

                    pixels[x + y * width] &= 0xFF000000 | (noiseValueR << 16) | (noiseValueG << 8) | noiseValueB;
                }
            }

            current = Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.ARGB_8888);

            // Update image view
            return current;
        }
        else
        {
            return null;
        }
    }

    Bitmap invert()
    {
        if (original != null)
        {
            current = imageProcessor.doInvert(original);
            return current;
        } else
        {
            return null;
        }
    }

    Bitmap greyscale()
    {
        if (original != null)
        {
            current = imageProcessor.doGreyScale(original);
            return current;
        } else
        {
            return null;
        }
    }

    Bitmap blackFilter()
    {
        if (original != null)
        {
            current = imageProcessor.applyBlackFilter(original);
            return current;
        } else
        {
            return null;
        }
    }

    Bitmap fleaEffect()
    {
        if (original != null)
        {
            current = imageProcessor.applyFleaEffect(original);
            return current;
        } else
        {
            return null;
        }
    }

    Bitmap gaussian()
    {
        if (original != null)
        {
            current = imageProcessor.applyGaussianBlur(original);
            return current;
        } else
        {
            return null;
        }
    }

    Bitmap meanRemove()
    {
        if (original != null)
        {
            current = imageProcessor.applyMeanRemoval(original);
            return current;
        } else
        {
            return null;
        }
    }

    double Clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
}
