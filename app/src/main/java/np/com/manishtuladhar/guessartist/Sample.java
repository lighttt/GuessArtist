package np.com.manishtuladhar.guessartist;

import android.content.Context;
import android.content.res.AssetManager;
import android.net.Uri;
import android.util.JsonReader;
import android.widget.Toast;

import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSourceInputStream;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.DefaultDataSource;
import com.google.android.exoplayer2.util.Util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Sample {
    private  int mSampleId;
    private String mArtist;
    private String mTitle;
    private String mUri;
    private String mAlbumArtId;

    public Sample(int mSampleId, String mArtist, String mTitle, String mUri, String mAlbumArtId) {
        this.mSampleId = mSampleId;
        this.mArtist = mArtist;
        this.mTitle = mTitle;
        this.mUri = mUri;
        this.mAlbumArtId = mAlbumArtId;
    }

    /**
     * Get the details of the song by id
     */
    public static Sample getSampleById(Context context,int sampleId)
    {
        JsonReader reader;
        ArrayList<Integer> sampleIds = new ArrayList<>();
        try{
          reader = readJsonFile(context);
          reader.beginArray();
          while (reader.hasNext())
          {
              Sample currentSample = readEntry(reader);
              if(currentSample.getSampleId() == sampleId)
              {
                  reader.close();
                  return currentSample;
              }
          }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Reading the json file for sample of songs
     */
    private static JsonReader readJsonFile(Context context) throws IOException{
        AssetManager assetManager = context.getAssets();
        String uri = null;
        try{
            for(String asset : assetManager.list(""))
            {
                if(asset.endsWith(".exolist.json"))
                {
                    uri = "asset:///" + asset;
                }
            }
        }
        catch (IOException e)
        {
            Toast.makeText(context, "Loading sample list error", Toast.LENGTH_SHORT).show();
        }

        //taking json document
        String userAgent = Util.getUserAgent(context,"GuessArtist");
        DataSource dataSource = new DefaultDataSource(context,userAgent,false);
        DataSpec dataSpec = new DataSpec(Uri.parse(uri));
        InputStream inputStream = new DataSourceInputStream(dataSource,dataSpec);

        JsonReader reader = null;
        try{
            reader = new JsonReader(new InputStreamReader(inputStream,"UTF-8"));
        }
        finally {
            Util.closeQuietly(dataSource);
        }
        return  reader;

    }


    /**
     * Read the json objects and create sample object for each
     */
    private static Sample readEntry(JsonReader reader)
    {
        Integer id = -1;
        String artist = null;
        String title = null;
        String uri = null;
        String albumArtID = null;

        try{
           reader.beginObject();
           while (reader.hasNext())
           {
               String name = reader.nextName();
               switch (name){
                   case "name":
                       title = reader.nextString();
                       break;
                   case "id":
                       id = reader.nextInt();
                       break;
                   case "artist":
                       artist = reader.nextString();
                       break;
                   case "uri":
                       uri = reader.nextString();
                       break;
                   case "albumArtID":
                       albumArtID = reader.nextString();
                       break;
                   default:
                       break;
               }
           }
           reader.endObject();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return new Sample(id,artist,title,uri,albumArtID);
    }


    public int getSampleId() {
        return mSampleId;
    }

    public void setSampleId(int mSampleId) {
        this.mSampleId = mSampleId;
    }

    public String getArtist() {
        return mArtist;
    }

    public void setArtist(String mArtist) {
        this.mArtist = mArtist;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getUri() {
        return mUri;
    }

    public void setUri(String mUri) {
        this.mUri = mUri;
    }

    public String getAlbumArtId() {
        return mAlbumArtId;
    }

    public void setAlbumArtId(String mAlbumArtId) {
        this.mAlbumArtId = mAlbumArtId;
    }
}
