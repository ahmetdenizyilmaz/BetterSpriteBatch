package com.ady.graphics.g2d.BetterSpriteBatch;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

/**
 * Created by ADY on 21.3.2018
 */

public class BetterSpriteBatch extends SpriteBatch {


    public BetterSpriteBatch() {
        super();
    }

    /**
     * Draw with 4 different colors
     * {@link SpriteBatch#draw(Texture, float[], int, int)}.
     * @param color0 bottom left
     * @param color1 bottom right
     * @param color2 top left
     * @param color3 top right
     *               2---3
     *               \       \
     *               0 ---1
     * @return void
     */
    public void draw(Texture texture, float x, float y, Color color0, Color color1, Color color2, Color color3) {
        int fy2 = texture.getHeight();
        int fx2 = texture.getWidth();
        draw(texture, new float[]{
                x, y, color0.toFloatBits(), 0f, 1f,
                x, y + fy2, color2.toFloatBits(), 0f, 0f,
                x + fx2, y + fy2, color3.toFloatBits(), 1f, 0f,
                x + fx2, y, color1.toFloatBits(), 1f, 1f,
        }, 0, 20);
    }

    /**
     *
     * @param texture
     * @param x
     * @param y
     * @param color0 bottom left
     * @param color1 bottom right
     * @param color2 top left
     * @param color3 top right
     *
     *               2---3
     *               \       \
     *               0 ---1
     * @param shearX   shear on X axis.
     * @param shearY  shear on Y axis
     */
    public void draw(Texture texture, float x, float y, Color color0, Color color1, Color color2, Color color3, float shearX,float shearY) {
        int fy2 = texture.getHeight();
        int fx2 = texture.getWidth();
        draw(texture, new float[]{
                x-shearX/2f, y-shearY/2f, color0.toFloatBits(), 0f, 1f,
                x+shearX/2f, y + fy2-shearY/2f, color2.toFloatBits(), 0f, 0f,
                x + fx2+shearX/2f, y + fy2+shearY/2f, color3.toFloatBits(), 1f, 0f,
                x + fx2-shearX/2f, y+shearY/2f, color1.toFloatBits(), 1f, 1f,
        }, 0, 20);
    }

    /**
     *      * @param texture
     * @param x
     * @param y
     * @param shearX  shear on X axis
     * @param shearY  shear on Y axis
     */
    public void draw(Texture texture, float x, float y, float shearX,float shearY)
    {
        draw(texture,x,y,Color.WHITE,Color.WHITE,Color.WHITE,Color.WHITE,shearX,shearY);
    }


    /**
     * Draw with 4 different colors
     * {@link SpriteBatch#draw(Texture, float[], int, int)}.
     * <p>
     *
     * @param color0     first color
     * @param color1     second color
     * @param horizontal horizontal or vertical
     *                   <p/>
     *                   <p>
     *                   1---1
     *                   \       \
     *                   0 ---0
     *                   <p>
     *                   0---1
     *                   \       \
     *                   0 ---1
     * @return void
     */

    public void draw(Texture texture, float x, float y, Color color0, Color color1, boolean horizontal) {
        if (horizontal)
            draw(texture, x, y, color0, color1, color0, color1);
        else
            draw(texture, x, y, color0, color0, color1, color1);
    }


    @Override
    public void draw (Texture texture, float x, float y) {
       super.draw(texture, x, y, texture.getWidth(), texture.getHeight());
    }

    /**
     * @param font  font
     * @param str string
     * @param x x coordinate
     * @param y y coordinate
     * @param c0  first component of color   R for rbg(0-1f)  hue for HSV(0-360)
     * @param c1 second component of color   G for rbg(0-1f)  saturation for HSV(0-1f)
     * @param c2 third component of color  B for rgb(0-1f)  value for HSV(0-1f)
     * @param a  alpha
     * @param c0d  change in first color component
     * @param c1d change in second color component
     * @param c2d change in third color component
     * @param ad  change in alpha
     * @param colornumb   indicates number of different colors
     * @param colorSpaceRGB color space true == RGB  false ==HSV
     */
    public void rainbowFontDraw(BitmapFont font, CharSequence str, float x, float y, float c0, float c1, float c2, float a, float c0d, float c1d, float c2d, float ad, int colornumb, boolean colorSpaceRGB) {
        int totalLength = str.length();
        if(colornumb==0)colornumb=totalLength;
        MathUtils.clamp(colornumb, 2, totalLength);
        int gradientLength = totalLength / colornumb;
        String str2 = "";
        boolean previousState = font.getData().markupEnabled;
        font.getData().markupEnabled = true;
        Color target = new Color(c0, c1, c2, a);
        if (!colorSpaceRGB) {
            target.fromHsv(c0, c1, c2);
        }
        for (int i = 0; i < totalLength; i += gradientLength) {
            float lerp = (float) i / (float) (totalLength - gradientLength);
            if (colorSpaceRGB) {
                target.set(c0 + c0d * lerp, c1 + c1d * lerp, c2 + c2d * lerp, a + ad * lerp);
            } else {
                target.fromHsv(c0 + c0d * lerp, c1 + c1d * lerp, c2 + c2d * lerp);
                target.a = a + ad * lerp;
            }
            if (i + gradientLength > totalLength) {
                if (colorSpaceRGB) {
                    target.set(c0 + c0d, c1 + c1d, c2 + c2d, a + ad);

                } else {
                    target.fromHsv(c0 + c0d, c1, c2);
                }
                str2 += "[#" + target.toString() + "]" + ((String) str).substring(i, totalLength) + "[] ";
            } else {
                str2 += "[#" + target.toString() + "]" + ((String) str).substring(i, i + gradientLength) + "[] ";
            }
        }
        font.draw(this, str2, x, y);
        font.getData().markupEnabled = previousState;
    }

    /**
     *
     * @param font
     * @param str
     * @param x
     * @param y
     * @param sourceColor
     * @param targetColor
     * @param colornumb
     * @param colorSpaceRGB
     */

    public void rainbowFontDraw(BitmapFont font, CharSequence str, float x, float y, Color sourceColor, Color targetColor, int colornumb, boolean colorSpaceRGB) {


        if (colorSpaceRGB) {
            rainbowFontDraw(font, str, x, y, sourceColor.r, sourceColor.g, sourceColor.b, sourceColor.a, targetColor.r - sourceColor.r, targetColor.g - sourceColor.g, targetColor.b - sourceColor.b, targetColor.a - sourceColor.a, colornumb, true);

        } else {
            float[] hsvs = new float[3];
            float[] hsvt = new float[3];
            sourceColor.toHsv(hsvs);
            targetColor.toHsv(hsvt);
            System.out.println("asdasd" + hsvt[0] + "   " + hsvs[1] + "   " + hsvs[2]);
            rainbowFontDraw(font, str, x, y, hsvs[0], hsvs[1], hsvs[2], sourceColor.a, hsvt[0] - hsvs[0], hsvt[1] - hsvs[1], hsvt[2] - hsvs[2], targetColor.a - sourceColor.a, colornumb, false);

        }
    }
}
