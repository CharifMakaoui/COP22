package society.aqua.cop22.Services;

import retrofit2.Call;
import retrofit2.http.GET;
import society.aqua.cop22.Modules.Feed;

/**
 * Created by MrCharif on 04/11/2016.
 */

public interface Articles {

    @GET("xml/atom.xml")
    Call<Feed> getArticles();
}
