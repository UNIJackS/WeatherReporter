// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP102 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP102 - 2024T1, Assignment 5
 * Name:
 * Username:
 * ID:
 */

import ecs100.*;
import java.util.*;
import java.nio.file.*;
import java.io.*;
import java.awt.Color;

/**
 * WeatherReporter
 * Analyses weather data from files of weather-station measurements.
 *
 * The weather data files consist of a set of measurements from weather stations around
 * New Zealand at a series of date/time stamps.
 * For each date/time, the file has:
 *  A line with the date and time (four integers for day, month, year, and time)
 *   eg "24 01 2021 1900"  for 24 Jan 2021 at 19:00 
 *  A line with the number of weather-stations for that date/time 
 *  Followed by a line of data for each weather station:
 *   - name: one token, eg "Cape-Reinga"
 *   - (x, y) coordinates on the map: two numbers, eg   186 38
 *   - four numbers for temperature, dew-point, suface-pressure, and sea-level-pressure
 * Some of the data files (eg weather1-hot.txt, and weather1-cold.txt) have data for just one date/time.
 * The weather-all.txt has data for lots of times. The date/times are all in order.
 * You should look at the files before trying to complete the methods below.
 *
 * Note, the data files were extracted from MetOffice weather data from 24-26 January 2021
 */

public class WeatherReporter{

    public static final double DIAM = 10;       // The diameter of the temperature circles.    
    public static final double LEFT_TEXT = 10;  // The left of the date text
    public static final double TOP_TEXT = 50;   // The top of the date text

    /**
     * Plots the temperatures for one date/time from a file on a map of NZ
     * Asks for the name of the file and opens a Scanner
     * A good design is to call plotSnapshot, passing the Scanner as an argument.
     */
    public void plotTemperatures(){
        /*# YOUR CODE HERE */
        Scanner file = new Scanner("");
        boolean invalidInput = true;
        while(invalidInput){
            try{
                file = new Scanner(Path.of(UIFileChooser.open()));
                invalidInput = false;
            }catch(IOException e){
                UI.println("error" + e);
            }
        }
        plotSnapshot(file); 

    }


    /**
     *  Plot the temperatures for the next snapshot in the file by drawing
     *   a filled coloured circle (size DIAM) at each weather-station location.
     *  The colour of the circle should indicate the temperature.
     *
     *  The method should
     *   - read the date/time and draw the date/time at the top-left of the map.
     *   - read the number of stations, then
     *   - for each station,
     *     - read the name, coordinates, and data, and
     *     - plot the temperature for that station. 
     *   (Hint: You will find the getTemperatureColor(...) method useful.)
     *
     *  Also finds the highest and lowest temperatures at that time, and
     *  plots them with a larger circle.
     *  (Hint: If more than one station has the highest (or coolest) temperature,
     *         you only need to draw a larger circle for one of them.
     */     
    public void plotSnapshot(Scanner sc){
        UI.drawImage("map-new-zealand.gif", 0, 0);
        /*# YOUR CODE HERE */
        
        //Reads and constructs the date and time
        String date = "";
        for(int i =0; i <=2 ; i +=1){
            date += sc.next();
            if(2 != i){
                date += "/";
            }
        }
        date += "  -";
        date += sc.next();
        
        //draws the date to the screen
        UI.drawString(date,LEFT_TEXT,TOP_TEXT);
        
        //Create array lists to store the read values
        ArrayList<Double> xList = new ArrayList<Double>();
        ArrayList<Double> yList = new ArrayList<Double>();
        ArrayList<Double> tempList = new ArrayList<Double>();
        
        //Stores the index of the highest and lowest temps
        double highestTempIndex=0;
        double lowestTempIndex=0;
        
        //Stores the adctual value of the highest and lowest temps
        double highestTemp = Double.MIN_VALUE;
        double lowestTemp = Double.MAX_VALUE;
        
        //Loops through all the stations
        int numOfStations = sc.nextInt();
        for(int currentStation =0; currentStation <= numOfStations-1; currentStation +=1){
            //Reads the name of the station
            String stationName = sc.next();
            //Reads the cordinates of the station
            xList.add(sc.nextDouble());
            yList.add(sc.nextDouble());
            //Reads the temp, dew-point, surface-pressure, and sea-level-pressure at the staion but only stores the temp
            tempList.add(sc.nextDouble());
            double buffer = sc.nextDouble();
            buffer = sc.nextDouble();
            buffer = sc.nextDouble(); 
            //checks if the temp from the current tation is the highest or lowest temp seen so far
            if(tempList.get(currentStation) < lowestTemp){
                lowestTemp = tempList.get(currentStation);
                lowestTempIndex = currentStation;
                UI.println("lowest temp at" + stationName);
            }
            if(tempList.get(currentStation) > highestTemp){
                highestTemp = tempList.get(currentStation);
                highestTempIndex = currentStation;
                UI.println("highest temp at" + stationName);
            }
        }
        
        //This loops through all the sations again and prints them with the approprate circle size
        for(int currentStation =0; currentStation <= numOfStations-1; currentStation +=1){
            UI.setColor(getTemperatureColor(tempList.get(currentStation)));
            UI.drawString(String.valueOf(tempList.get(currentStation)),xList.get(currentStation),yList.get(currentStation));
            if(lowestTempIndex == currentStation || highestTempIndex == currentStation){
                //prints a larger circle for the highest and lowest temps
                UI.drawOval(xList.get(currentStation)-DIAM,yList.get(currentStation)-DIAM,2*DIAM,2*DIAM);
            }else{
                //prints a normal circle for the rest of the temps
                UI.drawOval(xList.get(currentStation)-0.5f*DIAM,yList.get(currentStation)-0.5f*DIAM,DIAM,DIAM);
            }
        
        }
    }

