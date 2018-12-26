package ilgulee.com.plantplaceslec.dao;

public class PlantRawJsonDAO implements RawJsonDAO {
    private AsyncTaskDAO.OnDownloadListener mListener;

    public PlantRawJsonDAO(AsyncTaskDAO.OnDownloadListener listener) {
        mListener = listener;
    }

    @Override
    public String fetchJsonRawData(String url) {
        AsyncTaskDAO asyncTaskDAO=new AsyncTaskDAO(mListener);
        //asyncTaskDAO.execute(url);
        asyncTaskDAO.runInSameThread(url);
        return null;
    }
}
