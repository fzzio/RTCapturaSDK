package edu.realtime.rtcapturasdk;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.ErrorCallback;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;
public class CapturaActivity extends Activity {

	private final static String TAG = "Foto";
	private int noPruebas = 100;
	private ArrayList<Long> tiempos = new ArrayList<Long>();

	private TextView tlblEstado; // ltxt_estado
	private TextView tlblTiempoMax; // ltxt_tiempo_max
	private TextView tlblTiempoMin; // ltxt_tiempo_min
	private TextView tlblTiempoProm; // ltxt_tiempo_prom
	private TextView tlblFrames; // ltxt_frames

	// Camera vars
	private Camera mCamera;
	// private CameraPreview mCameraPreview;
	private SurfaceView mVideoCaptureView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		this.tlblEstado = (TextView) super.findViewById(R.id.ltxt_estado);
		this.tlblTiempoMax = (TextView) super
				.findViewById(R.id.ltxt_tiempo_max);
		this.tlblTiempoMin = (TextView) super
				.findViewById(R.id.ltxt_tiempo_min);
		this.tlblTiempoProm = (TextView) super
				.findViewById(R.id.ltxt_tiempo_prom);
		this.tlblFrames = (TextView) super.findViewById(R.id.ltxt_frames);

		// Verificamos ausencia o no de camaras
		if (!checkCameraHardware(this)) {
			this.tlblEstado.setText("No hay camara");
			return;
		} else {
			this.tlblEstado.setText("Camara detectada.");

			// Create our Preview view and set it as the content of our
			// activity.
			// mCameraPreview = new CameraPreview(this);

			// FrameLayout preview = (FrameLayout)
			// findViewById(R.id.camera_preview);
			// preview.addView(mCameraPreview);

			/** pruebas despues de esto **/
			mVideoCaptureView = (SurfaceView) findViewById(R.id.camera_preview);
			SurfaceHolder videoCaptureViewHolder = mVideoCaptureView
					.getHolder();
			videoCaptureViewHolder
					.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		}
	}

	/** Check if this device has a camera */
	private boolean checkCameraHardware(Context context) {
		if (context.getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_CAMERA)) {
			// this device has a camera
			return true;
		} else {
			// no camera on this device
			return false;
		}
	}

	public void iniciarCaptura(View v) {
		if (v.getId() == R.id.btn_captura_i) {
			startVideo();

		}
	}

	public void finalizarCaptura(View v) {
		if (v.getId() == R.id.btn_captura_f) {
			stopVideo();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		finish();
	}

	private void startVideo() {
		this.tlblEstado.setText("Capturando...");
		
		SurfaceHolder videoCaptureViewHolder = null;
		try {
			mCamera = Camera.open();
		} catch (RuntimeException e) {
			Log.e("CameraTest", "Camera Open filed");
			return;
		}
		mCamera.setErrorCallback(new ErrorCallback() {
			public void onError(int error, Camera camera) {
			}
		});

		if (null != mVideoCaptureView)
			videoCaptureViewHolder = mVideoCaptureView.getHolder();
		try {
			mCamera.setPreviewDisplay(videoCaptureViewHolder);
		} catch (Throwable t) {
		}

		Log.v("CameraTest", "Camera PreviewFrameRate = " + mCamera.getParameters().getPreviewFrameRate());
		Size previewSize = mCamera.getParameters().getPreviewSize();
		int dataBufferSize = (int) (previewSize.height * previewSize.width * (ImageFormat.getBitsPerPixel(mCamera.getParameters().getPreviewFormat()) / 8.0));
		mCamera.addCallbackBuffer(new byte[dataBufferSize]);
		mCamera.addCallbackBuffer(new byte[dataBufferSize]);
		mCamera.addCallbackBuffer(new byte[dataBufferSize]);
		mCamera.setPreviewCallbackWithBuffer(new Camera.PreviewCallback() {
			private long timestamp = System.currentTimeMillis();
			private long tiempoDuracion = 0;

			public synchronized void onPreviewFrame(byte[] data, Camera camera) {
				tiempoDuracion = (System.currentTimeMillis() - timestamp);
				tiempos.add(tiempoDuracion);
				
				Log.v("CameraTest", "Time Gap = " + tiempoDuracion);
				
				timestamp = System.currentTimeMillis();
				
				try {
					camera.addCallbackBuffer(data);
				} catch (Exception e) {
					Log.e("CameraTest", "addCallbackBuffer error");
					return;
				}
				
				if (tiempos.size() >= noPruebas){
					stopVideo();
					return;
				}
				
				return;
			}
		});
		try {
			mCamera.startPreview();
		} catch (Throwable e) {
			mCamera.release();
			mCamera = null;
			return;
		}
	}

	private void stopVideo() {
		if (null == mCamera)
			return;
		try {
			mCamera.stopPreview();
			mCamera.setPreviewDisplay(null);
			mCamera.setPreviewCallbackWithBuffer(null);
			mCamera.release();

			// Se elimina porque al ser asincronico el primer tiempo es aberrante a la muestra
			//tiempos.remove(0);
			calcularTiempos();

			tiempos.clear();

		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		mCamera = null;
		
		this.tlblEstado.setText("Captura finalizada.");
	}

	private void calcularTiempos() {
		long tiempoMaximo = 0;
		long tiempoMinimo = tiempos.get(0);
		long tiempoPromedio = 0;

		for (int i = 0; i < tiempos.size(); i++) {
			if (tiempos.get(i) > tiempoMaximo) {
				tiempoMaximo = tiempos.get(i);
				
			} else if (tiempos.get(i) < tiempoMinimo) {
				tiempoMinimo = tiempos.get(i);				
			}
			tiempoPromedio += tiempos.get(i);
		}
		tiempoPromedio = tiempoPromedio / tiempos.size();
		
		this.tlblTiempoMax.setText(tiempoMaximo + "");
		this.tlblTiempoMin.setText(tiempoMinimo + "");		
		this.tlblTiempoProm.setText(tiempoPromedio + "");
		this.tlblFrames.setText(tiempos.size() + "");

	}

	public void finish() {
		stopVideo();
		super.finish();
	};
}