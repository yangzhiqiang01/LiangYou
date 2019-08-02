package com.inhim.pj.dowloadfile.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.inhim.pj.R;
import com.inhim.pj.dowloadfile.download.DownloadInfo;
import com.inhim.pj.dowloadfile.download.MyBusinessInfoDid;
import com.inhim.pj.dowloadfile.download.limit.DownloadLimitManager;
import com.inhim.pj.dowloadfile.utils.FileUtil;
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
public class LimitDownloadAdapter extends BaseRecyclerDidViewAdapter<DownloadInfo,LimitDownloadAdapter.UploadHolder>  {

    private Context mContext;
    private List<DownloadInfo> mdata;
    private boolean isCheck, isAll;
    public LimitDownloadAdapter(Context context , List<DownloadInfo> mdata) {
        super(context);
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
                try{
                    File file = new File(getDownloadListData().get(i).getFilePath());
                    if(file.exists()){
                        file.delete();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                try{
                    DownloadLimitManager.getInstance().cancelDownload(mdata.get(i));
                    mdata.get(i).delete();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        setData(getDownloadListData());
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
        if(info.getProgress()!=0&&info.getTotal()!=0){
            float d = (float) info.getProgress() * (float) holder.progressBar.getMax() / (float) info.getTotal();
            holder.progressBar.setProgress((int) d);
            DownloadInfo downloadInfo=new DownloadInfo();
            //直接更新id为1的记录
            downloadInfo.setProgress(info.getProgress());
            downloadInfo.setTotal(info.getTotal());
            downloadInfo.updateAll("url = ?", info.getUrl());
        }else{
            holder.progressBar.setProgress(0);
        }
        if (isAll) {
            //当item为选中状态时暂停下载 以免下载中不断刷新item
            DownloadLimitManager.getInstance().pauseDownload(info.getUrl());
            holder.imageview2.setImageResource(R.mipmap.icon_download);
            holder.imageview2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DownloadLimitManager.getInstance().download(info.getUrl());
                }
            });
            holder.checkbox.setChecked(true);
        } else {
            holder.checkbox.setChecked(false);
        }
        if (isCheck) {
            holder.checkbox.setVisibility(View.VISIBLE);
        } else {
            holder.checkbox.setVisibility(View.GONE);
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
        }else if(DownloadInfo.DOWNLOAD_ERROR.equals(info.getDownloadStatus())){
            DownloadLimitManager.getInstance().download(info.getUrl());
        }else if (DownloadInfo.DOWNLOAD_OVER.equals(info.getDownloadStatus())){
            /*holder.main_progress.setProgress(holder.main_progress.getMax());
            holder.main_btn_down.setText("完成");*/
            List<DownloadInfo> infos = LitePal.findAll(DownloadInfo.class);
            if (infos.size() > 0) {
                try {
                    MyBusinessInfoDid infosDid = new MyBusinessInfoDid();
                    infosDid.setContent(infos.get(position).getContent());
                    infosDid.setCover(infos.get(position).getCover());
                    infosDid.setFilePath(infos.get(position).getFilePath());
                    //infosDid.setProgress(infos.get(position).getProgress());
                    infosDid.setReaderId(infos.get(position).getReaderId());
                    infosDid.setReaderTypeId(infos.get(position).getReaderTypeId());
                    infosDid.setTitle(infos.get(position).getTitle());
                    infosDid.setUrl(infos.get(position).getUrl());
                    infosDid.setSynopsis(infos.get(position).getSynopsis());
                    infosDid.setType(infos.get(position).getType());
                    infosDid.setTotal(infos.get(position).getTotal());
                    infosDid.save();
                    infos.get(position).delete();
                    infos.remove(position);
                    Intent intent = new Intent();
                    intent.setAction("the.search.data.dowload");
                    mContext.sendBroadcast(intent);
                    setData(infos);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }else  if(DownloadInfo.DOWNLOAD.equals(info.getDownloadStatus())){
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
        }else {
            //等待中
            if (info.getTotal() == 0){
                GlideUtils.displayFromUrl(info.getCover(), holder.imageview1, mContext);
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
                GlideUtils.displayFromUrl(info.getCover(), holder.imageview1, mContext);
                holder.tv_status.setText(FileUtil.formatFileSize(info.getProgress()));
                holder.tv_size.setText(FileUtil
                        .formatFileSize(info.getTotal()));
                holder.imageview2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DownloadLimitManager.getInstance().download(info.getUrl());
                    }
                });
                holder.imageview2.setImageResource(R.mipmap.icon_download);
                holder.tv_name.setText(info.getTitle());
            }

        }

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
