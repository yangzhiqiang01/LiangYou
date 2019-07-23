package com.inhim.downloader.core;


import com.inhim.downloader.domain.DownloadInfo;
import com.inhim.downloader.exception.DownloadException;

/**
 * Created by renpingqing on 17/1/22.
 */

public interface DownloadResponse {

  void onStatusChanged(DownloadInfo downloadInfo);

  void handleException(DownloadException exception);
}
