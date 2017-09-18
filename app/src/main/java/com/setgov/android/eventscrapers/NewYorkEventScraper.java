package com.setgov.android.eventscrapers;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.setgov.android.models.City;
import com.setgov.android.models.Event;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Ross on 6/23/2017.
 */

public class NewYorkEventScraper extends AsyncTask<Context,Void,ArrayList<Event>> {

    private Context mContext;
    public NewYorkEventScraper(Context context) {
        mContext = context;
    }

    @Override
    protected ArrayList<Event> doInBackground(Context... params) {
        ArrayList<Event> mEvents = new ArrayList();
        ArrayList<String> eventNames = new ArrayList<>();
        ArrayList<String> eventDate = new ArrayList<>();
        ArrayList<String> eventTime = new ArrayList<>();
        ArrayList<String> eventLocation = new ArrayList<>();
        City mCity= new City("New York","New York","New York","NY");

        try {
            InputStream inputStream = mContext.getResources().getAssets().open("newyorkdata.xls");
            //File file = new File("src/main/assets/fl_data.xls");
            POIFSFileSystem fs = new POIFSFileSystem(inputStream);
            HSSFWorkbook wb = new HSSFWorkbook(fs);
            HSSFSheet sheet = wb.getSheetAt(0);
            HSSFRow row;
            HSSFCell cell;

            int rows; // No of rows
            rows = sheet.getPhysicalNumberOfRows();

            int cols = 0; // No of columns
            int tmp = 0;

            // This trick ensures that we get the data properly even if it doesn't start from first few rows
            for(int i = 0; i < 10 || i < rows; i++) {
                row = sheet.getRow(i);
                if(row != null) {
                    tmp = sheet.getRow(i).getPhysicalNumberOfCells();
                    if(tmp > cols) cols = tmp;
                }
            }

            for(int r = 0; r < rows; r++) {
                row = sheet.getRow(r);
                if(row != null) {
                    for(int c = 0; c < cols; c++) {
                        cell = row.getCell((short)c);
                        if(cell != null) {
                            // Your code here
                            if(r>0){
                                if(c==0){
                                    Log.d("EXCEL0",cell.toString());
                                    eventNames.add(cell.toString());
                                } else if(c ==1) {
                                    Log.d("EXCEL1",cell.toString());
                                    eventDate.add(cell.toString());
                                } else if(c ==2) {
                                    Log.d("EXCEL2",cell.toString());
                                    eventTime.add(cell.toString());
                                } else if(c ==3) {
                                    Log.d("EXCEL3",cell.toString());
                                    eventLocation.add(cell.toString());
                                }
                            }
                        }
                    }
                }
            }
            for(int i = 0; i < eventNames.size();i++){
                String eventName = eventNames.get(i);
                String eventType = eventName.substring(eventName.lastIndexOf(" ")+1);
                Event event = new Event(eventName,eventType,mCity,eventDate.get(i)+" "+ eventTime.get(i),eventLocation.get(i));
                mEvents.add(event);
            }


        } catch(Exception ioe) {
            ioe.printStackTrace();
        }
        return mEvents;
    }
}
