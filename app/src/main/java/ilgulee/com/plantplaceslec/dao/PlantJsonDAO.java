package ilgulee.com.plantplaceslec.dao;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ilgulee.com.plantplaceslec.dto.PlantDTO;

public class PlantJsonDAO extends AsyncTask<String, Void, List<PlantDTO>> implements AsyncTaskDAO.OnDownloadListener {
    private static final String TAG = "PlantJsonDAO";
    private List<PlantDTO> mPlantDTOList = null;
    private String mBaseURL;
    private final OnDataAvailable mCallback;
    private boolean runningOnSameThread = false;

    public interface OnDataAvailable {
        void onDataAvailable(List<PlantDTO> data, DownloadStatus status);
    }

    public PlantJsonDAO(OnDataAvailable callback, String baseURL) {
        mBaseURL = baseURL;
        mCallback = callback;
    }

    @Override
    protected void onPostExecute(List<PlantDTO> plantDTOList) {
        Log.d(TAG, "onPostExecute: starts");
        if (mCallback != null) {
            mCallback.onDataAvailable(mPlantDTOList, DownloadStatus.OK);
        }
    }

    @Override
    protected List<PlantDTO> doInBackground(String... params) {
        Log.d(TAG, "doInBackground: starts");
        String destinationUri = createUri(params[0]);
        PlantRawJsonDAO plantRawJsonDAO = new PlantRawJsonDAO(this);
        plantRawJsonDAO.fetchJsonRawData(destinationUri);
        Log.d(TAG, "doInBackground: ends");
        return mPlantDTOList;
    }

    public void executeOnSameThread(String searchCriteria) {
        Log.d(TAG, "executeOnSameThread: starts");
        runningOnSameThread = true;
        String destinationUri = createUri(searchCriteria);

        PlantRawJsonDAO plantRawJsonDAO = new PlantRawJsonDAO(this);
        plantRawJsonDAO.fetchJsonRawData(destinationUri);
        Log.d(TAG, "executeOnSameThread: ends");
    }

    private String createUri(String searchCriteria) {
        Log.d(TAG, "createUri: starts");
        return Uri.parse(mBaseURL).buildUpon()
                .appendQueryParameter("Combined_Name", searchCriteria)
                .build().toString();
    }

    @Override
    public void onDownloadComplete(String data, DownloadStatus status) {
        Log.d(TAG, "onDownloadComplete: " + status);
        if (status == DownloadStatus.OK) {
            Log.d(TAG, "onDownloadComplete: data is " + data);
            mPlantDTOList = new ArrayList<>();
            try {
                JSONObject root = new JSONObject(data);
                JSONArray plantsArray = root.getJSONArray("plants");
                for (int i = 0; i < plantsArray.length(); i++) {
                    PlantDTO plant = new PlantDTO();
                    JSONObject jsonPlnatObject = plantsArray.getJSONObject(i);
                    plant.setId(jsonPlnatObject.getString("id"));
                    plant.setGenus(jsonPlnatObject.getString("genus"));
                    plant.setSpecies(jsonPlnatObject.getString("species"));
                    plant.setCultivar(jsonPlnatObject.getString("cultivar"));
                    plant.setCommon(jsonPlnatObject.getString("common"));
                    mPlantDTOList.add(plant);
                    Log.d(TAG, "onDownloadComplete: Object number " + i + " " + plant.toString());
                }
            } catch (JSONException e) {
                Log.e(TAG, "onDownloadComplete: Error processing json data" + e.getMessage());
                status = DownloadStatus.FAILED_OR_EMPTY;
            }
        } else {
            Log.e(TAG, "onDownloadComplete failed with status " + status);
        }
        if (runningOnSameThread && mCallback != null) {
            mCallback.onDataAvailable(mPlantDTOList, status);
        }
    }
}
