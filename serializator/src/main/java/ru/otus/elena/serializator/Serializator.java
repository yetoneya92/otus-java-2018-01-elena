
package ru.otus.elena.serializator;

import java.beans.Transient;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import static java.util.Arrays.asList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class Serializator {

    StringBuilder json = new StringBuilder();
    LinkedList<Object> memory = new LinkedList<>();
    Object obj=null;

    public String toJson(Object obj) {
        try {
            if (obj != null) {
                memory.add(obj);
            }
            this.obj = obj;
            json.append("{");
            s:
            for (Field field : obj.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                String s = Modifier.toString(field.getModifiers());
                Transient trans = field.getAnnotation(Transient.class);
                if (trans != null || s.contains("transient")) {
                    continue s;
                }
                json.append("\"").append(field.getName()).append("\":");
                if (!setFieldValue(field)) {
                    System.out.println("не сериализуется");
                    return null;
                }
            }
            json.replace(json.lastIndexOf(","), json.lastIndexOf(",") + 1, "");
            json.append("}");
            return json.toString();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private boolean setFieldValue(Field field)
            throws IllegalArgumentException, IllegalAccessException {       
        String type = field.getType().getSimpleName();        
        switch (type) {
            case "byte":
            case "short":
            case "int":
            case "double":
            case "long":
            case "float":
            case "boolean":
                json.append(field.get(obj));
                break;
            case "char":
            case "String":
                json.append("\"").append(field.get(obj)).append("\"");
                break;
            case "byte[]":
            case "short[]":    
            case "int[]":
            case "long[]":
            case "float[]":
            case "double[]":
            case "boolean[]":
            case "String[]":
            case "char[]":
                json.append("[");
                setArrayValues(field);
                json.append("]");
                break;
            case "ArrayList":
            case "LinkedList":
                json.append("[");
                setListValues(field);
                json.append("]");
                break;
            case "Set":   
                json.append("[");
                setSetValues(field);
                json.append("]");
                break;
            case "Queue":
                json.append("[");
                setQueueValues(field);
                json.append("]");
                break;
            case "Map":
                json.append("{");
                setMapValues(field);
                json.append("}");
                break;
            default:
                if (field.getType().getCanonicalName().contains("java")) {                 
                    return false;
                }
                Object o = field.get(obj);
                this.toJson(o);
                this.obj = memory.getLast();
                break;
        }
        json.append(",");
        return true;
    }

    private void setListValues(Field field) throws IllegalArgumentException, IllegalAccessException {
        List list =(List) field.get(obj);
        if (list.size() > 0) {
            String type = list.get(0).getClass().getSimpleName();          
            for (int i = 0; i < list.size(); i++) {
                setElement(type, list.get(i));
            }
            json.replace(json.lastIndexOf(","), json.lastIndexOf(",") + 1, "");
        }
    }

    private void setMapValues(Field field) throws IllegalArgumentException, IllegalAccessException {
        Map map = (Map) field.get(obj);
        if (map.size() > 0) {
            map.forEach((k,v)->{
                json.append("\"").append(k).append("\":");
                setElement(v.getClass().getSimpleName(),v);
                });       
        } json.replace(json.lastIndexOf(","), json.lastIndexOf(",") + 1, ""); 
    }
    
    private void setSetValues(Field field) throws IllegalArgumentException, IllegalAccessException{
        Set set=(Set)field.get(obj);
        if(set.size()>0){
            set.forEach(e->{
            setElement(e.getClass().getSimpleName(),e);
            });
        }json.replace(json.lastIndexOf(","), json.lastIndexOf(",") + 1, "");
    }
    
    private void setArrayValues(Field field)
            throws IllegalArgumentException, IllegalAccessException {
        Object o = field.get(obj);
        String type = o.getClass().getComponentType().getSimpleName();
        
        int length = Array.getLength(o);
        for (int i = 0; i < length; i++) {
            setElement(type, Array.get(o, i));
        }json.replace(json.lastIndexOf(","), json.lastIndexOf(",") + 1, "");
    }
    public void setQueueValues(Field field) throws IllegalArgumentException, IllegalAccessException{
        Queue queue=(Queue)field.get(obj);
        if(queue.size()>0){
            queue.forEach(e->{
            setElement(e.getClass().getSimpleName(),e);
            });
        }json.replace(json.lastIndexOf(","), json.lastIndexOf(",") + 1, "");
        
    }
    private void setElement(String type, Object o) {
        switch (type) {
            case "String":
            case "Character":
            case "char":
                json.append("\"").append(o).append("\",");
                break;
            case "Byte":
            case "Short":
            case "Integer":
            case "Long":
            case "Float":
            case "Double":
            case "Boolean":
            case "byte":
            case "short":
            case "int":
            case "long":
            case "float":
            case "double":
            case "boolean":
                json.append(o).append(",");
                break;
            default:
                break;
        }
    }
}
