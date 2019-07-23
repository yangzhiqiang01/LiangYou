package com.inhim.pj.dowloadvedio;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inhim.downloader.DownloadService;
import com.inhim.downloader.config.Config;
import com.inhim.pj.R;
import com.inhim.pj.dowloadvedio.adapter.BaseRecyclerViewAdapter;
import com.inhim.pj.dowloadvedio.adapter.DownloadListAdapter;
import com.inhim.pj.dowloadvedio.domain.MyBusinessInfo;
import com.inhim.pj.dowloadvedio.dummy.DummyContent;
import com.inhim.pj.http.Urls;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class HaveDownloadedFragment extends Fragment implements BaseRecyclerViewAdapter.OnItemClickListener {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private static final int REQUEST_DOWNLOAD_DETAIL_PAGE = 100;

    private RecyclerView recyclerView;
    private DownloadListAdapter downloadListAdapter;
    private SrearchReceiver srearchreceiver;
    private DeleteReceiver deleteReceiver;
    private TextView tvFile;
    private boolean isCheck;
    private LinearLayout lin_caozuo;
    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static HaveDownloadedFragment newInstance(int columnCount) {
        HaveDownloadedFragment fragment = new HaveDownloadedFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    public void initListener() {
        downloadListAdapter.setOnItemClickListener(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //实例化IntentFilter对象
        IntentFilter filter=new IntentFilter();
        filter.addAction("the.search.data");
        //注册广播接收
        srearchreceiver=new SrearchReceiver();
        if(getActivity()!=null){
            getActivity().registerReceiver(srearchreceiver,filter);
        }
        IntentFilter deleteFilter=new IntentFilter();
        deleteFilter.addAction("one.fragment.delete");
        //注册广播接收
        deleteReceiver=new DeleteReceiver();
        if(getActivity()!=null){
            getActivity().registerReceiver(deleteReceiver,deleteFilter);
        }
    }

    class DeleteReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(!isCheck){
                isCheck=true;
                downloadListAdapter.setCheck(isCheck,false);
                downloadListAdapter.notifyDataSetChanged();
                lin_caozuo.setVisibility(View.VISIBLE);
            }else{
                isCheck=false;
                downloadListAdapter.setCheck(isCheck,false);
                downloadListAdapter.notifyDataSetChanged();
                lin_caozuo.setVisibility(View.GONE);
            }
        }
    }
    class SrearchReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            initData();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(getActivity()!=null){
            getActivity().unregisterReceiver(srearchreceiver);
            getActivity().unregisterReceiver(deleteReceiver);
        }
    }

    public void initData() {

        try {
            Config config = new Config();
            config.setDownloadThread(3);
            config.setEachDownloadThread(2);
            config.setConnectTimeout(10000);
            config.setReadTimeout(10000);
            DownloadService.getDownloadManager(getActivity().getApplicationContext(), config);
        } catch (Exception e) {
            e.printStackTrace();
        }

        downloadListAdapter = new DownloadListAdapter(getActivity());
        downloadListAdapter.setData(getDownloadListData());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(downloadListAdapter);


    }

    private List<MyBusinessInfo> getDownloadListData() {
        List<MyBusinessInfo> myBusinessInfos= DataSupport.findAll(MyBusinessInfo.class);
        if(myBusinessInfos.size()>0){
        }
        return myBusinessInfos;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        recyclerView = view.findViewById(R.id.rv);
        lin_caozuo=view.findViewById(R.id.lin_caozuo);
        tvFile=view.findViewById(R.id.tvFile);
        tvFile.setText("存储路径:"+ Urls.getFilePath());
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new MyItemRecyclerViewAdapter(DummyContent.ITEMS, mListener));
        }
        initData();
        //initListener();
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        downloadListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(int position) {
        MyBusinessInfo data = downloadListAdapter.getData(position);
        Intent intent = new Intent(getActivity(), DownloadDetailActivity.class);
        intent.putExtra(DownloadDetailActivity.DATA, data);
        startActivityForResult(intent, REQUEST_DOWNLOAD_DETAIL_PAGE);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(DummyContent.DummyItem item);
    }
}
