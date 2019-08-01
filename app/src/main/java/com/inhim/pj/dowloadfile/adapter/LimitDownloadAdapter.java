package com.inhim.pj.dowloadfile.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.inhim.pj.R;
import com.inhim.pj.dowloadfile.download.DownloadInfo;
import com.inhim.pj.dowloadfile.download.limit.DownloadLimitManager;
import com.inhim.pj.dowloadvedio.util.FileUtil;
import com.inhim.pj.utils.GlideUtils;

import org.litepal.LitePal;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Created by zs
 * Date：2018年 09月 11日
 * Time：18:06
 * —————————————————————————————————————
 * About:
 * —————————————————————————————————————
 */
public class LimitDownloadAdapter extends RecyclerView.Adapter<LimitDownloadAdapter.UploadHolder>  {

    private Context mContext;
    private List<DownloadInfo> mdata;
    private boolean isCheck, isAll;
    public LimitDownloadAdapter(Context context , List<DownloadInfo> mdata) {
        this.mContext = context;
        this.mdata = mdata;
    }

    /**
     * 更新下载进度
     * @param info
     */
    public void updateProgress(DownloadInfo info){
        for (int i = 0; i < mdata.size(); i++){
            if (mdata.get(i).getUrl().equals(info.getUrl())){
                mdata.set(i,info);
                notifyItemChanged(i);
                break;
            }
        }
    }
    public void deleteFiles(Map<Integer, Boolean> deleteMap){
        for(int i=0;i<deleteMap.size();i++){
            if (deleteMap.get(i)) {
                File file = new File(mdata.get(i).getFilePath());
                if(file.exists()){
                    file.delete();
                }
                mdata.get(i).delete();
            }
        }
        notifyDataSetChanged();
    }

    private List<DownloadInfo> getDownloadListData() {
        List<DownloadInfo> myBusinessInfos = LitePal.findAll(DownloadInfo.class);
        if (myBusinessInfos.size() > 0) {
        }
        return myBusinessInfos;
    }
    public void setCheck(boolean isCheck, boolean isAll) {
        this.isCheck = isCheck;
        this.isAll = isAll;
    }
    @Override
    public UploadHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_download_info,null);
        return new UploadHolder(view);
    }

    @Override
    public void onBindViewHolder(UploadHolder holder, int position) {
        final DownloadInfo info = mdata.get(position);
        GlideUtils.displayFromUrl(info.getCover(), holder.imageview1, mContext);
        if(info.getProgress()!=0&&info.getTotal()!=0){
            float d = (float) info.getProgress() * (float) holder.progressBar.getMax() / (float) info.getTotal();
            long progress=info.getProgress();
            long Max=holder.progressBar.getMax();
            long total=info.getTotal();
            holder.progressBar.setProgress((int) d);
            DownloadInfo downloadInfo=new DownloadInfo();
            //直接更新id为1的记录
            downloadInfo.setProgress((long) d);
            downloadInfo.setTotal(info.getTotal());
            downloadInfo.update(position+1);
        }else{
            holder.progressBar.setProgress(0);
        }
        if (isCheck) {
            holder.checkbox.setVisibility(View.VISIBLE);
        } else {
            holder.checkbox.setVisibility(View.GONE);
        }
        if (isAll) {
            holder.checkbox.setChecked(true);
        } else {
            holder.checkbox.setChecked(false);
        }
        if (DownloadLimitManager.getInstance().getWaitUrl(info.getUrl())){
            holder.imageview2.setImageResource(R.mipmap.icon_download);
            holder.imageview2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DownloadLimitManager.getInstance().download(info.getUrl());
                }
            });
        }else if (DownloadInfo.DOWNLOAD_CANCEL.equals(info.getDownloadStatus())){
            holder.imageview2.setImageResource(R.mipmap.icon_download);
            holder.imageview2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DownloadLimitManager.getInstance().download(info.getUrl());
                }
            });
        }else if (DownloadInfo.DOWNLOAD_PAUSE.equals(info.getDownloadStatus())){
            holder.imageview2.setImageResource(R.mipmap.icon_download);
            holder.imageview2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DownloadLimitManager.getInstance().download(info.getUrl());
                }
            });
        }else if (DownloadInfo.DOWNLOAD_OVER.equals(info.getDownloadStatus())){
            /*holder.main_progress.setProgress(holder.main_progress.getMax());
            holder.main_btn_down.setText("完成");*/
        }else {
            if (info.getTotal() == 0){
                holder.tv_status.setText(FileUtil.formatFileSize(info.getProgress()));
                holder.tv_size.setText(FileUtil
                        .formatFileSize(info.getTotal()));
                holder.imageview2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DownloadLimitManager.getInstance().download(info.getUrl());
                    }
                });
                holder.tv_name.setText(info.getTitle());
            }else {
                holder.tv_status.setText(FileUtil.formatFileSize(info.getProgress()));
                holder.tv_size.setText(FileUtil
                        .formatFileSize(info.getTotal()));
                holder.imageview2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DownloadLimitManager.getInstance().pauseDownload(info.getUrl());
                    }
                });
                holder.imageview2.setImageResource(R.mipmap.icon_puse);
            }

        }

        /*holder.main_btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DownloadLimitManager.getInstance().cancelDownload(info);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public class UploadHolder extends RecyclerView.ViewHolder{

        private TextView tv_size;
        private TextView tv_status;
        private ProgressBar progressBar;
        private TextView tv_name;
        private ImageView imageview1, imageview2;
        private CheckBox checkbox;

        public UploadHolder(View view) {
            super(view);
            itemView.setClickable(true);
            tv_size = view.findViewById(R.id.tv_size);
            tv_status = view.findViewById(R.id.tv_status);
            progressBar = view.findViewById(R.id.pb);
            tv_name = view.findViewById(R.id.tv_name);
            imageview1 = view.findViewById(R.id.imageview1);
            imageview2 = view.findViewById(R.id.imageview2);
            checkbox = view.findViewById(R.id.checkbox);
        }
    }

}