    /**   CHALLENGE
     * DO NOT DO THIS IF YOU HAVE NOT DONE THE PREVIOUS METHODS
     *
     * Displays an animated view of the temperatures over all
     * the times in a weather data files, plotting the temperatures
     * for the first date/time, as in the completion, pausing for half a second,
     * then plotting the temperatures for the second date/time, and
     * repeating until all the data in the file has been shown.
     * 
     * (Hint, use the plotSnapshot(...) method)
     */
    public void animateTemperatures(){
        /*# YOUR CODE HERE */
        
        boolean invalidInput = true;
        int valueType = 1;
        while(invalidInput){
            valueType = UI.askInt("Which data type would you like ?\n 1)Tempriture\n 2)dew-point\n 3)surface-pressure\n 4)sea-level-pressure\n:");
            if(valueType == 1|| valueType == 2 || valueType == 3 || valueType == 4){
                invalidInput = false;
            }else{
                UI.println("please enter 1,2,3 or 4");
            }
        }
        
        
        invalidInput = true;
        Scanner file = new Scanner("");
        while(invalidInput){
            try{
                file = new Scanner(Path.of(UIFileChooser.open()));
                invalidInput = false;
            }catch(IOException e){
                UI.println("error" + e);
            }
        }
        
        
        Vector<Vector<String>> data = new Vector<Vector<String>>();
        
        
        int snapShotNumber = 0;
        while(file.hasNext()){
            data.add(new Vector<String>());
            //Reads and then prints date
            String date = "";
            for(int i =0; i <=2 ; i +=1){
                date += file.next();
                if(2 != i){
                    date += "/";
                }
                
            }
            date += "  -";
            date += file.next();
            
            //writes the date string to the 0 index in the vector
            data.get(snapShotNumber).add(date);
            //writes the number of stations string to the 1 index in the vector
            data.get(snapShotNumber).add(file.next());
            //Stores the index of the highest and lowest temps
            data.get(snapShotNumber).add("0");
            data.get(snapShotNumber).add("0");
            
            //Stores the actual value of the highest and lowest temps
            double highestValue = Double.MIN_VALUE;
            double lowestValue = Double.MAX_VALUE;
            
            for(int currentStation =0; currentStation <= Double.parseDouble(data.get(snapShotNumber).get(1))-1; currentStation +=1){
                //Reads the staiton name from the file
                data.get(snapShotNumber).add(file.next());
                //writes the x value to the 2 index of the vector
                data.get(snapShotNumber).add(file.next());
                //writes the y value to the 3 index of the vector
                data.get(snapShotNumber).add(file.next());
                //creates varables and then store the values read from the file
                String temp = file.next();
                String dew = file.next();
                String surface = file.next();
                String seaLevel = file.next();
                //Checks what value type is ment to be displayed and then writes it to the list
                switch(valueType){
                    case 1:     //temp
                        data.get(snapShotNumber).add(temp);
                        break;
                    case 2:     //dew-point
                        data.get(snapShotNumber).add(dew);
                        break;
                    case 3:     //surface- pressure
                        data.get(snapShotNumber).add(surface);
                        break;
                    case 4:     //sea-level-pressure
                        data.get(snapShotNumber).add(seaLevel);
                        break;
                }
 
                //checks if the value from the current tation is the highest or lowest value seen so far
                if(Double.parseDouble(data.get(snapShotNumber).get(7+ 4* currentStation)) < lowestValue){
                    //sets the lowest temp
                    lowestValue = Double.parseDouble(data.get(snapShotNumber).get(7 +4*currentStation));
                    
                    //sets the lowest value index
                    data.get(snapShotNumber).set(3,String.valueOf(currentStation));
                }
                if(Double.parseDouble(data.get(snapShotNumber).get(7+ 4* currentStation)) > highestValue){
                    //sets the highest temp
                    highestValue = Double.parseDouble(data.get(snapShotNumber).get(7+ 4* currentStation));
                    //sets the highest vlaue index
                    data.get(snapShotNumber).set(2,String.valueOf(currentStation));
                }
            }
            
            //goes at end of while
            snapShotNumber +=1;
        }
        
        
        double progressBarSegmentWidth = 392/(snapShotNumber+1);
        
        for(int currentSnapShot = 0; currentSnapShot < snapShotNumber; currentSnapShot +=1){
            UI.clearGraphics();
            //Draws the backround image/map
            UI.drawImage("map-new-zealand.gif", 0, 0);
            
            
            //draws the date to the screen
            UI.setColor(Color.red);
            UI.drawString(data.get(currentSnapShot).get(0),LEFT_TEXT,TOP_TEXT);
            
            if(snapShotNumber > 1){
                //draws the progress bar at the bottom
                UI.setColor(Color.green);
                UI.drawRect(0,736,392,30);
                UI.fillRect(0,736,392/(snapShotNumber-1)*currentSnapShot,30);
            }
            
                        
            //This loops through all the sations again and prints them with the approprate circle size
            for(int currentStation =0; currentStation <= Double.parseDouble(data.get(currentSnapShot).get(1))-1; currentStation +=1){
                if(valueType == 1){
                    UI.setColor(getTemperatureColor(Double.parseDouble(data.get(currentSnapShot).get(7+ 4* currentStation))));
                }else{
                    UI.setColor(Color.red);
                }
                
                //UI.drawString(data.get(currentSnapShot).get(7+ 4* currentStation),Double.parseDouble(data.get(currentSnapShot).get(5+ 4* currentStation)),Double.parseDouble(data.get(currentSnapShot).get(6+ 4* currentStation)));
                if(Double.parseDouble(data.get(currentSnapShot).get(3)) == currentStation || Double.parseDouble(data.get(currentSnapShot).get(2)) == currentStation){
                    //prints a larger circle for the highest and lowest temps
                    UI.fillOval(Double.parseDouble(data.get(currentSnapShot).get(5+ 4* currentStation))-DIAM,Double.parseDouble(data.get(currentSnapShot).get(6+ 4* currentStation))-DIAM,2*DIAM,2*DIAM);
                }else{
                    //prints a normal circle for the rest of the temps
                    UI.fillOval(Double.parseDouble(data.get(currentSnapShot).get(5+ 4* currentStation))-0.5f*DIAM,Double.parseDouble(data.get(currentSnapShot).get(6+ 4* currentStation))-0.5f*DIAM,DIAM,DIAM);
                }
            
            }
            
            UI.sleep(1000);
        }
        
    }


