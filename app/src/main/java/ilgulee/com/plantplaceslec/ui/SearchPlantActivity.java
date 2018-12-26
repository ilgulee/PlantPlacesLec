package ilgulee.com.plantplaceslec.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import ilgulee.com.plantplaceslec.R;
import ilgulee.com.plantplaceslec.dao.DownloadStatus;
import ilgulee.com.plantplaceslec.dao.PlantJsonDAO;
import ilgulee.com.plantplaceslec.dto.PlantDTO;

public class SearchPlantActivity extends AppCompatActivity implements View.OnClickListener,PlantJsonDAO.OnDataAvailable{
    private static final String TAG = "SearchPlantActivity";

    private Button mSearchButton;
    private AutoCompleteTextView mAutoCompleteTextView;
    private ListView mListView;
    private PlantJsonDAO mPlantJsonDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);

        mSearchButton = findViewById(R.id.btnSearch);
        mAutoCompleteTextView = findViewById(R.id.actFilterText);
        mListView=findViewById(R.id.listView);
        mSearchButton.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: starts");
        super.onResume();
        mPlantJsonDAO=new PlantJsonDAO(this, "http://plantplaces.com/perl/mobile/viewplantsjson.pl");
        Log.d(TAG, "onResume: ends");
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==mSearchButton.getId()){
            String filter = mAutoCompleteTextView.getText().toString();
            if (filter.equals("")) {
                displayInputError();
            } else {
                //mPlantJsonDAO.executeOnSameThread(filter);
                mPlantJsonDAO.execute(filter);
            }
        }

    }

    public void displayInputError() {
        Toast.makeText(this, "There is no filter input to search", Toast.LENGTH_SHORT).show();
    }

    private void displaySearchResult(List<PlantDTO> plantDTOList) {
        ArrayAdapter<PlantDTO> arrayAdapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, plantDTOList);
        mListView.setAdapter(arrayAdapter);
    }

    @Override
    public void onDataAvailable(List<PlantDTO> data, DownloadStatus status) {
        if(status==DownloadStatus.OK){
            Log.d(TAG, "onDataAvailable: data "+data);
            displaySearchResult(data);
        }else{
            Log.e(TAG, "onDataAvailable failed with status "+status);
        }
    }
}
