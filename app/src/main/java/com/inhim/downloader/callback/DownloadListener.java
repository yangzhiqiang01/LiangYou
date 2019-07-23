package com.inhim.downloader.callback;


import com.inhim.downloader.exception.DownloadException;

/**
 * Created by renpingqing on 17/2/23.
 */

public interface DownloadListener {

  void onStart();

  void onWaited();

  void onPaused();

  void onDownloading(long progress, long size);

  void onRemoved();

  void onDownloadSuccess();

  void onDownloadFailed(DownloadException e);
}
