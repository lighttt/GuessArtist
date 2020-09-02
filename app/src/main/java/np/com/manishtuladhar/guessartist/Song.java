package np.com.manishtuladhar.guessartist;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

public class Song {
    private  int mSongId;
    private String mArtist;
    private String mTitle;
    private String mUri;
    private String mAlbumArtId;

    public Song(int mSongId, String mArtist, String mTitle, String mUri, String mAlbumArtId) {
        this.mSongId = mSongId;
        this.mArtist = mArtist;
        this.mTitle = mTitle;
        this.mUri = mUri;
        this.mAlbumArtId = mAlbumArtId;
    }


    // =========================== ALBUM ART FUNCTIONS  ============================

    public static int getAlbumArtBySongId(Context context,int songId)
    {
        Song song = Song.getSongById(context,songId);
        int albumArtId = context.getResources().getIdentifier(
                song !=null ? song.getAlbumArtId() : null,
                "drawable",
                context.getPackageName()
        );
//        return BitmapFactory.decodeResource(context.getResources(),albumArtId);
        return albumArtId;
    }

    // =========================== SONG FUNCTIONS  ============================

    /**
     * Get the details of the song by id
     */
    public static Song getSongById(Context context, int songId)
    {
        JsonReader reader;
        ArrayList<Integer> songIds = new ArrayList<>();
        try{
          reader = readJsonFile(context);
          reader.beginArray();
          while (reader.hasNext())
          {
              Song currentSong = readEntry(reader);
              if(currentSong.getSongId() == songId)
              {
                  reader.close();
                  return currentSong;
              }
          }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }/**/


    /**
     * Retrieves the ids of all the songs
     */
    public static ArrayList<Integer> getAllSongIds(Context context)
    {
        JsonReader reader;
        ArrayList<Integer> songIds = new ArrayList<>();
        try{
            reader = readJsonFile(context);
            reader.beginArray();
            while (reader.hasNext())
            {
                songIds.add(readEntry(reader).getSongId());
            }
            reader.close();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        return songIds;
    }

    // =========================== JSON FUNCTIONS   ============================

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
    private static Song readEntry(JsonReader reader)
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
        return new Song(id,artist,title,uri,albumArtID);
    }



    // =========================== GETTER AND SETTER   ============================
    public int getSongId() {
        return mSongId;
    }

    public void setSongId(int mSongId) {
        this.mSongId = mSongId;
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
