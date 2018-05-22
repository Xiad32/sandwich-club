package com.udacity.sandwichclub.utils;

import android.content.ContentValues;
import android.support.annotation.NonNull;
import android.util.Log;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import static android.content.ContentValues.TAG;

public class JsonUtils {

    public static Sandwich parseSandwichJson(String jsonString) throws JSONException {
        Sandwich parsedSandwich = new Sandwich();

        JSONObject jsonObject = new JSONObject();
        jsonObject = buildJSON(jsonString);
        //jsonObject = jsonObject.getJSONObject("name");
        parsedSandwich = new Sandwich(
                jsonObject.getJSONObject("name").getString("mainName"),
                JSONObjToList(jsonObject.getJSONObject("name").getJSONArray("alsoKnownAs") , "alsoKnownAs"),
                jsonObject.getString("placeOfOrigin"),
                jsonObject.getString("description"),
                jsonObject.getString("image"),
                JSONObjToList(jsonObject.getJSONArray("ingredients") , "ingredients"));
        return parsedSandwich;
    }
    /*This is a very basic and limited JSON parser.
      No Spaces or \n in the JSON format will be parsed correctly
      Any Error will return a null value at the point of failure
     */
    private static JSONObject buildJSON(String json) throws JSONException {
        return buildJSON (json, 0);
    }
    private static JSONObject buildJSON (String json, int parsingPointer) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        //get first variable Name:
        boolean endOfBlock = false;
        while (parsingPointer < json.length()-1 &&
                !json.substring(parsingPointer, parsingPointer +1).equals("}")) {
            String varName = parseVarName(json, parsingPointer);
            parsingPointer = json.indexOf("\"", json.indexOf("\"", parsingPointer)+1);

            //find if value is list:
            if (json.substring(parsingPointer+1, parsingPointer + 3).equals(":{")) {
                //invalid layout
                jsonObject.put(varName, buildJSON(json, parsingPointer + 3));
                parsingPointer = json.indexOf("}", parsingPointer);
            }

            else if (json.substring(parsingPointer + 1, parsingPointer + 3).equals(":["))
                //value is list:
            {
                jsonObject.put(varName, parseJSONArray(json, parsingPointer + 3));
                parsingPointer = json.indexOf("]", parsingPointer);
            }

            else if (json.substring(parsingPointer +1 , parsingPointer + 2).equals(":")) {
                //value is single value:
                String valName = parseVarName(json, parsingPointer+2);
                parsingPointer = parsingPointer +2;
                parsingPointer = json.indexOf("\"", json.indexOf("\"", parsingPointer)+1);
//                indexof1stDelimiter = json.indexOf("\"", indexof2ndDelimiter + 1);
//                indexof2ndDelimiter = json.indexOf("\"", indexof1stDelimiter + 1);
//                String valueName = json.substring(indexof1stDelimiter,
//             (parsingPointer, parsingPointer + 5           indexof2ndDelimiter);
                jsonObject.put(varName, valName);
            }
            else {    //for empty variables
                jsonObject.put(varName, "");
                parsingPointer = json.indexOf(",", parsingPointer);
            }
                //check if end of block ("}"):
//            if (json.substring(parsingPointer + 1, parsingPointer + 2).equals("}"))
//                endOfBlock = true;
//            else
                parsingPointer = parsingPointer +1;

        }
            return jsonObject;
    }
    //Starts after the list identifier "["
    private static JSONArray parseJSONArray(String json, int startingPoint){
        JSONArray array = new JSONArray();
        while (json.charAt(startingPoint+1) != ']' && json.charAt(startingPoint) != ']' )
        {
            String varName = parseVarName(json, startingPoint);
            startingPoint = json.indexOf("\"", json.indexOf("\"", startingPoint)+1)+1;
            array.put(varName);
        }
        return array;
    }

    private static String parseVarName(String json, int startingPoint){
        int indexof1stDelimiter = json.indexOf("\"", startingPoint);
        int indexof2ndDelimiter = json.indexOf("\"", indexof1stDelimiter+1);
        String varName = json.substring(indexof1stDelimiter+1,
                indexof2ndDelimiter);
        startingPoint = indexof2ndDelimiter;
        return varName;
    }

    private static List<String> JSONObjToList(JSONArray Obj, String key) throws JSONException {
        List<String> list = new ArrayList<String>();
        for(int i = 0; i<Obj.length(); i++){
            list.add((String) Obj.get(i));
        }
        return list;
    }

}
