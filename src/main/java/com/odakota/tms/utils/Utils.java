package com.odakota.tms.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author haidv
 * @version 1.0
 */
public class Utils {

    private Utils() {
    }

    public static String convertToString(Date date) {
        return date == null ? null : date.toString();
    }


    public static String convertToStringFormat(Date date) {
        return convertToStringFormat(date, "yyyy-MM-dd");
    }

    public static String convertToStringFormat(Date date, String format) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    public static Date convertToDate(String date, String format) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        try {
            return dateFormat.parse(date);
        } catch (ParseException e) {
            return new Date();
        }
    }

    public static Date calculationTime(Date date, int day) {
        return calculationTime(date, day, 0, 0, 0);
    }

    public static Date calculationTimeMonth(Date date, int month) {
        return calculationTime(date, month, 0, 0, 0, 0);
    }

    public static Date calculationTime(Date date, int minute, int second) {
        return calculationTime(date, 0, minute, second);
    }

    public static Date calculationTime(Date date, int house, int minute, int second) {
        return calculationTime(date, 0, house, minute, second);
    }

    public static Date calculationTime(Date date, int day, int house, int minute, int second) {
        return calculationTime(date, 0, day, house, minute, second);
    }

    public static int getCurrentYear() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }

    public static Date calculationTime(Date date, int month, int day, int house, int minute, int second) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if (month != 0) {
            calendar.add(Calendar.MONTH, month);
        }
        if (day != 0) {
            calendar.add(Calendar.DATE, day);
        }
        if (house != 0) {
            calendar.add(Calendar.HOUR, house);
        }
        if (minute != 0) {
            calendar.add(Calendar.MINUTE, minute);
        }
        if (second != 0) {
            calendar.add(Calendar.SECOND, second);
        }
        return calendar.getTime();
    }

    public static String toJson(Object object) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        String jsonString = "";
        try {
            jsonString = mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            jsonString = "Can't build json from object";
        }
        return jsonString;
    }

    /**
     * trim properties of object
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static Object trimReflective(Object object) throws Exception {
        if (object == null) {
            return null;
        }

        Class<?> c = object.getClass();
        // Introspected usage to pick the getters conveniently thereby
        // excluding the Object getters
        for (PropertyDescriptor propertyDescriptor : Introspector.getBeanInfo(c, Object.class)
                                                                 .getPropertyDescriptors()) {
            Method method = propertyDescriptor.getReadMethod();
            String name = method.getName();

            // If the current level of Property is of type String
            if (method.getReturnType().equals(String.class)) {
                String property = (String) method.invoke(object);
                if (property != null) {
                    Method setter = c.getMethod("set" + name.substring(3), String.class);
                    if (setter != null)
                    // Setter to trim and set the trimmed String value
                    {
                        setter.invoke(object, property.trim());
                    }
                }
            }

            // If an Object Array of Properties - added additional check to
            // avoid getBytes returning a byte[] and process
            if (method.getReturnType().isArray() && !method.getReturnType().isPrimitive()
                && !method.getReturnType().equals(String[].class) && !method.getReturnType().equals(byte[].class)
                && method.invoke(object) instanceof Object[]) {
                // Type check for primitive arrays (would fail typecasting
                // in case of int[], char[] etc)
                Object[] objectArray = (Object[]) method.invoke(object);
                if (objectArray != null) {
                    for (Object obj : objectArray) {
                        // Recursively revisit with the current property
                        trimReflective(obj);
                    }
                }
            }
            // If a String array
            if (method.getReturnType().equals(String[].class)) {
                String[] propertyArray = (String[]) method.invoke(object);
                if (propertyArray != null) {
                    Method setter = c.getMethod("set" + name.substring(3), String[].class);
                    if (setter != null) {
                        String[] modifiedArray = new String[propertyArray.length];
                        for (int i = 0; i < propertyArray.length; i++) {
                            if (propertyArray[i] != null) {
                                modifiedArray[i] = propertyArray[i].trim();
                            }
                        }

                        // Explicit wrapping
                        setter.invoke(object, (Object) modifiedArray);
                    }
                }
            }
            // Collections start
            if (Collection.class.isAssignableFrom(method.getReturnType())) {
                Collection collectionProperty = (Collection) method.invoke(object);
                if (collectionProperty != null) {
                    for (int index = 0; index < collectionProperty.size(); index++) {
                        if (collectionProperty.toArray()[index] instanceof String) {
                            String element = (String) collectionProperty.toArray()[index];

                            if (element != null) {
                                // Check if List was created with
                                // Arrays.asList (non-resizable Array)
                                if (collectionProperty instanceof List) {
                                    ((List) collectionProperty).set(index, element.trim());
                                } else {
                                    collectionProperty.remove(element);
                                    collectionProperty.add(element.trim());
                                }
                            }
                        } else {
                            // Recursively revisit with the current property
                            trimReflective(collectionProperty.toArray()[index]);
                        }
                    }
                }
            }
            // Separate placement for Map with special conditions to process
            // keys and values
            if (method.getReturnType().equals(Map.class)) {
                Map mapProperty = (Map) method.invoke(object);
                if (mapProperty != null) {
                    // Keys
                    for (int index = 0; index < mapProperty.keySet().size(); index++) {
                        if (mapProperty.keySet().toArray()[index] instanceof String) {
                            String element = (String) mapProperty.keySet().toArray()[index];
                            if (element != null) {
                                mapProperty.put(element.trim(), mapProperty.get(element));
                                mapProperty.remove(element);
                            }
                        } else {
                            // Recursively revisit with the current property
                            trimReflective(mapProperty.get(index));
                        }

                    }
                    // Values
                    for (Map.Entry entry : (Set<Map.Entry>) mapProperty.entrySet()) {

                        if (entry.getValue() instanceof String) {
                            String element = (String) entry.getValue();
                            entry.setValue(element.trim());
                        } else {
                            // Recursively revisit with the current property
                            trimReflective(entry.getValue());
                        }
                    }
                }
            } else {
                // Catch a custom data type as property and send through
                // recursion
                Object property = method.invoke(object);
                if (property != null) {
                    trimReflective(property);
                }
            }
        }

        return object;
    }
}
