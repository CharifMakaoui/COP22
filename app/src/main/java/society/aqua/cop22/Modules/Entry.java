package society.aqua.cop22.Modules;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementListUnion;
import org.simpleframework.xml.Namespace;

import java.util.List;

/**
 * Created by MrCharif on 04/11/2016.
 */


public class Entry {

    public String title ;
    public String id;
    public String imgsrc;
    public String content;
    public String published;
    public Author author;

    @Override
    public String toString() {
        return "title : "+ title + "  image : "+imgsrc;
    }
}
