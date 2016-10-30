package it.furtmeier.personalKartei;

import java.io.File;

import android.content.SharedPreferences;
import android.util.Log;

public class DateDifference  { 
	
	final String TAG = "States";
	private PersonalKartei personalKartei;
	SharedPreferences erasePref;	

	
     public void main() {  
    	 DateDifference massDelOldFiles = new DateDifference();  
    	 erasePref = personalKartei.getSharedPreferences("Photoeraser",personalKartei.MODE_PRIVATE);
         massDelOldFiles.deleteFilesOlderThanNdays(erasePref.getInt("Photoeraser",3),CameraActivity.directory.getPath());  
     }  
  
     public void deleteFilesOlderThanNdays(int daysBack, String dirWay)  
     {  
         File directory = new File(dirWay);  
         if(directory.exists())  
         {     
             File[] listFiles = directory.listFiles();  
             long purgeTime = System.currentTimeMillis() - ((long)daysBack * 24 * 60 * 60 * 1000);  
             for(File listFile : listFiles)  
             {  
                 if(listFile.lastModified() > purgeTime)  
                 {  
                     System.out.println("File or directory name: " + listFile);  
                     if (listFile.isFile())  
                     {  
                    	 // listFile.delete();  
                         System.out.println("This is a file: " + listFile);  
                         System.out.println("listFile.lastModified()" + listFile.lastModified());  
                         System.out.println("purgeTime: " + purgeTime);  
                     }  
                     else  
                     {  
                         System.out.println("This is a directory: " + listFile);  
                         System.out.println("Date last modified: " + listFile.lastModified());  
                         deleteFilesOlderThanNdays(daysBack, listFile.getAbsolutePath());  
                     }  
                 }  
             }  
         }   
         else 
        	  Log.d(TAG, "Files were not deleted, directory " + dirWay + " does'nt exist!");
       }  
 }  
