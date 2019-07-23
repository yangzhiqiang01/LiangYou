package com.inhim.downloader.db;

import com.inhim.downloader.domain.DownloadInfo;
import com.inhim.downloader.domain.DownloadThreadInfo;

import java.util.List;


/**
 * Created by renpingqing on 17/1/23.
 */

public interface DownloadDBController {

  List<DownloadInfo> findAllDownloading();

  List<DownloadInfo> findAllDownloaded();

  DownloadInfo findDownloadedInfoById(int id);

  void pauseAllDownloading();

  void createOrUpdate(DownloadInfo downloadInfo);

  void createOrUpdate(DownloadThreadInfo downloadThreadInfo);

  void delete(DownloadInfo downloadInfo);

  void delete(DownloadThreadInfo download);
}
