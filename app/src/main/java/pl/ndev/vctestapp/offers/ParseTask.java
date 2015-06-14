package pl.ndev.vctestapp.offers;

import android.os.AsyncTask;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by l1em1on1 on 14/05/14.
 */
public class ParseTask extends AsyncTask<String, Void, List<Item>>
{
    public interface TaskCallback
    {
        public void onTaskFinished(Type taskType, List<Item> result);
    }

    private final TaskCallback callback;
    private ObjectMapper mapper;

    public ParseTask(TaskCallback callback) {
        this.callback = callback;
        this.mapper = new ObjectMapper();
    }

    @Override
    protected List<Item> doInBackground(String... offersJson) {
        if (offersJson.length == 0) {
            return null;
        }

        String json = offersJson[0];

        try {
            Container.ITEMS = mapper.readValue(json, new TypeReference<List<Item>>(){});
        } catch (IOException e) {
            Container.ITEMS.clear();
        }

        return Container.ITEMS;
    }

    @Override
    protected void onPostExecute(List<Item> result) {
        callback.onTaskFinished(ParseTask.class, result);
    }
}
