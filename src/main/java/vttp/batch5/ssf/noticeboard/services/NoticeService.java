package vttp.batch5.ssf.noticeboard.services;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import vttp.batch5.ssf.noticeboard.models.Notice;
import vttp.batch5.ssf.noticeboard.repositories.NoticeRepository;


@Service
public class NoticeService {

	@Value("${publishing.host}")
  	private String publishURL;

	@Autowired
	private NoticeRepository repo;

	// TODO: Task 3
	// You can change the signature of this method by adding any number of parameters
	// and return any type
	public Map<String, String> postToNoticeServer(Notice notice) {

		JsonArrayBuilder cat = Json.createArrayBuilder();

		for (String c:notice.getCategories()){
			cat.add(c);
		}
		JsonArray categories = cat.build();

		JsonObject req_payload = Json.createObjectBuilder()
			.add("title", notice.getTitle())
			.add("poster", notice.getPoster())
			.add("postDate", notice.getPostDate().getTime())
			.add("categories", categories)
			.add("text", notice.getText())
			.build();

		String URL = publishURL + "/notice";

		RequestEntity<String> req = RequestEntity
			.post(URL)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.body(req_payload.toString(), String.class);

		RestTemplate template = new RestTemplate();
		Integer statusCode = null;
		String message = null;
		JsonObject obj = null;
		JsonObject body = null;

		try {
			ResponseEntity<String> resp = template.exchange(req, String.class);

			statusCode = resp.getStatusCodeValue();
			message = resp.getBody().toString();

			obj = Json.createReader(new StringReader(message)).readObject();

			body = Json.createObjectBuilder()
				.add("message/id", obj.getString("id"))
				.add("timestamp", obj.getJsonNumber("timestamp"))
				.build();

			repo.insertNotices(notice, body.toString());

		} catch (HttpStatusCodeException e) {
			ResponseEntity<String> resp = ResponseEntity.status(e.getRawStatusCode()).headers(e.getResponseHeaders())
                .body(e.getResponseBodyAsString());
			
			statusCode = resp.getStatusCodeValue();
			message = resp.getBody().toString();
	
			obj = Json.createReader(new StringReader(message)).readObject();

			body = Json.createObjectBuilder()
				.add("message/id", obj.getString("message"))
				.add("timestamp", obj.getJsonNumber("timestamp"))
				.build();
		}
		
		Map<String, String> response = new HashMap<>();

		response.put("statusCode", String.valueOf(statusCode));
		response.put("message", body.toString());

		return response;
	}

	public HttpStatusCode getHealthStatus(){
		if (repo.getRandomKey()){
			return HttpStatusCode.valueOf(200);
		} else {
			return HttpStatusCode.valueOf(503);
		}
	}
}