    /**
     * Returns a color representing that temperature
     * The colors are increasingly blue below 15 degrees, and
     * increasingly red above 15 degrees.
     */
    public Color getTemperatureColor(double temp){
        double max = 37, min = -5, mid = (max+min)/2;
        if (temp < min || temp > max){
            return Color.white;
        }
        else if (temp <= mid){ //blue range: hues from .7 to .5
            double tempFracOfRange = (temp-min)/(mid-min);
            double hue = 0.7 -  tempFracOfRange*(0.7-0.5); 
            return Color.getHSBColor((float)hue, 1.0F, 1.0F);
        }
        else { //red range: .15 to 0.0
            double tempFracOfRange = (temp-mid)/(max-mid);
            double hue = 0.15 -  tempFracOfRange*(0.15-0.0); 
            return Color.getHSBColor((float)hue, 1.0F, 1.0F);
        }
    }

    /**
     * Setup the interface with buttons
     */
    public void setupGUI(){
        UI.initialise();
        UI.addButton("Plot temperature", this::plotTemperatures);
        UI.addButton("Animate temperature", this::animateTemperatures);
        UI.addButton("Quit", UI::quit);
        UI.setWindowSize(800,750);
        UI.setFontSize(18);
    }

    /**
     *  Main: Create object and call setupGUI on it
     */
    public static void main(String[] arguments){
        WeatherReporter obj = new WeatherReporter();
        obj.setupGUI();
    }    

}
