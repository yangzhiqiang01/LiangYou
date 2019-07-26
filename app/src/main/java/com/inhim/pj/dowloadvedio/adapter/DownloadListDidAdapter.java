package com.inhim.pj.dowloadvedio.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.inhim.downloader.DownloadService;
import com.inhim.downloader.callback.DownloadManager;
import com.inhim.downloader.domain.DownloadInfo;
import com.inhim.pj.R;
import com.inhim.pj.activity.VideoActivity;
import com.inhim.pj.dowloadvedio.callback.MyDownloadListener;
import com.inhim.pj.dowloadvedio.db.DBController;
import com.inhim.pj.dowloadvedio.domain.MyBusinessInfoDid;
import com.inhim.pj.dowloadvedio.util.FileUtil;
import com.inhim.pj.http.Urls;
import com.inhim.pj.utils.GlideUtils;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.lang.ref.SoftReference;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;


/**
 * Created by renpingqing on 17/1/19.
 */
public class DownloadListDidAdapter extends
        BaseRecyclerDidViewAdapter<MyBusinessInfoDid, DownloadListDidAdapter.ViewHolder> {

    private static final String TAG = "DownloadListAdapter";
    private final Context context;
    private final DownloadManager downloadManager;
    private DBController dbController;
    private boolean isCheck,isAll;
    public DownloadListDidAdapter(Context context) {
        super(context);
        this.context = context;
        downloadManager = DownloadService.getDownloadManager(context.getApplicationContext());
        try {
            dbController = DBController.getInstance(context.getApplicationContext());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void setCheck(boolean isCheck,boolean isAll){
        this.isCheck=isCheck;
        this.isAll=isAll;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(
                R.layout.item_download_info1, parent, false));
    }

    @Override
    public void onBindViewHolder(DownloadListDidAdapter.ViewHolder holder, final int position) {
        holder.bindData(getData(position), position);

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
            tv_name = view.findViewById(R.id.tv_name);
            imageview1 = view.findViewById(R.id.imageview1);
            imageview2 = view.findViewById(R.id.imageview2);
            checkbox = view.findViewById(R.id.checkbox);
        }

        @SuppressWarnings("unchecked")
        public void bindData(final MyBusinessInfoDid data, final int position) {
            tv_name.setText(data.getTitle());
            if(data.getProgress()!=null&&!data.getProgress().equals("0")){
                tv_status.setText(data.getProgress());
            }else{
                tv_status.setText("未播放");
            }
            GlideUtils.displayFromUrl(data.getCover(), imageview1,context);
            imageview2.setImageResource(R.mipmap.icon_video);
            downloadInfo = downloadManager.getDownloadById(data.getUrl().hashCode());
            if(isCheck){
                checkbox.setVisibility(View.VISIBLE);
            }else{
                checkbox.setVisibility(View.GONE);
            }
            if(isAll){
                checkbox.setChecked(true);
            }else{
                checkbox.setChecked(false);
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
            if (downloadInfo != null) {
                tv_size.setText(FileUtil.formatFileSize(downloadInfo.getSize()));
            }
        }
    }
    public void deleteFiles(Map<Integer, Boolean> deleteMap){
        List<MyBusinessInfoDid> listInfo = getDownloadListData();
        for(int i=0;i<deleteMap.size();i++){
            if (deleteMap.get(i)) {
                File file = new File(listInfo.get(i).getFilePath());
                if(file.exists()){
                    file.delete();
                }
                listInfo.get(i).delete();
                try {
                    DBController instance =  DBController.getInstance(context);
                    List<DownloadInfo> list=instance.findAllDownloaded();
                    for(int j=0;j<list.size();j++){
                        instance.delete(instance.findAllDownloaded().get(j));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        setData(getDownloadListData());
    }
    private List<MyBusinessInfoDid> getDownloadListData() {
        List<MyBusinessInfoDid> myBusinessInfos = DataSupport.findAll(MyBusinessInfoDid.class);
        if (myBusinessInfos.size() > 0) {
        }
        return myBusinessInfos;
    }

}
