package com.example.menukeyincab;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class MainActivity extends Activity implements MultiChoiceModeListener, OnItemLongClickListener {

    ActionMode mActionMode;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
        // use of overflow menu on devices with menu button
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if(menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception ex) {
            // Ignore
        }
        
        ListView listview = (ListView)findViewById(R.id.list);
        
     // create the grid item mapping
        String[] from = new String[] {"rowid", "col_1", "col_2", "col_3"};
        int[] to = new int[] { R.id.item1, R.id.item2, R.id.item3, R.id.item4 };
 
        // prepare the list of all records
        List<HashMap<String, String>> fillMaps = new ArrayList<HashMap<String, String>>();
        for(int i = 0; i < 10; i++){
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("rowid", "" + i);
            map.put("col_1", "col_1_item_" + i);
            map.put("col_2", "col_2_item_" + i);
            map.put("col_3", "col_3_item_" + i);
            fillMaps.add(map);
        }
 
        // fill in the grid_item layout
        SimpleAdapter adapter = new SimpleAdapter(this, fillMaps, R.layout.list_item, from, to);
        listview.setAdapter(adapter);
        
        
        listview.setMultiChoiceModeListener(this);
        listview.setOnItemLongClickListener(this);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_menu, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onCreateActionMode(ActionMode mode, Menu menu) {
		
		MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.action_context_menu, menu);
        return true;		
	}

	@Override
	public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
		
		return false;
	}

	@Override
	public void onDestroyActionMode(ActionMode mode) {
		mActionMode = null;		
	}

	@Override
	public void onItemCheckedStateChanged(ActionMode mode, int position,
			long id, boolean checked) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {

		if (mActionMode != null) {
            return false;
        }

        // Start the CAB using the ActionMode.Callback defined above
        mActionMode = startActionMode(this);
        view.setSelected(true);
        
		return false;
	}
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		
		if(event.getAction() == KeyEvent.ACTION_UP)
		{
			if(event.getKeyCode() == KeyEvent.KEYCODE_MENU)
			{
				if(mActionMode !=null)
				{				
					View contextBarView = getActionContextBarView();
					//View actionBarView = getActionBarView();
					//if(contextBarView.getParent() == actionBarView)
					{						
						ViewGroup group = (ViewGroup) ((ViewGroup)contextBarView).getChildAt(1);
						if(group.getChildCount() == 1) 
						{
							//  R.menu.action_context_menu (android:showAsAction="never")
							View overflowMenuButton = group.getChildAt(0);
							if(null != overflowMenuButton)
								overflowMenuButton.performClick();
						}
						/*/
						else if(group.getChildCount()==2)
						{
							ImageButton btn = (ImageButton) ((ViewGroup)group).getChildAt(1);
							if(null != btn)
								btn.performClick();
							//btn.getContentDescription() = More options
						}
						//*/
					}
					
				}
			}
		}		
		return super.dispatchKeyEvent(event);
	}
	
	@Deprecated
	public View getActionBarView() {
	    Window window = getWindow();
	    View v = window.getDecorView();
	    int resId = getResources().getIdentifier("action_bar_container", "id", "android");
	    return v.findViewById(resId);
	}
	
	public View getActionContextBarView()
	{
	    Window window = getWindow();
	    View v = window.getDecorView();
	    int resId = getResources().getIdentifier("action_context_bar", "id", "android");
	    return v.findViewById(resId);		
	}
}
