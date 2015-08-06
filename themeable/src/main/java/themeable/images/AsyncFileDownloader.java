package themeable.images;

import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;

import okio.BufferedSink;
import okio.Okio;

/**
 * Created by brett on 12/06/15.
 */
public class AsyncFileDownloader  {

    private static final String TAG = AsyncFileDownloader.class.getSimpleName();

    private final OkHttpClient client = HttpsTrustManager.getTrustAllHttpClient();

    interface Callback {
        void onCompleted(File downloadedFile);
        void onError(Exception e, int responseCode);
    }

    public void downloadFile(String url, String outputFilePath, final Callback clientCallback) {
        if(url == null) {
            throw new IllegalArgumentException("URL cannot be null");
        }

        if(outputFilePath == null) {
            throw new IllegalArgumentException("Output path cannot be null");
        }

        File dir = new File(outputFilePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        if(!dir.getParentFile().exists()) {
            throw new IllegalArgumentException("Failed to create download folder for file download: " + dir.getParentFile().getAbsolutePath());
        }

        Request request = setupRequest(url);
        startDownload(outputFilePath, clientCallback, request);
    }

    private Request setupRequest(String url) {
        Request.Builder builder = new Request.Builder().url(url);
        return builder.build();
    }

    private void startDownload(final String outputFileAbsolutePath, final Callback clientCallback, Request request) {
       client.newCall(request).enqueue(new com.squareup.okhttp.Callback() {
            @Override
            public void onFailure(Request request, IOException throwable) {
                Log.e(TAG, "Failed to download file from server", throwable);
                if(clientCallback != null) {
                    clientCallback.onError(throwable, 0);
                }
            }

            @Override
            public void onResponse(Response response) {
                if (response.isSuccessful()) {
                    try {
                        File downloadedFile = new File(outputFileAbsolutePath);
                        File dir = downloadedFile.getParentFile();

                        if (!dir.exists()) {
                            dir.mkdirs();
                        }

                        if (downloadedFile.exists()) {
                            downloadedFile.delete();
                        }

                        BufferedSink sink = Okio.buffer(Okio.sink(downloadedFile));
                        sink.writeAll(response.body().source());
                        sink.close();
                        Log.d(TAG, "Downloaded file to: " + downloadedFile.getAbsolutePath() + " " + downloadedFile.exists());
                        if(clientCallback != null) {
                            clientCallback.onCompleted(downloadedFile);
                        }
                    } catch (IOException e) {
                        if(clientCallback != null) {
                            clientCallback.onError(e, response.code());
                        }
                    }
                } else {
                    if(clientCallback != null) {
                        clientCallback.onError(null, response.code());
                    }
                }
            }
        });
    }
}
