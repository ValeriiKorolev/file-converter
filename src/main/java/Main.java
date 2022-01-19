import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

public class Main {

    public static void main(String args[]) throws IOException, ParserConfigurationException, SAXException {

        //Task 1
        // Конвертируем строки csv в объекты класса Employee
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileNameCSV = "data.csv";
        List<Employee> list1 = parseCSV(columnMapping, fileNameCSV);
        list1.forEach(System.out::println);
        // Конвертируем объекты класса Employee строку формата json
        String json1 = listToJson(list1);
        System.out.println(json1);
        // Записываем в файл json
        writeString(json1, "data.json");

        //Task 2
        // Конвертируем xml в объекты класса Employee
        String fileNameXML = "data.xml";
        List<Employee> list2 = parseXML(fileNameXML);
        list2.forEach(System.out::println);
        //Конвертируем объекты класса Employee строку формата json
        String json2 = listToJson(list2);
        System.out.println(json2);
        // Записываем в файл json
        writeString(json2, "data1.json");

        //Task 3
        //чтение файла JSON, его парсинг и преобразование объектов JSON в классы Java
        String json = readString("data1.json");
        List<Employee> list = jsonToList(json);
        list.forEach(System.out::println);

    }

    public static List<Employee> parseCSV(String[] columnMapping, String fileName) {
        List<Employee> staff = null;
        try (CSVReader csvReader = new CSVReader(new FileReader(fileName))) {
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);
            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(csvReader)
                    .withMappingStrategy(strategy)
                    .build();
            staff = csv.parse();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return staff;
    }

    public static List<Employee> parseXML(String fileName) throws IOException, SAXException, ParserConfigurationException {
        List<Employee> list = new ArrayList<Employee>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new File(fileName));
        Node node = doc.getDocumentElement();

        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node_ = nodeList.item(i);
            if (node_.getNodeName().equals("employee")) {
                NodeList nodeList1 = node_.getChildNodes();
                long id =0;
                String firstName = null;
                String lastName = null;
                String country = null;
                int age = 0;
                for (int j = 0; j < nodeList1.getLength(); j++) {
                    Node node_1 = nodeList1.item(j);
                    String attrName = node_1.getNodeName();
                    String attrValue = node_1.getTextContent();
                    switch (attrName) {
                        case "id" :
                            id = Long.parseLong(attrValue);
                            break;
                        case "firstName" :
                            firstName = attrValue;
                            break;
                        case "lastName" :
                            lastName = attrValue;
                            break;
                        case "country" :
                            country = attrValue;
                            break;
                        case "age" :
                            age = Integer.parseInt(attrValue);
                    }
                }
                list.add(new Employee(id, firstName, lastName, country, age));
            }
        }
        return list;
    }

    public static String listToJson(List<Employee> list) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Type listType = new TypeToken<List<Employee>>() {}.getType();
        String json = gson.toJson(list, listType);
        return json;
    }

    public static void writeString(String json, String fileName) {
        try (FileWriter file = new FileWriter(fileName)) {
            file.write(json);
            file.flush();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static String readString(String fileName) {
        String jsonString = null;
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            jsonString = br.readLine();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return jsonString;
    }

    public static List<Employee> jsonToList(String json) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Type listType = new TypeToken<List<Employee>>() {}.getType();
        List<Employee> list = gson.fromJson(json, listType);
        return list;
    }

}
