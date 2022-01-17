import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

//import javax.lang.model.element.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

public class Main {

    public static void main(String args[]) throws IOException, ParserConfigurationException, SAXException {

        //Task1
        // Конвертируем строки csv в объекты класса Employee
        //String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        //String fileName = "data.csv";
        //List<Employee> list = parseCSV(columnMapping, fileName);
        //list.forEach(System.out::println);
        // Конвертируем объекты класса Employee строку формата json
        //String json = listToJson(list);
        //System.out.println(json);
        // Записываем в файл json
        //writeString(json, "data.json");

        //Task2
        // Конвертируем xml в объекты класса Employee
        String fileName = "data.xml";
        List<Employee> list = parseXML(fileName);
        list.forEach(System.out::println);
        //Конвертируем объекты класса Employee строку формата json
        String json = listToJson(list);
        System.out.println(json);
        // Записываем в файл json
        writeString(json, "data1.json");

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

}
