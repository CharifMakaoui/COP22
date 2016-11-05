package society.aqua.cop22.Modules;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementListUnion;
import org.simpleframework.xml.Namespace;

import java.util.List;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by MrCharif on 04/11/2016.
 */


public class Entry extends RealmObject {
    @PrimaryKey
    public String id;

    public String title ;
    public String imgsrc;
    public String content;
    public String published;
    public Author author;
    public boolean isFav;

    @Override
    public String toString() {
        return "title : "+ title + "  image : "+imgsrc;
    }
}
