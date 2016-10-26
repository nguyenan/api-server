package com.wut.test;
/*
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import com.google.common.collect.Multimap;
import com.wut.model.Data;
import com.wut.model.list.ListData;
import com.wut.model.map.MappedData;
import com.wut.model.scalar.BooleanData;
import com.wut.model.scalar.DateData;
import com.wut.model.scalar.EmailData;
import com.wut.model.scalar.IntegerData;
import com.wut.model.scalar.ScalarData;
import com.wut.model.scalar.StringData;
import com.wut.model.scalar.TimeData;
import com.wut.model.scalar.UrlData;

public class RandomDataGenerator {
        private Multimap<Class<? extends Data>, String> scalars;// = new ArrayListMultimap<Class<? extends Data>, String>();
        private Multimap<Class<? extends Data>, Data> examples;// = new ArrayListMultimap<Class<? extends Data>, Data>();

        private static Random randNum = new Random();
        
        public RandomDataGenerator() {
                // add scalars to scalar map
                scalars.put(EmailData.class, "example@email.com");
                scalars.put(EmailData.class, "another.example@domain.com");
                scalars.put(StringData.class, "an example");
                scalars.put(BooleanData.class, "false");
                scalars.put(BooleanData.class, "true");
                scalars.put(IntegerData.class, "-100");
                scalars.put(IntegerData.class, "1");
                scalars.put(IntegerData.class, "100");
                scalars.put(IntegerData.class, "99");
                scalars.put(UrlData.class, "http://localhost/path/to/page.html");
                scalars.put(TimeData.class, "4pm");
                scalars.put(TimeData.class, "5:00am");
                scalars.put(DateData.class, "July 12th");
                
                scalars.put(MappedData.class, "{id:1, name:who, something:what}");
                scalars.put(MappedData.class, "{one:1, two:2, three:3}");
                scalars.put(ListData.class, "[1,2,3,4,5,6,7,8,9,10]");
                scalars.put(ListData.class, "[one,two,three,four,five]");
                
                
                // put scalars
                for (Class<? extends Data> dataClass : scalars.keySet()) {
                        Collection<String> collection = scalars.get(dataClass);
                        for (String str : collection) {
                                try {
                                        if (ScalarData.class.isAssignableFrom(dataClass)) {
                                                Class<? extends ScalarData> scalarClass = (Class<? extends ScalarData>) dataClass;
                                                ScalarData scalar = scalarClass.newInstance();
                                                scalar.fromRawString(str);
                                                examples.put(dataClass, scalar);
                                        }
                                } catch (InstantiationException e) {
                                        e.printStackTrace();
                                } catch (IllegalAccessException e) {
                                        e.printStackTrace();
                                }
                        }
                }
                
                // put map
                MappedData map = new MappedData();
                for (Class<? extends Data> dataClass : examples.keySet()) {
                        Collection<Data> collection = examples.get(dataClass);
                        for (Data d : collection) {
                                map.put(dataClass.getSimpleName(), d);
                        }
                }
                
                // put list
                ArrayList<Data> list = new ArrayList<Data>();
                for (Class<? extends Data> dataClass : examples.keySet()) {
                        Collection<Data> collection = examples.get(dataClass);
                        for (Data d : collection) {
                                list.add(d);
                        }
                }
                
                examples.put(MappedData.class, map);
                examples.put(ListData.class, new ListData(list));
        }
        
        @SuppressWarnings("unused")
        private static String randomString(int length) {
                UUID uuid = UUID.randomUUID();
                String myRandom = uuid.toString().replaceAll("-", "").substring(0,length);
                return myRandom;
        }
        
        private static String randomDictionaryString(int maxLength) {
                try {
                        BufferedReader br = new BufferedReader(new FileReader("words.txt"));
                        final int numberLinesInWordsTxt = 103000;
                        long lineNumber = Math.abs(randNum.nextLong() % numberLinesInWordsTxt);
                        String line = "startval";
                        while (lineNumber-- > 0) {
                                line = br.readLine().trim();
                        }
                        if (line.length() > maxLength)
                                return line.substring(0, maxLength-1);
                        else
                                return line;
                } catch (FileNotFoundException e) {
                        e.printStackTrace();
                } catch (IOException e) {
                        e.printStackTrace();
                }
                
                return "";
        }

        public static List<Map<String, Object>> getSampleData(
                        Map<String, String> tableSchema, int numSampleRecords) {
                List<Map<String, Object>> sampleData = new ArrayList<Map<String, Object>>();
                for (int i = 0; i < numSampleRecords; i++) {
                        Map<String, Object> newData = new HashMap<String, Object>();
                        for (String colName : tableSchema.keySet()) {
                                String colType = tableSchema.get(colName).toLowerCase();

                                if (colType.startsWith("int")) {
                                        newData.put(colName, Integer.valueOf(Math.abs(randNum.nextInt())));
                                } else if (colType.startsWith("varchar")) {
                                        newData.put(colName, randomDictionaryString(10));
                                } else if (colType.startsWith("date")) {
                                        newData.put(colName, new Date(randNum.nextLong() % 12));
                                } else if (colType.startsWith("time")) {
                                        newData.put(colName, new Timestamp(randNum.nextLong() % 60));
                                } else if (colType.startsWith("timestamp")) {
                                        newData.put(colName, new Time(randNum.nextLong() % 60));
                                }
                        }
                        sampleData.add(newData);
                }
                return sampleData;
        }
        
        
        public Data getExampleValue(Class<? extends Data> clazz) {
                return examples.get(clazz).iterator().next();
        }
        
        public String getExampleString(Class<? extends Data> clazz) {
                return scalars.get(clazz).iterator().next();
        }
        
}
*/