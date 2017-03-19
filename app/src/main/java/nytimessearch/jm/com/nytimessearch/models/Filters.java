package nytimessearch.jm.com.nytimessearch.models;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Jared12 on 3/19/17.
 */

public class Filters {
    Date beginDate;
    String sortOrder;
    ArrayList<String> deskValues;

    public Filters(Date searchBeginDate, String searchSortOrder, ArrayList<String> searchDeskValues) {
        beginDate = searchBeginDate;
        sortOrder = searchSortOrder;
        deskValues = searchDeskValues;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public ArrayList<String> getDeskValues() {
        return deskValues;
    }
}
