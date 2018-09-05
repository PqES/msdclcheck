package org.ufla.dcc.jsdepextractor.project;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ExtractJsCommonTypes {

  private static ExtractJsCommonTypes instance;

  private Map<String, JsType> jsCommonTypes;

  public static ExtractJsCommonTypes getInstance() {
    if (instance == null) {
      try {
        instance = new ExtractJsCommonTypes();
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }
    }
    return instance;
  }

  private ExtractJsCommonTypes() throws FileNotFoundException {
    defineJsCommonTypes();
  }

  private Map<String, JsCommonTypeString> extractJsCommonTypesString()
      throws FileNotFoundException {
    Gson gson = new Gson();
    ClassLoader classLoader = ClassLoader.getSystemClassLoader();
    BufferedReader br =
        new BufferedReader(new InputStreamReader(classLoader.getResourceAsStream("typesjs.json")));
    Collection<JsCommonTypeString> jsCommonTypesString =
        gson.fromJson(br, new TypeToken<Collection<JsCommonTypeString>>() {}.getType());
    Map<String, JsCommonTypeString> jsCommonTypesStringMap = new HashMap<>();
    for (JsCommonTypeString jsCommonTypeString : jsCommonTypesString) {
      jsCommonTypesStringMap.put(jsCommonTypeString.getName(), jsCommonTypeString);
    }
    return jsCommonTypesStringMap;
  }


  private void extractJsCommonType(JsCommonTypeString jsCommonTypeString,
      Map<String, JsCommonTypeString> jsCommonTypesStringMap, Map<String, JsType> jsCommonTypes) {
    JsCommonType jsCommonType = new JsCommonType(jsCommonTypeString.getName());
    jsCommonTypes.put(jsCommonType.getName(), jsCommonType);
    if (!jsCommonTypes.containsKey(jsCommonTypeString.getDefaultMemberType())) {
      extractJsCommonType(jsCommonTypesStringMap.get(jsCommonTypeString.getDefaultMemberType()),
          jsCommonTypesStringMap, jsCommonTypes);
    }
    jsCommonType.setDefaultMemberType(jsCommonTypes.get(jsCommonTypeString.getDefaultMemberType()));
    Map<String, JsType> memberTypes = new HashMap<>();
    for (Map.Entry<String, String> entry : jsCommonTypeString.getMemberTypes().entrySet()) {
      if (!jsCommonTypes.containsKey(entry.getValue())) {
        extractJsCommonType(jsCommonTypesStringMap.get(entry.getValue()), jsCommonTypesStringMap,
            jsCommonTypes);
      }
      memberTypes.put(entry.getKey(), jsCommonTypes.get(entry.getValue()));
    }
    jsCommonType.setMemberTypes(memberTypes);
  }

  private void defineJsCommonTypes() throws FileNotFoundException {
    jsCommonTypes = new HashMap<>();
    Map<String, JsCommonTypeString> jsCommonTypesStringMap = extractJsCommonTypesString();
    for (JsCommonTypeString jsCommonTypeString : jsCommonTypesStringMap.values()) {
      if (!jsCommonTypes.containsKey(jsCommonTypeString.getName())) {
        extractJsCommonType(jsCommonTypeString, jsCommonTypesStringMap, jsCommonTypes);
      }
    }
  }

  public Map<String, JsType> getJsCommonTypes() {
    return jsCommonTypes;
  }

  public JsType getObjectType() {
    return jsCommonTypes.get("Object");
  }

  public JsType getUndefinedType() {
    return jsCommonTypes.get("undefined");
  }

  public JsType getType(String typeName) {
    return jsCommonTypes.getOrDefault(typeName, getObjectType());
  }

}
