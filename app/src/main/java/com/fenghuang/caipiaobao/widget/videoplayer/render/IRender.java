package com.fenghuang.caipiaobao.widget.videoplayer.render;
import android.view.View;

import com.fenghuang.caipiaobao.widget.videoplayer.player.IPlayer;
import com.fenghuang.caipiaobao.widget.videoplayer.widget.BaseVideoView;

/**
 * Created by Taurus on 2017/11/19.
 *
 * frame rendering view,
 * using the method can refer to BaseVideoView {@link BaseVideoView}
 *
 */

public interface IRender {

    //use TextureView for render
    int RENDER_TYPE_TEXTURE_VIEW = 0;

    //use SurfaceView for render
    int RENDER_TYPE_SURFACE_VIEW = 1;

    void setRenderCallback(IRenderCallback renderCallback);

    /**
     * update video rotation, such as some video maybe rotation 90 degree.
     * @param degree
     */
    void setVideoRotation(int degree);

    void setVideoSampleAspectRatio(int videoSarNum, int videoSarDen);

    /**
     * update video show aspect ratio
     *
     * see also
     * {@link AspectRatio#AspectRatio_16_9}
     * {@link AspectRatio#AspectRatio_4_3}
     * {@link AspectRatio#AspectRatio_FIT_PARENT}
     * {@link AspectRatio#AspectRatio_FILL_PARENT}
     * {@link AspectRatio#AspectRatio_MATCH_PARENT}
     * {@link AspectRatio#AspectRatio_ORIGIN}
     *
     * @param aspectRatio
     */
    void updateAspectRatio(AspectRatio aspectRatio);

    /**
     * update video size ,width and height.
     * @param videoWidth
     * @param videoHeight
     */
    void updateVideoSize(int videoWidth, int videoHeight);

    View getRenderView();

    /**
     * release render,the render will become unavailable
     */
    void release();

    /**
     * IRenderHolder is responsible for associate the decoder with rendering views.
     *
     * see also
     * {@link RenderSurfaceView.InternalRenderHolder#bindPlayer(IPlayer)}
     * {@link RenderTextureView.InternalRenderHolder#bindPlayer(IPlayer)}
     *
     */
    interface IRenderHolder{
        void bindPlayer(IPlayer player);
    }

    /**
     *
     * see also
     * {@link RenderSurfaceView.IRenderCallback}
     * {@link RenderTextureView.IRenderCallback}
     *
     */
    interface IRenderCallback{
        void onSurfaceCreated(IRenderHolder renderHolder, int width, int height);
        void onSurfaceChanged(IRenderHolder renderHolder, int format, int width, int height);
        void onSurfaceDestroy(IRenderHolder renderHolder);
    }

}
