package com.inhim.pj.dowloadfile.download.limit;

import android.util.Log;


import com.inhim.pj.dowloadfile.download.DownloadInfo;
import com.inhim.pj.dowloadfile.download.MyBusinessInfoDid;

import org.greenrobot.eventbus.EventBus;
import org.litepal.LitePal;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by zs
 * Date：2018年 09月 12日
 * Time：13:50
 * —————————————————————————————————————
 * About: 观察者
 * —————————————————————————————————————
 */
public class DownloadLimitObserver implements Observer<DownloadInfo> {

    public Disposable d;//可以用于取消注册的监听者
    public DownloadInfo downloadInfo;

    @Override
    public void onSubscribe(Disposable d) {
        this.d = d;
    }

    @Override
    public void onNext(DownloadInfo value) {
        this.downloadInfo = value;
        downloadInfo.setDownloadStatus(DownloadInfo.DOWNLOAD);
        EventBus.getDefault().post(downloadInfo);
    }

    @Override
    public void onError(Throwable e) {
        Log.d("My_Log","onError");
        if (DownloadLimitManager.getInstance().getDownloadUrl(downloadInfo.getUrl())){
            DownloadLimitManager.getInstance().pauseDownload(downloadInfo.getUrl());
            downloadInfo.setDownloadStatus(DownloadInfo.DOWNLOAD_ERROR);
            EventBus.getDefault().post(downloadInfo);
        }else{
            downloadInfo.setDownloadStatus(DownloadInfo.DOWNLOAD_PAUSE);
            EventBus.getDefault().post(downloadInfo);
        }

    }

    @Override
    public void onComplete() {
        Log.d("My_Log","onComplete");
        if (downloadInfo != null){
            List<DownloadInfo> person = LitePal.where("url = ?", downloadInfo.getUrl()).find(DownloadInfo.class);
            if(person!=null&&person.size()>0){
                for(int i=0;i<person.size();i++){
                    MyBusinessInfoDid infosDid = new MyBusinessInfoDid();
                    infosDid.setContent(person.get(i).getContent());
                    infosDid.setCover(person.get(i).getCover());
                    infosDid.setFilePath(person.get(i).getFilePath());
                    //infosDid.setProgress(infos.get(i).getProgress());
                    infosDid.setReaderId(person.get(i).getReaderId());
                    infosDid.setReaderTypeId(person.get(i).getReaderTypeId());
                    infosDid.setTitle(person.get(i).getTitle());
                    infosDid.setUrl(person.get(i).getUrl());
                    infosDid.setSynopsis(person.get(i).getSynopsis());
                    infosDid.setType(person.get(i).getType());
                    infosDid.setTotal(person.get(i).getTotal());
                    infosDid.save();
                    person.get(i).delete();
                }
            }
            downloadInfo.setDownloadStatus(DownloadInfo.DOWNLOAD_OVER);
            EventBus.getDefault().post(downloadInfo);
        }
    }
}
