/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm2batch.util;

import com.tm2batch.service.LogService;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map.Entry;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;
import jakarta.json.JsonWriter;

/**
 *
 * @author miker_000
 */
public class JsonUtils {

    
    
    public static JsonObject getJsonObject( String s )
    {
        try
        {
            s = s.replaceAll("\n", "\\n");

            s = s.trim();
            
            if( s.startsWith( "\"" ) )
                s = s.substring(1,s.length() );
            if( s.endsWith("\"") )
                s = s.substring(0, s.length()-1 );
            
            JsonObject o;
            try (JsonReader jsonReader = Json.createReader(new StringReader( s ) )) {
                o = jsonReader.readObject();
            }

            return o;
        }

        catch( Exception e )
        {
            LogService.logIt( e, "JsonUtils.getJsonObject() " + s );

            return null;
        }
    }
    
    
    public static JsonArrayBuilder addJsonObjectToJsonArray( JsonArray ja, JsonObject joToAdd )
    {
        JsonArrayBuilder jab = JsonUtils.jsonArrayToBuilder(ja);        
        JsonObjectBuilder joToAddB = JsonUtils.jsonObjectToBuilder( joToAdd );        
        jab.add(joToAddB);        
        return jab;                
    }
    
    
    
    
    public static JsonObjectBuilder jsonObjectToBuilder(JsonObject jo) 
    {
        JsonObjectBuilder job = Json.createObjectBuilder();

        for (Entry<String, JsonValue> entry : jo.entrySet()) 
        {
            job.add(entry.getKey(), entry.getValue());
        }

        return job;
    }    

    public static JsonArrayBuilder jsonArrayToBuilder(JsonArray ja) 
    {
        JsonArrayBuilder job = Json.createArrayBuilder();

        for (JsonValue jv : ja ) 
        {
            job.add(jv);
        }

        return job;
    }    
    
    
    public static String getJsonObjectAsString( JsonObject jo )
    {
        try
        {
            StringWriter os = new StringWriter();
            try (JsonWriter jsonWriter = Json.createWriter( os )) {
                jsonWriter.writeObject(jo);
            }

            return os.toString();
        }

        catch( Exception e )
        {
            LogService.logIt( e, "JsonUtils.getJsonObjectAsString() " );

            return null;
        }
    }
    
    
    
    public static JsonObject convertJsonStringtoObject(String jsonStr) throws Exception
    {
        try
        {
            JsonReader r = Json.createReader( new StringReader(jsonStr) );

            return r.readObject();
        }

        catch( Exception e )
        {
            LogService.logIt( e, "JsonUtils.convertJsonStringtoObject() jsonStr=" + jsonStr  );
        }

        return null;
    }


    public static String convertJsonObjectToString( JsonObject jo ) throws Exception
    {
        try
        {
            StringWriter sw = new StringWriter();
            try (JsonWriter jw = Json.createWriter(sw)) {
                jw.writeObject(jo);
            }

           return sw.getBuffer().toString();
        }
        catch( Exception e )
        {
            LogService.logIt(e, "JsonUtils.convertJsonObjecttoString() " );
            
            return null;
        }
              
    }
    
    public static String getStringFmJson(JsonObject o, String key) {
        try {
            
            if( o.isNull(key))
                return null;
            
            return o.getString(key);
        } catch (Exception e) {
            LogService.logIt(e, "JsonUtils.getStringFmJson() NONFatal key=" + key);
            return null;
        }
    }
    
    
}
