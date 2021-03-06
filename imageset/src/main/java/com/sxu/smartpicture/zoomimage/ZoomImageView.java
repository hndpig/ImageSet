package com.sxu.smartpicture.zoomimage;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.GestureDetector;

import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.view.GenericDraweeView;
import com.github.chrisbanes.photoview.OnMatrixChangedListener;
import com.github.chrisbanes.photoview.OnOutsidePhotoTapListener;
import com.github.chrisbanes.photoview.OnPhotoTapListener;
import com.github.chrisbanes.photoview.OnScaleChangedListener;
import com.github.chrisbanes.photoview.OnSingleFlingListener;
import com.github.chrisbanes.photoview.OnViewDragListener;
import com.github.chrisbanes.photoview.OnViewTapListener;
import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.sxu.smartpicture.imageloader.ImageLoaderManager;
import com.sxu.smartpicture.imageloader.WrapImageView;
import com.sxu.smartpicture.imageloader.instance.FrescoInstance;


/*******************************************************************************
 * Description: 可缩放的图片组件
 *
 * Author: Freeman
 *
 * Date: 2018/9/12
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/
public class ZoomImageView extends WrapImageView {

	private boolean isFresco;
	private PhotoViewAttacher attacher;
	private ScaleType pendingScaleType;

	public ZoomImageView(Context context) {
		this(context, null);
	}

	public ZoomImageView(Context context, AttributeSet attr) {
		this(context, attr, 0);
	}

	public ZoomImageView(Context context, AttributeSet attr, int defStyle) {
		super(context, attr, defStyle);
		init();
	}

	private void init() {
		isFresco = ImageLoaderManager.getInstance().getImageLoaderInstance() instanceof FrescoInstance;
		if (isFresco) {
			attacher = new FrescoAttacher(this);
		} else {
			attacher = new PhotoViewAttacher(this);
			super.setScaleType(ScaleType.MATRIX);
		}
		if (pendingScaleType != null) {
			setScaleType(pendingScaleType);
			pendingScaleType = null;
		}
	}

	@Override protected void onDraw(@NonNull Canvas canvas) {
		if (isFresco) {
			canvas.concat(attacher.getImageMatrix());
			super.onDraw(canvas);
		} else {
			super.onDraw(canvas);
		}
	}

	/**
	 * Get the current {@link PhotoViewAttacher} for this view. Be wary of holding on to references
	 * to this attacher, as it has a reference to this view, which, if a reference is held in the
	 * wrong place, can cause memory leaks.
	 *
	 * @return the attacher.
	 */
	public PhotoViewAttacher getAttacher() {
		return attacher;
	}

	@Override
	public ScaleType getScaleType() {
		return attacher.getScaleType();
	}

	@Override
	public Matrix getImageMatrix() {
		return attacher.getImageMatrix();
	}

	@Override
	public void setOnLongClickListener(OnLongClickListener l) {
		attacher.setOnLongClickListener(l);
	}

	@Override
	public void setOnClickListener(OnClickListener l) {
		attacher.setOnClickListener(l);
	}

	@Override
	public void setScaleType(ScaleType scaleType) {
		if (attacher == null) {
			pendingScaleType = scaleType;
		} else {
			attacher.setScaleType(scaleType);
		}
	}

	@Override
	public void setImageDrawable(Drawable drawable) {
		super.setImageDrawable(drawable);
		// setImageBitmap calls through to this method
		if (attacher != null) {
			attacher.update();
		}
	}

	@Override
	public void setImageResource(int resId) {
		super.setImageResource(resId);
		if (attacher != null) {
			attacher.update();
		}
	}

	@Override
	public void setImageURI(Uri uri) {
		super.setImageURI(uri);
		if (attacher != null) {
			attacher.update();
		}
	}

	@Override
	protected boolean setFrame(int l, int t, int r, int b) {
		boolean changed = super.setFrame(l, t, r, b);
		if (changed) {
			attacher.update();
		}
		return changed;
	}

	public void setRotationTo(float rotationDegree) {
		attacher.setRotationTo(rotationDegree);
	}

	public void setRotationBy(float rotationDegree) {
		attacher.setRotationBy(rotationDegree);
	}

	@Deprecated
	public boolean isZoomEnabled() {
		return attacher.isZoomEnabled();
	}

	public boolean isZoomable() {
		return attacher.isZoomable();
	}

	public void setZoomable(boolean zoomable) {
		attacher.setZoomable(zoomable);
	}

	public RectF getDisplayRect() {
		return attacher.getDisplayRect();
	}

	public void getDisplayMatrix(Matrix matrix) {
		attacher.getDisplayMatrix(matrix);
	}

	public boolean setDisplayMatrix(Matrix finalRectangle) {
		return attacher.setDisplayMatrix(finalRectangle);
	}

	public void getSuppMatrix(Matrix matrix) {
		attacher.getSuppMatrix(matrix);
	}

	public boolean setSuppMatrix(Matrix matrix) {
		return attacher.setDisplayMatrix(matrix);
	}

	public float getMinimumScale() {
		return attacher.getMinimumScale();
	}

	public float getMediumScale() {
		return attacher.getMediumScale();
	}

	public float getMaximumScale() {
		return attacher.getMaximumScale();
	}

	public float getScale() {
		return attacher.getScale();
	}

	public void setAllowParentInterceptOnEdge(boolean allow) {
		attacher.setAllowParentInterceptOnEdge(allow);
	}

	public void setMinimumScale(float minimumScale) {
		attacher.setMinimumScale(minimumScale);
	}

	public void setMediumScale(float mediumScale) {
		attacher.setMediumScale(mediumScale);
	}

	public void setMaximumScale(float maximumScale) {
		attacher.setMaximumScale(maximumScale);
	}

	public void setScaleLevels(float minimumScale, float mediumScale, float maximumScale) {
		attacher.setScaleLevels(minimumScale, mediumScale, maximumScale);
	}

	public void setOnMatrixChangeListener(OnMatrixChangedListener listener) {
		attacher.setOnMatrixChangeListener(listener);
	}

	public void setOnPhotoTapListener(OnPhotoTapListener listener) {
		attacher.setOnPhotoTapListener(listener);
	}

	public void setOnOutsidePhotoTapListener(OnOutsidePhotoTapListener listener) {
		attacher.setOnOutsidePhotoTapListener(listener);
	}

	public void setOnViewTapListener(OnViewTapListener listener) {
		attacher.setOnViewTapListener(listener);
	}

	public void setOnViewDragListener(OnViewDragListener listener) {
		attacher.setOnViewDragListener(listener);
	}

	public void setScale(float scale) {
		attacher.setScale(scale);
	}

	public void setScale(float scale, boolean animate) {
		attacher.setScale(scale, animate);
	}

	public void setScale(float scale, float focalX, float focalY, boolean animate) {
		attacher.setScale(scale, focalX, focalY, animate);
	}

	public void setZoomTransitionDuration(int milliseconds) {
		attacher.setZoomTransitionDuration(milliseconds);
	}

	public void setOnDoubleTapListener(GestureDetector.OnDoubleTapListener onDoubleTapListener) {
		attacher.setOnDoubleTapListener(onDoubleTapListener);
	}

	public void setOnScaleChangeListener(OnScaleChangedListener onScaleChangedListener) {
		attacher.setOnScaleChangeListener(onScaleChangedListener);
	}

	public void setOnSingleFlingListener(OnSingleFlingListener onSingleFlingListener) {
		attacher.setOnSingleFlingListener(onSingleFlingListener);
	}
}
