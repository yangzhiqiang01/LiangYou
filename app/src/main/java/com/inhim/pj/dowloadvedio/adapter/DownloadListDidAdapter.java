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
import com.inhim.pj.utils.GlideUtils;
import java.lang.ref.SoftReference;
import java.sql.SQLException;


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
            rl_item = view.findViewById(R.id.rl_item);
            imageview1 = view.findViewById(R.id.imageview1);
            imageview2 = view.findViewById(R.id.imageview2);
            checkbox = view.findViewById(R.id.checkbox);
        }

        @SuppressWarnings("unchecked")
        public void bindData(final MyBusinessInfoDid data, final int position) {
            tv_name.setText(data.getTitle());
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

   /*   iv_deldete.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View view) {
          List<MyBusinessInfoDid> infos = DataSupport.findAll(MyBusinessInfoDid.class);
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
      });
*/
//      Download button
     /* imageview1.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          Intent intent=new Intent(context, VideoActivity.class);
          intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
          List<MyBusinessInfoDid> infos = DataSupport.findAll(MyBusinessInfoDid.class);
          if(infos.size()>0){
            try{
              intent.putExtra("result",infos.get(position));
              context.startActivity(intent);
            }catch (Exception e){
              e.printStackTrace();
            }
          }

        }
      });*/

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
                tv_status.setText(" 已完成");

            }
        }
    }
}
