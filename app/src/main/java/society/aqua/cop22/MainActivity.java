package society.aqua.cop22;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import society.aqua.cop22.Adapters.ArticlesAdapter;
import society.aqua.cop22.Modules.Author;
import society.aqua.cop22.Modules.Entry;
import society.aqua.cop22.Modules.TagValue;
import society.aqua.cop22.Utils.KEYS;
import society.aqua.cop22.Utils.XMLRequest;

public class MainActivity extends AppCompatActivity {

    private String LOG_TAG = "Home_Page";
    private String URL_PAGE = "xml/atom.xml";
    private List<Entry> entryList = new ArrayList<>();

    private Toolbar toolbar;
    private RecyclerView recyclerView_articles;
    ArticlesAdapter adapter_articles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String URL = KEYS.BASE_URL + URL_PAGE;
        RequestGet(URL);

        toolbar = (Toolbar) findViewById(R.id.app_bare);
        setSupportActionBar(toolbar);

        recyclerView_articles = (RecyclerView) findViewById(R.id.list_articles);
        recyclerView_articles.setLayoutManager(new LinearLayoutManager(this));
        adapter_articles = new ArticlesAdapter(new ArrayList<Entry>(),this);
        recyclerView_articles.setAdapter(adapter_articles);
    }

    public void RequestGet(String URL){
        XMLRequest xmlRequest = new XMLRequest(
                URL,
                new Response.Listener<XmlPullParser>() {
                    @Override
                    public void onResponse(XmlPullParser response) {
                        List<TagValue> entry = new ArrayList<>();
                        boolean startNewEntry = false;
                        try {
                            int eventType = response.getEventType();
                            String SelectedTag = "";
                            while (eventType != XmlPullParser.END_DOCUMENT) {

                                if (eventType == XmlPullParser.START_TAG) {
                                    String nodeName = response.getName();
                                    if("entry".equals(nodeName)){
                                        startNewEntry = true;
                                    }
                                    if(startNewEntry){
                                        SelectedTag = nodeName;
                                    }
                                }
                                else if (eventType == XmlPullParser.END_TAG) {
                                    String nodeName = response.getName();
                                    if("entry".equals(nodeName)){
                                        startNewEntry = false;
                                        SerializeEntry(entry);
                                        entry = null;
                                        entry = new ArrayList<>();
                                    }
                                }

                                else if (eventType == XmlPullParser.TEXT) {
                                    String nodeText = response.getText();
                                    if(startNewEntry){
                                        TagValue tagValue = new TagValue();
                                        tagValue.Tag = SelectedTag;
                                        tagValue.value = nodeText;
                                        entry.add(tagValue);
                                    }
                                }
                                eventType = response.next();
                            }

                            Log.d(LOG_TAG,"Articles size : "+ entryList.size());
                            adapter_articles.update(entryList);

                        } catch (XmlPullParserException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(LOG_TAG, error.getMessage(), error);
            }
        });

        Volley.newRequestQueue(this).add(xmlRequest);
    }

    public void SerializeEntry(List<TagValue> entry){
        Entry entry_Ser = new Entry();
        for(int i = 0; i< entry.size(); i++){
            Log.d(LOG_TAG, "TAG NAME : "+entry.get(i).Tag);
            switch (entry.get(i).Tag){
                case "title" :
                    if(!entry.get(i).value.equals("") && entry.get(i).value != null && entry.get(i).value.length() > 4){
                        Log.d(LOG_TAG, "title : "+ entry.get(i).value.length() +"  Data : " + entry.get(i).value);
                        entry_Ser.title = entry.get(i).value;
                    } break;
                case "id" :
                    if(!entry.get(i).value.equals("") && entry.get(i).value != null && entry.get(i).value.length() > 4){
                        Log.d(LOG_TAG, "id : "+ entry.get(i).value.length() +"  Data : " + entry.get(i).value);
                        entry_Ser.id = entry.get(i).value;
                    } break;
                case "photo:imgsrc" :
                    if(!entry.get(i).value.equals("") && entry.get(i).value != null && entry.get(i).value.length() > 4){
                        Log.d(LOG_TAG, "imgsrc : "+ entry.get(i).value.length() +"  Data : " + entry.get(i).value);
                        String imageDefault = entry.get(i).value.replace("imagette", "default");
                        entry_Ser.imgsrc = imageDefault;
                    } break;
                case "published" :
                    if(!entry.get(i).value.equals("") && entry.get(i).value != null && entry.get(i).value.length() > 4){
                        Log.d(LOG_TAG, "published : "+ entry.get(i).value.length() +"  Data : " + entry.get(i).value);
                        entry_Ser.published = entry.get(i).value;
                    } break;
                case "name" :
                    if(!entry.get(i).value.equals("") && entry.get(i).value != null && entry.get(i).value.length() > 4){
                        Log.d(LOG_TAG, "author name : "+ entry.get(i).value.length() +"  Data : " + entry.get(i).value);;
                        Author author = new Author();
                        author.name = entry.get(i).value;
                        entry_Ser.author = author;
                    } break;
                case "content" :
                    if(!entry.get(i).value.equals("") && entry.get(i).value != null && entry.get(i).value.length() > 4){
                        Log.d(LOG_TAG, "content length : "+ entry.get(i).value.length() +"  Data : " + entry.get(i).value);
                        entry_Ser.content = entry.get(i).value;
                        Log.d(LOG_TAG, "*************************** Start New Entry object **********************");
                    } break;
            }
        }
        entryList.add(entry_Ser);
    }
}
