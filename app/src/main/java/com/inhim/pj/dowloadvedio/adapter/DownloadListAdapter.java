package com.inhim.pj.dowloadvedio.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.inhim.downloader.DownloadService;
import com.inhim.downloader.callback.DownloadManager;
import com.inhim.downloader.domain.DownloadInfo;
import com.inhim.pj.R;
import com.inhim.pj.dowloadvedio.callback.MyDownloadListener;
import com.inhim.pj.dowloadvedio.db.DBController;
import com.inhim.pj.dowloadvedio.domain.MyBusinessInfLocal;
import com.inhim.pj.dowloadvedio.domain.MyBusinessInfo;
import com.inhim.pj.dowloadvedio.domain.MyBusinessInfoDid;
import com.inhim.pj.dowloadvedio.util.FileUtil;
import com.inhim.pj.http.Urls;
import com.inhim.pj.utils.GlideUtils;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.lang.ref.SoftReference;
import java.sql.SQLException;
import java.util.List;


/**
 * Created by renpingqing on 17/1/19.
 */
public class DownloadListAdapter extends
        BaseRecyclerViewAdapter<MyBusinessInfo, DownloadListAdapter.ViewHolder> {

    private static final String TAG = "DownloadListAdapter";
    private final Context context;
    private final DownloadManager downloadManager;
    private DBController dbController;
    private boolean isCheck, isAll;

    public DownloadListAdapter(Context context) {
        super(context);
        this.context = context;
        downloadManager = DownloadService.getDownloadManager(context.getApplicationContext());
        try {
            dbController = DBController.getInstance(context.getApplicationContext());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setCheck(boolean isCheck, boolean isAll) {
        this.isCheck = isCheck;
        this.isAll = isAll;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(
                R.layout.item_download_info, parent, false));
    }

    @Override
    public void onBindViewHolder(DownloadListAdapter.ViewHolder holder, final int position) {
        holder.bindData(getData(position), position, context);

        holder.itemView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(position);
                }
            }
        });
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        //private final ImageView iv_icon;
        private final TextView tv_size;
        private final TextView tv_status;
        private final ProgressBar pb;
        private final TextView tv_name;
        private DownloadInfo downloadInfo;
        private LinearLayout rl_item;
        private ImageView imageview1, imageview2;
        private CheckBox checkbox;

        public ViewHolder(View view) {
            super(view);
            itemView.setClickable(true);
            tv_size = view.findViewById(R.id.tv_size);
            tv_status = view.findViewById(R.id.tv_status);
            pb = view.findViewById(R.id.pb);
            tv_name = view.findViewById(R.id.tv_name);
            rl_item = view.findViewById(R.id.rl_item);
            imageview1 = view.findViewById(R.id.imageview1);
            imageview2 = view.findViewById(R.id.imageview2);
            checkbox = view.findViewById(R.id.checkbox);
        }

        @SuppressWarnings("unchecked")
        public void bindData(final MyBusinessInfo data, final int position, final Context context) {
            if (isCheck) {
                checkbox.setVisibility(View.VISIBLE);
            } else {
                checkbox.setVisibility(View.GONE);
            }
            if (isAll) {
                checkbox.setChecked(true);
            } else {
                checkbox.setChecked(false);
            }
            tv_name.setText(data.getTitle());
            GlideUtils.displayFromUrl(data.getCover(), imageview1);
            if (data != null) {
                // Get download task status
                if (data.getUrl() == null || data.getUrl().equals("")) {
                    downloadInfo = downloadManager.getDownloadById(data.getUrl().hashCode());
                }
            }

            // Set a download listener
            if (downloadInfo != null) {
                downloadInfo
                        .setDownloadListener(new MyDownloadListener(new SoftReference(ViewHolder.this)) {
                            //  Call interval about one second
                            @Override
                            public void onRefresh() {
                                if (getUserTag() != null && getUserTag().get() != null) {
                                    ViewHolder viewHolder = (ViewHolder) getUserTag().get();
                                    viewHolder.refresh(position);
                                }
                            }
                        });

            }

            refresh(position);

     /* iv_deldete.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View view) {
          List<MyBusinessInfo> infos = DataSupport.findAll(MyBusinessInfo.class);
          if(infos.size()>0){
            try{
              DBController instance =  DBController.getInstance(context);
              for(int i=0;i<instance.findAllDownloaded().size();i++){
                instance.delete(instance.findAllDownloaded().get(i));
              }

              tv_size.setText("");
              pb.setProgress(0);
              infos.get(position).delete();
              String video= Urls.getFilePath()+data.getName();
              File file=new File(video);
              file.delete();
              setData(infos);

            }catch (Exception e){
              e.printStackTrace();
            }
          }else{
            setData(infos);
          }

        }
      });*/


//      Download button
            imageview1.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (downloadInfo != null) {

                        switch (downloadInfo.getStatus()) {
                            case DownloadInfo.STATUS_NONE:
                            case DownloadInfo.STATUS_PAUSED:
                            case DownloadInfo.STATUS_ERROR:

                                //resume downloadInfo
                                downloadManager.resume(downloadInfo);
                                break;

                            case DownloadInfo.STATUS_DOWNLOADING:
                            case DownloadInfo.STATUS_PREPARE_DOWNLOAD:

                            case DownloadInfo.STATUS_WAIT:
                                //pause downloadInfo
                                downloadManager.pause(downloadInfo);
                                break;

                        }
                    } else {
                        downloadInfo = new DownloadInfo.Builder().setUrl(data.getUrl())
                                .setPath(data.getFilePath())
                                .build();

                        downloadInfo
                                .setDownloadListener(new MyDownloadListener(new SoftReference(ViewHolder.this)) {

                                    @Override
                                    public void onRefresh() {
                                        notifyDownloadStatus();

                                        if (getUserTag() != null && getUserTag().get() != null) {
                                            ViewHolder viewHolder = (ViewHolder) getUserTag().get();
                                            viewHolder.refresh(position);
                                        }
                                    }
                                });
                        downloadManager.download(downloadInfo);

                        //save extra info to my database.
                        MyBusinessInfLocal myBusinessInfLocal;
                        myBusinessInfLocal = new MyBusinessInfLocal(
                                data.getUrl().hashCode(),
                                data.getTitle(), data.getCover(),
                                data.getUrl());

                        try {
                            dbController.createOrUpdateMyDownloadInfo(myBusinessInfLocal);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

        }

        private void notifyDownloadStatus() {
            if (downloadInfo.getStatus() == DownloadInfo.STATUS_REMOVED) {
                try {
                    dbController.deleteMyDownloadInfo(downloadInfo.getUri().hashCode());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }

        private void refresh(int position) {
            if (downloadInfo == null) {
                tv_size.setText("");
                pb.setProgress(0);
                //bt_action.setText("Download");
                tv_status.setText("点击开始");
                imageview2.setImageResource(R.mipmap.icon_download);
            } else {
                switch (downloadInfo.getStatus()) {
                    case DownloadInfo.STATUS_NONE:
                        //bt_action.setText("Download");
                        tv_status.setText(FileUtil.formatFileSize(downloadInfo.getProgress()));
                        break;
                    case DownloadInfo.STATUS_PAUSED:
                    case DownloadInfo.STATUS_ERROR:
                        // bt_action.setText("Continue");
                        tv_status.setText(FileUtil.formatFileSize(downloadInfo.getProgress()));
                        try {
                            pb.setProgress((int) (downloadInfo.getProgress() * 100.0 / downloadInfo.getSize()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        tv_size.setText(FileUtil
                                .formatFileSize(downloadInfo.getSize()));
                        imageview2.setImageResource(R.mipmap.icon_download);
                        break;

                    case DownloadInfo.STATUS_DOWNLOADING:
                    case DownloadInfo.STATUS_PREPARE_DOWNLOAD:
                        //bt_action.setText("Pause");
                        try {
                            pb.setProgress((int) (downloadInfo.getProgress() * 100.0 / downloadInfo.getSize()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        tv_size.setText(FileUtil
                                .formatFileSize(downloadInfo.getSize()));
                        tv_status.setText(FileUtil.formatFileSize(downloadInfo.getProgress()));
                        imageview2.setImageResource(R.mipmap.icon_puse);
                        break;
                    case DownloadInfo.STATUS_COMPLETED:
                        imageview2.setImageResource(R.mipmap.icon_puse);
                        try {
                            pb.setProgress((int) (downloadInfo.getProgress() * 100.0 / downloadInfo.getSize()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        List<MyBusinessInfo> infos = DataSupport.findAll(MyBusinessInfo.class);
                        if (infos.size() > 0) {
                            try {
                                MyBusinessInfoDid infosDid = new MyBusinessInfoDid();
                                infosDid.setContent(infos.get(position).getContent());
                                infosDid.setCover(infos.get(position).getCover());
                                infosDid.setFilePath(infos.get(position).getFilePath());
                                infosDid.setProgress(infos.get(position).getProgress());
                                infosDid.setReaderId(infos.get(position).getReaderId());
                                infosDid.setReaderTypeId(infos.get(position).getReaderTypeId());
                                infosDid.setTitle(infos.get(position).getTitle());
                                infosDid.setUrl(infos.get(position).getUrl());
                                infosDid.setSynopsis(infos.get(position).getSynopsis());
                                infosDid.save();
                                infos.get(position).delete();
                                setData(infos);
                                Intent intent = new Intent();
                                intent.setAction("the.search.data.dowload");
                                context.sendBroadcast(intent);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    case DownloadInfo.STATUS_REMOVED:
                        tv_size.setText(FileUtil
                                .formatFileSize(downloadInfo.getSize()));
                        pb.setProgress(0);
                        tv_status.setText("0KB");
                        imageview2.setImageResource(R.mipmap.icon_download);
                    case DownloadInfo.STATUS_WAIT:
                        tv_size.setText(FileUtil
                                .formatFileSize(downloadInfo.getSize()));
                        pb.setProgress(0);
                        tv_status.setText("等待中");
                        imageview2.setImageResource(R.mipmap.icon_download);
                        break;
                }

            }
        }
    }

    public void deleteFile(List<MyBusinessInfo>  infos){
        if(infos.size()>0){
            try{
                DBController instance =  DBController.getInstance(context);
                for(int i=0;i<instance.findAllDownloaded().size();i++){
                    instance.delete(instance.findAllDownloaded().get(i));
                }
                setData(infos);

            }catch (Exception e){
                e.printStackTrace();
            }
        }else{
            setData(infos);
        }

    }
}
