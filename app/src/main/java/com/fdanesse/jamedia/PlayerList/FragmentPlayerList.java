package com.fdanesse.jamedia.PlayerList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fdanesse.jamedia.PlayerActivity;
import com.fdanesse.jamedia.R;
//import com.fdanesse.jamedia.Utils;

import java.util.ArrayList;


public class FragmentPlayerList extends Fragment {

    private ArrayList<ListItem> lista;
    private RecyclerView recyclerView;
    private ItemListAdapter listAdapter;

    public FragmentPlayerList() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_player_list, container, false);
        recyclerView = (RecyclerView) layout.findViewById(R.id.reciclerview);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        Bundle bundle = getArguments();
        lista = (ArrayList<ListItem>) bundle.getSerializable("tracks");

        listAdapter = new ItemListAdapter(lista, this);
        recyclerView.setAdapter(listAdapter);
        return layout;
    }

    protected void playtrack(int index, View view){
        //FIXME: Solucionar animacion
        if (view.getAlpha() == 0.5f){
            //Utils.setActiveView(view);
            PlayerActivity.playtrack(index);
        }
        else{
            //Utils.setInactiveView(view);
            PlayerActivity.stop();
        }
    }
}