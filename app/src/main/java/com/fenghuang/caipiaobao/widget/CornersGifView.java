package com.fenghuang.caipiaobao.widget;

import android.net.Uri;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.fenghuang.caipiaobao.manager.ImageManager;


/**
 * @ Author  QinTian
 * @ Date  2020-03-21
 * @ Describe
 */
public class CornersGifView {
    /**
     * @param imageView
     * @param url
     * @param isCircle  0:false 1:Circle 2:Round
     */
    public static void setImage(SimpleDraweeView imageView, String url, int isCircle) {

        if (url != null) {
            Uri parse = Uri.parse(url);
            String[] split = url.split("\\.");
            if (split.length > 1 && split[split.length - 1].equalsIgnoreCase("gif")) { //如果是动图  则用代码设置
                DraweeController controller = Fresco.newDraweeControllerBuilder()
                        .setUri(parse).setAutoPlayAnimations(true).build();
                imageView.setController(controller);

                switch (isCircle) {//如果是圆形 用代码设置（Layout里必须设置背景）
                    case 1:// 自定义变量 用来判断是不是圆形
                        setCircle(imageView);
                        break;
                }

            } else {
                imageView.setImageURI(parse);
            }
        } else {
            ImageManager.INSTANCE.loadImg("",imageView);
        }
    }

    private static void setCircle(SimpleDraweeView imageView) {
        RoundingParams roundingParams = imageView.getHierarchy().getRoundingParams();
        roundingParams.setRoundAsCircle(true);
        imageView.getHierarchy().setRoundingParams(roundingParams);
    }

}
