package com.ws;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
import org.codehaus.jackson.type.TypeReference;

public class JacksonTester {

	public static void main(String args[]) {

		JacksonTester tester = new JacksonTester();
		// tester.objectToJsonString();
		// tester.simpleDataBinding();
		// tester.directObjectBinding();
		// tester.createTreeFromString();
		// tester.createTreeFromNode();
		// tester.jacksonStreaming();
		tester.jacksonParser();
	}

	private void jacksonParser() {

		try {
			JsonFactory jasonFactory = new JsonFactory();
			JsonParser jsonParser = jasonFactory.createJsonParser(new File("student.json"));

			while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
				// get the current token
				String fieldname = jsonParser.getCurrentName();

				if ("name".equals(fieldname)) {
					// move to next token
					jsonParser.nextToken();
					System.out.println(jsonParser.getText());
				}
				if ("age".equals(fieldname)) {
					// move to next token
					jsonParser.nextToken();
					System.out.println(jsonParser.getNumberValue());
				}

				if ("verified".equals(fieldname)) {
					// move to next token
					jsonParser.nextToken();
					System.out.println(jsonParser.getBooleanValue());
				}

				if ("marks".equals(fieldname)) {
					// move to [
					jsonParser.nextToken();
					// loop till token equal to "]"

					while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
						System.out.println(jsonParser.getNumberValue());
					}
				}
			}

		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void jacksonStreaming() {
		try {
			JsonFactory jasonFactory = new JsonFactory();

			JsonGenerator jsonGenerator = jasonFactory.createJsonGenerator(new File("student.json"), JsonEncoding.UTF8);

			// {
			jsonGenerator.writeStartObject();

			// "name" : "Mahesh Kumar"
			jsonGenerator.writeStringField("name", "Mahesh Kumar2");

			// "age" : 21
			jsonGenerator.writeNumberField("age", 22);

			// "verified" : false
			jsonGenerator.writeBooleanField("verified", false);

			// "marks" : [100, 90, 85]
			jsonGenerator.writeFieldName("marks");

			// [
			jsonGenerator.writeStartArray();
			// 100, 90, 85
			jsonGenerator.writeNumber(101);
			jsonGenerator.writeNumber(91);
			jsonGenerator.writeNumber(81);
			// ]

			jsonGenerator.writeEndArray();

			// }

			jsonGenerator.writeEndObject();
			jsonGenerator.close();

			// result student.json
			// {
			// "name":"Mahesh Kumar",
			// "age":21,
			// "verified":false,
			// "marks":[100,90,85]
			// }

			ObjectMapper mapper = new ObjectMapper();
			Map<String, Object> dataMap = mapper.readValue(new File("student.json"), Map.class);

			System.out.println(dataMap.get("name"));
			System.out.println(dataMap.get("age"));
			System.out.println(dataMap.get("verified"));
			System.out.println(dataMap.get("marks"));

		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void createTreeFromNode() {

		try {
			ObjectMapper mapper = new ObjectMapper();

			JsonNode rootNode = mapper.createObjectNode();
			JsonNode marksNode = mapper.createArrayNode();

			((ArrayNode) marksNode).add(100);
			((ArrayNode) marksNode).add(90);
			((ArrayNode) marksNode).add(85);

			((ObjectNode) rootNode).put("name", "Mahesh Kumar");
			((ObjectNode) rootNode).put("age", 21);
			((ObjectNode) rootNode).put("verified", false);
			((ObjectNode) rootNode).put("marks", marksNode);

			mapper.writeValue(new File("student.json"), rootNode);

			rootNode = mapper.readTree(new File("student.json"));

			JsonNode nameNode = rootNode.path("name");
			System.out.println("Name: " + nameNode.getTextValue());

			JsonNode ageNode = rootNode.path("age");
			System.out.println("Age: " + ageNode.getIntValue());

			JsonNode verifiedNode = rootNode.path("verified");
			System.out.println("Verified: " + (verifiedNode.getBooleanValue() ? "Yes" : "No"));

			JsonNode marksNode1 = rootNode.path("marks");
			Iterator<JsonNode> iterator = marksNode1.getElements();
			System.out.print("Marks: [ ");

			while (iterator.hasNext()) {
				JsonNode marks = iterator.next();
				System.out.print(marks.getIntValue() + " ");
			}

			System.out.println("]");

			Student student = mapper.treeToValue(rootNode, Student.class);

			System.out.println("======Tree to Values from Student");
			System.out.println("Name: " + student.getName());
			System.out.println("Age: " + student.getAge());
			System.out.println("Verified: " + (student.isVerified() ? "Yes" : "No"));
			System.out.println("Marks: " + Arrays.toString(student.getMarks()));

		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void createTreeFromString() {
		try {
			ObjectMapper mapper = new ObjectMapper();
			String jsonString = "{\"name\":\"Mahesh Kumar\",  \"age\":21,\"verified\":false,\"marks\": [100,90,85]}";
			JsonNode rootNode = mapper.readTree(jsonString);

			JsonNode nameNode = rootNode.path("name");
			System.out.println("Name: " + nameNode.getTextValue());

			JsonNode ageNode = rootNode.path("age");
			System.out.println("Age: " + ageNode.getIntValue());

			JsonNode verifiedNode = rootNode.path("verified");
			System.out.println("Verified: " + (verifiedNode.getBooleanValue() ? "Yes" : "No"));

			JsonNode marksNode = rootNode.path("marks");
			Iterator<JsonNode> iterator = marksNode.getElements();
			System.out.print("Marks: [ ");

			while (iterator.hasNext()) {
				JsonNode marks = iterator.next();
				System.out.print(marks.getIntValue() + " ");
			}

			System.out.println("]");
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void directObjectBinding() {
		try {
			ObjectMapper mapper = new ObjectMapper();

			Map<String, UserData> userDataMap = new HashMap<String, UserData>();
			UserData studentData = new UserData();
			int[] marks = { 1, 2, 3 };

			Student student = new Student();
			student.setAge(10);
			student.setName("Mahesh");

			// JAVA Object
			studentData.setStudent(student);

			// JAVA String
			studentData.setName("Mahesh Kumar");

			// JAVA Boolean
			studentData.setVerified(Boolean.FALSE);

			// Array
			studentData.setMarks(marks);
			TypeReference ref = new TypeReference<Map<String, UserData>>() {
			};

			userDataMap.put("studentData1", studentData);
			mapper.writeValue(new File("student.json"), userDataMap);

			// {
			// "studentData1":
			// {
			// "student":
			// {
			// "name":"Mahesh",
			// "age":10
			// },
			// "name":"Mahesh Kumar",
			// "verified":false,
			// "marks":[1,2,3]
			// }
			// }

			userDataMap = mapper.readValue(new File("student.json"), ref);

			System.out.println(userDataMap.get("studentData1").getStudent());
			System.out.println(userDataMap.get("studentData1").getName());
			System.out.println(userDataMap.get("studentData1").getVerified());
			System.out.println(Arrays.toString(userDataMap.get("studentData1").getMarks()));

		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void simpleDataBinding() {
		try {
			ObjectMapper mapper = new ObjectMapper();

			Map<String, Object> studentDataMap = new HashMap<String, Object>();
			int[] marks = { 1, 2, 3 };

			Student student = new Student();

			student.setAge(10);
			student.setName("Mahesh");

			// JAVA Object
			studentDataMap.put("student", student);

			// JAVA String
			studentDataMap.put("name", "Mahesh Kumar");

			// JAVA Boolean
			studentDataMap.put("verified", Boolean.FALSE);

			// Array
			studentDataMap.put("marks", marks);

			// result student.json
			// {
			// "student":{"name":"Mahesh","age":10},
			// "marks":[1,2,3],
			// "verified":false,
			// "name":"Mahesh Kumar"
			// }

			mapper.writeValue(new File("student.json"), studentDataMap);

			studentDataMap = mapper.readValue(new File("student.json"), Map.class);

			System.out.println(studentDataMap.get("student"));
			System.out.println(studentDataMap.get("name"));
			System.out.println(studentDataMap.get("verified"));
			System.out.println(studentDataMap.get("marks"));

		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void objectToJsonString() {

		try {
			Student student = new Student();
			student.setAge(10);
			student.setName("Mahesh");
			this.writeJSON(student);

			Student student1 = this.readJSON();

			System.out.println("student1");
			System.out.println(student1);

		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void jasonStringToObject() {
		String jsonString = "{\"name\":\"Mahesh\", \"age\":21}";
		ObjectMapper mapper = new ObjectMapper();
		// map json to student

		try {
			Student student = mapper.readValue(jsonString, Student.class);

			System.out.println("student object :-  \\n" + student);

			mapper.enable(SerializationConfig.Feature.INDENT_OUTPUT);
			jsonString = mapper.writeValueAsString(student);

			System.out.println("jsonString :- \\n" + jsonString);

		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void writeJSON(Student student) throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(new File("student.json"), student);
	}

	private Student readJSON() throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		Student student = mapper.readValue(new File("student.json"), Student.class);
		return student;
	}
}

class Student {
	private String name;
	private int age;
	boolean verified;
	int[] marks;

	public Student() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public boolean isVerified() {
		return verified;
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
	}

	public int[] getMarks() {
		return marks;
	}

	public void setMarks(int[] marks) {
		this.marks = marks;
	}

	public String toString() {
		return "Student [ name: " + name + ", age: " + age + " ]";
	}
}

class UserData {
	private Student student;
	private String name;
	private Boolean verified;
	private int[] marks;

	public UserData() {
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getVerified() {
		return verified;
	}

	public void setVerified(Boolean verified) {
		this.verified = verified;
	}

	public int[] getMarks() {
		return marks;
	}

	public void setMarks(int[] marks) {
		this.marks = marks;
	}
}