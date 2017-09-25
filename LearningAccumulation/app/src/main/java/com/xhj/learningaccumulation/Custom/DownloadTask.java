package com.xhj.learningaccumulation.Custom;

import android.os.AsyncTask;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadTask extends AsyncTask<String, Integer, String> {

	private final DownloadCallback mCallback;
	private final String mFilePath;

	public DownloadTask(String path, DownloadCallback callback) {
		this.mCallback = callback;
		this.mFilePath = path;
	}

	@Override
	protected String doInBackground(String... sUrl) {
		InputStream input = null;
		OutputStream output = null;
		HttpURLConnection connection = null;
		try {
			URL url = new URL(sUrl[0]);
			connection = (HttpURLConnection) url.openConnection();
			connection.connect();

			int code = connection.getResponseCode();

			if (code != HttpURLConnection.HTTP_OK){
				return "Server returned HTTP " + connection.getResponseCode() + " " + connection.getResponseMessage();
			}

			// this will be useful to display download percentage
			// might be -1: server did not report the length
			int fileLength = connection.getContentLength();

			// download the file
			input = connection.getInputStream();
			output = new FileOutputStream(mFilePath);

			byte data[] = new byte[4096];
			long total = 0;
			int count;
			while ((count = input.read(data)) != -1) {
				// allow canceling with back button
				if (isCancelled()) {
					return null;
				}
				total += count;
				// publishing the progress....
				if (fileLength > 0) { // only if total length is known
					publishProgress((int) (total * 100 / fileLength));
				}
				output.write(data, 0, count);
			}
		} catch (Exception e) {
			return e.toString();
		} finally {
			try {
				if (output != null)
					output.close();
				if (input != null)
					input.close();
			} catch (IOException ignored) {
			}

			if (connection != null)
				connection.disconnect();
		}
		return null;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (mCallback != null) {
			mCallback.onStart();
		}
	}

	@Override
	protected void onProgressUpdate(Integer... progress) {
		super.onProgressUpdate(progress);
		if (mCallback != null) {
			mCallback.onProgress(progress);
		}
	}

	@Override
	protected void onPostExecute(String result) {
		if (mCallback != null) {
			mCallback.onFinished(result);
		}
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
		if (mCallback != null) {
			mCallback.onCancelled();
		}
	}

	public interface DownloadCallback {

		void onStart();

		void onProgress(Integer... progress);

		void onCancelled();

		void onFinished(String result);
	}

}